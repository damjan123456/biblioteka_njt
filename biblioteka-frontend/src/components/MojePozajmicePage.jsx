import { useEffect, useState } from "react";
import { getPozajmice, auth } from "../services/api";

export default function MojePozajmicePage({ adminView = false }) {
  const [pozajmice, setPozajmice] = useState([]);

  useEffect(() => {
    async function load() {
      const user = auth.getUser();
      // ako je adminView, prosledi null da bi backend vratio sve pozajmice;
      // inače prosledi id člana da bi dobio samo svoje
      const data = await getPozajmice(adminView ? undefined : user?.id);
      setPozajmice(data || []);
    }
    load();
  }, [adminView]);

  return (
    <div style={{ padding: 16 }}>
      <h2>{adminView ? "Sve pozajmice" : "Moje pozajmice"}</h2>
      {pozajmice.length === 0 && (
        <div>Nema pozajmica</div>
      )}
      {pozajmice.map((p) => (
        <div key={p.id} style={{ marginBottom: 16, border: "1px solid #ddd", padding: 12 }}>
          <div><b>ID pozajmice:</b> {p.id}</div>
          {!adminView && (
            <div><b>Datum:</b> {p.datumPozajmice} — {p.datumRokaPozajmice}</div>
          )}
          {adminView && (
            <div>
              <b>Član ID:</b> {p.clanId} &nbsp;&nbsp;
              <b>Datum:</b> {p.datumPozajmice} — {p.datumRokaPozajmice}
            </div>
          )}
          <div><b>Ukupna kazna:</b> {p.ukupnaKazna}</div>
          <div><b>Ukupan broj poena:</b> {p.ukupanBrojPoena}</div>
          <table style={{ width: "100%", marginTop: 8 }}>
            <thead>
              <tr>
                <th style={{ textAlign:"left" }}>Knjiga</th>
                <th style={{ textAlign:"left" }}>Status</th>
                <th style={{ textAlign:"left" }}>Datum vraćanja</th>
              </tr>
            </thead>
            <tbody>
              {p.stavke.map((s) => (
                <tr key={s.id}>
                  <td>{s.knjigaNaslov}</td>
                  <td>{s.statusNaziv}</td>
                  <td>{s.datumVracanja || "-"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ))}
    </div>
  );
}
