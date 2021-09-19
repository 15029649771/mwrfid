package com.mwrfid.views.modelodispositivo;

import com.mwrfid.data.entity.ModeloDispositivo;
import com.mwrfid.data.entity.TipoDispositivo;
import com.mwrfid.data.service.ModeloDispositivoService;
import com.mwrfid.data.service.TipoDispositivoRepository;
import com.mwrfid.views.MainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Optional;

@PageTitle("Modelo Dispositivo")
@Route(value = "modelodispositivo/:modeloDispositivoID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
@Uses(Icon.class)
public class ModeloDispositivoView extends Div implements BeforeEnterObserver {
    @Autowired
    TipoDispositivoRepository tr;

    private final String MODELODISPOSITIVO_ID = "modeloDispositivoID";
    private final String MODELODISPOSITIVO_EDIT_ROUTE_TEMPLATE = "modelodispositivo/%d/edit";

    private Grid<ModeloDispositivo> grid = new Grid<>(ModeloDispositivo.class, false);

    private TextField id;
    private TextField modelodispositivo;
    private ComboBox<TipoDispositivo> idtipodispositivo;
    private TextField path_drivers;

    private TextField txtFiltro;
    private Button btnFiltro;

    private Button nuevo = new Button("Nuevo");
    private Button guardar = new Button("Guardar");
    private Button eliminar = new Button("Eliminar");

    private BeanValidationBinder<ModeloDispositivo> binder;

    private ModeloDispositivo modeloDispositivo;

    private ModeloDispositivoService modeloDispositivoService;

    public ModeloDispositivoView(@Autowired ModeloDispositivoService modeloDispositivoService) {
        this.modeloDispositivoService = modeloDispositivoService;
        addClassNames("modelodispositivo-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        eliminar.setEnabled(false);
        txtFiltro.setPlaceholder("Ingrese ocurrencia");


        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("modelodispositivo").setAutoWidth(true);
        grid.addColumn(this::FKTipoDispositivo).setHeader("Tipo Dispositivo").setAutoWidth(true).setSortable(true);
        grid.addColumn("path_drivers").setAutoWidth(true);

        grid.setItems(query -> modeloDispositivoService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        //grid.setHeightFull();
        grid.setHeight("92%");

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                eliminar.setEnabled(true);
                //guardar.setEnabled(true);
                UI.getCurrent().navigate(String.format(MODELODISPOSITIVO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                eliminar.setEnabled(false);
                //guardar.setEnabled(false);
                clearForm();
                UI.getCurrent().navigate(ModeloDispositivoView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(ModeloDispositivo.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(id).withConverter(new StringToIntegerConverter("Solo se pueden ingresar numeros")).bind("id");

        binder.bindInstanceFields(this);

        nuevo.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        guardar.addClickListener(e -> {
            try {
                if (this.modeloDispositivo == null) {
                    this.modeloDispositivo = new ModeloDispositivo();
                }
                binder.writeBean(this.modeloDispositivo);

                modeloDispositivoService.update(this.modeloDispositivo);
                clearForm();
                refreshGrid();
                Notification.show("Se guardo el registro.");
                UI.getCurrent().navigate(ModeloDispositivoView.class);
            } catch (ValidationException validationException) {
                Notification.show("Error: ha ocurrido una excepcion al intentar grabar en la base de datos.");
            }
        });

        // manejo del fitro
        btnFiltro.addClickListener(e -> {
            try {
                if (txtFiltro.getValue().equalsIgnoreCase("")) {
                    grid.setItems(modeloDispositivoService.findAll());
                } else {
                    grid.setItems(modeloDispositivoService.findAll(txtFiltro.getValue()));
                    refreshGrid();
                }
                refreshGrid();
                Notification.show("Actualizacion correcta.");
                UI.getCurrent().navigate(ModeloDispositivoView.class);
            } catch (Exception validationException) {
                Notification.show("Ocurrio un error cuando se intentaba actualizar.");
            }
        });




        // manejo de boton eliminar
        eliminar.addClickListener(e -> {

            //try {
            Dialog dialog = new Dialog();
            dialog.add(new Text("Confirma la eliminacion del registro seleccionado?"));
            dialog.setCloseOnEsc(false);
            dialog.setCloseOnOutsideClick(false);

            dialog.open();

            Button confirmButton = new Button("Si", event -> {
                Notification.show("Se elimino el registro seleccionado");
                modeloDispositivoService.delete(this.modeloDispositivo.getId());
                refreshGrid();
                dialog.close();

            });
            Button cancelButton = new Button("No", event -> {
                Notification.show("No se elimino registro");
                dialog.close();
            });

            Shortcuts.addShortcutListener(dialog, () -> {
                //message.setText("Cancelled...");
                dialog.close();
            }, Key.ESCAPE);
            dialog.add(new Div( confirmButton, cancelButton));




        });


    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> modeloDispositivoID = event.getRouteParameters().getInteger(MODELODISPOSITIVO_ID);
        if (modeloDispositivoID.isPresent()) {
            Optional<ModeloDispositivo> modeloDispositivoFromBackend = modeloDispositivoService.get(modeloDispositivoID.get());
            if (modeloDispositivoFromBackend.isPresent()) {

                populateForm(modeloDispositivoFromBackend.get());
            } else {

                Notification.show(
                        String.format("The requested modeloDispositivo was not found, ID = %d", modeloDispositivoID.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ModeloDispositivoView.class);
            }
        }

    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        id = new TextField("Id");
        id.setReadOnly(true);
        modelodispositivo = new TextField("Modelo de Dispositivo");
        idtipodispositivo = new ComboBox("Tipo de Dispositivo");
        path_drivers = new TextField("Path Drivers");

        Component[] fields = new Component[]{id, modelodispositivo,  idtipodispositivo,  path_drivers};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        nuevo.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        eliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonLayout.add(nuevo,guardar,eliminar);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        HorizontalLayout fl= new HorizontalLayout();
        fl.setSpacing(true);
        fl.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        txtFiltro = new TextField("");
        btnFiltro = new Button("Filtrar");
        btnFiltro.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        fl.add(txtFiltro,btnFiltro);

        wrapper.add(fl,grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getGenericDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(ModeloDispositivo value) {
        this.modeloDispositivo = value;
        //Tipos de Dispositivo
        List<TipoDispositivo> mList = tr.findAll();
        idtipodispositivo.setItems(mList);
        idtipodispositivo.setItemLabelGenerator(TipoDispositivo::getTipodispositivo);
        //
        binder.readBean(this.modeloDispositivo);

    }

    // constraints
    private String FKTipoDispositivo(ModeloDispositivo ob) {
        String salida ="";
        if (ob.getIdtipodispositivo()!=null) salida= ob.getIdtipodispositivo().getTipodispositivo();
        return salida;
    }


}
