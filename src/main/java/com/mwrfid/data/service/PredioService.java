package com.mwrfid.data.service;

import com.mwrfid.data.entity.Predio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import java.time.LocalDateTime;

@Service
public class PredioService extends CrudService<Predio, Integer> {

    private PredioRepository repository;

    public PredioService(@Autowired PredioRepository repository) {
        this.repository = repository;
    }

    @Override
    protected PredioRepository getRepository() {
        return repository;
    }

}
