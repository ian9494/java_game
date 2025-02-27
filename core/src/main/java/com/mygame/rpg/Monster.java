package com.mygame.rpg;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.utils.Null;

public class Monster extends Character {
    private String monsterID;
    private String chineseName;
    private String description;
    private int expReward;
    private int encounter_rate;
    private int gold;
    private List<DropItem> itemDrops;

    public Monster(String monsterID, String name, String chineseName, String description, int hp, int Atk, int Def, int Spd, int expReward, int gold, String iconPath, int encounter_rate, List<DropItem> itemsDrops) {
        super(name, 1); // 怪物等級通常不變
        this.encounter_rate = encounter_rate;
        this.monsterID = monsterID;
        this.chineseName = chineseName;
        this.description = description;
        this.maxHp = hp;
        this.maxMp = mp;
        this.Atk = Atk;
        this.Def = Def;
        this.Spd = Spd;
        this.hp = maxHp;
        this.mp = maxMp;
        this.gold = gold;
        this.expReward = expReward;
        this.itemDrops = itemsDrops;

    }

    public int getExpReward() {return expReward; }
    public int getGoldReward() { return gold; }
    public int getEncounterRate() { return encounter_rate; }

    public DropItem getRandomDrop() {
        if (itemDrops == null || itemDrops.isEmpty()) {
            return null;
        }
        Random rand = new Random();
        return itemDrops.get(rand.nextInt(itemDrops.size()));
    }
}



