package com.mwrfid.data.entity;

import javax.persistence.Entity;

import com.mwrfid.data.AbstractEntity;
import java.time.LocalDateTime;

@Entity
public class Dispositivo extends AbstractEntity {

    private String dispositivo_desc;
    private Integer idmodelodispositivo;
    private Integer cantidad_puertos;
    private String observaciones;
    private String usuarioalt;
    private String usuarioact;
    private LocalDateTime fechaalt;
    private LocalDateTime fechaact;

    public String getDispositivo() {
        return dispositivo_desc;
    }
    public void setDispositivo(String dispositivo) {
        this.dispositivo_desc = dispositivo;
    }
    public Integer getIdmodelodispositivo() {
        return idmodelodispositivo;
    }
    public void setIdmodelodispositivo(Integer idmodelodispositivo) {
        this.idmodelodispositivo = idmodelodispositivo;
    }
    public Integer getCantidad_puertos() {
        return cantidad_puertos;
    }
    public void setCantidad_puertos(Integer cantidad_puertos) {
        this.cantidad_puertos = cantidad_puertos;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
