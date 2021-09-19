package com.mwrfid.data.service;

import com.mwrfid.data.entity.Predio;

import com.mwrfid.data.entity.TipoDispositivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PredioRepository extends JpaRepository<Predio, Integer> {
    @Query("select t from Predio t " +
            "where lower(t.predio)  like lower(concat('%', :ocurrencia, '%') ) or " +
            "cast (t.id as string) = :ocurrencia  ")
    List<Predio> search(@Param("ocurrencia") String ocurrencia);
}