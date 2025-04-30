package com.mygame.rpg.item;

import java.util.HashMap;
import java.util.Map;


public class Weapon extends Equipment {
    private String currentStage;
    private Map<Integer, Integer> skillMastery; // key: skillID, value: level

    public Weapon() {
        this.currentStage = "1";
        this.skillMastery = new HashMap<>();
    }

    public void increaseSkillMastery(int skillID, int amount) {
        skillMastery.put(skillID, skillMastery.getOrDefault(skillID, 0) + amount);
    }

    public int getSkillMastery(int skillID) {
        return skillMastery.getOrDefault(skillID, 0);
    }
    public String getCurrentStage() {
        return currentStage;
    }
    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }

    


}
