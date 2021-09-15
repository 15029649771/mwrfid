package com.mwrfid.data.service;

import com.mwrfid.data.entity.ModeloDispositivo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import java.time.LocalDateTime;

@Service
public class ModeloDispositivoService extends CrudService<ModeloDispositivo, Integer> {

    private ModeloDispositivoRepository repository;

    public ModeloDispositivoService(@Autowired ModeloDispositivoRepository repository) {
        this.repository = repository;
    }

    @Override
    protected ModeloDispositivoRepository getRepository() {
        return repository;
    }

}
