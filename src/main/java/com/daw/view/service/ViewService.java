package com.daw.view.service;

import com.daw.view.entity.AccountEntity;
import com.daw.view.entity.BattleEntity;
import com.daw.view.entity.ItemEntity;
import com.daw.view.enums.ItemType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.daw.view.Constants.*;

@Service
public class ViewService {
    @Value("${password.hash.salt}")
    private String salt;

    public static final Set<String> logins = new CopyOnWriteArraySet<>();

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

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
        // TODO add functionality
        return null;
    }

    public BattleEntity move(String login, String opponentId, String attack, String defence) {
        return restTemplate.postForEntity(
                        String.format(BATTLE_MOVE_URL, login, attack, defence, opponentId),
                        null,
                        BattleEntity.class)
                .getBody();
    }

    public AccountEntity getAccountByLogin(String login) {
        var response = restTemplate.getForEntity(String.format(API_GET_URL, login), AccountEntity.class);
        return response.getBody();
    }

    public void createAccount(AccountEntity accountEntity) {
        if (!accountExists(accountEntity.getLogin())) {
            accountEntity.setPassword(hashPassword(accountEntity.getPassword()).get());
            accountEntity.setMoney(20);
            accountEntity.setLevel(1);
            accountEntity.setPoints(0);
            accountEntity.setStorage(new HashSet<>());
            accountEntity.setCreatedAt(DateTime.now().toString());
            accountEntity.setUpdatedAt(DateTime.now().toString());
            accountEntity.setPasswordChangedAt(DateTime.now().toString());

            restTemplate.postForEntity(API_CREATE_URL, accountEntity, AccountEntity.class);
        }
    }

    public void updateAccount(AccountEntity accountEntity) {
        restTemplate.postForEntity(API_UPDATE_URL, accountEntity, AccountEntity.class);
    }

    public boolean login(AccountEntity accountEntity, String password) {
        return hashPassword(password).get().equals(accountEntity.getPassword());
    }

    public void deleteAccount(String login) {
        restTemplate.delete(String.format(API_DELETE_URL, login));
    }

    public AccountEntity getByLogin(String login) {
        var response = restTemplate.getForEntity(String.format(API_GET_URL, login), AccountEntity.class);
        return response.getBody();
    }

    public Optional<String> hashPassword(String password) {
        char[] chars = password.toCharArray();
        byte[] bytes = salt.getBytes();

        var spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            var fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println("Exception encountered in hashPassword()");
            return Optional.empty();
        } finally {
            spec.clearPassword();
        }
    }

    public ItemEntity getItem(String itemId) {
        var response = restTemplate.getForEntity(String.format(API_GET_ITEM_URL, itemId), ItemEntity.class);
        return response.getBody();
    }

    public void buy(String login, String itemId) {
        var account = getByLogin(login);
        var item = getItem(itemId);
        if (account.getMoney() >= item.getPrice()) {
            account.getStorage().add(getItem(itemId));
            var accountToUpdate = AccountEntity.builder()
                    .login(login)
                    .money(account.getMoney() - item.getPrice())
                    .storage(account.getStorage())
                    .build();
            updateAccount(accountToUpdate);
        }
    }

    public void sell(String login, String itemId) {
        var account = getByLogin(login);
        var item = getItem(itemId);
        account.getStorage().remove(getItem(itemId));
        unwear(login, itemId);

        var accountToUpdate = AccountEntity.builder()
                .login(login)
                .money(account.getMoney() + item.getPrice())
                .storage(account.getStorage())
                .build();
        updateAccount(accountToUpdate);
    }

    public void wear(String login, String itemId) {
        var item = getItem(itemId);

        var accountToUpdate = new AccountEntity();
        accountToUpdate.setLogin(login);
        switch (item.getType()) {
            case ItemType.BODY -> accountToUpdate.setBody(item);
            case ItemType.HEAD -> accountToUpdate.setHead(item);
            case ItemType.LEGS -> accountToUpdate.setLegs(item);
            case ItemType.WEAPON -> accountToUpdate.setWeapon(item);
        }
        updateAccount(accountToUpdate);
    }

    public void unwear(String login, String itemId) {
        var item = getItem(itemId);

        var accountToUpdate = new AccountEntity();
        accountToUpdate.setLogin(login);
        switch (item.getType()) {
            case ItemType.BODY -> accountToUpdate.setBody(new ItemEntity());
            case ItemType.HEAD -> accountToUpdate.setHead(new ItemEntity());
            case ItemType.LEGS -> accountToUpdate.setLegs(new ItemEntity());
            case ItemType.WEAPON -> accountToUpdate.setWeapon(new ItemEntity());
        }
        updateAccount(accountToUpdate);
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
