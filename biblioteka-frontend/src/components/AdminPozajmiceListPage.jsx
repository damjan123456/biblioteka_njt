// src/components/AdminPozajmiceListPage.jsx
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  getPozajmice,
  deletePozajmica,
  getClanovi,
  getKnjige,
  getStatusi,
} from "../services/api";

export default function AdminPozajmiceListPage() {
  const [items, setItems] = useState([]);
  const [clanoviMap, setClanoviMap] = useState({});
  const [knjigeMap, setKnjigeMap] = useState({});
  const [statusMap, setStatusMap] = useState({});
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  async function load() {
    setLoading(true);
    try {
      const [poz, cls, knj, sts] = await Promise.all([
        getPozajmice(),       // /api/pozajmice
        getClanovi?.() ?? [], // /api/clanovi  (ako nemaš endpoint, ukloni)
        getKnjige(),          // /api/knjige
        getStatusi(),         // /api/statusi
      ]);

      const cMap = Array.isArray(cls)
        ? cls.reduce((acc, c) => { acc[c.id] = c; return acc; }, {})
        : {};

      const kMap = Array.isArray(knj)
        ? knj.reduce((acc, k) => { acc[k.id] = k; return acc; }, {})
        : {};

      const sMap = Array.isArray(sts)
        ? sts.reduce((acc, s) => { acc[s.id] = s.naziv; return acc; }, {})
        : {};

      setClanoviMap(cMap);
      setKnjigeMap(kMap);
      setStatusMap(sMap);
      setItems(poz || []);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  async function handleDelete(id) {
    if (!window.confirm("Obrisati pozajmicu?")) return;
    await deletePozajmica(id);   // DELETE /api/pozajmice/{id}
    await load();
  }

  return (
    <div style={{ padding: 20 }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 12 }}>
        <h2>Pozajmice</h2>
        <button className="btn" onClick={() => navigate("/admin/pozajmice/novo")}>Nova pozajmica</button>
      </div>

      {loading ? <div>Učitavanje…</div> : (
        <table className="table" style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Član</th>
              <th>Datum pozajmice</th>
              <th>Rok vraćanja</th>
              <th>Uk. poena</th>
              <th>Uk. kazna</th>
              <th>Stavke</th>
              <th>Akcije</th>
            </tr>
          </thead>
          <tbody>
            {items.map(p => (
              <tr key={p.id}>
                <td>{p.id}</td>
                <td>
                  {clanoviMap[p.clanId]
                    ? `${clanoviMap[p.clanId].ime} ${clanoviMap[p.clanId].prezime}`
                    : `#${p.clanId}`}
                </td>
                <td>{p.datumPozajmice}</td>
                <td>{p.datumRokaPozajmice}</td>
                <td>{p.ukupanBrojPoena}</td>
                <td>{p.ukupnaKazna}</td>
                <td>
                  {(p.stavke || []).map(s => (
                    <div key={s.id}>
                      {knjigeMap[s.knjigaId]?.naslov || `#${s.knjigaId}`} ·
                      {" "}{statusMap[s.statusId] || `#${s.statusId}`} ·
                      {" "}poeni {s.brojPoena} ·
                      {" "}kazna {s.kazna} ·
                      {" "}vraćeno {s.datumVracanja || "-"}
                    </div>
                  ))}
                </td>
                <td style={{ whiteSpace: "nowrap" }}>
                  <button className="btn" onClick={() => navigate(`/admin/pozajmice/${p.id}/edit`)}>Izmeni</button>
                  <button className="btn" style={{ marginLeft: 8 }} onClick={() => handleDelete(p.id)}>Obriši</button>
                </td>
              </tr>
            ))}
            {items.length === 0 && (
              <tr><td colSpan={8} style={{ textAlign: "center" }}>Nema pozajmica</td></tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}
