package com.mwrfid.views.predio;

import java.util.Optional;

import com.mwrfid.data.entity.Predio;
import com.mwrfid.data.service.PredioService;

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

@PageTitle("Predio")
@Route(value = "predio/:predioID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
public class PredioView extends Div implements BeforeEnterObserver {

    private final String PREDIO_ID = "predioID";
    private final String PREDIO_EDIT_ROUTE_TEMPLATE = "predio/%d/edit";

    private Grid<Predio> grid = new Grid<>(Predio.class, false);

    private TextField id;
    private TextField predio;
    private TextField domicilio;
    private TextField observaciones;
    private TextField latitud;
    private TextField longitud;
    private TextField usuarioalt;
    private TextField usuarioact;
    private DateTimePicker fechaalt;
    private DateTimePicker fechaact;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Predio> binder;

    private Predio prediox;

    private PredioService predioService;

    public PredioView(@Autowired PredioService predioService) {
        this.predioService = predioService;
        addClassNames("predio-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("predio").setAutoWidth(true);
        grid.addColumn("domicilio").setAutoWidth(true);
        grid.addColumn("observaciones").setAutoWidth(true);
        grid.addColumn("latitud").setAutoWidth(true);
        grid.addColumn("longitud").setAutoWidth(true);
        grid.addColumn("usuarioalt").setAutoWidth(true);
        grid.addColumn("usuarioact").setAutoWidth(true);
        grid.addColumn("fechaalt").setAutoWidth(true);
        grid.addColumn("fechaact").setAutoWidth(true);
        grid.setItems(query -> predioService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PREDIO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(PredioView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Predio.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(id).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("id");
        binder.forField(latitud).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("latitud");
        binder.forField(longitud).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("longitud");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.prediox == null) {
                    this.prediox = new Predio();
                }
                binder.writeBean(this.prediox);

                predioService.update(this.prediox);
                clearForm();
                refreshGrid();
                Notification.show("Predio details stored.");
                UI.getCurrent().navigate(PredioView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the predio details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> predioId = event.getRouteParameters().getInteger(PREDIO_ID);
        if (predioId.isPresent()) {
            Optional<Predio> predioFromBackend = predioService.get(predioId.get());
            if (predioFromBackend.isPresent()) {
                populateForm(predioFromBackend.get());
            } else {
                Notification.show(String.format("The requested predio was not found, ID = %d", predioId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(PredioView.class);
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
        predio = new TextField("Predio");
        domicilio = new TextField("Domicilio");
        observaciones = new TextField("Observaciones");
        latitud = new TextField("Latitud");
        longitud = new TextField("Longitud");
        usuarioalt = new TextField("Usuarioalt");
        usuarioact = new TextField("Usuarioact");
        fechaalt = new DateTimePicker("Fechaalt");
        fechaalt.setStep(Duration.ofSeconds(1));
        fechaact = new DateTimePicker("Fechaact");
        fechaact.setStep(Duration.ofSeconds(1));
        Component[] fields = new Component[]{id, predio, domicilio, observaciones, latitud, longitud, usuarioalt,
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

    private void populateForm(Predio value) {
        this.prediox = value;
        binder.readBean(this.prediox);

    }
}
