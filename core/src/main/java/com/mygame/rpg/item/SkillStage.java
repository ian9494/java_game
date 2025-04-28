package com.mygame.rpg.item;

import java.util.*;

public class SkillStage {
    private String stage;
    private String label;
    private List<String> parentSkills;
    private List<Skill> activeSkills;
    private PassiveSkill passiveSkill;

    public String getStage() { return stage; }
    public String getLabel() { return label; }
    public List<String> getParentSkills() { return parentSkills; }
    public List<Skill> getActiveSkills() { return activeSkills; }
    public PassiveSkill getPassiveSkill() { return passiveSkill; }
}
