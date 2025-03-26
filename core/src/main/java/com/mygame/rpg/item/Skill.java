package com.mygame.rpg.item;

public class Skill {
    private String skillID;
    private String name;
    private String type; // "sword_skill", "magic", "physical"
    private int power;

    //getter and setter
    public String getSkillID() { return skillID; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getPower() { return power; }

    public Skill(String skillID, String name, String type, int power) {
        this.skillID = skillID;
        this.name = name;
        this.type = type;
        this.power = power;
    }

    public Skill() {}

}
