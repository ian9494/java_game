package com.mygame.rpg.item;

import java.util.List;

public class SkillEffect {
    private String type; // "damage", "heal", "buff", "debuff"
    private String status; // 針對狀態類型 "bleed", "stun"
    private int chance = 100; // 觸發機率
    private int stacks; // 堆疊數量
    private int duration; // 持續時間
    private List<Double> hitMultipliers; // 觸發次數
    private int amount; // 效果數值變化

    // getter and setter
    public String getType() { return type; }
    public String getStatus() { return status; }
    public int getChance() { return chance; }
    public int getStacks() { return stacks; }
    public int getDuration() { return duration; }
    public List<Double> getHitMultipliers() {return hitMultipliers;}
    public int getAmount() { return amount; }
}
