package com.daw.view.controller;

import com.daw.view.service.BattleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.daw.view.util.SessionUtil.getSessionAttribute;
import static com.daw.view.util.SessionUtil.redirect;

@EnableWebMvc
@Controller
@Slf4j
public class BattleController {
    private final BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @PostMapping("/battle/bot/start")
    public ModelAndView battleWithBotStart(HttpServletRequest req) {
        var login = getSessionAttribute(req);
        log.info("battle with bot start for {}", login);
        var battle = battleService.startBattleWithBot(login);
        return new ModelAndView("battle")
                .addObject("battle", battle);
    }

    @PostMapping("/battle/start")
    public ModelAndView battleStart(HttpServletRequest req,
                                    HttpServletResponse res) {
        var login = getSessionAttribute(req);
        log.info("battle start for {}", login);
        var battle = battleService.startBattle(login);
        // TODO add functionality
        redirect(res, login);
        return null;
    }

    @PostMapping("/battle/move")
    public ModelAndView move(@RequestParam(required = false) String attack,
                             @RequestParam(required = false) String defence,
                             @RequestParam(required = false) String opponent,
                             HttpServletRequest req) {
        var login = getSessionAttribute(req);
        log.info("battle move for {}", login);
        var battle = battleService.move(login, opponent, attack, defence);
        if (battle != null) {
            return new ModelAndView("battle")
                    .addObject("battle", battle);
        } else return new ModelAndView("success")
                .addObject("account", battleService.getAccountByLogin(login));
    }
}
