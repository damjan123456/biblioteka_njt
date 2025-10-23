package com.biblioteka.biblioteka_backend.service;

import com.biblioteka.biblioteka_backend.dto.PozajmicaCreateReq;
import com.biblioteka.biblioteka_backend.dto.StavkaCreateReq;
import com.biblioteka.biblioteka_backend.dto.VratiStavkuRequest;
import com.biblioteka.biblioteka_backend.model.*;
import com.biblioteka.biblioteka_backend.repo.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class PozajmicaService {

    private final PozajmicaRepo pozajmicaRepo;
    private final ClanRepo clanRepo;
    private final KnjigaRepo knjigaRepo;
    private final StavkaPozajmiceRepo stavkaRepo;
    private final StatusPozajmiceRepo statusRepo;
    private final LoyaltyPraviloRepo loyaltyRepo;
    Integer DAILY_FINE = 50;

    public PozajmicaService(PozajmicaRepo pozajmnicaRepo,
                            ClanRepo clanRepo,
                            KnjigaRepo knjigaRepo,
                            StavkaPozajmiceRepo stavkaRepo,
                            StatusPozajmiceRepo statusRepo, LoyaltyPraviloRepo loyaltyRepo) {
        this.pozajmicaRepo = pozajmnicaRepo;
        this.clanRepo = clanRepo;
        this.knjigaRepo = knjigaRepo;
        this.stavkaRepo = stavkaRepo;
        this.statusRepo = statusRepo;
        this.loyaltyRepo = loyaltyRepo;
    }

    public Pozajmica createPozajmica(PozajmicaCreateReq dto) {
        if (dto.getDatumRokaPozajmice().isBefore(dto.getDatumPozajmice()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if (dto.getDatumPozajmice().isBefore(LocalDate.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pocetak mora biti danas ili kasnije");

        if (dto.getStavke() == null || dto.getStavke().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bar jedna stavka");

        Clan clan = clanRepo.findById(dto.getClanId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clan nije nadjen"));

        // skupljanje jedinstvenih knjiga iz stavki
        List<Integer> bookIds = new ArrayList<>();
        for (StavkaCreateReq s : dto.getStavke()) {
            Integer id = s.getKnjigaId();
            if (id == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "knjigaId je obavezan");
            if (!bookIds.contains(id))
                bookIds.add(id);
        }

        // provera preklapanja
        if (stavkaRepo.existsOverlappingAny(bookIds, dto.getDatumPozajmice(), dto.getDatumRokaPozajmice()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Preklapanje sa postojecim pozajmicama");

        List<Knjiga> knjige = knjigaRepo.findAllById(bookIds);
        if (knjige.size() != bookIds.size())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Knjiga ne postoji");

        StatusPozajmice aktivna = statusRepo.findByNaziv("AKTIVNA")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Status AKTIVNA nije definisan"));

        Pozajmica p = Pozajmica.builder()
                .clan(clan)
                .datumPozajmice(dto.getDatumPozajmice())
                .datumRokaPozajmice(dto.getDatumRokaPozajmice())
                .ukupanBrojPoena(0)
                .ukupnaKazna(BigDecimal.ZERO)
                .build();

        List<StavkaPozajmice> stavke = new ArrayList<>();
        for (StavkaCreateReq sDto : dto.getStavke()) {
            Knjiga k = knjige.stream().filter(b -> b.getId().equals(sDto.getKnjigaId()))
                    .findFirst().orElse(null);
            if (k == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Knjiga ne postoji");

            if (k.getDostupna() != null && !k.getDostupna())
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Knjiga nije dostupna");

            StavkaPozajmice s = StavkaPozajmice.builder()
                    .pozajmica(p)
                    .knjiga(k)
                    .status(aktivna)
                    .brojPoena(0)
                    .kazna(BigDecimal.ZERO)
                    .build();
            stavke.add(s);
        }

        p.setStavke(stavke);
        return pozajmicaRepo.save(p);
    }

    public List<Pozajmica> getAllPozajmice(Integer clanId) {
        return clanId != null ? pozajmicaRepo.findAllWithRelByClanId(clanId)
                              : pozajmicaRepo.findAllWithRel();
    }


    public void deletePozajmica(Integer id) {
        Pozajmica r = pozajmicaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pozajmnica nije pronadjena"));
        pozajmicaRepo.delete(r);
    }

    public void vratiStavku(Integer stavkaId, VratiStavkuRequest req) {
        StavkaPozajmice s = stavkaRepo.findById(stavkaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stavka nije pronađena"));

        Pozajmica p = s.getPozajmica();
        if (p == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stavka nema pozajmicu");

        int oldPozPoeni = p.getUkupanBrojPoena() == null ? 0 : p.getUkupanBrojPoena();

        // eventualno setuj status
        if (req.getStatusId() != null) {
            StatusPozajmice st = statusRepo.findById(req.getStatusId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status ne postoji"));
            s.setStatus(st);
        }

        // datum vraćanja
        LocalDate datumVracanja = req.getDatumVracanja();
        s.setDatumVracanja(datumVracanja);

        // preračun poena prema pravilima
        int poeni = 0;
        if (datumVracanja != null) {
            boolean onTime = !datumVracanja.isAfter(p.getDatumRokaPozajmice());

            
            List<LoyaltyPravilo> pravila =  loyaltyRepo.findAll();
            for (LoyaltyPravilo lp : pravila) {
                if ("po_knjizi".equalsIgnoreCase(lp.getTipObracuna())) {
                    poeni += lp.getVrednostPoena();
                } else if ("bonus_na_vreme".equalsIgnoreCase(lp.getTipObracuna()) && onTime) {
                    poeni += lp.getVrednostPoena();
                }
            }
        }
        s.setBrojPoena(poeni);

        // preračun kazne
        BigDecimal kazna = BigDecimal.ZERO;
        if (datumVracanja != null && datumVracanja.isAfter(p.getDatumRokaPozajmice())) {
            long daysLate = ChronoUnit.DAYS.between(p.getDatumRokaPozajmice(), datumVracanja);
            if (daysLate > 0) {
                kazna = BigDecimal.valueOf(daysLate * DAILY_FINE);
            }
        }
        s.setKazna(kazna);

        // snimi stavku
        stavkaRepo.save(s);

        // recompute sumarni poeni/kazna na pozajmici
        int sumPoeni = p.getStavke().stream().mapToInt(x -> x.getBrojPoena() == null ? 0 : x.getBrojPoena()).sum();
        BigDecimal sumKazna = p.getStavke().stream()
                .map(x -> x.getKazna() == null ? BigDecimal.ZERO : x.getKazna())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        p.setUkupanBrojPoena(sumPoeni);
        p.setUkupnaKazna(sumKazna);
        pozajmicaRepo.save(p);

        // ažuriraj ukupne poene člana za razliku
        Clan clan = p.getClan();
        if (clan != null) {
            int diff = sumPoeni - oldPozPoeni;
            clan.setUkupnoPoena((clan.getUkupnoPoena() == null ? 0 : clan.getUkupnoPoena()) + diff);
            clanRepo.save(clan);
        }
    }

}
