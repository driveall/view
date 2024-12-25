package com.daw.view.service;

import com.daw.view.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.daw.view.Constants.*;

@Service
public class ViewService {

    public static final Set<String> logins = new CopyOnWriteArraySet<>();

    private final RestTemplate restTemplate;

    public ViewService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean accountExists(String login) {
        return getByLogin(login) != null;
    }

    public BattleEntity startBattleWithBot(String login) {
        return restTemplate.postForEntity(
                String.format(BATTLE_START_WITH_BOT_URL, login), null, BattleEntity.class).getBody();
    }

    public BattleEntity startBattle(String login) {
        return restTemplate.postForEntity(
                String.format(BATTLE_START_URL, login), null, BattleEntity.class).getBody();
    }

    public void cancelBattle(String login) {
        restTemplate.postForEntity(
                String.format(BATTLE_CANCEL_URL, login), null, BattleEntity.class);
    }

    public BattleEntity move(String login, String opponentId, String attack, String defence) {
        return restTemplate.postForEntity(
                        String.format(BATTLE_MOVE_URL, login, attack, defence, opponentId),
                        null,
                        BattleEntity.class)
                .getBody();
    }

    public void createAccount(AccountEntity accountEntity) {
        restTemplate.postForEntity(API_CREATE_URL, accountEntity, AccountEntity.class);
    }

    public void updateAccount(AccountEntity accountEntity) {
        restTemplate.postForEntity(API_UPDATE_URL, accountEntity, AccountEntity.class);
    }

    public boolean login(String login, String password) {
        return restTemplate.postForEntity(String.format(API_LOGIN_URL, login, password), null, Boolean.class).getBody();
    }

    public void deleteAccount(String login) {
        restTemplate.delete(String.format(API_DELETE_URL, login));
    }

    public AccountEntity getByLogin(String login) {
        var response = restTemplate.getForEntity(String.format(API_GET_URL, login), AccountEntity.class);
        return response.getBody();
    }

    public ItemEntity getItem(String itemId) {
        var response = restTemplate.getForEntity(String.format(API_GET_ITEM_URL, itemId), ItemEntity.class);
        return response.getBody();
    }

    public List<ItemEntity> getAllItems() {
        var response = restTemplate.getForEntity(API_GET_ALL_ITEMS_URL, Items.class);
        return response.getBody().getItems();
    }

    public List<AccountEntity> getAllAccounts() {
        var response = restTemplate.getForEntity(API_GET_ALL_URL, Accounts.class);
        return response.getBody().getAccounts();
    }

    public void buy(String login, String itemId) {
        restTemplate.postForEntity(String.format(API_BUY_URL, itemId, login), null, Boolean.class);
    }

    public void sell(String login, String itemId) {
        restTemplate.postForEntity(String.format(API_SELL_URL, itemId, login), null, Boolean.class);
    }

    public void wear(String login, String itemId) {
        restTemplate.postForEntity(String.format(API_WEAR_URL, itemId, login), null, Boolean.class);
    }

    public void unwear(String login, String itemId) {
        restTemplate.postForEntity(String.format(API_UNWEAR_URL, itemId, login), null, Boolean.class);
    }

    public String getPlayersOnline() {
        var playersString = new StringBuilder();
        logins.forEach(login -> {
            playersString.append(login);
            playersString.append("; ");
        });
        return playersString.toString();
    }
}
