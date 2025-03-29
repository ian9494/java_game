package com.mygame.rpg.character;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Null;
import com.mygame.rpg.battle.DropItem;

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

    public List<DropItem> getRandomDrop() {
        Random rand = new Random();

        // 檢查 itemDrops 是否為 null 或空列表
        if (itemDrops == null || itemDrops.isEmpty()) {
            return null;
        }

        // 確認掉落道具是物品還是清單
        Gdx.app.log("Monster", "itemDrops size: " + itemDrops.size());
        for (DropItem item : itemDrops) {
            Gdx.app.log("Monster", "item: " + item.getName() + ", drop rate: " + item.getDropRate());
        }
        // 如果掉落物品的數量大於 1，則隨機選擇一個掉落物品

        // 跑過所有掉落物品，機率掉落成功的全部都添加到掉落物品清單
        List<DropItem> successfulDrops = new ArrayList<>();
        for (DropItem item : itemDrops) {
            if (rand.nextInt(100) < item.getDropRate()) {
                successfulDrops.add(item);
                Gdx.app.log("Monster", "Dropped success!");
            }
        }
        return successfulDrops;
    }
}



