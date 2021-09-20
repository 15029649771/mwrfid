package com.mwrfid.data.service;

import com.mwrfid.data.entity.ModeloDispositivo;
import com.mwrfid.data.entity.Puesto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import java.time.LocalDateTime;
import java.util.List;

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
    public List<Puesto> findAll() {
        return getRepository().findAll();
    }

    public List<Puesto> findAll(String ocurrencia) {
        if(ocurrencia==null || ocurrencia.isEmpty()){
            return getRepository().findAll();
        } else{
            return getRepository().search(ocurrencia);
        }
    }
}
