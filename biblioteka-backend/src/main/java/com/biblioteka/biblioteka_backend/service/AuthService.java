// src/main/java/com/biblioteka/biblioteka_backend/service/AuthService.java
package com.biblioteka.biblioteka_backend.service;

import com.biblioteka.biblioteka_backend.dto.LoginRequest;
import com.biblioteka.biblioteka_backend.dto.LoginResponse;
import com.biblioteka.biblioteka_backend.model.Clan;
import com.biblioteka.biblioteka_backend.repo.ClanRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final ClanRepo clanRepo;

    public AuthService(ClanRepo clanRepo) {
        this.clanRepo = clanRepo;
    }

    public LoginResponse authenticate(LoginRequest request) {
        Optional<Clan> opt = clanRepo.findByEmail(request.getEmail());
        if (opt.isPresent()) {
            Clan user = opt.get();
            if (user.getPassword().equals(request.getPassword())) {
                return LoginResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .role(user.getUloga() != null ? user.getUloga().getNaziv() : "CLAN")
                        .message("Login uspesan")
                        .build();
            }
        }
        throw new RuntimeException("Pogrešan email ili lozinka.");
    }
}
