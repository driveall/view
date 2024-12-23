package com.daw.view.controller;

import com.daw.view.entity.AccountEntity;
import com.daw.view.entity.ItemEntity;
import com.daw.view.service.SessionListener;
import com.daw.view.service.ValidationService;
import com.daw.view.service.ViewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.javapoet.ClassName;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.*;

import static com.daw.view.Constants.*;
import static com.daw.view.service.ViewService.logins;
import static com.daw.view.util.SessionUtil.*;

@EnableWebMvc
@Controller
@Slf4j
public class ViewController {
    private final ViewService viewService;
    private final ValidationService validationService;
    private final SessionListener sessionListener;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    private final Map<String, String> propsUA = new HashMap<>();
    private final Map<String, String> propsUS = new HashMap<>();

    public ViewController(ViewService viewService,
                          ValidationService validationService,
                          SessionListener sessionListener,
                          MessageSource messageSource,
                          LocaleResolver localeResolver) throws IOException {
        this.viewService = viewService;
        this.validationService = validationService;
        this.sessionListener = sessionListener;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;

        var properties = new Properties();
        properties.load(ClassName.class.getResourceAsStream("/lang/messages_ua.properties"));
        for (String s : properties.stringPropertyNames()) {
            propsUA.put(s, String.valueOf(properties.get(s)));
        }

        properties = new Properties();
        properties.load(ClassName.class.getResourceAsStream("/lang/messages_us.properties"));
        for (String p : properties.stringPropertyNames()) {
            propsUS.put(p, String.valueOf(properties.get(p)));
        }
    }

    @PostMapping("/lang")
    public void changeLanguage(@RequestParam String lng,
                               HttpServletRequest req,
                               HttpServletResponse resp) throws IOException {
        log.info("lang change to {}", lng);
        req.getSession().setAttribute("lang", lng);
        resp.sendRedirect(INDEX_PAGE_PATH);
    }

    @GetMapping({"/index", "/"})
    public ModelAndView getIndex(HttpServletRequest req,
                                 HttpServletResponse resp) throws IOException {
        var login = getSessionAttribute(req);
        if (login == null) {
            log.info("index get");

            var mav = new ModelAndView("index")
                    .addObject("onlineCount",
                            ((Map)req.getSession().getServletContext().getAttribute("sessions")).size());
            addMessagesToModelAndView(mav, req);
            return mav;
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
            var mav = new ModelAndView("login");
            addMessagesToModelAndView(mav, req);
            return mav;
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
            var mav = new ModelAndView("register");
            addMessagesToModelAndView(mav, req);
            return mav;
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
            logins.add(login);
            var account = viewService.getByLogin(login);
            var mav = new ModelAndView("main")
                    .addObject("account", account)
                    .addObject("playersOnline", viewService.getPlayersOnline())
                    .addObject("onlineCount", ((Map)req.getSession()
                            .getServletContext().getAttribute("sessions")).size());
            addMessagesToModelAndView(mav, req);
            return mav;
        }
        resp.sendRedirect(INDEX_PAGE_PATH);
        return null;
    }

    @GetMapping({"/accounts"})
    public ModelAndView getAccounts(HttpServletRequest req,
                                    HttpServletResponse resp) {
        var login = getSessionAttribute(req);
        if (login != null) {
            log.info("accounts get for {}", login);
            var mav = new ModelAndView("accounts").addObject("accounts", viewService.getAllAccounts());
            addMessagesToModelAndView(mav, req);
            return mav;
        } else {
            log.info("accounts get, redirect");
            redirect(resp, INDEX_PAGE_PATH);
            return null;
        }
    }

    @GetMapping("/shop")
    public ModelAndView getShop(HttpServletRequest req,
                                HttpServletResponse resp) throws IOException {
        var login = getSessionAttribute(req);
        log.info("shop get login {}", login);
        if (login != null) {
            var account = viewService.getByLogin(login);
            List<ItemEntity> stuff = viewService.getAllItems();
            stuff = stuff.stream()
                    .filter(i -> i.getLevel() <= account.getLevel())
                    .toList();
            stuff = stuff.stream()
                    .filter(i -> !account.getStorage().contains(i))
                    .toList();
            var mav = new ModelAndView("shop")
                    .addObject("account", account)
                    .addObject("stuff", stuff);
            addMessagesToModelAndView(mav, req);
            return mav;
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
            var mav = new ModelAndView("wear")
                    .addObject("account", account);
            addMessagesToModelAndView(mav, req);
            return mav;
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
            var mav = new ModelAndView("update")
                    .addObject("account", account);
            addMessagesToModelAndView(mav, req);
            return mav;
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
            logins.add(login);
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
        var login = getSessionAttribute(req);
        log.info("unlogin for {}", login);
        removeSessionAttribute(req);
        logins.remove(login);
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

    @PostMapping("/battle/bot/start")
    public ModelAndView battleWithBotStart(HttpServletRequest req) {
        var login = getSessionAttribute(req);
        log.info("battle with bot start for {}", login);
        var battle = viewService.startBattleWithBot(login);
        var mav = new ModelAndView("battle")
                .addObject("battle", battle);
        addMessagesToModelAndView(mav, req);
        return mav;
    }

    @PostMapping("/battle/start")
    public ModelAndView battleStart(HttpServletRequest req,
                                    HttpServletResponse res) {
        var login = getSessionAttribute(req);
        log.info("battle start for {}", login);
        var battle = viewService.startBattle(login);
        var mav = new ModelAndView("battle")
                .addObject("battle", battle);
        addMessagesToModelAndView(mav, req);
        return mav;
    }

    @PostMapping("/battle/cancel")
    public void battleCancel(HttpServletRequest req,
                                    HttpServletResponse resp) throws IOException {
        var login = getSessionAttribute(req);
        log.info("battle cancel for {}", login);
        viewService.cancelBattle(login);
        resp.sendRedirect(SUCCESS_PAGE_PATH);
    }

    @PostMapping("/battle/move")
    public ModelAndView move(@RequestParam(required = false) String attack,
                             @RequestParam(required = false) String defence,
                             @RequestParam(required = false) String opponent,
                             HttpServletRequest req,
                             HttpServletResponse resp) throws IOException {
        var login = getSessionAttribute(req);
        log.info("battle move for {}", login);
        var battle = viewService.move(login, opponent, attack, defence);
        if (battle != null) {
            var mav = new ModelAndView("battle")
                    .addObject("battle", battle);
            addMessagesToModelAndView(mav, req);
            return mav;
        } else {
            resp.sendRedirect(SUCCESS_PAGE_PATH);
            return null;
        }
    }

    private void addMessagesToModelAndView(ModelAndView mav, HttpServletRequest req) {
        mav.addObject("msg", getLocaleFromSession(req).equals("us") ? propsUS : propsUA);
    }

    public String getLocaleFromSession(HttpServletRequest req) {
        var lang = (String) req.getSession().getAttribute("lang");
        return lang == null ? "us" : lang;
    }
}
