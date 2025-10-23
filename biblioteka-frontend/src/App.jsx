import { useState } from "react";
import { BrowserRouter, Routes, Route, Navigate, Link, useNavigate } from "react-router-dom";
import HomePage from "./components/HomePage.jsx";
import { auth } from "./services/api.js";

// Placeholder stranice — napravi ove komponente u /src/components/
import KnjigePage from "./components/KnjigePage.jsx";
import AutoriPage from "./components/AutoriPage.jsx";
import MojePozajmicePage from "./components/MojePozajmicePage.jsx";
import IzmeniPozajmicuPage from "./components/IzmeniPozajmicuPage.jsx";
import DodajPozajmicuPage from "./components/DodajPozajmicuPage.jsx";
import AdminPozajmiceListPage from "./components/AdminPozajmiceListPage.jsx";

function RequireAuth({ children, logged }) {
  if (!logged) return <Navigate to="/" replace />;
  return children;
}

function RequireRole({ anyOf, children }) {
  const role = auth.getRole();
  if (!auth.isLoggedIn()) return <Navigate to="/" replace />;
  if (!anyOf.includes(role)) return <Navigate to="/" replace />;
  return children;
}

function Nav({ onLogout }) {
  const navigate = useNavigate();
  const role = auth.getRole();
  return (
    <div style={{
      background:"#e9f1ff",
      borderBottom:"1px solid #c9d8ff",
      color:"#1c2f5c",
      padding:"8px 12px",
      display:"flex",
      justifyContent:"space-between",
      alignItems:"center"
    }}>
      <div style={{ display:"flex", gap:12 }}>
          {role === "CLAN" &&(
              <>
                 <Link to="/knjige" style={{ color:"#1e4bd8", textDecoration:"none" }}>Knjige</Link>
                 <Link to="/mojePozajmice" style={{ color:"#1e4bd8", textDecoration:"none" }}>Moje pozajmice</Link>

              </>
          )}
          {role === "ADMIN" && (
              <>
                  <Link to="/admin/knjige" style={{ color:"#1e4bd8", textDecoration:"none" }}>Knjige</Link>
                  <Link to="/admin/autori" style={{ color:"#1e4bd8", textDecoration:"none" }}>Autori</Link>
                  <Link to="/admin/pozajmice/novo" style={{ color:"#1e4bd8", textDecoration:"none" }}>Dodaj pozajmicu</Link>
                  <Link to="/admin/pozajmice" style={{ color:"#1e4bd8", textDecoration:"none" }}>Pozajmice</Link>

              </>
          )}

      </div>
      <button
        className="btn"
        onClick={() => { auth.clear(); onLogout(); navigate("/"); }}>
        Odjava
      </button>
    </div>
  );
}

export default function App() {
  const [logged, setLogged] = useState(auth.isLoggedIn());

  return (
      <BrowserRouter>
        {logged && <Nav onLogout={() => setLogged(false)} />}

        <Routes>
          <Route path="/" element={<HomePage onLogin={() => setLogged(true)} />} />

          {/* Rute za CLAN */}
          <Route path="/knjige" element={
            <RequireRole anyOf={["CLAN"]}><KnjigePage/></RequireRole>
          } />
          <Route path="/mojePozajmice" element={
            <RequireRole anyOf={["CLAN"]}><MojePozajmicePage/></RequireRole>
          } />


          {/* Rute za ADMIN */}
          <Route path="/admin/knjige" element={
            <RequireRole anyOf={["ADMIN"]}><KnjigePage/></RequireRole>
          } />
          <Route path="/admin/autori" element={
            <RequireRole anyOf={["ADMIN"]}><AutoriPage/></RequireRole>
          } />
          <Route path="/admin/pozajmice" element={
              <RequireAuth logged={logged}><AdminPozajmiceListPage/></RequireAuth>} />
          <Route path="/admin/pozajmice/novo" element={
              <RequireAuth logged={logged}><DodajPozajmicuPage/></RequireAuth>} />
          <Route path="/admin/pozajmice/:id" element={
              <RequireAuth logged={logged}><IzmeniPozajmicuPage/></RequireAuth>} />


          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
  );
}
