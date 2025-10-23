import axios from "axios";

const api = axios.create({ baseURL: "http://localhost:8080/api" });

api.interceptors.response.use(
  (r) => r,
  (e) => {
    const msg = e.response?.data?.message || e.response?.data || "Greška";
    alert(msg);
    return Promise.reject(e);
  }
);

/* AUTH */
export const login = async (credentials /* {email, password} */) => {
  const res = await api.post("/auth/login", credentials);
  return res.data;
};

export const auth = {
  isLoggedIn: () => !!sessionStorage.getItem("user"),
  getUser: () => JSON.parse(sessionStorage.getItem("user") || "{}"),
  getRole: () => (JSON.parse(sessionStorage.getItem("user") || "{}").role || null),
  save: (u) => sessionStorage.setItem("user", JSON.stringify(u)),
  clear: () => sessionStorage.removeItem("user"),
};

/* AUTORI */
export const getAutori = async (q) => {
  const params = q ? { q } : {};
  const res = await api.get("/autori", { params });
  return res.data;
};


export const createAutor = async (autor /* {ime, prezime} */) => {
  const res = await api.post("/autori", autor);
  return res.data;
};

/* KNJIGE */
export const getKnjige = async (search) => {
  const q = (search ?? "").trim();
  const params = q ? { q } : {};
  const res = await api.get("/knjige", { params });
  return res.data;
};

export const createKnjiga = async (dto) => {
  const res = await api.post("/knjige", dto);
  return res.data;
};

export const updateKnjiga = async (id, dto) => {
  const res = await api.put(`/knjige/${id}`, dto);
  return res.data;
};

export const deleteKnjiga = async (id) => {
  await api.delete(`/knjige/${id}`);
};

/* STATUSI POZAJMICE */
export const getStatusi = async () => {
  const res = await api.get("/statusi-pozajmice");
  return res.data;
};

/* LOYALTY PRAVILA */
export const getLoyaltyPravila = async (search) => {
  const params = {};
  if (search) params.search = search;
  const res = await api.get("/loyalty", { params });
  return res.data;
};

export const createLoyaltyPravilo = async (dto /* {naziv, opis, tipObracuna, vrednostPoena} */) => {
  const res = await api.post("/loyalty", dto);
  return res.data;
};

export const updateLoyaltyPravilo = async (id, dto) => {
  const res = await api.put(`/loyalty/${id}`, dto);
  return res.data;
};

export const deleteLoyaltyPravilo = async (id) => {
  await api.delete(`/loyalty/${id}`);
};

/* CLANOVI */
export const registerClan = async (dto /* {ime, prezime, email, password, ...} */) => {
  const res = await api.post("/clanovi", dto);
  return res.data;
};

export const getClanovi = async (q) => {
  const params = q ? { q } : {};
  const res = await api.get("/clanovi", { params });
  return res.data;
};

/* POZAJMICE */
export const getPozajmice = async (clanId) => {
  const params = {};
  if (clanId) params.clanId = clanId;
  const res = await api.get("/pozajmice", { params });
  return res.data;
};

export const createPozajmica = async (dto) => {
  const body = {
    ...dto,
    clanId: Number(dto.clanId),
    stavke: (dto.stavke || []).map((s) => ({ ...s, knjigaId: Number(s.knjigaId) })),
  };
  const res = await api.post("/pozajmice", body);
  return res.data;
};

export const deletePozajmica = async (id) => {
  await api.delete(`/pozajmice/${id}`);
};

export const getPozajmica = async (id) => {
  const res = await api.get(`/pozajmice/${id}`);
  return res.data;
};

export const vratiStavku = async (stavkaId, req) => {
  const res = await api.put(`/pozajmice/stavke/${stavkaId}/vrati`, req);
  return res.data;
};

/* CLAN – poeni */
export const getMojiPoeni = async (clanId) => {
  const res = await api.get(`/clanovi/${clanId}/poeni`);
  return res.data; // {poeni: number}
};

/* POZAJMICE – adminsko menjanje statusa jedne stavke */
export const setStavkaStatus = async (stavkaId, statusId) => {
  const res = await api.put(`/pozajmice/stavke/${stavkaId}/status`, { statusId });
  return res.data; // vraća ažuriranu stavku ili OK poruku
};

/* POZAJMICE – opcionalno: vrati stavku sa datumom, kaznom i poenima */
export const vratiStavkuV2 = async (stavkaId, { datumVracanja, statusId, kazna, poeni }) => {
  const res = await api.put(`/pozajmice/stavke/${stavkaId}/vrati`, {
    datumVracanja, statusId, kazna, poeni
  });
  return res.data;
};



