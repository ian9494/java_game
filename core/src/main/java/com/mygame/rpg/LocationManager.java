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
    private Map<String, Location> locations;

    public LocationManager() {
        locations = new HashMap<>();
        loadLocations();
    }

    private void loadLocations() {
        FileHandle file = Gdx.files.internal("world_map.json");
        JsonReader jsonReader = new JsonReader();
        JsonValue json = jsonReader.parse(file);

        for (JsonValue locationJson : json.get("locations")) {
            String name = locationJson.name();
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

            List<String> connections = new ArrayList<>();
            for (JsonValue connection : locationJson.get("connections")) {
                connections.add(connection.asString());
            }

            locations.put(name, new Location(name, chineseName, isTown, gatherableItems, possibleEnemies, connections));
        }
    }

    public Location getLocationByName(String name) {
        return locations.get(name);
    }

    public List<String> getConnections(int locationCode) {
        Location location = locations.get(locationCode);
        return (location != null) ? location.getConnections() : new ArrayList<>();
    }
}
