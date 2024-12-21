package com.daw.view.service;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

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
        ((ConcurrentHashMap<String, HttpSession>)servletContext.getAttribute("sessions"))
                .put(event.getSession().getId(), event.getSession());
    }
    public void sessionDestroyed(HttpSessionEvent event){
        ((ConcurrentHashMap<String, HttpSession>)servletContext.getAttribute("sessions"))
                .remove(event.getSession().getId());
    }
}
