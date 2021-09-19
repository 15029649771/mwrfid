package com.mwrfid.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.mwrfid.data.AbstractEntity;
import java.time.LocalDateTime;

@Entity
public class Predio
               // extends AbstractEntity
{
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull(message = "No se puede dejar el campo vacio")
    @NotEmpty(message = "El campo no puede quedar vacio")
    @Column(name = "predio", nullable = false, unique = true, length = 100)
    private String predio;
    private String domicilio;
    private String observaciones;
    private Integer latitud;
    private Integer longitud;
    private String usuarioalt;
    private String usuarioact;
    private LocalDateTime fechaalt;
    private LocalDateTime fechaact;

    public String getPredio() {
        return predio;
    }
    public void setPredio(String predio) {
        this.predio = predio;
    }
    public String getDomicilio() {
        return domicilio;
    }
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public Integer getLatitud() {
        return latitud;
    }
    public void setLatitud(Integer latitud) {
        this.latitud = latitud;
    }
    public Integer getLongitud() {
        return longitud;
    }
    public void setLongitud(Integer longitud) {
        this.longitud = longitud;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
