package com.mwrfid.data.service;

import com.mwrfid.data.entity.TipoDispositivo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import java.time.LocalDateTime;

@Service
public class TipoDispositivoService extends CrudService<TipoDispositivo, Integer> {

    private TipoDispositivoRepository repository;

    public TipoDispositivoService(@Autowired TipoDispositivoRepository repository) {
        this.repository = repository;
    }

    @Override
    protected TipoDispositivoRepository getRepository() {
        return repository;
    }

}
