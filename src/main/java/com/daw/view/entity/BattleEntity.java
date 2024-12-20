package com.daw.view.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BattleEntity {
    private String id;
    private String playerId;

    private Set<PlayerEntity> teamOne;
    private Set<PlayerEntity> teamTwo;

    private int move = 0;
    private boolean started = false;

    private boolean teamOneFinish = false;
    private boolean teamTwoFinish = false;
    private boolean battleFinished = false;

    public PlayerEntity getPlayer(String playerId) {
        var player = teamOne.stream().filter(p -> p.getId().equals(playerId)).findFirst().orElse(null);
        if (player == null) {
            player = teamTwo.stream().filter(p -> p.getId().equals(playerId)).findFirst().get();
        }
        return player;
    }

    public PlayerEntity getOpponentForSinglePlayerBattle(String playerId) {
        var player = teamOne.stream().filter(p -> p.getId().equals(playerId)).findFirst().orElse(null);
        if (player == null) {
            return teamOne.stream().findFirst().get();
        }
        return teamTwo.stream().findFirst().get();
    }
}
