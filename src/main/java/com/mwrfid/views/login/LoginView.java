package com.mwrfid.views.login;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import java.io.ByteArrayOutputStream;
import com.vaadin.flow.component.html.Image;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import elemental.json.Json;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import org.springframework.web.util.UriUtils;

@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay {
    public LoginView() {
        setAction("login");

        LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Middleware Trazabilidad Backend");
        i18n.getHeader().setDescription("Bienvenido");
        i18n.setAdditionalInformation(null);

        setI18n(i18n);
        setI18n(createSpanishI18n());
        setForgotPasswordButtonVisible(false);
        setOpened(true);
    }

    private LoginI18n createSpanishI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Middleware Trazabilidad Backend");
        i18n.getHeader().setDescription("Middleware Trazabilidad");
        i18n.getForm().setUsername("Usuario");
        i18n.getForm().setTitle("Acceda a su cuenta");
        i18n.getForm().setSubmit("Ingresar");
        i18n.getForm().setPassword("Clave");
        i18n.getForm().setForgotPassword("Olvide mi contraseña");
        i18n.getErrorMessage().setTitle("Usuario / Contraseña invalidos");
        i18n.getErrorMessage()
                .setMessage("Ingrese usuario y clave e intente nuevamente");
        i18n.setAdditionalInformation(
                "Version 1.0 - Playa Giron ");
        return i18n;
    }



}
