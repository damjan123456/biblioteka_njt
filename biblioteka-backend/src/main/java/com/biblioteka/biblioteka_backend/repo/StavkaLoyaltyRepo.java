// src/main/java/com/biblioteka/biblioteka_backend/repo/StavkaLoyaltyRepo.java
package com.biblioteka.biblioteka_backend.repo;

import com.biblioteka.biblioteka_backend.model.StavkaLoyalty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StavkaLoyaltyRepo extends JpaRepository<StavkaLoyalty, Long> {}
