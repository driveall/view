package com.daw.view.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.daw.view.Constants.ATTRIBUTE_LOGIN;

public class SessionUtil {
    public static void addSessionAttribute(HttpServletRequest req, String login) {
        req.getSession().setAttribute(ATTRIBUTE_LOGIN, login);
    }

    public static void removeSessionAttribute(HttpServletRequest req) {
        req.getSession().removeAttribute(ATTRIBUTE_LOGIN);
    }

    public static String getSessionAttribute(HttpServletRequest req) {
        return (String) req.getSession().getAttribute(ATTRIBUTE_LOGIN);
    }

    public static void redirect(HttpServletResponse resp, String path) {
        try {
            resp.sendRedirect(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
