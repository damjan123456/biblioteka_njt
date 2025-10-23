// src/components/IzmeniPozajmicuPage.jsx
import { useEffect, useMemo, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  getPozajmice,
  getStatusi,
  getLoyaltyPravila,
  getKnjige,
  vratiStavku,
} from "../services/api";

const DAILY_FINE = 50; // RSD/dan

export default function IzmeniPozajmicuPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [pozajmica, setPozajmica] = useState(null);
  const [rows, setRows] = useState([]);
  const [statusi, setStatusi] = useState([]);
  const [pravila, setPravila] = useState([]);
  const [knjige, setKnjige] = useState([]);
  const [msg, setMsg] = useState("");

  const naslovById = useMemo(() => {
    const map = new Map();
    for (const k of knjige) map.set(k.id, k.naslov);
    return map;
  }, [knjige]);

  const nazivStatusaById = useMemo(() => {
    const map = new Map();
    for (const s of statusi) map.set(s.id, s.naziv);
    return map;
  }, [statusi]);

  function parseISO(d) {
    return d ? new Date(d + "T00:00:00") : null;
  }
  function todayISO() {
    const d = new Date();
    const p = (x) => String(x).padStart(2, "0");
    return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}`;
  }
  function daysBetween(d1, d2) {
    if (!d1 || !d2) return 0;
    const ms = parseISO(d1).getTime() - parseISO(d2).getTime();
    return Math.ceil(ms / (1000 * 60 * 60 * 24));
  }
  function calcPoeni(datumVracanja, rok) {
    if (!datumVracanja) return 0;
    const onTime = parseISO(datumVracanja) <= parseISO(rok);
    let pts = 0;
    for (const p of pravila) {
      if (p.tipObracuna === "po_knjizi") pts += p.vrednostPoena ?? 0;
      if (p.tipObracuna === "bonus_na_vreme" && onTime) pts += p.vrednostPoena ?? 0;
    }
    return pts;
  }
  function calcKazna(datumVracanja, rok) {
    if (!datumVracanja) return 0;
    const lateDays = daysBetween(datumVracanja, rok);
    return lateDays > 0 ? lateDays * DAILY_FINE : 0;
  }

  const ukupnaKazna = useMemo(
    () => rows.reduce((sum, r) => sum + (Number(r.kazna) || 0), 0),
    [rows]
  );

  useEffect(() => {
    let mounted = true;
    (async () => {
      try {
        setLoading(true);
        const [allPoz, st, rules, allBooks] = await Promise.all([
          getPozajmice(),
          getStatusi(),
          getLoyaltyPravila(),
          getKnjige(),
        ]);
        if (!mounted) return;

        const p = (allPoz || []).find((x) => String(x.id) === String(id));
        if (!p) {
          setMsg("Pozajmica nije pronađena");
          setLoading(false);
          return;
        }

        setPozajmica(p);
        setStatusi(st || []);
        setPravila(rules || []);
        setKnjige(allBooks || []);

        const init = (p.stavke || []).map((s) => {
          const datumVracanja = s.datumVracanja || "";
          const statusId = s.statusId || "";
          const poeni = calcPoeni(datumVracanja, p.datumRokaPozajmice);
          const kazna = calcKazna(datumVracanja, p.datumRokaPozajmice);
          return {
            stavkaId: s.id,
            knjigaId: s.knjigaId,
            datumVracanja,
            statusId,
            poeni,
            kazna,
          };
        });
        setRows(init);
      } catch {
        setMsg("Greška pri učitavanju");
      } finally {
        if (mounted) setLoading(false);
      }
    })();
    return () => {
      mounted = false;
    };
  }, [id]);

  function recompute(cur) {
    if (!pozajmica) return cur;
    return {
      ...cur,
      poeni: calcPoeni(cur.datumVracanja, pozajmica.datumRokaPozajmice),
      kazna: calcKazna(cur.datumVracanja, pozajmica.datumRokaPozajmice),
    };
  }

  function updateRow(idx, patch) {
    setRows((prev) => {
      const next = [...prev];
      let cur = { ...next[idx], ...patch };

      // ako je status postavljen na VRACENA i nema datuma, popuni danas
      if ("statusId" in patch) {
        const naziv = nazivStatusaById.get(Number(cur.statusId));
        if (!cur.datumVracanja && naziv && naziv.toUpperCase() === "VRACENA") {
          cur.datumVracanja = todayISO();
        }
      }

      // preračun po promeni datuma/statusa
      if ("datumVracanja" in patch || "statusId" in patch) {
        cur = recompute(cur);
      }

      next[idx] = cur;
      return next;
    });
  }

  async function handleSave() {
    setMsg("");
    try {
      for (const r of rows) {
        await vratiStavku(r.stavkaId, {
          datumVracanja: r.datumVracanja || null,
          statusId: r.statusId ? Number(r.statusId) : null,
          kazna: Number.isFinite(+r.kazna) ? +r.kazna : 0, // izračunato, ne ručni unos
          poeni: Number.isFinite(+r.poeni) ? +r.poeni : 0, // izračunato, ne ručni unos
        });
      }
      setMsg("Sačuvano");
    } catch {
      setMsg("Greška pri čuvanju");
    }
  }

  if (loading) return <div style={{ padding: 16 }}>Učitavanje…</div>;
  if (!pozajmica) return <div style={{ padding: 16 }}>{msg || "Nema podataka"}</div>;

  return (
    <div style={{ padding: 16 }}>
      <h2>Izmena pozajmice #{pozajmica.id}</h2>
      <div style={{ marginBottom: 8 }}>
        <b>Član:</b> {pozajmica.clanId} &nbsp;|&nbsp; <b>Rok vraćanja:</b> {pozajmica.datumRokaPozajmice}
      </div>

      <table style={{ width: "100%", borderCollapse: "collapse", marginTop: 8 }}>
        <thead>
          <tr style={{ borderBottom: "1px solid #ddd", textAlign: "left" }}>
            <th>Naslov</th>
            <th>Status</th>
            <th>Datum vraćanja</th>
            <th>Poeni</th>
            <th>Kazna (RSD)</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((r, idx) => (
            <tr key={r.stavkaId} style={{ borderBottom: "1px solid #eee" }}>
              <td>{naslovById.get(r.knjigaId) || `#${r.knjigaId}`}</td>
              <td>
                <select
                  className="input"
                  value={r.statusId || ""}
                  onChange={(e) => updateRow(idx, { statusId: e.target.value })}
                >
                  <option value="">-- izaberi --</option>
                  {statusi.map((s) => (
                    <option key={s.id} value={s.id}>{s.naziv}</option>
                  ))}
                </select>
              </td>
              <td>
                <input
                  type="date"
                  className="input"
                  value={r.datumVracanja || ""}
                  onChange={(e) => updateRow(idx, { datumVracanja: e.target.value })}
                />
              </td>
              <td style={{ fontWeight: 600 }}>{r.poeni ?? 0}</td>
              <td style={{ fontWeight: 600 }}>{r.kazna ?? 0}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div style={{ marginTop: 12 }}>
        <b>Ukupna kazna:</b> {ukupnaKazna} RSD
      </div>

      {msg && (
        <div style={{ marginTop: 8, color: msg === "Sačuvano" ? "#2f855a" : "#c53030" }}>
          {msg}
        </div>
      )}

      <div style={{ marginTop: 12, display: "flex", gap: 8 }}>
        <button className="btn" onClick={handleSave}>Sačuvaj</button>
        <button className="btn" onClick={() => navigate(-1)}>Nazad</button>
      </div>
    </div>
  );
}
