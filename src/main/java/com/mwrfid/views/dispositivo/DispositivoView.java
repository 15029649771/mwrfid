package com.mwrfid.views.dispositivo;

import java.util.Optional;

import com.mwrfid.data.entity.Dispositivo;
import com.mwrfid.data.service.DispositivoService;

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

@PageTitle("Dispositivo")
@Route(value = "dispositivo/:dispositivoID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
public class DispositivoView extends Div implements BeforeEnterObserver {

    private final String DISPOSITIVO_ID = "dispositivoID";
    private final String DISPOSITIVO_EDIT_ROUTE_TEMPLATE = "dispositivo/%d/edit";

    private Grid<Dispositivo> grid = new Grid<>(Dispositivo.class, false);

    private TextField id;
    private TextField dispositivo;
    private TextField idmodelodispositivo;
    private TextField cantidad_puertos;
    private TextField observaciones;
    private TextField usuarioalt;
    private TextField usuarioact;
    private DateTimePicker fechaalt;
    private DateTimePicker fechaact;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Dispositivo> binder;

    private Dispositivo dispositivo;

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

        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("dispositivo").setAutoWidth(true);
        grid.addColumn("idmodelodispositivo").setAutoWidth(true);
        grid.addColumn("cantidad_puertos").setAutoWidth(true);
        grid.addColumn("observaciones").setAutoWidth(true);
        grid.addColumn("usuarioalt").setAutoWidth(true);
        grid.addColumn("usuarioact").setAutoWidth(true);
        grid.addColumn("fechaalt").setAutoWidth(true);
        grid.addColumn("fechaact").setAutoWidth(true);
        grid.setItems(query -> dispositivoService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(DISPOSITIVO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(DispositivoView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Dispositivo.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(id).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("id");
        binder.forField(idmodelodispositivo).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("idmodelodispositivo");
        binder.forField(cantidad_puertos).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("cantidad_puertos");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.dispositivo == null) {
                    this.dispositivo = new Dispositivo();
                }
                binder.writeBean(this.dispositivo);

                dispositivoService.update(this.dispositivo);
                clearForm();
                refreshGrid();
                Notification.show("Dispositivo details stored.");
                UI.getCurrent().navigate(DispositivoView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the dispositivo details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> dispositivoId = event.getRouteParameters().getInteger(DISPOSITIVO_ID);
        if (dispositivoId.isPresent()) {
            Optional<Dispositivo> dispositivoFromBackend = dispositivoService.get(dispositivoId.get());
            if (dispositivoFromBackend.isPresent()) {
                populateForm(dispositivoFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested dispositivo was not found, ID = %d", dispositivoId.get()), 3000,
                        Notification.Position.BOTTOM_START);
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
        dispositivo = new TextField("Dispositivo");
        idmodelodispositivo = new TextField("Idmodelodispositivo");
        cantidad_puertos = new TextField("Cantidad_puertos");
        observaciones = new TextField("Observaciones");
        usuarioalt = new TextField("Usuarioalt");
        usuarioact = new TextField("Usuarioact");
        fechaalt = new DateTimePicker("Fechaalt");
        fechaalt.setStep(Duration.ofSeconds(1));
        fechaact = new DateTimePicker("Fechaact");
        fechaact.setStep(Duration.ofSeconds(1));
        Component[] fields = new Component[]{id, dispositivo, idmodelodispositivo, cantidad_puertos, observaciones,
                usuarioalt, usuarioact, fechaalt, fechaact};

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

    private void populateForm(Dispositivo value) {
        this.dispositivo = value;
        binder.readBean(this.dispositivo);

    }
}
