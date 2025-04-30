package com.mygame.rpg.item;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class EquipmentDatabase {
    private static final Map<String, Equipment> equipmentMap = new HashMap<>();

    public static void loadFromJson(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            List<Equipment> equipmentList = gson.fromJson(reader, new TypeToken<List<Equipment>>(){}.getType());

            equipmentMap.clear();
            for (Equipment eq : equipmentList) {
                equipmentMap.put(eq.getEquipmentID(), eq);
            }

            System.out.println("[EquipmentDatabase] Loaded " + equipmentMap.size() + " equipment(s).");
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

