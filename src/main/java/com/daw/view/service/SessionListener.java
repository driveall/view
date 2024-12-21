package com.daw.view.service;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionListener implements HttpSessionListener {

    private boolean sessionsAttributeAdded = false;

    public void init(HttpSession session) {
        if (!sessionsAttributeAdded) {
            session.getServletContext().setAttribute("sessions", new ConcurrentHashMap<String, HttpSession>());
            sessionsAttributeAdded = true;
        }
    }

    public void sessionCreated(HttpSessionEvent event){
        var session = event.getSession();
        init(session);
        ((ConcurrentHashMap<String, HttpSession>)session.getServletContext().getAttribute("sessions"))
                .put(session.getId(), session);
    }
    public void sessionDestroyed(HttpSessionEvent event){
        HttpSession   session = event.getSession();
        ((ConcurrentHashMap<String, HttpSession>)session.getServletContext().getAttribute("sessions"))
                .remove(session.getId());
    }
}
