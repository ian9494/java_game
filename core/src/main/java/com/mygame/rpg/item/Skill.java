package com.mygame.rpg.item;

import com.mygame.rpg.item.SkillEffect;
import java.util.ArrayList;
import java.util.List;

public class Skill {
    private String skillID;
    private String name;
    private String type; // "sword_skill", "magic", "physical"
    private int empCost; // 消耗的能量值
    private int cooldown; // 冷卻時間
    private int CooldownRemaining; // 剩餘冷卻時間
    private double damageMultiplier; // 傷害倍率
    private String description; // 技能描述
    private List<SkillEffect> effects; // 技能效果列表
    private String stage; // 技能樹階段

    //getter and setter
    public String getSkillID() { return skillID; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getEmpCost() { return empCost; }
    public int getCooldown() { return cooldown; }
    public double getDamageMultiplier() { return damageMultiplier; }
    public String getDescription() { return description; }
    public List<SkillEffect> getEffects() { return effects; }
    public String getStage() { return stage; }

    public Skill(String skillID, String name, String type, int empCost) {
        this.skillID = skillID;
        this.name = name;
        this.type = type;
        this.empCost = empCost;
        this.description = "This is a skill description.";
        this.effects = new ArrayList<>();
        this.stage = ""; // default stage
    }

    public Skill() {}

    public boolean isOnCooldown() {
        return CooldownRemaining > 0;
    }

    public void triggerCooldown() {
        CooldownRemaining = cooldown;
    }

    public void tickCooldown() {
        if (CooldownRemaining > 0) {
            CooldownRemaining--;
        }
    }

}
