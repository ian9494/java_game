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

    public LocationManager() {
        locations = new HashMap<>();
        loadLocations();
    }

    private void loadLocations() {
        FileHandle file = Gdx.files.internal("json\\world_map.json");
        JsonReader jsonReader = new JsonReader();
        JsonValue json = jsonReader.parse(file);

        for (JsonValue locationJson : json.get("locations")) {
            int id = locationJson.getInt("id");
            String name = locationJson.getString("name");
            String chineseName = locationJson.getString("chinese_name");
            boolean isTown = locationJson.getBoolean("is_town");

            List<String> gatherableItems = new ArrayList<>();
            for (JsonValue item : locationJson.get("gatherable_items")) {
                gatherableItems.add(item.asString());
            }

            List<String> possibleEnemies = new ArrayList<>();
            for (JsonValue enemy : locationJson.get("possible_enemies")) {
                possibleEnemies.add(enemy.asString());
            }

            List<Integer> connections = new ArrayList<>();
            for (JsonValue connection : locationJson.get("connections")) {
                connections.add(connection.asInt());
            }

            locations.put(id, new Location(id, name, chineseName, isTown, gatherableItems, possibleEnemies, connections));
        }
    }

    public Location getLocationByID(int id) {
        return locations.get(id);
    }

    public List<Integer> getConnections(int locationID) {
        Location location = locations.get(locationID);
        return (location != null) ? location.getConnections() : new ArrayList<>();
    }
}
