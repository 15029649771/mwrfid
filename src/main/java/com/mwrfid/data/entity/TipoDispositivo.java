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
public class TipoDispositivo
//        extends AbstractEntity
{

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull(message = "No se puede dejar el campo vacio")
    @NotEmpty(message = "El campo no puede quedar vacio")
    @Column(name = "tipodispositivo", nullable = false, unique = true, length = 100)
    private String tipodispositivo;

    private boolean lectura;
    private boolean escritura;
    private String usuarioalt;
    private String usuarioact;
    private LocalDateTime fechaalt;
    private LocalDateTime fechaact;


    public String getTipodispositivo() {
        return tipodispositivo;
    }
    public void setTipodispositivo(String tipodispositivo) {
        this.tipodispositivo = tipodispositivo;
    }
    public boolean isLectura() {
        return lectura;
    }
    public void setLectura(boolean lectura) {
        this.lectura = lectura;
    }
    public boolean isEscritura() {
        return escritura;
    }
    public void setEscritura(boolean escritura) {
        this.escritura = escritura;
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
