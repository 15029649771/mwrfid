package com.mwrfid.data.service;

import com.mwrfid.data.entity.TipoDispositivo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TipoDispositivoRepository extends JpaRepository<TipoDispositivo, Integer> {
    @Query("select t from TipoDispositivo t " +
            "where lower(t.tipodispositivo)  like lower(concat('%', :ocurrencia, '%') ) or " +
            "cast (t.id as string) = :ocurrencia  ")
    List<TipoDispositivo> search(@Param("ocurrencia") String ocurrencia);
}