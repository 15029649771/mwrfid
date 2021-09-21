package com.mwrfid.views.puesto;

import com.mwrfid.data.entity.Predio;
import com.mwrfid.data.entity.Puesto;
import com.mwrfid.data.entity.Dispositivo;
import com.mwrfid.data.entity.TipoDispositivo;
import com.mwrfid.data.service.DispositivoRepository;
import com.mwrfid.data.service.PredioRepository;
import com.mwrfid.data.service.PuestoService;
import com.mwrfid.data.service.TipoDispositivoRepository;
import com.mwrfid.views.MainLayout;
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

@PageTitle("Puesto")
@Route(value = "puesto/:puestoID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
@Uses(Icon.class)
public class PuestoView extends Div implements BeforeEnterObserver {
    @Autowired
    DispositivoRepository dr;

    @Autowired
    PredioRepository pr;

    private final String PUESTO_ID = "puestoID";
    private final String PUESTO_EDIT_ROUTE_TEMPLATE = "puesto/%d/edit";

    private Grid<Puesto> grid = new Grid<>(Puesto.class, false);

    private TextField id;
    private TextField puesto;
    private ComboBox<Dispositivo> iddispositivo;
    private ComboBox<Predio> idpredio;
    private TextField puerto;
    private TextField observaciones;


    private TextField txtFiltro;
    private Button btnFiltro;

    private Button nuevo = new Button("Nuevo");
    private Button guardar = new Button("Guardar");
    private Button eliminar = new Button("Eliminar");

    private BeanValidationBinder<Puesto> binder;

    private Puesto puestoC;

    private PuestoService puestoService;

    public PuestoView(@Autowired PuestoService puestoService) {
        this.puestoService = puestoService;
        addClassNames("puesto-view", "flex", "flex-col", "h-full");

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
        grid.addColumn("puesto").setAutoWidth(true);
        grid.addColumn(this::FKDispositivo).setHeader("Dispositivo").setAutoWidth(true).setSortable(true);
        grid.addColumn(this::FKPredio).setHeader("Predio").setAutoWidth(true).setSortable(true);
        grid.addColumn("puerto").setAutoWidth(true).setHeader("N.Puerto");
        grid.addColumn("observaciones").setAutoWidth(true).setHeader("Observaciones");
        grid.addComponentColumn(item -> auditButton(grid, item))
                .setHeader("Auditar");

        grid.setItems(query -> puestoService.list(
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
                UI.getCurrent().navigate(String.format(PUESTO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                eliminar.setEnabled(false);
                //guardar.setEnabled(false);
                clearForm();
                UI.getCurrent().navigate(PuestoView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Puesto.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(id).withConverter(new StringToIntegerConverter("Solo se pueden ingresar numeros")).bind("id");
        binder.forField(puerto).withConverter(new StringToIntegerConverter("Solo numeros")).bind("puerto");
        binder.bindInstanceFields(this);

        nuevo.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        guardar.addClickListener(e -> {
            try {
                LocalDateTime now = LocalDateTime.now();
                String usuario=   VaadinSession.getCurrent().getAttribute("username").toString();
                if (this.puestoC == null) {
                    this.puestoC = new Puesto();
                    this.puestoC.setUsuarioalt(usuario);
                    this.puestoC.setFechaalt(now);
                } else {
                    this.puestoC.setFechaact(now);
                    this.puestoC.setUsuarioact(usuario);
                }

                binder.writeBean(this.puestoC);

                puestoService.update(this.puestoC);
                clearForm();
                refreshGrid();
                Notification.show("Se guardo el registro.");
                UI.getCurrent().navigate(PuestoView.class);
            } catch (ValidationException validationException) {
                Notification.show("Error: ha ocurrido una excepcion al intentar grabar en la base de datos.");
            }
        });

        // manejo del fitro
        btnFiltro.addClickListener(e -> {
            try {
                if (txtFiltro.getValue().equalsIgnoreCase("")) {
                    grid.setItems(puestoService.findAll());
                } else {
                    grid.setItems(puestoService.findAll(txtFiltro.getValue()));
                    refreshGrid();
                }
                refreshGrid();
                Notification.show("Actualizacion correcta.");
                UI.getCurrent().navigate(PuestoView.class);
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
                puestoService.delete(this.puestoC.getId());
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
        Optional<Integer> puestoID = event.getRouteParameters().getInteger(PUESTO_ID);
        if (puestoID.isPresent()) {
            Optional<Puesto> puestoFromBackend = puestoService.get(puestoID.get());
            if (puestoFromBackend.isPresent()) {

                populateForm(puestoFromBackend.get());
            } else {

                Notification.show(
                        String.format("The requested puesto was not found, ID = %d", puestoID.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(PuestoView.class);
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
        puesto = new TextField("Puesto");
        iddispositivo = new ComboBox("Dispositivo");
        idpredio = new ComboBox("Predio");
        puerto = new TextField("Puerto");
        observaciones = new TextField("Observaciones");

        Component[] fields = new Component[]{id, puesto,  iddispositivo, idpredio, puerto, observaciones};

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

    private void populateForm(Puesto value) {
        this.puestoC = value;
        //Tipos de Dispositivo
        List<Dispositivo> mList = dr.findAll();
        iddispositivo.setItems(mList);
        iddispositivo.setItemLabelGenerator(Dispositivo::getDispositivo);

        //Predios
        List<Predio> pList = pr.findAll();
        idpredio.setItems(pList);
        idpredio.setItemLabelGenerator(Predio::getPredio);

        binder.readBean(this.puestoC);

    }

    // constraints
    private String FKDispositivo(Puesto ob) {
        String salida ="";
        if (ob.getIddispositivo()!=null) salida= ob.getIddispositivo().getDispositivo();
        return salida;
    }


    private String FKPredio(Puesto ob) {
        String salida ="";
        if (ob.getIdpredio()!=null) salida= ob.getIdpredio().getPredio();
        return salida;
    }
    private Button auditButton(Grid<Puesto> grid, Puesto item) {
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
