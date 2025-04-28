package com.mygame.rpg.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.*;

public class SkillDatabase {
    private static final Map<String, SkillTreeData> skillTrees = new HashMap<>();

    public static void loadAllSkillTrees(String directoryPath) {
        skillTrees.clear();
        Gson gson = new Gson();

        FileHandle dirHandle = Gdx.files.internal(directoryPath);
        if (!dirHandle.exists() || !dirHandle.isDirectory()) {
            Gdx.app.error("SkillDatabase", "Skill directory not found: " + directoryPath);
            return;
        }

        for (FileHandle file : dirHandle.list()) {
            if (file.extension().equals("json")) {
                try {
                    SkillTreeData tree = gson.fromJson(file.readString(), SkillTreeData.class);
                    skillTrees.put(tree.getCategory(), tree);
                } catch (Exception e) {
                    Gdx.app.error("SkillDatabase", "Failed to load skill tree from file: " + file.name(), e);
                }
            }
        }
    }

    public static SkillTreeData getSkillTree(String category) {
        return skillTrees.get(category);
    }

    public static Skill getSkillByID(String id) {
        for (SkillTreeData tree : skillTrees.values()) {
            for (SkillStage stage : tree.getStages()) {
                for (Skill skill : stage.getActiveSkills()) {
                    if (String.valueOf(skill.getSkillID()).equals(id)) {
                        return skill;
                    }
                }
            }
        }
        return null;
    }

    public static List<Skill> getSkillByCategory(String category) {
        SkillTreeData tree = skillTrees.get(category);
        if (tree == null) {
            return Collections.emptyList();
        }

        List<Skill> skills = new ArrayList<>();
        for (SkillStage stage : tree.getStages()) {
            skills.addAll(stage.getActiveSkills());
        }
        return skills;
    }
}
