package com.mygame.rpg.item;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;

public class Weapon extends Equipment {
    @SerializedName("currentStage") // 確保變數被序列化
    private String currentStage;

    @SerializedName("skillMastery") // 確保變數被序列化
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
