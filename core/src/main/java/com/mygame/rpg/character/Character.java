package com.mygame.rpg.character;

// 移除未使用的 Gdx import
// import com.badlogic.gdx.Gdx;

public class Character {
    protected String name;
    protected int LV;
    protected int exp;
    protected int expToNextLV;
    protected int hp;
    protected int maxHp;
    protected int mp;
    protected int maxMp;
    protected int Atk;
    protected int Def;
    protected int Spd;
    protected int LocationID;
    protected int actionBar; // 行動條
    protected boolean readyToAct; // 新增布林值來標記角色是否準備好行動


    public Character(String name, int LV) {
        this.name = name;
        this.LV = LV;
    }

    // Getters and setters for attributes
    public String getName() { return name; }
    public int getLV() { return LV; }
    public int getExp() { return exp; }
    public int getExpToNextLV() { return expToNextLV; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getMp() { return mp; }
    public int getMaxMp() { return maxMp; }
    public int getAtk() { return Atk; }
    public int getDef() { return Def; }
    public int getSpd() { return Spd; }
    public int getLocationID() {return LocationID;}
    public int getActionBar() {return actionBar;}
    public boolean isReadyToAct() { return readyToAct; }
    public void setReadyToAct(boolean readyToAct) { this.readyToAct = readyToAct; }


    // damage counter
    public void takeDamage(int damage) {
        this.hp = Math.max(0, this.hp - damage); // 確保 HP 不小於 0
    }

    // detect if character is alive
    public boolean isAlive() {
        return this.hp > 0;
    }

    // increment action bar
    public void incrementActionBar(int value) {
        this.actionBar += value;
        if (this.actionBar >= 100) {
            this.readyToAct = true;
        }
    }

    // reset action bar
    public void resetActionBar() {
        this.actionBar = 0;
        this.readyToAct = false;
    }

    // 設置角色的 HP 並確保值是有效的
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(hp, maxHp)); }
    public void setMp(int mp) { this.mp = Math.max(0, Math.min(mp, maxMp)); }
}
