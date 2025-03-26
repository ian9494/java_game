package com.mygame.rpg.item;

import java.util.List;

public class Equipment {
    private String equipmentID;
    private String name;
    private String description;
    private int attackBonus;
    private int defenseBonus;
    private EquipSlot slot; // "weapon", "armor"
    private List<String> allowedSkillTypes; // 允許的技能類型，例如 ["sword_skill", "physical"]

    public Equipment(String equipmentID, String name, String description, int attackBonus, int defenseBonus, EquipSlot equipSlot, List<String> allowedSkillTypes) {
        this.equipmentID = equipmentID;
        this.name = name;
        this.description = description;
        this.attackBonus = attackBonus;
        this.defenseBonus = defenseBonus;
        this.slot = equipSlot;
        this.allowedSkillTypes = allowedSkillTypes;
    }

    public Equipment() {}

    public List<String> getAllowedSkillTypes() { return allowedSkillTypes; }
    public EquipSlot getSlot() { return slot; }
    public String getEquipmentID() { return equipmentID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getAttackBonus() { return attackBonus; }
    public int getDefenseBonus() { return defenseBonus; }
}
