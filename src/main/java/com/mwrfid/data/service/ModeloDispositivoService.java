package com.mwrfid.data.service;

import com.mwrfid.data.entity.ModeloDispositivo;

import com.mwrfid.data.entity.TipoDispositivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import java.time.LocalDateTime;
import java.util.List;

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

    public List<ModeloDispositivo> findAll() {
        return getRepository().findAll();
    }

    public List<ModeloDispositivo> findAll(String ocurrencia) {
        if(ocurrencia==null || ocurrencia.isEmpty()){
            return getRepository().findAll();
        } else{
            return getRepository().search(ocurrencia);
        }
    }
}
