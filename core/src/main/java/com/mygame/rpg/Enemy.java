package com.mygame.rpg;

import java.util.List;
import java.util.Random;

public class Enemy extends Character {
    private int expReward;
    private List<String> itemDrops;

    public Enemy(String name, int hp, int mp, int Atk, int Def, int Spd, int expReward, List<String> itemsDrops) {
        super(name, 1); // 怪物等級通常不變
        this.maxHp = hp;
        this.maxMp = mp;
        this.Atk = Atk;
        this.Def = Def;
        this.Spd = Spd;
        this.hp = maxHp;
        this.mp = maxMp;
        this.expReward = expReward;
        this.itemDrops = itemsDrops;

    }

    public int getExpReward() {return expReward; }

    public String getRandomDrop() {
        if (itemDrops == null || itemDrops.isEmpty()) {
            return "no item dropped";
        }
        Random rand = new Random();
        return itemDrops.get(rand.nextInt(itemDrops.size()));
    }
}



