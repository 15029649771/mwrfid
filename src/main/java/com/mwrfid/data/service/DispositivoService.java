package com.mwrfid.data.service;

import com.mwrfid.data.entity.Dispositivo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import java.time.LocalDateTime;

@Service
public class DispositivoService extends CrudService<Dispositivo, Integer> {

    private DispositivoRepository repository;

    public DispositivoService(@Autowired DispositivoRepository repository) {
        this.repository = repository;
    }

    @Override
    protected DispositivoRepository getRepository() {
        return repository;
    }

}
