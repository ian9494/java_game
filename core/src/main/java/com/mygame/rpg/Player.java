package com.mygame.rpg;

import com.badlogic.gdx.Gdx;

public class Player extends Character {
    private int exp;
    private int expToNextLV;

    public int getExpToNextLV() { return expToNextLV; }
    public int getExp() { return exp; }

    public Player(String name) {
        super(name, 1); // 玩家初始等級 1
        this.exp = 0;
        updateStats(); // 依據等級計算屬性
    }

    // 根據等級計算數值
    public void updateStats() {
        Gdx.app.log("Player - Status", "Updating stats for " + name + "...");
        this.maxHp = (100 + (LV - 1) * 20);
        this.maxMp = 50 + (LV - 1) * 10;
        this.Atk = 10 + (LV - 1) * 3;
        this.Def = 5 + (LV - 1) * 2;
        this.Spd = 10 + (LV - 1);
        this.hp = maxHp;
        this.mp = maxMp;
        this.expToNextLV = LV * (LV * 20);
        Gdx.app.log("Player - Status", "expToNextLV: " + expToNextLV);
    }

    // 獲得經驗
    public void gainExp(int amount) {
        exp += amount;
        while (exp >= expToNextLV) {
            LVUp();
        }
    }

    // 升級
    private void LVUp() {
        exp -= expToNextLV;
        LV++;
        expToNextLV += LV * 20; // 升級所需經驗增加
        updateStats(); // 重新計算屬性
        Gdx.app.log("Player - Status", name + " Leveled up to " + LV + "!");
    }
}
