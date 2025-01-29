package com.mygame.rpg;

public class Enemy extends Character {
    public Enemy(String name, int hp, int mp, int attack, int defense, int speed) {
        super(name, 1); // 怪物等級通常不變
        this.maxHp = hp;
        this.maxMp = mp;
        this.Atk = attack;
        this.Def = defense;
        this.Spd = speed;
        this.hp = maxHp;
        this.mp = maxMp;
    }
}
