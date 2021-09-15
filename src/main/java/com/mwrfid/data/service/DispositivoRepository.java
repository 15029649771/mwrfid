package com.mwrfid.data.service;

import com.mwrfid.data.entity.Dispositivo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Integer> {

}