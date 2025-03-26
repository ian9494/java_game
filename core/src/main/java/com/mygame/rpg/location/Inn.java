package com.mygame.rpg.location;

import com.mygame.rpg.character.Player;

public class Inn {
    private int basicRoomCost = 10;
    private int premiumRoomCost = 50;

    public Boolean rest(Player player, boolean premium) {
        int cost = premium ? premiumRoomCost : basicRoomCost;
        if (player.spendGold(cost)) {
            if (premium) {
                player.restoreHPMP(100);
            } else {
                player.restoreHPMP(50);
            }
            return true;
        }
        else {
            return false;
        }
    }
}
