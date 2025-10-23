import { useEffect, useMemo, useState } from "react";
import { getKnjige, getClanovi, createPozajmica } from "../services/api";

export default function DodajPozajmicuPage() {
  const [clanovi, setClanovi] = useState([]);
  const [knjige, setKnjige] = useState([]);

  const [clanId, setClanId] = useState("");
  const [datumPozajmice, setDatumPozajmice] = useState(() => new Date().toISOString().slice(0,10));
  const [datumRokaPozajmice, setDatumRokaPozajmice] = useState(() => {
    const d = new Date(); d.setDate(d.getDate()+14); return d.toISOString().slice(0,10);
  });

  // form za dodavanje jedne stavke
  const [odabranaKnjigaId, setOdabranaKnjigaId] = useState("");
  const [stavke, setStavke] = useState([]); // [{knjigaId, knjigaNaslov}]

  useEffect(() => {
    Promise.all([getClanovi(), getKnjige()]).then(([cl, kn]) => {
      setClanovi(cl || []);
      setKnjige(kn || []);
    });
  }, []);

  const knjigaMap = useMemo(
    () => Object.fromEntries((knjige||[]).map(k => [k.id, k.naslov])),
    [knjige]
  );

  function addStavka() {
    const id = Number(odabranaKnjigaId);
    if (!id) return alert("Izaberi knjigu.");
    setStavke(prev => [...prev, { knjigaId: id, knjigaNaslov: knjigaMap[id] || String(id) }]);
    setOdabranaKnjigaId("");
  }

  function removeStavka(id) {
    setStavke(prev => prev.filter(s => s.knjigaId !== id));
  }

  async function handleCreate() {
    if (!clanId) return alert("Izaberi člana.");
    if (!stavke.length) return alert("Dodaj bar jednu stavku.");
    const body = {
      clanId: Number(clanId),
      datumPozajmice,
      datumRokaPozajmice,
      stavke: stavke.map(s => ({ knjigaId: s.knjigaId }))
    };
    try {
      await createPozajmica(body);
      alert("Pozajmica kreirana.");
      // reset
      setClanId("");
      setStavke([]);
    } catch (e) {
      // interceptor već prikazuje poruku
    }
  }

  return (
    <div style={{ padding: 16 }}>
      <h2>Dodaj pozajmicu</h2>

      {/* Član */}
      <div style={{ display:"grid", gridTemplateColumns:"220px 1fr", gap:8, maxWidth:640, marginTop:8 }}>
        <label>Član</label>
        <select className="input" value={clanId} onChange={e=>setClanId(e.target.value)}>
          <option value="">-- izaberi člana --</option>
          {(clanovi||[]).map(c => (
            <option key={c.id} value={c.id}>
              {c.ime} {c.prezime} ({c.email})
            </option>
          ))}
        </select>

        <label>Datum pozajmice</label>
        <input type="date" className="input" value={datumPozajmice} onChange={e=>setDatumPozajmice(e.target.value)} />

        <label>Rok vraćanja</label>
        <input type="date" className="input" value={datumRokaPozajmice} onChange={e=>setDatumRokaPozajmice(e.target.value)} />
      </div>

      <hr style={{ margin:"16px 0" }} />

      {/* Dodavanje stavke */}
      <div style={{ display:"flex", gap:8, alignItems:"center", flexWrap:"wrap" }}>
        <select className="input" value={odabranaKnjigaId} onChange={e=>setOdabranaKnjigaId(e.target.value)}>
          <option value="">-- izaberi knjigu --</option>
          {(knjige||[]).map(k => (
            <option key={k.id} value={k.id}>{k.naslov} — {k.isbn}</option>
          ))}
        </select>
        <button className="btn" type="button" onClick={addStavka}>Dodaj stavku</button>
      </div>

      {/* Lista stavki u ovoj pozajmici */}
      <table className="table" style={{ marginTop:12 }}>
        <thead>
          <tr>
            <th>#</th>
            <th>Knjiga</th>
            <th>Akcije</th>
          </tr>
        </thead>
        <tbody>
          {stavke.length === 0 ? (
            <tr><td colSpan={3} style={{ textAlign:"center" }}>Nema stavki</td></tr>
          ) : (
            stavke.map((s, i) => (
              <tr key={s.knjigaId}>
                <td>{i+1}</td>
                <td>{s.knjigaNaslov}</td>
                <td>
                  <button className="btn danger" type="button" onClick={()=>removeStavka(s.knjigaId)}>Ukloni</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      <div style={{ marginTop:12 }}>
        <button className="btn primary" type="button" onClick={handleCreate}>Kreiraj pozajmicu</button>
      </div>
    </div>
  );
}
