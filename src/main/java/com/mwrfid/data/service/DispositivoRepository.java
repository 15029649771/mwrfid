package com.mwrfid.data.service;

import com.mwrfid.data.entity.Dispositivo;

import com.mwrfid.data.entity.ModeloDispositivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Integer> {
    @Query("select t from Dispositivo t " +
            "where lower(t.dispositivo)  like lower(concat('%', :ocurrencia, '%') ) or " +
            "cast (t.id as string) = :ocurrencia  ")
    List<Dispositivo> search(@Param("ocurrencia") String ocurrencia);
}