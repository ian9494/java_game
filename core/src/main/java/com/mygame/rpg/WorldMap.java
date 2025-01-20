package com.mygame.rpg;

import java.util.HashMap;
import java.util.Map;

public class WorldMap {
    private Map<String, Region> regions;

    public WorldMap() {
        this.regions = new HashMap<>();
    }

    public void addRegion(Region region) {
        regions.put(region.getName(), region);
    }

    public Region getRegion(String name) {
        return regions.get(name);
    }

    public void connectRegions(String region1Name, String region2Name, String direction) {
        Region region1 = regions.get(region1Name);
        Region region2 = regions.get(region2Name);

        if (region1 != null && region2 != null) {
            // 連接兩個區域，並根據方向設定相反方向
            region1.connect(direction, region2);
            region2.connect(getOppositeDirection(direction), region1);
        }
    }

    private String getOppositeDirection(String direction) {
        switch (direction) {
            case "UP": return "DOWN";
            case "DOWN": return "UP";
            case "LEFT": return "RIGHT";
            case "RIGHT": return "LEFT";
            default: throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    public Map<String, Region> getAllRegions() {
        return regions;
    }
}
