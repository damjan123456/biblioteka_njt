// src/components/AutoriPage.jsx
import { useEffect, useState } from "react";
import { getAutori, createAutor } from "../services/api.js";

export default function AutoriPage() {
  const [items, setItems] = useState([]);
  const [form, setForm] = useState({ ime: "", prezime: "" });

  const [note, setNote] = useState({ text: "", ok: true, show: false });
  function notify(text, ok = true) {
    setNote({ text, ok, show: true });
    clearTimeout(notify._t);
    notify._t = setTimeout(() => setNote((s) => ({ ...s, show: false })), 2200);
  }

  async function load() {
    try {
      const data = await getAutori(); // vrati sve
      setItems(data || []);
    } catch {
      notify("Greška pri učitavanju", false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function addNew(e) {
    e.preventDefault();
    const ime = form.ime.trim();
    const prezime = form.prezime.trim();
    if (!ime || !prezime) return notify("Ime i prezime su obavezni", false);
    try {
      await createAutor({ ime, prezime });
      setForm({ ime: "", prezime: "" });
      await load(); // da se odmah pojavi u tabeli
      notify("Autor je dodat");
    } catch {
      notify("Dodavanje nije uspelo", false);
    }
  }

  return (
    <div style={{ padding: 16, maxWidth: 900, margin: "0 auto" }}>
      {note.show && (
        <div style={{ position: "fixed", top: 12, left: 0, right: 0, display: "flex", justifyContent: "center", zIndex: 9999 }}>
          <div
            style={{
              padding: "10px 14px",
              borderRadius: 8,
              boxShadow: "0 4px 14px rgba(0,0,0,.12)",
              background: note.ok ? "#e6ffed" : "#ffeaea",
              color: note.ok ? "#05610d" : "#8a1111",
              border: `1px solid ${note.ok ? "#b7f5c2" : "#ffc0c0"}`
            }}
          >
            {note.text}
          </div>
        </div>
      )}

      <h2>Autori</h2>

      <table style={{ width: "100%", borderCollapse: "collapse", marginBottom: 24 }}>
        <thead>
          <tr style={{ background: "#eef3ff" }}>
            <th style={{ textAlign: "left", padding: 8, borderBottom: "1px solid #ddd" }}>ID</th>
            <th style={{ textAlign: "left", padding: 8, borderBottom: "1px solid #ddd" }}>Ime</th>
            <th style={{ textAlign: "left", padding: 8, borderBottom: "1px solid #ddd" }}>Prezime</th>
          </tr>
        </thead>
        <tbody>
          {items.map((a) => (
            <tr key={a.id} style={{ borderBottom: "1px solid #eee" }}>
              <td style={{ padding: 8 }}>{a.id}</td>
              <td style={{ padding: 8 }}>{a.ime}</td>
              <td style={{ padding: 8 }}>{a.prezime}</td>
            </tr>
          ))}
          {items.length === 0 && (
            <tr>
              <td colSpan={3} style={{ padding: 12, color: "#666" }}>Nema autora</td>
            </tr>
          )}
        </tbody>
      </table>

      <form onSubmit={addNew} style={{ padding: 12, border: "1px solid #ddd", borderRadius: 8, background: "#fafcff" }}>
        <h3>Dodaj novog autora</h3>
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 12 }}>
          <div>
            <label>Ime</label>
            <input
              className="input"
              value={form.ime}
              onChange={(e) => setForm((s) => ({ ...s, ime: e.target.value }))}
              required
            />
          </div>
          <div>
            <label>Prezime</label>
            <input
              className="input"
              value={form.prezime}
              onChange={(e) => setForm((s) => ({ ...s, prezime: e.target.value }))}
              required
            />
          </div>
        </div>
        <div style={{ marginTop: 12 }}>
          <button className="btn" type="submit">Dodaj</button>
        </div>
      </form>
    </div>
  );
}
