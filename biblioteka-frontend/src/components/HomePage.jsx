// src/components/HomePage.jsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login, auth } from "../services/api";

export default function HomePage({ onLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
      setError("");
      try {
        const data = await login({ email: email.trim(), password });
        if (!data?.id) throw new Error("nevalidan odgovor");

        auth.save(data);
        onLogin?.();

        navigate(data.role === "ADMIN" ? "/admin/knjige" : "/knjige");

        alert("Uspešna prijava");
      } catch (err) {
        setError(
          err?.response?.data?.message ||
          err?.message ||
          "Neuspešna prijava"
        );
      }
  }

  return (
    <div style={{
      minHeight:"100vh", display:"flex", justifyContent:"center", alignItems:"center",
      background:"linear-gradient(135deg,#cce3ff 0%,#e8f2ff 100%)"
    }}>
      <div style={{ background:"#fff", padding:"32px 36px", borderRadius:12, boxShadow:"0 4px 20px rgba(0,0,0,.1)", width:360 }}>
        <h1 style={{ color:"#004aad", margin:"0 0 8px" }}>Biblioteka</h1>
        <h3 style={{ color:"#0057c2", margin:"0 0 16px" }}>Prijava člana</h3>

        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom:10 }}>
            <label style={{ color:"#004aad", fontWeight:600 }}>Email</label>
            <input
              type="email"
              className="input"
              value={email}
              onChange={(e)=>setEmail(e.target.value)}
              required
            />
          </div>

          <div style={{ marginBottom:12 }}>
            <label style={{ color:"#004aad", fontWeight:600 }}>Lozinka</label>
            <input
              type="password"
              className="input"
              value={password}
              onChange={(e)=>setPassword(e.target.value)}
              required
            />
          </div>

          {error && <div style={{ color:"#c53030", marginBottom:10, fontWeight:600 }}>{error}</div>}

          <button className="btn" type="submit" style={{ width:"100%" }}>Prijavi se</button>
        </form>
      </div>
    </div>
  );
}
