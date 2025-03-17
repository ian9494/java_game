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
    private Map<String, String> itemDescriptions = new HashMap<>();
    private Map<String, String> itemIcons = new HashMap<>();
    private Map<String, String> itemTypes = new HashMap<>();
    private Map<String, Effect> itemEffects = new HashMap<>();

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

        JsonValue items = json.get("items");
        if (items == null) {
            throw new IllegalArgumentException("JSON 文件中缺少 'items' 鍵");
        }

        for (JsonValue item : items) {
            String id = item.getString("id");
            itemNames.put(id, item.getString("name"));
            chineseNames.put(id, item.getString("chinese_name"));
            itemTypes.put(id, item.getString("type"));
            rarity.put(id, item.getString("rarity"));

            Effect effect = new Effect();
            JsonValue effectValue = item.get("effect");
            if (effectValue != null) {
                for (JsonValue effectEntry : effectValue) {
                    // Gdx.app.log("ItemManager-loadItems", "Loading effect for item " + id + " with key: " + effectEntry.name());
                    if (effectEntry.isObject()) {
                        // Gdx.app.log("ItemManager-loadItems", "Setting effect for " + effectEntry.name());
                        effect.setEffect(effectEntry.name(), effectEntry.getInt("value"));
                    } else if (effectEntry.isArray()) {
                        // Gdx.app.log("ItemManager-loadItems", "Setting effect for " + effectEntry.name() + " with values: " + effectEntry.get(0).asInt() + ", " + effectEntry.get(1).asInt());
                        effect.setEffect(effectEntry.name(), effectEntry.get(0).asInt(), effectEntry.get(1).asInt());
                    } else {
                        // Gdx.app.log("ItemManager-loadItems", "Setting effect for " + effectEntry.name() + " with value: " + effectEntry.asInt());
                        effect.setEffect(effectEntry.name(), effectEntry.asInt());
                    }
                }
            }
            itemEffects.put(id, effect);
        }
    }

    public String getItemName(String itemID) {
        return itemNames.get(itemID);
    }

    public String getItemChineseName(String itemID) {
        return chineseNames.getOrDefault(itemID, "未知物品");
    }

    public String getItemRarity(String itemID) {
        return rarity.getOrDefault(itemID, "Common");
    }

    public String getItemDescription(String itemID) {
        return itemDescriptions.getOrDefault(itemID, "No description available.");
    }

    public String getItemType(String itemID) {
        return itemTypes.getOrDefault(itemID, "Consumable");
    }

    public Effect getItemEffect(String itemID) {
        return itemEffects.getOrDefault(itemID, new Effect());
    }
}
