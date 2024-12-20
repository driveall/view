package com.daw.view.service;

import com.daw.view.entity.AccountEntity;
import com.daw.view.entity.BattleEntity;
import com.daw.view.entity.ItemEntity;
import com.daw.view.entity.PlayerEntity;
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
import java.util.concurrent.atomic.AtomicBoolean;

import static com.daw.view.Constants.*;

@Service
public class ViewService {
    @Value("${password.hash.salt}")
    private String salt;

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean accountExists(String login) {
        return getByLogin(login) != null;
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

    public BattleEntity startBattleFor(String login) {
        var existsBattle = restTemplate.getForEntity(String.format(BATTLE_GET_URL, login), BattleEntity.class);
        // TODO hardcore remove old unknown battle
        if (existsBattle.hasBody()) {
            restTemplate.delete(String.format(String.format(BATTLE_DELETE_URL, login)));
        }
        var account = getByLogin(login);
        var battle = BattleEntity.builder()
                .move(1)
                .started(true)
                .teamOne(Set.of(PlayerEntity.builder()
                        .id(account.getLogin())
                        .armor(calculateArmor(account))
                        .damage(calculateDamage(account))
                        .health(calculateHealth(account))
                        .build()))
                .teamTwo(Set.of(PlayerEntity.builder()
                        .id("BotLevel" + account.getLevel())
                        .armor(2)
                        .damage(4)
                        .health(calculateHealth(account))
                        .build()))
                .build();
        var response = restTemplate.postForEntity(BATTLE_CREATE_URL, battle, String.class);
        return restTemplate.getForEntity(String.format(BATTLE_GET_URL, response.getBody()), BattleEntity.class).getBody();
    }

    public BattleEntity move(String login, String opponentId, String attack, String defence) {
        var battle = restTemplate.getForEntity(String.format(BATTLE_GET_URL, login), BattleEntity.class).getBody();
        if (battle != null) {
            var players = findPlayers(battle, login, opponentId);
            var player = players.get(0);
            var opponent = players.get(1);
            if (attack != null && !attack.isEmpty()
                    && defence != null && !defence.isEmpty()
                    && opponentId != null && !opponentId.isEmpty()) {
                player.setAttack(attack);
                player.setDefense(defence);
                player.setOpponent(opponentId);
                player.setMoveFinished(true);
                // TODO hardcoded opponent is bot so it finishes move now
                opponent.setAttack("Center");
                opponent.setDefense("Center");
                opponent.setOpponent(login);
                opponent.setMoveFinished(true);
                // set teams finished
                if (teamFinished(battle.getTeamOne())) {
                    battle.setTeamOneFinish(true);
                }
                if (teamFinished(battle.getTeamTwo())) {
                    battle.setTeamTwoFinish(true);
                }
                // calculate
                if (battle.isTeamOneFinish() && battle.isTeamTwoFinish()) {
                    calculateFor(battle.getTeamOne(), battle.getTeamTwo());
                    calculateFor(battle.getTeamTwo(), battle.getTeamOne());
                    // check battle ends or not
                    if (allTeamLost(battle.getTeamOne()) || allTeamLost(battle.getTeamTwo())) {
                        battle.setBattleFinished(true);
                    } else {
                        prepareNextMove(battle);
                    }
                }
                if (battle.isBattleFinished()) {
                    player.setResultsViewed(true);
                    // TODO hardcoded bot is viewed too
                    opponent.setResultsViewed(true);
                }
                restTemplate.put(BATTLE_UPDATE_URL, battle);
            }
            battle = restTemplate.getForEntity(String.format(BATTLE_GET_URL, login), BattleEntity.class).getBody();

            if (allPlayersViewedResults(battle)) {
                payRewards(battle);
                restTemplate.delete(String.format(BATTLE_DELETE_URL, battle.getId()));
            }
            return battle;
        }
        return null;
    }

    private void payRewards(BattleEntity battle) {
        battle.getTeamOne().forEach(p -> {
            if (p.getHealth() > 0) {
                var account = getByLogin(p.getId());
                account.setPoints(account.getPoints() + p.getHealth());
                if (account.getPoints() >= account.getLevel() * account.getLevel() * account.getLevel() * 100) {
                    account.setLevel(account.getLevel() + 1);
                }
                updateAccount(account);
            }
        });
    }

    private boolean allPlayersViewedResults(BattleEntity battle) {
        var viewed = new AtomicBoolean(true);
        battle.getTeamOne().forEach(p -> {
            if (!p.isResultsViewed()) {
                viewed.set(false);
            }
        });
        battle.getTeamTwo().forEach(p -> {
            if (!p.isResultsViewed()) {
                viewed.set(false);
            }
        });
        return viewed.get();
    }

    private void prepareNextMove(BattleEntity battle) {
        battle.getTeamOne().forEach(player -> {
            player.setDefense(null);
            player.setAttack(null);
            player.setOpponent(null);
            player.setMoveFinished(false);
        });
        battle.getTeamTwo().forEach(player -> {
            player.setDefense(null);
            player.setAttack(null);
            player.setOpponent(null);
            player.setMoveFinished(false);
        });
        battle.setMove(battle.getMove() + 1);
        battle.setTeamOneFinish(false);
        battle.setTeamTwoFinish(false);
    }

    private boolean allTeamLost(Set<PlayerEntity> team) {
        var allPlayersLost = new AtomicBoolean(true);
        team.forEach(p -> {
            if (p.getHealth() > 0) {
                allPlayersLost.set(false);
            }
        });
        return allPlayersLost.get();
    }

    private void calculateFor(Set<PlayerEntity> team, Set<PlayerEntity> opponents) {
        team.forEach(player -> {
            var opponent = opponents.stream()
                    .filter(p -> player.getOpponent().equals(p.getId()))
                    .findFirst().get();
            if (player.getAttack().equals(opponent.getDefense())) {
                var totalDamage = player.getDamage() - opponent.getArmor();
                if (totalDamage < 1) {
                    totalDamage = 1;
                }
                opponent.setHealth(opponent.getHealth() - totalDamage);
            }
        });
    }

    private boolean teamFinished(Set<PlayerEntity> players) {
        var allFinished = new AtomicBoolean(true);
        players.forEach(player -> {
            if (!player.isMoveFinished()) {
                allFinished.set(false);
            }
        });
        return allFinished.get();
    }

    private List<PlayerEntity> findPlayers(BattleEntity battle, String login, String opponentId) {
        PlayerEntity opponent = null;
        PlayerEntity player = battle.getTeamOne().stream()
                .filter(e -> e.getId().equals(login))
                .findFirst().get();
        if (player.getId() == null) {
            player = battle.getTeamTwo().stream()
                    .filter(e -> e.getId().equals(login))
                    .findFirst().get();
            opponent = battle.getTeamOne().stream()
                    .filter(e -> e.getId().equals(opponentId))
                    .findFirst().get();
        } else {
            opponent = battle.getTeamTwo().stream()
                    .filter(e -> e.getId().equals(opponentId))
                    .findFirst().get();
        }
        var players = new LinkedList<PlayerEntity>();
        players.add(player);
        players.add(opponent);
        return players;
    }

    private int calculateDamage(AccountEntity account) {
        int damage = 0;
        if (account.getWeapon() != null) {
            damage += account.getWeapon().getPoints();
        }
        return damage;
    }

    private int calculateArmor(AccountEntity account) {
        int armor = 0;
        if (account.getHead() != null && account.getHead().getPoints() != null) {
            armor += account.getHead().getPoints();
        }
        if (account.getLegs() != null && account.getLegs().getPoints() != null) {
            armor += account.getLegs().getPoints();
        }
        if (account.getBody() != null && account.getBody().getPoints() != null) {
            armor += account.getBody().getPoints();
        }
        return armor;
    }

    private int calculateHealth(AccountEntity account) {
        return account.getLevel() * account.getLevel() * 10;
    }
}
