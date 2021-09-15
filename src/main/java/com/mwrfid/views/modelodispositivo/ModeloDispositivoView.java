package com.mwrfid.views.modelodispositivo;

import java.util.Optional;

import com.mwrfid.data.entity.ModeloDispositivo;
import com.mwrfid.data.service.ModeloDispositivoService;

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
import javax.annotation.security.PermitAll;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import java.time.Duration;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToIntegerConverter;

@PageTitle("Modelo Dispositivo")
@Route(value = "modelodispositivo/:modeloDispositivoID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
public class ModeloDispositivoView extends Div implements BeforeEnterObserver {

    private final String MODELODISPOSITIVO_ID = "modeloDispositivoID";
    private final String MODELODISPOSITIVO_EDIT_ROUTE_TEMPLATE = "modelodispositivo/%d/edit";

    private Grid<ModeloDispositivo> grid = new Grid<>(ModeloDispositivo.class, false);

    private TextField id;
    private TextField modelodispositivo;
    private TextField idtipodispositivo;
    private TextField path_drivers;
    private TextField usuarioalt;
    private TextField usuarioact;
    private DateTimePicker fechaalt;
    private DateTimePicker fechaact;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<ModeloDispositivo> binder;

    private ModeloDispositivo modeloDispositivo;

    private ModeloDispositivoService modeloDispositivoService;

    public ModeloDispositivoView(@Autowired ModeloDispositivoService modeloDispositivoService) {
        this.modeloDispositivoService = modeloDispositivoService;
        addClassNames("modelo-dispositivo-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("modelodispositivo").setAutoWidth(true);
        grid.addColumn("idtipodispositivo").setAutoWidth(true);
        grid.addColumn("path_drivers").setAutoWidth(true);
        grid.addColumn("usuarioalt").setAutoWidth(true);
        grid.addColumn("usuarioact").setAutoWidth(true);
        grid.addColumn("fechaalt").setAutoWidth(true);
        grid.addColumn("fechaact").setAutoWidth(true);
        grid.setItems(query -> modeloDispositivoService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent()
                        .navigate(String.format(MODELODISPOSITIVO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ModeloDispositivoView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(ModeloDispositivo.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(id).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("id");
        binder.forField(idtipodispositivo).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("idtipodispositivo");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.modeloDispositivo == null) {
                    this.modeloDispositivo = new ModeloDispositivo();
                }
                binder.writeBean(this.modeloDispositivo);

                modeloDispositivoService.update(this.modeloDispositivo);
                clearForm();
                refreshGrid();
                Notification.show("ModeloDispositivo details stored.");
                UI.getCurrent().navigate(ModeloDispositivoView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the modeloDispositivo details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> modeloDispositivoId = event.getRouteParameters().getInteger(MODELODISPOSITIVO_ID);
        if (modeloDispositivoId.isPresent()) {
            Optional<ModeloDispositivo> modeloDispositivoFromBackend = modeloDispositivoService
                    .get(modeloDispositivoId.get());
            if (modeloDispositivoFromBackend.isPresent()) {
                populateForm(modeloDispositivoFromBackend.get());
            } else {
                Notification.show(String.format("The requested modeloDispositivo was not found, ID = %d",
                        modeloDispositivoId.get()), 3000, Notification.Position.BOTTOM_START);
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
        modelodispositivo = new TextField("Modelodispositivo");
        idtipodispositivo = new TextField("Idtipodispositivo");
        path_drivers = new TextField("Path_drivers");
        usuarioalt = new TextField("Usuarioalt");
        usuarioact = new TextField("Usuarioact");
        fechaalt = new DateTimePicker("Fechaalt");
        fechaalt.setStep(Duration.ofSeconds(1));
        fechaact = new DateTimePicker("Fechaact");
        fechaact.setStep(Duration.ofSeconds(1));
        Component[] fields = new Component[]{id, modelodispositivo, idtipodispositivo, path_drivers, usuarioalt,
                usuarioact, fechaalt, fechaact};

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

    private void populateForm(ModeloDispositivo value) {
        this.modeloDispositivo = value;
        binder.readBean(this.modeloDispositivo);

    }
}
