package com.mwrfid.data.service;

import com.mwrfid.data.entity.Predio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface PredioRepository extends JpaRepository<Predio, Integer> {

}