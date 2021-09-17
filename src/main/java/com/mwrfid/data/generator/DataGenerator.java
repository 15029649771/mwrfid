package com.mwrfid.data.generator;

import com.vaadin.flow.spring.annotation.SpringComponent;

import com.mwrfid.data.service.UserRepository;
import com.mwrfid.data.entity.Users;
import java.util.Collections;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.mwrfid.data.Role;
import com.mwrfid.data.service.ModeloDispositivoRepository;
import com.mwrfid.data.entity.ModeloDispositivo;
import com.mwrfid.data.service.DispositivoRepository;
import com.mwrfid.data.entity.Dispositivo;
import com.mwrfid.data.service.PredioRepository;
import com.mwrfid.data.entity.Predio;
import com.mwrfid.data.service.PuestoRepository;
import com.mwrfid.data.entity.Puesto;
import com.mwrfid.data.service.TipoDispositivoRepository;
import com.mwrfid.data.entity.TipoDispositivo;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository,
            ModeloDispositivoRepository modeloDispositivoRepository, DispositivoRepository dispositivoRepository,
            PredioRepository predioRepository, PuestoRepository puestoRepository,
            TipoDispositivoRepository tipoDispositivoRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 2 User entities...");
            Users user = new Users();
            user.setName("John Normal");
            user.setUsername("user");
            user.setHashedPassword(passwordEncoder.encode("user"));
            user.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            user.setRoles(Collections.singleton(Role.USER));
            userRepository.save(user);
            Users admin = new Users();
            admin.setName("John Normal");
            admin.setUsername("admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1607746882042-944635dfe10e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            admin.setRoles(Collections.singleton(Role.ADMIN));
            userRepository.save(admin);
            logger.info("... generating 100 Modelo Dispositivo entities...");
            ExampleDataGenerator<ModeloDispositivo> modeloDispositivoRepositoryGenerator = new ExampleDataGenerator<>(
                    ModeloDispositivo.class, LocalDateTime.of(2021, 9, 15, 0, 0, 0));
            modeloDispositivoRepositoryGenerator.setData(ModeloDispositivo::setId, DataType.ID);
            modeloDispositivoRepositoryGenerator.setData(ModeloDispositivo::setId, DataType.NUMBER_UP_TO_100);
            modeloDispositivoRepositoryGenerator.setData(ModeloDispositivo::setModelodispositivo, DataType.WORD);
            //modeloDispositivoRepositoryGenerator.setData(ModeloDispositivo::setIdtipodispositivo,
            //        DataType.NUMBER_UP_TO_100);
            modeloDispositivoRepositoryGenerator.setData(ModeloDispositivo::setPath_drivers, DataType.WORD);
            modeloDispositivoRepositoryGenerator.setData(ModeloDispositivo::setUsuarioalt, DataType.WORD);
            modeloDispositivoRepositoryGenerator.setData(ModeloDispositivo::setUsuarioact, DataType.WORD);
            modeloDispositivoRepositoryGenerator.setData(ModeloDispositivo::setFechaalt,
                    DataType.DATETIME_LAST_10_YEARS);
            modeloDispositivoRepositoryGenerator.setData(ModeloDispositivo::setFechaact,
                    DataType.DATETIME_LAST_10_YEARS);
            modeloDispositivoRepository.saveAll(modeloDispositivoRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Dispositivo entities...");
            ExampleDataGenerator<Dispositivo> dispositivoRepositoryGenerator = new ExampleDataGenerator<>(
                    Dispositivo.class, LocalDateTime.of(2021, 9, 15, 0, 0, 0));
            dispositivoRepositoryGenerator.setData(Dispositivo::setId, DataType.ID);
            dispositivoRepositoryGenerator.setData(Dispositivo::setId, DataType.NUMBER_UP_TO_100);
            dispositivoRepositoryGenerator.setData(Dispositivo::setDispositivo, DataType.WORD);
            dispositivoRepositoryGenerator.setData(Dispositivo::setIdmodelodispositivo, DataType.NUMBER_UP_TO_100);
            dispositivoRepositoryGenerator.setData(Dispositivo::setCantidad_puertos, DataType.NUMBER_UP_TO_100);
            dispositivoRepositoryGenerator.setData(Dispositivo::setObservaciones, DataType.WORD);
            dispositivoRepositoryGenerator.setData(Dispositivo::setUsuarioalt, DataType.WORD);
            dispositivoRepositoryGenerator.setData(Dispositivo::setUsuarioact, DataType.WORD);
            dispositivoRepositoryGenerator.setData(Dispositivo::setFechaalt, DataType.DATETIME_LAST_10_YEARS);
            dispositivoRepositoryGenerator.setData(Dispositivo::setFechaact, DataType.DATETIME_LAST_10_YEARS);
            dispositivoRepository.saveAll(dispositivoRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Predio entities...");
            ExampleDataGenerator<Predio> predioRepositoryGenerator = new ExampleDataGenerator<>(Predio.class,
                    LocalDateTime.of(2021, 9, 15, 0, 0, 0));
            predioRepositoryGenerator.setData(Predio::setId, DataType.ID);
            predioRepositoryGenerator.setData(Predio::setId, DataType.NUMBER_UP_TO_100);
            predioRepositoryGenerator.setData(Predio::setPredio, DataType.WORD);
            predioRepositoryGenerator.setData(Predio::setDomicilio, DataType.WORD);
            predioRepositoryGenerator.setData(Predio::setObservaciones, DataType.WORD);
            predioRepositoryGenerator.setData(Predio::setLatitud, DataType.NUMBER_UP_TO_100);
            predioRepositoryGenerator.setData(Predio::setLongitud, DataType.NUMBER_UP_TO_100);
            predioRepositoryGenerator.setData(Predio::setUsuarioalt, DataType.WORD);
            predioRepositoryGenerator.setData(Predio::setUsuarioact, DataType.WORD);
            predioRepositoryGenerator.setData(Predio::setFechaalt, DataType.DATETIME_LAST_10_YEARS);
            predioRepositoryGenerator.setData(Predio::setFechaact, DataType.DATETIME_LAST_10_YEARS);
            predioRepository.saveAll(predioRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Puesto entities...");
            ExampleDataGenerator<Puesto> puestoRepositoryGenerator = new ExampleDataGenerator<>(Puesto.class,
                    LocalDateTime.of(2021, 9, 15, 0, 0, 0));
            puestoRepositoryGenerator.setData(Puesto::setId, DataType.ID);
            puestoRepositoryGenerator.setData(Puesto::setId, DataType.NUMBER_UP_TO_100);
            puestoRepositoryGenerator.setData(Puesto::setPuesto, DataType.WORD);
            puestoRepositoryGenerator.setData(Puesto::setIddispositivo, DataType.NUMBER_UP_TO_100);
            puestoRepositoryGenerator.setData(Puesto::setPuerto, DataType.NUMBER_UP_TO_100);
            puestoRepositoryGenerator.setData(Puesto::setObservaciones, DataType.WORD);
            puestoRepositoryGenerator.setData(Puesto::setLatitud, DataType.NUMBER_UP_TO_100);
            puestoRepositoryGenerator.setData(Puesto::setLongitud, DataType.NUMBER_UP_TO_100);
            puestoRepositoryGenerator.setData(Puesto::setUsuarioalt, DataType.WORD);
            puestoRepositoryGenerator.setData(Puesto::setUsuarioact, DataType.WORD);
            puestoRepositoryGenerator.setData(Puesto::setFechaalt, DataType.DATETIME_LAST_10_YEARS);
            puestoRepositoryGenerator.setData(Puesto::setFechaact, DataType.DATETIME_LAST_10_YEARS);
            puestoRepositoryGenerator.setData(Puesto::setIdpredio, DataType.NUMBER_UP_TO_100);
            puestoRepository.saveAll(puestoRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Tipo Dispositivo entities...");
            ExampleDataGenerator<TipoDispositivo> tipoDispositivoRepositoryGenerator = new ExampleDataGenerator<>(
                    TipoDispositivo.class, LocalDateTime.of(2021, 9, 15, 0, 0, 0));
           // tipoDispositivoRepositoryGenerator.setData(TipoDispositivo::setId, DataType.ID);
           // tipoDispositivoRepositoryGenerator.setData(TipoDispositivo::setId, DataType.NUMBER_UP_TO_100);
            tipoDispositivoRepositoryGenerator.setData(TipoDispositivo::setTipodispositivo, DataType.WORD);
            tipoDispositivoRepositoryGenerator.setData(TipoDispositivo::setLectura, DataType.BOOLEAN_50_50);
            tipoDispositivoRepositoryGenerator.setData(TipoDispositivo::setEscritura, DataType.BOOLEAN_50_50);
            tipoDispositivoRepositoryGenerator.setData(TipoDispositivo::setUsuarioalt, DataType.WORD);
            tipoDispositivoRepositoryGenerator.setData(TipoDispositivo::setUsuarioact, DataType.WORD);
            tipoDispositivoRepositoryGenerator.setData(TipoDispositivo::setFechaalt, DataType.DATETIME_LAST_10_YEARS);
            tipoDispositivoRepositoryGenerator.setData(TipoDispositivo::setFechaact, DataType.DATETIME_LAST_10_YEARS);
            tipoDispositivoRepository.saveAll(tipoDispositivoRepositoryGenerator.create(100, seed));

            logger.info("Generated demo data");
        };
    }

}