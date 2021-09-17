package com.mwrfid.data.service;

import com.mwrfid.data.entity.ModeloDispositivo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ModeloDispositivoRepository extends JpaRepository<ModeloDispositivo, Integer> {
    @Query("select t from ModeloDispositivo t " +
            "where lower(t.modelodispositivo)  like lower(concat('%', :ocurrencia, '%') ) or " +
            "cast (t.id as string) = :ocurrencia  ")
    List<ModeloDispositivo> search(@Param("ocurrencia") String ocurrencia);
}