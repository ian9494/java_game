package com.mygame.rpg.item;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;
import com.badlogic.gdx.Gdx;

import java.util.List;
import java.util.ArrayList;

public class Weapon extends Equipment {
    @SerializedName("currentStage") // 確保變數被序列化
    private List<Integer> currentStage; // 當前武器的階段，例如 [1, 1, 2] 代表第一個分支的第二個階段

    @SerializedName("weaponType") // 確保變數被序列化
    private String weaponType; // 武器類型，例如 "sword", "bow"


    @SerializedName("skillMastery") // 確保變數被序列化
    private Map<String, Integer> skillMastery; // key: skillID, value: level


    public Weapon() {
        this.currentStage = new ArrayList<>();
        this.skillMastery = new HashMap<>();
    }

    public void increaseSkillMastery(String skillID, int amount) {
        skillMastery.put(skillID, skillMastery.getOrDefault(skillID, 0) + amount);
    }

    public List<Skill> getAvailableSkillIds() {
        List<Skill> availableSkills = new ArrayList<>();

        SkillTreeData skillTree = SkillDatabase.getSkillTree(weaponType);
        if (skillTree == null) {
            Gdx.app.log("weapon-skillTree", "No skill tree found for weapon type: " + weaponType);
            return availableSkills;
        }

        for (SkillStage stage : skillTree.getStages()) {
            if (StagePathUtils.isSameBranch(stage.getStage(), this.currentStage)) {
                availableSkills.addAll(stage.getActiveSkills());
            }
        }
        return availableSkills;
    }

    public int getSkillMastery(int skillID) {return skillMastery.getOrDefault(skillID, 0); }
    public Map<String, Integer> getSkillMastery() {return skillMastery; }
    public void setSkillMastery(Map<String, Integer> skillMastery) {this.skillMastery = skillMastery; }
    public List<Integer> getCurrentStage() {return currentStage; }
    public void setCurrentStage(List<Integer> currentStage) {this.currentStage = currentStage; }






}
