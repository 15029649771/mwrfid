package com.mwrfid.data.service;

import com.mwrfid.data.entity.Dispositivo;

import com.mwrfid.data.entity.ModeloDispositivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import java.time.LocalDateTime;
import java.util.List;

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
    public List<Dispositivo> findAll() {
        return getRepository().findAll();
    }

    public List<Dispositivo> findAll(String ocurrencia) {
        if(ocurrencia==null || ocurrencia.isEmpty()){
            return getRepository().findAll();
        } else{
            return getRepository().search(ocurrencia);
        }
    }
}
