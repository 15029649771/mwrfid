package com.mwrfid.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.mwrfid.data.AbstractEntity;
import java.time.LocalDateTime;

@Entity
public class ModeloDispositivo
        //extends AbstractEntity
{
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull(message = "No se puede dejar el campo vacio")
    @NotEmpty(message = "El campo no puede quedar vacio")
    @Column(name = "modelodispositivo", nullable = false, unique = true, length = 100)
    private String modelodispositivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtipodispositivo")
    private TipoDispositivo idtipodispositivo;

    @NotNull(message = "No se puede dejar el campo vacio")
    @NotEmpty(message = "El campo no puede quedar vacio")
    @Column(name = "path_drivers", nullable = false, unique = true, length = 100)
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
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoDispositivo getIdtipodispositivo() {
        return idtipodispositivo;
    }

    public void setIdtipodispositivo(TipoDispositivo idtipodispositivo) {
        this.idtipodispositivo = idtipodispositivo;
    }
}
