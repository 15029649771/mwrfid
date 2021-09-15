package com.mwrfid.data.entity;

import javax.persistence.Entity;

import com.mwrfid.data.AbstractEntity;
import java.time.LocalDateTime;

@Entity
public class ModeloDispositivo extends AbstractEntity {

    private String modelodispositivo;
    private Integer idtipodispositivo;
    private String path_drivers;
    private String usuarioalt;
    private String usuarioact;
    private LocalDateTime fechaalt;
    private LocalDateTime fechaact;

    public String getModelodispositivo() {
        return modelodispositivo;
    }
    public void setModelodispositivo(String modelodispositivo) {
        this.modelodispositivo = modelodispositivo;
    }
    public Integer getIdtipodispositivo() {
        return idtipodispositivo;
    }
    public void setIdtipodispositivo(Integer idtipodispositivo) {
        this.idtipodispositivo = idtipodispositivo;
    }
    public String getPath_drivers() {
        return path_drivers;
    }
    public void setPath_drivers(String path_drivers) {
        this.path_drivers = path_drivers;
    }
    public String getUsuarioalt() {
        return usuarioalt;
    }
    public void setUsuarioalt(String usuarioalt) {
        this.usuarioalt = usuarioalt;
    }
    public String getUsuarioact() {
        return usuarioact;
    }
    public void setUsuarioact(String usuarioact) {
        this.usuarioact = usuarioact;
    }
    public LocalDateTime getFechaalt() {
        return fechaalt;
    }
    public void setFechaalt(LocalDateTime fechaalt) {
        this.fechaalt = fechaalt;
    }
    public LocalDateTime getFechaact() {
        return fechaact;
    }
    public void setFechaact(LocalDateTime fechaact) {
        this.fechaact = fechaact;
    }

}
