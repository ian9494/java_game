package com.mygame.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationManager {
    private Map<Integer, Location> locations; // 使用 ID 作為 Key
    private Map<Integer, List<GatherableObject>> gatherablesByLocation; // 使用 Location ID 作為 Key
    private Map<Integer, List<Monster>> monstersByLocation; // 使用 Location ID 作為 Key

    public LocationManager() {
        // 初始化 locations 和 gatherablesByLocation
        locations = new HashMap<>();
        gatherablesByLocation = new HashMap<>();
        monstersByLocation = new HashMap<>();

        // 讀取 locations 和 gatherables
        loadLocations();
        loadGatherables();
        loadMonsters();
    }

    // 從 world_map.json 讀取 locations
    private void loadLocations() {
        // 抓取 world_map.json 檔案
        FileHandle worldMapFile = Gdx.files.internal("json/world_map.json");
        JsonReader jsonReader = new JsonReader();
        JsonValue json = jsonReader.parse(worldMapFile);

        // 讀取 world_maps.json
        for (JsonValue locationJson : json.get("locations")) {
            int id = locationJson.getInt("id");
            String name = locationJson.getString("name");
            String chineseName = locationJson.getString("chinese_name");
            String description = locationJson.getString("description");
            boolean isTown = locationJson.getBoolean("is_town");

            // 讀取 connections
            List<Integer> connections = new ArrayList<>();
            for (JsonValue connection : locationJson.get("connections")) {
                connections.add(connection.asInt());
            }

            Location location = new Location(id, name, chineseName, description, isTown, connections);
            locations.put(id, location);

            location.printPossibleMonsters();
        }
    }

    // 從 gatherable.json 讀取 gatherables
    private void loadGatherables() {
        FileHandle file = Gdx.files.internal("json/gatherable.json");
        JsonReader jsonReader = new JsonReader();
        JsonValue json = jsonReader.parse(file);

        for (JsonValue regionJson : json.get("regions")) {
            int regionID = regionJson.getInt("region_ID");
            List<GatherableObject> gatherableList = new ArrayList<>();

            // 讀取 gatherable_object
            for (JsonValue objectJson : regionJson.get("gatherable_object")) {
                String objectName = objectJson.getString("object_name");
                String chineseName = objectJson.getString("chinese_name");
                String description = objectJson.getString("description");
                String iconPath = objectJson.getString("iconPath");
                int encounterRate = objectJson.getInt("encounter_rate");

                // 讀取 gatherable_object 中的 drop_items
                List<DropItem> dropItems = new ArrayList<>();
                for (JsonValue dropItemJson : objectJson.get("drop_items")) {
                    String itemID = dropItemJson.getString("item_ID");
                    int minDrop = dropItemJson.get("drop_count").get(0).asInt();
                    int maxDrop = dropItemJson.get("drop_count").get(1).asInt();
                    int dropRate = dropItemJson.getInt("drop_rate");

                    dropItems.add(new DropItem(itemID, minDrop, maxDrop, dropRate));
                }

                gatherableList.add(new GatherableObject(encounterRate, objectName, chineseName, description, iconPath, dropItems));
            }

            gatherablesByLocation.put(regionID, gatherableList);
            Gdx.app.log("LocationManager", "Loaded gatherables for region " + regionID);
        }
    }

    // 從 region_monster.json 讀取怪物
    private void loadMonsters() {
        FileHandle file = Gdx.files.internal("json/region_monsters.json");
        JsonReader jsonReader = new JsonReader();
        JsonValue json = jsonReader.parse(file);

        for (JsonValue regionJson : json.get("regions")) {
            int regionID = regionJson.getInt("region_ID");
            List<Monster> monsterList = new ArrayList<>();

            // 讀取 monsters
            for (JsonValue monsterJson : regionJson.get("monsters")) {
                String monsterID = monsterJson.getString("monster_ID");
                String name = monsterJson.has("name") ? monsterJson.getString("name") : "Unknown";
                Gdx.app.log("LocationManager", "Loaded monster " + name);
                String chineseName = monsterJson.getString("chinese_name");
                String description = monsterJson.getString("description");
                String iconPath = monsterJson.getString("iconPath");
                int encounterRate = monsterJson.getInt("encounter_rate");

                // 隨機取出一個等級
                int minLv = monsterJson.get("attributes").get("Lv").get(0).asInt();
                int maxLv = monsterJson.get("attributes").get("Lv").get(1).asInt();
                int Lv = minLv + (int)(Math.random() * ((maxLv - minLv) + 1));
                // 計算HP
                int maxHPBase = monsterJson.get("attributes").get("hp").getInt("base");
                int maxHPGrowth = monsterJson.get("attributes").get("hp").getInt("growth");
                int maxHP = maxHPBase + maxHPGrowth*Lv;
                // 計算atk
                int atkBase = monsterJson.get("attributes").get("atk").getInt("base");
                int atkGrowth = monsterJson.get("attributes").get("atk").getInt("growth");
                int atk = atkBase + atkGrowth*Lv;

                int def = monsterJson.get("attributes").getInt("def");
                int spd = monsterJson.get("attributes").getInt("spd");
                int exp = monsterJson.get("attributes").getInt("exp");
                int gold = monsterJson.get("attributes").getInt("gold");

                // 載入 itemDrops
                List<DropItem> itemDrops = new ArrayList<>();
                for (JsonValue dropItemJson : monsterJson.get("attributes").get("drop_items")) {
                    String itemID = dropItemJson.getString("item_ID");
                    int minDrop = dropItemJson.get("drop_count").get(0).asInt();
                    int maxDrop = dropItemJson.get("drop_count").get(1).asInt();
                    int dropRate = dropItemJson.getInt("drop_rate");

                    itemDrops.add(new DropItem(itemID, minDrop, maxDrop, dropRate));
                }

                monsterList.add(new Monster(monsterID, name, chineseName, description, maxHP, atk, def, spd, exp, gold, iconPath, encounterRate, itemDrops));
            }

            monstersByLocation.put(regionID, monsterList);
            Gdx.app.log("LocationManager", "Loaded monsters for region " + regionID);
        }
    }

    // 根據地區 ID 獲取該地區的可採集物品
    public List<GatherableObject> getGatherablesObjects(int locationID) {
        return gatherablesByLocation.getOrDefault(locationID, new ArrayList<>());
    }

    // 根據地區 ID 獲取該地區的怪物
    public List<Monster> getMonsters(int locationID) {
        return monstersByLocation.getOrDefault(locationID, new ArrayList<>());
    }

    public Location getLocationByID(int id) {
        return locations.get(id);
    }

    // 回傳 Location 的 name
    public String getLocationName(int id) {
        Location location = locations.get(id);
        return (location != null) ? location.getName() : "";
    }

    public List<Integer> getConnections(int locationID) {
        Location location = locations.get(locationID);
        return (location != null) ? location.getConnections() : new ArrayList<>();
    }
}
