package com.mygame.rpg;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.utils.Null;

public class Enemy extends Character {
    private int expReward;
    private List<DropItem> itemDrops;

    public Enemy(String name, int hp, int mp, int Atk, int Def, int Spd, int expReward, List<DropItem> itemsDrops) {
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

    public DropItem getRandomDrop() {
        if (itemDrops == null || itemDrops.isEmpty()) {
            return null;
        }
        Random rand = new Random();
        return itemDrops.get(rand.nextInt(itemDrops.size()));
    }
}



