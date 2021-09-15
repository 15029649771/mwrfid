package com.mwrfid.views.tipodispositivo;

import java.util.Optional;

import com.mwrfid.data.entity.TipoDispositivo;
import com.mwrfid.data.service.TipoDispositivoService;

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
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.data.converter.StringToIntegerConverter;

@PageTitle("Tipo Dispositivo")
@Route(value = "tipodispositivo/:tipoDispositivoID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
@Uses(Icon.class)
public class TipoDispositivoView extends Div implements BeforeEnterObserver {

    private final String TIPODISPOSITIVO_ID = "tipoDispositivoID";
    private final String TIPODISPOSITIVO_EDIT_ROUTE_TEMPLATE = "tipodispositivo/%d/edit";

    private Grid<TipoDispositivo> grid = new Grid<>(TipoDispositivo.class, false);

    private TextField id;
    private TextField tipodispositivo;
    private Checkbox lectura;
    private Checkbox escritura;
    private TextField usuarioalt;
    private TextField usuarioact;
    private DateTimePicker fechaalt;
    private DateTimePicker fechaact;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<TipoDispositivo> binder;

    private TipoDispositivo tipoDispositivo;

    private TipoDispositivoService tipoDispositivoService;

    public TipoDispositivoView(@Autowired TipoDispositivoService tipoDispositivoService) {
        this.tipoDispositivoService = tipoDispositivoService;
        addClassNames("tipo-dispositivo-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("tipodispositivo").setAutoWidth(true);
        TemplateRenderer<TipoDispositivo> lecturaRenderer = TemplateRenderer.<TipoDispositivo>of(
                "<vaadin-icon hidden='[[!item.lectura]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></vaadin-icon><vaadin-icon hidden='[[item.lectura]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></vaadin-icon>")
                .withProperty("lectura", TipoDispositivo::isLectura);
        grid.addColumn(lecturaRenderer).setHeader("Lectura").setAutoWidth(true);

        TemplateRenderer<TipoDispositivo> escrituraRenderer = TemplateRenderer.<TipoDispositivo>of(
                "<vaadin-icon hidden='[[!item.escritura]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></vaadin-icon><vaadin-icon hidden='[[item.escritura]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></vaadin-icon>")
                .withProperty("escritura", TipoDispositivo::isEscritura);
        grid.addColumn(escrituraRenderer).setHeader("Escritura").setAutoWidth(true);

        grid.addColumn("usuarioalt").setAutoWidth(true);
        grid.addColumn("usuarioact").setAutoWidth(true);
        grid.addColumn("fechaalt").setAutoWidth(true);
        grid.addColumn("fechaact").setAutoWidth(true);
        grid.setItems(query -> tipoDispositivoService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(TIPODISPOSITIVO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(TipoDispositivoView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(TipoDispositivo.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(id).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("id");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.tipoDispositivo == null) {
                    this.tipoDispositivo = new TipoDispositivo();
                }
                binder.writeBean(this.tipoDispositivo);

                tipoDispositivoService.update(this.tipoDispositivo);
                clearForm();
                refreshGrid();
                Notification.show("TipoDispositivo details stored.");
                UI.getCurrent().navigate(TipoDispositivoView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the tipoDispositivo details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> tipoDispositivoId = event.getRouteParameters().getInteger(TIPODISPOSITIVO_ID);
        if (tipoDispositivoId.isPresent()) {
            Optional<TipoDispositivo> tipoDispositivoFromBackend = tipoDispositivoService.get(tipoDispositivoId.get());
            if (tipoDispositivoFromBackend.isPresent()) {
                populateForm(tipoDispositivoFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested tipoDispositivo was not found, ID = %d", tipoDispositivoId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(TipoDispositivoView.class);
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
        tipodispositivo = new TextField("Tipodispositivo");
        lectura = new Checkbox("Lectura");
        lectura.getStyle().set("padding-top", "var(--lumo-space-m)");
        escritura = new Checkbox("Escritura");
        escritura.getStyle().set("padding-top", "var(--lumo-space-m)");
        usuarioalt = new TextField("Usuarioalt");
        usuarioact = new TextField("Usuarioact");
        fechaalt = new DateTimePicker("Fechaalt");
        fechaalt.setStep(Duration.ofSeconds(1));
        fechaact = new DateTimePicker("Fechaact");
        fechaact.setStep(Duration.ofSeconds(1));
        Component[] fields = new Component[]{id, tipodispositivo, lectura, escritura, usuarioalt, usuarioact, fechaalt,
                fechaact};

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

    private void populateForm(TipoDispositivo value) {
        this.tipoDispositivo = value;
        binder.readBean(this.tipoDispositivo);

    }
}
