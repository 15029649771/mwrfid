package com.mwrfid.security;

import java.util.Optional;

import com.mwrfid.data.entity.Users;
import com.mwrfid.data.service.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

    @Autowired
    private UserRepository userRepository;

    private UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
            return userDetails;
        }
        return null;
    }

    public Optional<Users> get() {
        UserDetails details = getAuthenticatedUser();
        if (details == null) {
            return Optional.empty();
        }
        VaadinSession.getCurrent().setAttribute("username", details.getUsername());
        return Optional.of(userRepository.findByUsername(details.getUsername()));
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(SecurityConfiguration.LOGOUT_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }

}
