package com.mygame.rpg.item;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class EquipmentDatabase {
    private static final Map<String, Equipment> equipmentMap = new HashMap<>();

    public static void loadFromJson(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            JsonArray array = gson.fromJson(reader, JsonArray.class);

            equipmentMap.clear();
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                String slot = obj.get("slot").getAsString();

                if (slot.equalsIgnoreCase("WEAPON")) {
                    // 是武器：用 Weapon 類別解析
                    Weapon weapon = gson.fromJson(obj, Weapon.class);
                    equipmentMap.put(weapon.getEquipmentID(), weapon);
                } else {
                    // 是其他裝備：用 Equipment 類解析
                    Equipment eq = gson.fromJson(obj, Equipment.class);
                    equipmentMap.put(eq.getEquipmentID(), eq);
                }
            }
            Gdx.app.log("EquipmentDatabase", "Loaded " + equipmentMap.size() + " equipment items.");

            // Debug: 印出所有裝備的名稱和 ID
            Equipment testEq = equipmentMap.get("301"); // 請用實際存在的武器 ID
            if (testEq instanceof Weapon) {
                Weapon weapon = (Weapon) testEq;
                System.out.println("[Debug] Weapon loaded: " + weapon.getName());
                System.out.println("[Debug] Current stage: " + weapon.getCurrentStage());
            } else {
                System.out.println("[Debug] Equipment is NOT a weapon.");
            }

        } catch (Exception e) {
            System.err.println("[EquipmentDatabase] Failed to load: " + e.getMessage());
        }
    }

    public static Equipment getEquipmentByID(String id) {
        return equipmentMap.get(id);
    }

    public static Weapon getWeaponByID(String id) {
        Equipment equipment = equipmentMap.get(id);
        if (equipment instanceof Weapon) {
            return (Weapon) equipment;
        }
        return null;
    }

    public static List<Equipment> getAllEquipment() {
        return new ArrayList<>(equipmentMap.values());
    }
}

