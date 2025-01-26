package com.mygame.rpg;

import com.badlogic.gdx.Gdx;

public class Character {
    private String name;
    private int LV;
    private int hp;
    private int maxHp;
    private int mp;
    private int maxMp;
    private int Atk;
    private int Def;
    private int Spd;
    private int Location;
    private int actionBar; // 行動條


    public Character(String name, int maxHp, int maxMp, int Atk, int Def, int Spd) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMp = maxMp;
        this.mp = maxMp;
        this.Atk = Atk;
        this.Def = Def;
        this.Spd = Spd;
    }

    // Getters and setters for attributes
    public String getName() { return name; }
    public int getLV() { return LV; }
    public int getHp() { return hp; }
    public int getMp() { return mp; }
    public int getAtk() { return Atk; }
    public int getDef() { return Def; }
    public int getSpd() { return Spd; }
    public int getLocation() {return Location;}
    public int getActionBar() {return actionBar;}


    // damage counter
    public void takeDamage(int damage) {
        this.hp = Math.max(0, this.hp - damage); // 確保 HP 不小於 0
    }

    // detect if character is alive
    public boolean isAlive() {
        // Gdx.app.log("TAG", String.valueOf(this.hp));
        return this.hp > 1;
    }

    // increment action bar
    public void incrementActionBar(float value) {
        this.actionBar += value;
    }

    // reset action bar
    public void resetActionBar() {
        this.actionBar = 0;
    }

    // 設置角色的 HP 並確保值是有效的
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(hp, maxHp)); }

}
