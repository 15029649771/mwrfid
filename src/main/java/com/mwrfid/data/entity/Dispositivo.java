package com.mwrfid.data.entity;

import javax.persistence.*;

import com.mwrfid.data.AbstractEntity;
import java.time.LocalDateTime;

@Entity
public class Dispositivo
//        extends AbstractEntity
{

    @Id
    @GeneratedValue
    private Integer id;

    private String dispositivo;
    //private Integer idmodelodispositivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmodelodispositivo")
    private ModeloDispositivo idmodelodispositivo;

    private Integer cantidad_puertos;
    private String observaciones;
    private String usuarioalt;
    private String usuarioact;
    private LocalDateTime fechaalt;
    private LocalDateTime fechaact;

    public String getDispositivo() {
        return dispositivo;
    }
    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }
    //public Integer getIdmodelodispositivo() {
     //   return idmodelodispositivo;
    //}
    //public void setIdmodelodispositivo(Integer idmodelodispositivo) {
      //  this.idmodelodispositivo = idmodelodispositivo;
    //}
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ModeloDispositivo getIdmodelodispositivo() {
        return idmodelodispositivo;
    }

    public void setIdmodelodispositivo(ModeloDispositivo idmodelodispositivo) {
        this.idmodelodispositivo = idmodelodispositivo;
    }
}
