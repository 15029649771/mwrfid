package com.mwrfid.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.mwrfid.data.AbstractEntity;
import java.time.LocalDateTime;

@Entity
public class Puesto
       // extends AbstractEntity
{

    @Id
    @GeneratedValue
    private Integer id;


    @NotNull(message = "No se puede dejar el campo vacio")
    @NotEmpty(message = "El campo no puede quedar vacio")
    @Column(name = "puesto", nullable = false, unique = true, length = 100)
    private String puesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddispositivo")
    private Dispositivo iddispositivo;


    // ver si van o no null
    private Integer puerto;
    private String observaciones;
    private Integer latitud;
    private Integer longitud;

    private String usuarioalt;
    private String usuarioact;
    private LocalDateTime fechaalt;
    private LocalDateTime fechaact;
    private Integer idpredio;

    public String getPuesto() {
        return puesto;
    }
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    //public Integer getIddispositivo() {
    //    return iddispositivo;
    //}
    //public void setIddispositivo(Integer iddispositivo) {
    //    this.iddispositivo = iddispositivo;
   // }
    public Integer getPuerto() {
        return puerto;
    }
    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
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
    public Integer getIdpredio() {
        return idpredio;
    }
    public void setIdpredio(Integer idpredio) {
        this.idpredio = idpredio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Dispositivo getIddispositivo() {
        return iddispositivo;
    }

    public void setIddispositivo(Dispositivo iddispositivo) {
        this.iddispositivo = iddispositivo;
    }
}
