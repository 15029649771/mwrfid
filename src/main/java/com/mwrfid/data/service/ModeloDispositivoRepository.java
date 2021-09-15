package com.mwrfid.data.service;

import com.mwrfid.data.entity.ModeloDispositivo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface ModeloDispositivoRepository extends JpaRepository<ModeloDispositivo, Integer> {

}