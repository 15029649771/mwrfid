package com.mwrfid.data.service;

import com.mwrfid.data.entity.Predio;

import com.mwrfid.data.entity.TipoDispositivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import java.time.LocalDateTime;
import java.util.List;

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
    public List<Predio> findAll() {
        return getRepository().findAll();
    }

    public List<Predio> findAll(String ocurrencia) {
        if(ocurrencia==null || ocurrencia.isEmpty()){
            return getRepository().findAll();
        } else{
            return getRepository().search(ocurrencia);
        }
    }
}
