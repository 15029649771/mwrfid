package com.mwrfid.data.service;

import com.mwrfid.data.entity.Puesto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import java.time.LocalDateTime;

@Service
public class PuestoService extends CrudService<Puesto, Integer> {

    private PuestoRepository repository;

    public PuestoService(@Autowired PuestoRepository repository) {
        this.repository = repository;
    }

    @Override
    protected PuestoRepository getRepository() {
        return repository;
    }

}
