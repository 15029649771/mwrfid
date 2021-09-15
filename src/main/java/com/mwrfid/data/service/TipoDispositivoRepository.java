package com.mwrfid.data.service;

import com.mwrfid.data.entity.TipoDispositivo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface TipoDispositivoRepository extends JpaRepository<TipoDispositivo, Integer> {

}