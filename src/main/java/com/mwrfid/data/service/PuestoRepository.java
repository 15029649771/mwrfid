package com.mwrfid.data.service;

import com.mwrfid.data.entity.ModeloDispositivo;
import com.mwrfid.data.entity.Puesto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PuestoRepository extends JpaRepository<Puesto, Integer> {
    @Query("select t from Puesto t " +
            "where lower(t.puesto)  like lower(concat('%', :ocurrencia, '%') ) or " +
            "cast (t.id as string) = :ocurrencia  ")
    List<Puesto> search(@Param("ocurrencia") String ocurrencia);
}