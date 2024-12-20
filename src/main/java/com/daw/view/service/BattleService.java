package com.daw.view.service;

import com.daw.view.entity.AccountEntity;
import com.daw.view.entity.BattleEntity;
import com.daw.view.entity.PlayerEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.daw.view.Constants.*;

@Service
public class BattleService {

    private final RestTemplate restTemplate = new RestTemplate();

    public BattleEntity startBattleWithBot(String login) {
        var existsBattle = restTemplate.getForEntity(String.format(BATTLE_GET_URL, login), BattleEntity.class);
        // TODO hardcore remove old unknown battle
        if (existsBattle.hasBody()) {
            restTemplate.delete(String.format(String.format(BATTLE_DELETE_URL, login)));
        }
        var account = getAccountByLogin(login);
        var armor = calculateArmor(account);
        var damage = calculateDamage(account);
        var health = calculateHealth(account);
        var battle = BattleEntity.builder()
                .move(1)
                .started(true)
                .teamOne(Set.of(PlayerEntity.builder()
                        .id(account.getLogin())
                        .armor(armor)
                        .damage(damage)
                        .health(health)
                        .build()))
                .teamTwo(Set.of(PlayerEntity.builder()
                        .id("BotLevel" + account.getLevel())
                        .armor(armor)
                        .damage(damage)
                        .health(health)
                        .resultsViewed(true)
                        .build()))
                .build();
        var response = restTemplate.postForEntity(BATTLE_CREATE_URL, battle, String.class);
        battle = restTemplate.getForEntity(String.format(BATTLE_GET_URL, response.getBody()), BattleEntity.class).getBody();
        battle.setPlayerId(login);
        return battle;
    }

    public BattleEntity startBattle(String login) {
        // TODO add functionality
        return null;
    }

    public BattleEntity move(String login, String opponentId, String attack, String defence) {
        var battle = restTemplate.getForEntity(String.format(BATTLE_GET_URL, login), BattleEntity.class).getBody();
        if (battle != null && login != null && !login.isEmpty()) {
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
                if (opponentId.contains("BotLevel")) {
                    opponent.setAttack("Center");
                    opponent.setDefense("Center");
                    opponent.setOpponent(login);
                    opponent.setMoveFinished(true);
                }
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
                }
                restTemplate.put(BATTLE_UPDATE_URL, battle);
            }
            battle = restTemplate.getForEntity(String.format(BATTLE_GET_URL, login), BattleEntity.class).getBody();

            if (allPlayersViewedResults(battle)) {
                payRewards(battle);
                restTemplate.delete(String.format(BATTLE_DELETE_URL, battle.getId()));
            }
            battle.setPlayerId(login);
        }
        return battle;
    }

    public AccountEntity getAccountByLogin(String login) {
        var response = restTemplate.getForEntity(String.format(API_GET_URL, login), AccountEntity.class);
        return response.getBody();
    }

    private void updateAccount(AccountEntity accountEntity) {
        restTemplate.postForEntity(API_UPDATE_URL, accountEntity, AccountEntity.class);
    }

    private void payRewards(BattleEntity battle) {
        battle.getTeamOne().forEach(p -> {
            if (p.getHealth() > 0) {
                var account = getAccountByLogin(p.getId());
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
        return (account.getLevel() * 10) + (account.getPoints() / 10);
    }
}
