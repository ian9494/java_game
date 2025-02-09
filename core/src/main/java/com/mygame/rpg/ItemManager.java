package com.mygame.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {
    private static ItemManager instance;
    private Map<String, String> itemNames = new HashMap<>();
    private Map<String, String> chineseNames = new HashMap<>();
    private Map<String, String> rarity = new HashMap<>();
    // private Map<String, String> itemDescriptions = new HashMap<>();
    private Map<String, String> itemIcons = new HashMap<>();
    private Map<String, String> itemTypes = new HashMap<>();
    private Map<String, String> itemEffects = new HashMap<>();

    private ItemManager() {
        loadItems();
    }

    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    private void loadItems() {
        FileHandle file = Gdx.files.internal("json/items.json");
        JsonReader jsonReader = new JsonReader();
        JsonValue json = jsonReader.parse(file);

        for (JsonValue item : json.get("items")) {
            String id = item.getString("id");
            itemNames.put(id, item.getString("name"));
            // itemDescriptions.put(id, item.getString("description"));
            chineseNames.put(id, item.getString("chinese_name"));
        }
    }

    public String getItemName(String itemID) {
        return itemNames.getOrDefault(itemID, "Unknown Item");
    }

    public String getItemChineseName(String itemID) {
        return chineseNames.getOrDefault(itemID, "未知物品");
    }

    public String getItemRarity(String itemID) {
        return rarity.getOrDefault(itemID, "Common");
    }

    // public String getItemDescription(String itemID) {
    //     return itemDescriptions.getOrDefault(itemID, "No description available.");
    // }
}
