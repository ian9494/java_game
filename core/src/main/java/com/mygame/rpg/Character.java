package com.mygame.rpg;

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



    public void setLocation(int location) {
        Location = location;
    }

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


    // damage counter
    public void takeDamage(int damage) {
        this.hp = Math.max(0, this.hp - damage); // 確保 HP 不小於 0
    }

    // detect if character is alive
    public boolean isAlive() {
        return this.hp > 0;
    }

    // set character's HP and make sure the value is valid
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(hp, maxHp)); }

}
