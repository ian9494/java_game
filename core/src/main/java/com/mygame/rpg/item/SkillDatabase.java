package com.mygame.rpg.item;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class SkillDatabase {
    private static final Map<String, Skill> skills = new HashMap<>();

    public static void loadFromJson(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            List<Skill> skillList = gson.fromJson(reader, new TypeToken<List<Skill>>(){}.getType());

            skills.clear();
            for (Skill skill : skillList) {
                skills.put(skill.getSkillID(), skill);
            }

            System.out.println("[SkillDatabase] Loaded " + skills.size() + " skills.");
        } catch (Exception e) {
            System.err.println("[SkillDatabase] Failed to load skills.json: " + e.getMessage());
        }
    }

    public static Skill getSkillByID(String id) {
        return skills.get(id);
    }

    public static List<Skill> getAllSkills() {
        return new ArrayList<>(skills.values());
    }

    public static List<Skill> getSkillsByAllowedTypes(List<String> allowedTypes) {
        List<Skill> result = new ArrayList<>();
        for (Skill skill : skills.values()) {
            if (allowedTypes.contains(skill.getType())) {
                result.add(skill);
            }
        }
        return result;
    }
}
