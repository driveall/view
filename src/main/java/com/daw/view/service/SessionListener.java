package com.daw.view.service;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

import static com.daw.view.Constants.ATTRIBUTE_LOGIN;
import static com.daw.view.Constants.ATTRIBUTE_SESSIONS;
import static com.daw.view.service.ViewService.logins;

@Configuration()
public class SessionListener implements HttpSessionListener {

    private final ServletContext servletContext;
    private boolean sessionsAttributeAdded = false;

    public SessionListener(ServletContext servletContext) {
        this.servletContext = servletContext;
        init();
    }

    public void init() {
        if (!sessionsAttributeAdded) {
            servletContext.setAttribute("sessions", new ConcurrentHashMap<String, HttpSession>());
            sessionsAttributeAdded = true;
        }
    }

    public void sessionCreated(HttpSessionEvent event){
        ((ConcurrentHashMap<String, HttpSession>)servletContext.getAttribute(ATTRIBUTE_SESSIONS))
                .put(event.getSession().getId(), event.getSession());
    }
    public void sessionDestroyed(HttpSessionEvent event){
        ((ConcurrentHashMap<String, HttpSession>)servletContext.getAttribute(ATTRIBUTE_SESSIONS))
                .remove(event.getSession().getId());
        var login = event.getSession().getAttribute(ATTRIBUTE_LOGIN);
        if (login != null) {
            logins.remove(login);
        }
    }
}
