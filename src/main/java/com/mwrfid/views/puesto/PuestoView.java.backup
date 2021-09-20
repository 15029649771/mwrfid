package com.mwrfid.views.puesto;

import java.util.Optional;

import com.mwrfid.data.entity.Puesto;
import com.mwrfid.data.service.PuestoService;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.mwrfid.views.MainLayout;
import javax.annotation.security.RolesAllowed;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import java.time.Duration;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToIntegerConverter;

@PageTitle("Puesto")
@Route(value = "puesto/:puestoID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("admin")
public class PuestoView extends Div implements BeforeEnterObserver {

    private final String PUESTO_ID = "puestoID";
    private final String PUESTO_EDIT_ROUTE_TEMPLATE = "puesto/%d/edit";

    private Grid<Puesto> grid = new Grid<>(Puesto.class, false);

    private TextField id;
    private TextField puesto;
    private TextField iddispositivo;
    private TextField puerto;
    private TextField observaciones;
    private TextField latitud;
    private TextField longitud;
    private TextField usuarioalt;
    private TextField usuarioact;
    private DateTimePicker fechaalt;
    private DateTimePicker fechaact;
    private TextField idpredio;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Puesto> binder;

    private Puesto puestox;

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

        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("puesto").setAutoWidth(true);
        grid.addColumn("iddispositivo").setAutoWidth(true);
        grid.addColumn("puerto").setAutoWidth(true);
        grid.addColumn("observaciones").setAutoWidth(true);
        grid.addColumn("latitud").setAutoWidth(true);
        grid.addColumn("longitud").setAutoWidth(true);
        grid.addColumn("usuarioalt").setAutoWidth(true);
        grid.addColumn("usuarioact").setAutoWidth(true);
        grid.addColumn("fechaalt").setAutoWidth(true);
        grid.addColumn("fechaact").setAutoWidth(true);
        grid.addColumn("idpredio").setAutoWidth(true);
        grid.setItems(query -> puestoService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PUESTO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(PuestoView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Puesto.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(id).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("id");
        binder.forField(iddispositivo).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("iddispositivo");
        binder.forField(puerto).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("puerto");
        binder.forField(latitud).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("latitud");
        binder.forField(longitud).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("longitud");
        binder.forField(idpredio).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("idpredio");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.puestox == null) {
                    this.puestox = new Puesto();
                }
                binder.writeBean(this.puestox);

                puestoService.update(this.puestox);
                clearForm();
                refreshGrid();
                Notification.show("Puesto details stored.");
                UI.getCurrent().navigate(PuestoView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the puesto details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> puestoId = event.getRouteParameters().getInteger(PUESTO_ID);
        if (puestoId.isPresent()) {
            Optional<Puesto> puestoFromBackend = puestoService.get(puestoId.get());
            if (puestoFromBackend.isPresent()) {
                populateForm(puestoFromBackend.get());
            } else {
                Notification.show(String.format("The requested puesto was not found, ID = %d", puestoId.get()), 3000,
                        Notification.Position.BOTTOM_START);
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
        puesto = new TextField("Puesto");
        iddispositivo = new TextField("Iddispositivo");
        puerto = new TextField("Puerto");
        observaciones = new TextField("Observaciones");
        latitud = new TextField("Latitud");
        longitud = new TextField("Longitud");
        usuarioalt = new TextField("Usuarioalt");
        usuarioact = new TextField("Usuarioact");
        fechaalt = new DateTimePicker("Fechaalt");
        fechaalt.setStep(Duration.ofSeconds(1));
        fechaact = new DateTimePicker("Fechaact");
        fechaact.setStep(Duration.ofSeconds(1));
        idpredio = new TextField("Idpredio");
        Component[] fields = new Component[]{id, puesto, iddispositivo, puerto, observaciones, latitud, longitud,
                usuarioalt, usuarioact, fechaalt, fechaact, idpredio};

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
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Puesto value) {
        this.puestox = value;
        binder.readBean(this.puestox);

    }
}
