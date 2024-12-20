package com.daw.view.controller;

import com.daw.view.entity.AccountEntity;
import com.daw.view.entity.ItemEntity;
import com.daw.view.service.ValidationService;
import com.daw.view.service.ViewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.HashSet;

import static com.daw.view.Constants.*;
import static com.daw.view.util.SessionUtil.*;

@EnableWebMvc
@Controller
@Slf4j
public class ViewController {
    private final ViewService viewService;
    private final ValidationService validationService;

    public ViewController(ViewService viewService, ValidationService validationService) {
        this.viewService = viewService;
        this.validationService = validationService;
    }

    @GetMapping({"/index", "/"})
    public ModelAndView getIndex(HttpServletRequest req,
                                 HttpServletResponse resp) {
        var login = getSessionAttribute(req);
        if (login == null) {
            log.info("index get");
            return new ModelAndView("index");
        } else {
            log.info("index get for {}, redirect", login);
            redirect(resp, SUCCESS_PAGE_PATH);
            return null;
        }
    }

    @GetMapping("/login")
    public ModelAndView getLogin(HttpServletRequest req,
                                 HttpServletResponse resp) {
        var login = getSessionAttribute(req);
        if (login == null) {
            log.info("login get");
            return new ModelAndView("login");
        } else {
            log.info("login get for {}, redirect", login);
            redirect(resp, SUCCESS_PAGE_PATH);
            return null;
        }
    }

    @GetMapping("/register")
    public ModelAndView getRegister(HttpServletRequest req,
                                    HttpServletResponse resp) {
        var login = getSessionAttribute(req);
        if (login == null) {
            log.info("register get");
            return new ModelAndView("register");
        } else {
            log.info("register get for {}, redirect", login);
            redirect(resp, SUCCESS_PAGE_PATH);
            return null;
        }
    }

    @GetMapping("/success")
    public ModelAndView getSuccess(HttpServletRequest req,
                                   HttpServletResponse resp) throws IOException {
        var login = getSessionAttribute(req);
        log.info("success get for {}", login);
        if (login != null) {
            var account = viewService.getByLogin(login);
            return new ModelAndView("main")
                    .addObject("account", account);
        }
        resp.sendRedirect(INDEX_PAGE_PATH);
        return null;
    }

    @GetMapping("/shop")
    public ModelAndView getShop(HttpServletRequest req,
                                HttpServletResponse resp) throws IOException {
        var login = getSessionAttribute(req);
        log.info("shop get login {}", login);
        if (login != null) {
            var account = viewService.getByLogin(login);
            var stuff = new HashSet<ItemEntity>();
            stuff.add(viewService.getItem("ITEM:1"));
            stuff.add(viewService.getItem("ITEM:2"));
            stuff.add(viewService.getItem("ITEM:3"));
            stuff.add(viewService.getItem("ITEM:4"));
            stuff.removeAll(account.getStorage());
            return new ModelAndView("shop")
                    .addObject("account", account)
                    .addObject("stuff", stuff);
        }
        resp.sendRedirect(INDEX_PAGE_PATH);
        return null;
    }

    @GetMapping("/wear")
    public ModelAndView getWear(HttpServletRequest req,
                                HttpServletResponse resp) throws IOException {
        var login = getSessionAttribute(req);
        log.info("wear get for {}", login);
        if (login != null) {
            var account = viewService.getByLogin(login);
            return new ModelAndView("wear")
                    .addObject("account", account);
        }
        resp.sendRedirect(INDEX_PAGE_PATH);
        return null;
    }

    @GetMapping("/update")
    public ModelAndView getUpdate(HttpServletRequest req,
                                  HttpServletResponse resp) throws IOException {
        var login = getSessionAttribute(req);
        log.info("update get for {}", login);
        if (login != null) {
            var account = viewService.getByLogin(login);
            return new ModelAndView("update")
                    .addObject("account", account);
        }
        resp.sendRedirect(INDEX_PAGE_PATH);
        return null;
    }

    @PostMapping("/login")
    public void login(@NonNull @RequestParam String login,
                      @NonNull @RequestParam String pass,
                      HttpServletRequest req,
                      HttpServletResponse resp) {
        log.info("login for {}", login);
        var accountEntity = viewService.getByLogin(login);
        if (accountEntity != null && viewService.login(accountEntity, pass)) {
            addSessionAttribute(req, accountEntity.getLogin());
            redirect(resp, SUCCESS_PAGE_PATH);
        } else {
            redirect(resp, INDEX_PAGE_PATH);
        }
    }

    @PostMapping("/register")
    public void register(@NonNull @RequestParam String login,
                         @NonNull @RequestParam String pass,
                         @NonNull @RequestParam String pass2,
                         @Nullable @RequestParam(required = false) String email,
                         @Nullable @RequestParam(required = false) String phone,
                         HttpServletRequest req,
                         HttpServletResponse resp) {
        log.info("register for {}", login);
        var accountEntity = AccountEntity.builder()
                .login(login)
                .password(pass)
                .passwordConfirmed(pass2)
                .build();
        if (email != null && !email.isEmpty() && validationService.validateEmailAddress(email)) {
            accountEntity.setEmail(email);
        }
        if (phone != null && !phone.isEmpty() && validationService.validatePhoneNumber(phone)) {
            accountEntity.setPhone(validationService.formatPhoneNumber(phone));
        }
        viewService.createAccount(accountEntity);
        accountEntity = viewService.getByLogin(login);
        if (accountEntity != null) {
            addSessionAttribute(req, accountEntity.getLogin());
            redirect(resp, SUCCESS_PAGE_PATH);
        } else {
            redirect(resp, INDEX_PAGE_PATH);
        }
    }

    @PostMapping("/update")
    public void update(@NonNull @RequestParam String login,
                       @NonNull @RequestParam String pass,
                       @NonNull @RequestParam String pass2,
                       @NonNull @RequestParam String email,
                       @NonNull @RequestParam String phone,
                       HttpServletRequest req,
                       HttpServletResponse resp) {
        log.info("update for {}", login);
        var accountEntity = AccountEntity.builder()
                .login(login)
                .build();
        if (!pass.isEmpty() && pass.equals(pass2)) {
            accountEntity.setPassword(pass);
            accountEntity.setPasswordConfirmed(pass2);
        }
        if (!email.isEmpty() && validationService.validateEmailAddress(email)) {
            accountEntity.setEmail(email);
        }
        if (!phone.isEmpty() && validationService.validatePhoneNumber(phone)) {
            accountEntity.setPhone(validationService.formatPhoneNumber(phone));
        }

        viewService.updateAccount(accountEntity);
        accountEntity = viewService.getByLogin(login);
        if (accountEntity != null) {
            addSessionAttribute(req, accountEntity.getLogin());
            redirect(resp, SUCCESS_PAGE_PATH);
        } else {
            redirect(resp, INDEX_PAGE_PATH);
        }
    }

    @PostMapping("/unlogin")
    public void unlogin(HttpServletRequest req,
                        HttpServletResponse resp) {
        log.info("unlogin for {}", getSessionAttribute(req));
        removeSessionAttribute(req);
        redirect(resp, INDEX_PAGE_PATH);
    }

    @PostMapping("/delete")
    public void delete(@RequestParam String login,
                       HttpServletRequest req,
                       HttpServletResponse resp) {
        log.info("delete for {}", login);
        removeSessionAttribute(req);
        viewService.deleteAccount(login);
        redirect(resp, INDEX_PAGE_PATH);
    }

    @PostMapping("/buy")
    public void buy(@RequestParam String itemId,
                    HttpServletRequest req,
                    HttpServletResponse resp) {
        var login = getSessionAttribute(req);
        log.info("buy {} for {}", itemId, login);
        viewService.buy(login, itemId);
        redirect(resp, SHOP_PAGE_PATH);
    }

    @PostMapping("/sell")
    public void sell(@RequestParam String itemId,
                     HttpServletRequest req,
                     HttpServletResponse resp) {
        var login = getSessionAttribute(req);
        log.info("sell {} for {}", itemId, login);
        viewService.sell(login, itemId);
        redirect(resp, SHOP_PAGE_PATH);
    }

    @PostMapping("/wear")
    public void wear(@RequestParam String itemId,
                     HttpServletRequest req,
                     HttpServletResponse resp) {
        var login = getSessionAttribute(req);
        log.info("wear {} for {}", itemId, login);
        viewService.wear(login, itemId);
        redirect(resp, WEAR_PAGE_PATH);
    }

    @PostMapping("/unwear")
    public void unwear(@RequestParam String itemId,
                       HttpServletRequest req,
                       HttpServletResponse resp) {
        var login = getSessionAttribute(req);
        log.info("unwear {} for {}", itemId, login);
        viewService.unwear(login, itemId);
        redirect(resp, WEAR_PAGE_PATH);
    }
}
