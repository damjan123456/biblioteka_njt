-- data.sql

INSERT INTO ULOGA (naziv) VALUES ('CLAN'), ('ADMIN');

INSERT INTO STATUSPOZAJMICE (naziv) VALUES ('AKTIVNA'), ('VRACENA'), ('ZAKASNELA');

INSERT INTO LOYALTYPRAVILO (naziv, opis, tip_obracuna, vrednost_poena) VALUES
  ('PO_KNJIZI', 'Fiksni broj poena po knjizi', 'po_knjizi', 5),
  ('BONUS_NA_VREME', 'Bonus ako je vraćena do roka', 'bonus_na_vreme', 3);

INSERT INTO AUTOR (ime, prezime) VALUES
  ('IVO', 'ANDRIĆ'),
  ('MEŠA', 'SELIMOVIĆ');

INSERT INTO KNJIGA (naslov, isbn, godina_izdanja, izdavac, opis, dostupna) VALUES
  ('NA DRINI ĆUPRIJA', 'ISBN-0001', 1941, 'PROSVETA', 'ROMAN', TRUE),
  ('DERVIŠ I SMRT',    'ISBN-0002', 1966, 'VES. MASLEŠA', 'ROMAN', TRUE);

INSERT INTO KNJIGA_AUTOR (id_knjiga, id_autor) VALUES (1,1), (2,2);

INSERT INTO CLAN (ime, prezime, email, password, telefon, adresa, datum_uclanjenja, ukupno_poena, id_uloga) VALUES
  ('PETAR','PETROVIĆ','petar@example.com','pera123','060-111-222','KRALJA PETRA 1, BG','2025-01-15',0,
   (SELECT id_uloga FROM ULOGA WHERE naziv='CLAN'));
INSERT INTO CLAN
  (ime, prezime, email, password, telefon, adresa, datum_uclanjenja, ukupno_poena, id_uloga)
VALUES
  ('Admin', 'Admin', 'admin@biblioteka.local', 'admin', '060000000', 'Ulica 1', CURDATE(), 0,
   (SELECT id_uloga FROM ULOGA WHERE naziv = 'ADMIN'));
INSERT INTO POZAJMICA (id_clan, datum_pozajmice, datum_roka_pozajmice, ukupna_kazna, ukupan_broj_poena) VALUES
  ((SELECT id_clan FROM CLAN WHERE email='petar@example.com'), '2025-10-01', '2025-10-15', 0.00, 0);

INSERT INTO STAVKAPOZAJMICE (id_pozajmica, id_knjiga, id_status, broj_poena, kazna, datum_vracanja) VALUES
  (1, 1, (SELECT id_status FROM STATUSPOZAJMICE WHERE naziv='AKTIVNA'), 0, 0.00, NULL),
  (1, 2, (SELECT id_status FROM STATUSPOZAJMICE WHERE naziv='VRACENA'), 0, 0.00, '2025-10-12');

INSERT INTO STAVKA_LOYALTY (id_stavka, id_pravilo, poeni, napomena) VALUES
  (1, (SELECT id_pravilo FROM LOYALTYPRAVILO WHERE naziv='PO_KNJIZI'), 5, 'Fiksni poeni'),
  (2, (SELECT id_pravilo FROM LOYALTYPRAVILO WHERE naziv='PO_KNJIZI'), 5, 'Fiksni poeni'),
  (2, (SELECT id_pravilo FROM LOYALTYPRAVILO WHERE naziv='BONUS_NA_VREME'), 3, 'Vraćena na vreme');

UPDATE STAVKAPOZAJMICE S
JOIN (SELECT id_stavka, COALESCE(SUM(poeni),0) AS sum_poena
      FROM STAVKA_LOYALTY GROUP BY id_stavka) X
  ON X.id_stavka = S.id_stavka
SET S.broj_poena = X.sum_poena;

UPDATE POZAJMICA P
JOIN (SELECT id_pozajmica, COALESCE(SUM(broj_poena),0) AS sum_poena
      FROM STAVKAPOZAJMICE GROUP BY id_pozajmica) X
  ON X.id_pozajmica = P.id_pozajmica
SET P.ukupan_broj_poena = X.sum_poena;

UPDATE CLAN C
JOIN (SELECT id_clan, COALESCE(SUM(ukupan_broj_poena),0) AS total
      FROM POZAJMICA GROUP BY id_clan) X
  ON X.id_clan = C.id_clan
SET C.ukupno_poena = X.total;
