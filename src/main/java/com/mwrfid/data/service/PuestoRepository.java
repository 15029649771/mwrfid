package com.mwrfid.data.service;

import com.mwrfid.data.entity.Puesto;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface PuestoRepository extends JpaRepository<Puesto, Integer> {

}