package com.mwrfid.views.dispositivo;

import com.mwrfid.data.entity.Dispositivo;
import com.mwrfid.data.entity.ModeloDispositivo;
import com.mwrfid.data.entity.TipoDispositivo;
import com.mwrfid.data.service.DispositivoService;
import com.mwrfid.data.service.DispositivoRepository;
import com.mwrfid.data.service.ModeloDispositivoRepository;
import com.mwrfid.views.MainLayout;
//import com.sun.org.apache.xpath.internal.operations.Mod;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import javax.annotation.security.PermitAll;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@PageTitle("Dispositivo")
@Route(value = "dispositivo/:dispositivoID?/:action?(edit)", layout = MainLayout.class)

@PermitAll
@Uses(Icon.class)
@Uses(Icon.class)
public class DispositivoView extends Div implements BeforeEnterObserver {
    @Autowired
    ModeloDispositivoRepository tr;

    private final String DISPOSITIVO_ID = "dispositivoID";
    private final String DISPOSITIVO_EDIT_ROUTE_TEMPLATE = "dispositivo/%d/edit";

    private Grid<Dispositivo> grid = new Grid<>(Dispositivo.class, false);

    private TextField id;
    private TextField dispositivo;
    private ComboBox<ModeloDispositivo> idmodelodispositivo;
    private TextField cantidad_puertos;
    private TextField observaciones;


    private TextField txtFiltro;
    private Button btnFiltro;

    private Button nuevo = new Button("Nuevo");
    private Button guardar = new Button("Guardar");
    private Button eliminar = new Button("Eliminar");

    private BeanValidationBinder<Dispositivo> binder;

    private Dispositivo dispositivoC;

    private DispositivoService dispositivoService;

    public DispositivoView(@Autowired DispositivoService dispositivoService) {
        this.dispositivoService = dispositivoService;
        addClassNames("dispositivo-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        eliminar.setEnabled(false);
        txtFiltro.setPlaceholder("Ingrese ocurrencia");


        // Configure Grid
        grid.addColumn("id").setAutoWidth(true).setHeader("Id");
        grid.addColumn("dispositivo").setAutoWidth(true).setHeader("Dispositivo");
        grid.addColumn("cantidad_puertos").setAutoWidth(true).setHeader("N.Puertos");
        grid.addColumn("observaciones").setAutoWidth(true).setHeader("Observaciones");
        grid.addComponentColumn(item -> auditButton(grid, item))
                .setHeader("Auditar");


        grid.setItems(query -> dispositivoService.list(
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
                UI.getCurrent().navigate(String.format(DISPOSITIVO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                eliminar.setEnabled(false);
                //guardar.setEnabled(false);
                clearForm();
                UI.getCurrent().navigate(DispositivoView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Dispositivo.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(id).withConverter(new StringToIntegerConverter("Solo se pueden ingresar numeros")).bind("id");
        binder.forField(cantidad_puertos).withConverter(new StringToIntegerConverter("Solo se pueden cargar numeros"))
                .bind("cantidad_puertos");

        binder.bindInstanceFields(this);

        nuevo.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        guardar.addClickListener(e -> {
            try {
                LocalDateTime now = LocalDateTime.now();
                String usuario=   VaadinSession.getCurrent().getAttribute("username").toString();
                if (this.dispositivoC == null) {
                    this.dispositivoC = new Dispositivo();
                    this.dispositivoC.setUsuarioalt(usuario);
                    this.dispositivoC.setFechaalt(now);
                } else {
                    this.dispositivoC.setFechaact(now);
                    this.dispositivoC.setUsuarioact(usuario);
                }
                binder.writeBean(this.dispositivoC);
                dispositivoService.update(this.dispositivoC);
                clearForm();
                refreshGrid();
                Notification.show("Se guardo el registro.");
                UI.getCurrent().navigate(DispositivoView.class);
            } catch (ValidationException validationException) {
                Notification.show("Error: ha ocurrido una excepcion al intentar grabar en la base de datos.");
            }
        });

        // manejo del fitro
        btnFiltro.addClickListener(e -> {
            try {
                if (txtFiltro.getValue().equalsIgnoreCase("")) {
                    grid.setItems(dispositivoService.findAll());
                } else {
                    grid.setItems(dispositivoService.findAll(txtFiltro.getValue()));
                    refreshGrid();
                }
                refreshGrid();
                Notification.show("Actualizacion correcta.");
                UI.getCurrent().navigate(DispositivoView.class);
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
                dispositivoService.delete(this.dispositivoC.getId());
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
        Optional<Integer> dispositivoID = event.getRouteParameters().getInteger(DISPOSITIVO_ID);
        if (dispositivoID.isPresent()) {
            Optional<Dispositivo> dispositivoFromBackend = dispositivoService.get(dispositivoID.get());
            if (dispositivoFromBackend.isPresent()) {

                populateForm(dispositivoFromBackend.get());
            } else {

                Notification.show(
                        String.format("The requested dispositivo was not found, ID = %d", dispositivoID.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(DispositivoView.class);
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
        dispositivo = new TextField("Dispositivo");
        idmodelodispositivo = new ComboBox("Modelo de Dispositivo");
        cantidad_puertos = new TextField("Cantidad_puertos");
        observaciones = new TextField("Observaciones");
        Component[] fields = new Component[]{id, dispositivo,  idmodelodispositivo, cantidad_puertos,observaciones};

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

    private void populateForm(Dispositivo value) {
        this.dispositivoC = value;
        //Modelos  de Dispositivo
        List<ModeloDispositivo> mList = tr.findAll();
        idmodelodispositivo.setItems(mList);
        idmodelodispositivo.setItemLabelGenerator(ModeloDispositivo::getModelodispositivo);
        //
        binder.readBean(this.dispositivoC);

    }
    private Button auditButton(Grid<Dispositivo> grid, Dispositivo item) {
        @SuppressWarnings("unchecked")
        Button button = new Button("Auditar", clickEvent -> {
            VerticalLayout fm = new VerticalLayout();
            Dialog dialog = new Dialog();

            String usuarioalt="";
            if (item.getUsuarioalt()==null)  usuarioalt+="No Registrado "; else usuarioalt+=item.getUsuarioalt();
            TextField spUsuarioalt= new TextField("Usuario Alta: ");
            spUsuarioalt.setValue(usuarioalt);
            spUsuarioalt.setReadOnly(true);

            String usuarioact="";
            if (item.getUsuarioact()==null)  usuarioact+="No Registrado "; else usuarioact+=item.getUsuarioact();
            TextField spUsuarioact= new TextField("Usuario Actualizacion: ");
            spUsuarioact.setValue(usuarioact);
            spUsuarioact.setReadOnly(true);

            String fechaalt="";
            if (item.getFechaalt()==null)  fechaalt+="No Registrada "; else fechaalt+=item.getFechaalt();
            TextField spFechaalt = new TextField("Fecha de Alta: ");
            spFechaalt.setValue(fechaalt);
            spFechaalt.setReadOnly(true);

            String fechaact="";
            if (item.getFechaact()==null)  fechaact+="No Registrada "; else fechaact+=item.getFechaact();
            TextField spFechaact = new TextField("Fecha Ultima Actualizacion: ");
            spFechaact.setValue(fechaact);
            spFechaact.setReadOnly(true);

            fm.add(spUsuarioalt, spUsuarioact, spFechaalt, spFechaact);
            dialog.add(fm);


            dialog.setWidth("250px");
            dialog.setHeight("400px");
            dialog.open();

        });
        return button;
    }
}
