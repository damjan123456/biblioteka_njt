import { useEffect, useMemo, useState } from "react";
import { auth } from "../services/api.js";
import {
  getKnjige, createKnjiga, updateKnjiga, deleteKnjiga, getAutori
} from "../services/api.js";

export default function KnjigePage() {
  const user = auth.getUser();
  const isAdmin = user?.role === "ADMIN";

  const [items, setItems] = useState([]);
  const [search, setSearch] = useState("");

  const [authors, setAuthors] = useState([]); // [{id, ime, prezime}]
  const authorMap = useMemo(() => {
    const m = {};
    for (const a of authors) m[a.id] = `${a.ime} ${a.prezime}`;
    return m;
  }, [authors]);

  const [editingId, setEditingId] = useState(null);
  const [editForm, setEditForm] = useState({
    naslov:"", isbn:"", godinaIzdanja:"", izdavac:"", opis:"", dostupna:true,
    selectedAuthors:[]
  });

  const [newForm, setNewForm] = useState({
    naslov:"", isbn:"", godinaIzdanja:"", izdavac:"", opis:"", dostupna:true,
    selectedAuthors:[]
  });

  // notifikacije
  const [note, setNote] = useState({ text:"", type:"ok", visible:false });
  function notify(text, ok=true) {
    setNote({ text, type: ok ? "ok" : "err", visible:true });
    window.clearTimeout((notify)._t);
    (notify)._t = window.setTimeout(() => setNote(s => ({...s, visible:false})), 2500);
  }

  async function load(q) {
    try {
      const [books, aut] = await Promise.all([getKnjige(q || ""), getAutori("")]);
      setItems(books || []);
      setAuthors(aut || []);
    } catch {
      notify("Učitavanje nije uspelo", false);
    }
  }

  useEffect(() => { load(""); }, []);

  async function handleSearch(e) {
    e.preventDefault();
    await load(search);
  }

  function beginEdit(b) {
    setEditingId(b.id);
    setEditForm({
      naslov: b.naslov || "",
      isbn: b.isbn || "",
      godinaIzdanja: b.godinaIzdanja ?? "",
      izdavac: b.izdavac || "",
      opis: b.opis || "",
      dostupna: !!b.dostupna,
      selectedAuthors: Array.isArray(b.autorIds) ? b.autorIds
                        : Array.isArray(b.autoriIds) ? b.autoriIds
                        : []
    });
  }
  function cancelEdit(){ setEditingId(null); }

  async function saveEdit(id) {
    try {
      const dto = {
        naslov: editForm.naslov,
        isbn: editForm.isbn,
        godinaIzdanja: editForm.godinaIzdanja ? Number(editForm.godinaIzdanja) : null,
        izdavac: editForm.izdavac || null,
        opis: editForm.opis || null,
        dostupna: !!editForm.dostupna,
        autorIds: editForm.selectedAuthors
      };
      await updateKnjiga(id, dto);
      await load(search);
      setEditingId(null);
      notify("Knjiga je izmenjena");
    } catch {
      notify("Izmena nije uspela", false);
    }
  }

  async function remove(id) {
    if (!window.confirm("Obrisati knjigu?")) return;
    try {
      await deleteKnjiga(id);
      await load(search);
      notify("Knjiga je obrisana");
    } catch {
      notify("Brisanje nije uspelo", false);
    }
  }

  async function addNew(e) {
    e.preventDefault();
    try {
      const dto = {
        naslov: newForm.naslov,
        isbn: newForm.isbn,
        godinaIzdanja: newForm.godinaIzdanja ? Number(newForm.godinaIzdanja) : null,
        izdavac: newForm.izdavac || null,
        opis: newForm.opis || null,
        dostupna: !!newForm.dostupna,
        autorIds: newForm.selectedAuthors
      };
      await createKnjiga(dto);
      setNewForm({ naslov:"", isbn:"", godinaIzdanja:"", izdavac:"", opis:"", dostupna:true, selectedAuthors:[] });
      setSearch("");
      await load("");
      notify("Knjiga je dodata");
    } catch {
      notify("Dodavanje nije uspelo", false);
    }
  }

  function renderAuthors(b){
    if (Array.isArray(b.autori) && b.autori.length>0) return b.autori.join(", ");
    const ids = Array.isArray(b.autorIds) ? b.autorIds
             : Array.isArray(b.autoriIds) ? b.autoriIds : [];
    if (ids.length>0) return ids.map(id => authorMap[id] || `#${id}`).join(", ");
    return "-";
  }

  return (
    <div style={{ padding:16, maxWidth:1200, margin:"0 auto" }}>
      {/* top notification */}
      {note.visible && (
        <div style={{
          position:"fixed", top:12, left:0, right:0, display:"flex", justifyContent:"center", zIndex:9999
        }}>
          <div style={{
            padding:"10px 14px",
            borderRadius:8,
            boxShadow:"0 4px 14px rgba(0,0,0,.12)",
            background: note.type==="ok" ? "#e6ffed" : "#ffeaea",
            color: note.type==="ok" ? "#05610d" : "#8a1111",
            border: `1px solid ${note.type==="ok" ? "#b7f5c2" : "#ffc0c0"}`
          }}>
            {note.text}
          </div>
        </div>
      )}

      <h2>Knjige</h2>

      {/* Pretraga */}
      <form onSubmit={handleSearch} style={{ display:"flex", gap:8, marginBottom:12 }}>
        <input
          value={search}
          onChange={(e)=>setSearch(e.target.value)}
          placeholder="Pretraga po naslovu, ISBN, izdavaču…"
          className="input"
          style={{ flex:1 }}
        />
        <button className="btn" type="submit">Pretraži</button>
      </form>

      {/* Tabela */}
      <table style={{ width:"100%", borderCollapse:"collapse", marginBottom:24 }}>
        <thead>
          <tr style={{ background:"#eef3ff" }}>
            <th style={{ textAlign:"left", padding:8, borderBottom:"1px solid #ddd" }}>Naslov</th>
            <th style={{ textAlign:"left", padding:8, borderBottom:"1px solid #ddd" }}>ISBN</th>
            <th style={{ textAlign:"left", padding:8, borderBottom:"1px solid #ddd" }}>Godina</th>
            <th style={{ textAlign:"left", padding:8, borderBottom:"1px solid #ddd" }}>Izdavač</th>
            <th style={{ textAlign:"left", padding:8, borderBottom:"1px solid #ddd" }}>Autori</th>
            <th style={{ textAlign:"left", padding:8, borderBottom:"1px solid #ddd" }}>Dostupna</th>
            {isAdmin && <th style={{ padding:8, borderBottom:"1px solid #ddd" }}>Akcije</th>}
          </tr>
        </thead>
        <tbody>
          {items.map(b => {
            const isEditing = editingId === b.id;
            return (
              <tr key={b.id} style={{ borderBottom:"1px solid #eee" }}>
                <td style={{ padding:8 }}>
                  {isEditing ? (
                    <input className="input" value={editForm.naslov}
                      onChange={e=>setEditForm({...editForm, naslov:e.target.value})}/>
                  ) : b.naslov}
                </td>
                <td style={{ padding:8 }}>
                  {isEditing ? (
                    <input className="input" value={editForm.isbn}
                      onChange={e=>setEditForm({...editForm, isbn:e.target.value})}/>
                  ) : b.isbn}
                </td>
                <td style={{ padding:8 }}>
                  {isEditing ? (
                    <input className="input" type="number" value={editForm.godinaIzdanja ?? ""}
                      onChange={e=>setEditForm({...editForm, godinaIzdanja:e.target.value})}/>
                  ) : (b.godinaIzdanja ?? "")}
                </td>
                <td style={{ padding:8 }}>
                  {isEditing ? (
                    <input className="input" value={editForm.izdavac}
                      onChange={e=>setEditForm({...editForm, izdavac:e.target.value})}/>
                  ) : (b.izdavac || "")}
                </td>
                <td style={{ padding:8, maxWidth:320 }}>
                  {isEditing ? (
                    <select
                      multiple
                      className="input"
                      value={editForm.selectedAuthors.map(String)}
                      onChange={(e)=>{
                        const vals = Array.from(e.target.selectedOptions).map(o=>Number(o.value));
                        setEditForm({...editForm, selectedAuthors: vals});
                      }}
                      style={{ height: 90 }}
                    >
                      {authors.map(a=>(
                        <option key={a.id} value={a.id}>{a.ime} {a.prezime}</option>
                      ))}
                    </select>
                  ) : renderAuthors(b)}
                </td>
                <td style={{ padding:8 }}>
                  {isEditing ? (
                    <input type="checkbox" checked={!!editForm.dostupna}
                      onChange={e=>setEditForm({...editForm, dostupna:e.target.checked})}/>
                  ) : (b.dostupna ? "DA" : "NE")}
                </td>

                {isAdmin && (
                  <td style={{ padding:8, whiteSpace:"nowrap" }}>
                    {!isEditing ? (
                      <>
                        <button className="btn" onClick={()=>beginEdit(b)} style={{ marginRight:8 }}>Izmeni</button>
                        <button className="btn" onClick={()=>remove(b.id)}>Obriši</button>
                      </>
                    ) : (
                      <>
                        <button className="btn" onClick={()=>saveEdit(b.id)} style={{ marginRight:8 }}>Sačuvaj</button>
                        <button className="btn" onClick={cancelEdit}>Otkaži</button>
                      </>
                    )}
                  </td>
                )}
              </tr>
            );
          })}
          {items.length === 0 && (
            <tr><td colSpan={isAdmin?7:6} style={{ padding:12, color:"#666" }}>Nema podataka</td></tr>
          )}
        </tbody>
      </table>

      {/* Nova knjiga – samo admin */}
      {isAdmin && (
        <form onSubmit={addNew} style={{ padding:12, border:"1px solid #ddd", borderRadius:8, background:"#fafcff" }}>
          <h3>Dodaj novu knjigu</h3>
          <div style={{ display:"grid", gridTemplateColumns:"repeat(3,1fr)", gap:12 }}>
            <div>
              <label>Naslov</label>
              <input className="input" value={newForm.naslov}
                     onChange={e=>setNewForm({...newForm, naslov:e.target.value})} required/>
            </div>
            <div>
              <label>ISBN</label>
              <input className="input" value={newForm.isbn}
                     onChange={e=>setNewForm({...newForm, isbn:e.target.value})} required/>
            </div>
            <div>
              <label>Godina</label>
              <input type="number" className="input" value={newForm.godinaIzdanja}
                     onChange={e=>setNewForm({...newForm, godinaIzdanja:e.target.value})}/>
            </div>
            <div>
              <label>Izdavač</label>
              <input className="input" value={newForm.izdavac}
                     onChange={e=>setNewForm({...newForm, izdavac:e.target.value})}/>
            </div>
            <div style={{ gridColumn:"span 2" }}>
              <label>Opis</label>
              <input className="input" value={newForm.opis}
                     onChange={e=>setNewForm({...newForm, opis:e.target.value})}/>
            </div>
            <div>
              <label>Autori</label>
              <select
                multiple
                className="input"
                value={newForm.selectedAuthors.map(String)}
                onChange={(e)=>{
                  const vals = Array.from(e.target.selectedOptions).map(o=>Number(o.value));
                  setNewForm({...newForm, selectedAuthors: vals});
                }}
                style={{ height: 90 }}
                required
              >
                {authors.map(a=>(
                  <option key={a.id} value={a.id}>{a.ime} {a.prezime}</option>
                ))}
              </select>
            </div>
            <div style={{ display:"flex", alignItems:"center", gap:8 }}>
              <input type="checkbox" checked={newForm.dostupna}
                     onChange={e=>setNewForm({...newForm, dostupna:e.target.checked})}/>
              <span>Dostupna</span>
            </div>
          </div>
          <div style={{ marginTop:12 }}>
            <button className="btn" type="submit">Dodaj</button>
          </div>
        </form>
      )}
    </div>
  );
}
