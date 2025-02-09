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

    public LocationManager() {
        // 初始化 locations 和 gatherablesByLocation
        locations = new HashMap<>();
        gatherablesByLocation = new HashMap<>();

        // 讀取 locations 和 gatherables
        loadLocations();
        loadGatherables();
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

            locations.put(id, new Location(id, name, chineseName, description, isTown, connections));
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

                // 讀取 gatherable_object 中的 drop_items
                List<DropItem> dropItems = new ArrayList<>();
                for (JsonValue dropItemJson : objectJson.get("drop_items")) {
                    String itemID = dropItemJson.getString("item_ID");
                    int minDrop = dropItemJson.get("drop_count").get(0).asInt();
                    int maxDrop = dropItemJson.get("drop_count").get(1).asInt();
                    int dropRate = dropItemJson.getInt("drop_rate");

                    dropItems.add(new DropItem(itemID, minDrop, maxDrop, dropRate));
                }

                gatherableList.add(new GatherableObject(objectName, chineseName, description, iconPath, dropItems));
            }

            gatherablesByLocation.put(regionID, gatherableList);
            Gdx.app.log("LocationManager", "Loaded gatherables for region " + regionID);
        }
    }

    // 根據地區 ID 獲取該地區的可採集物品
    public List<GatherableObject> getGatherablesObjects(int locationID) {
        return gatherablesByLocation.getOrDefault(locationID, new ArrayList<>());
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
