package com.mygame.rpg;

import java.util.HashMap;
import java.util.Map;


public class Region {
    private String name;
    private String description;
    private String iconPath;
    private Map<String, Region> connections; // 使用方向作為鍵

    public Region(String name, String description, String iconPath) {
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
        this.connections = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void connect(String direction, Region region) {
        connections.put(direction, region);
    }

    public Region getConnectedRegion(String direction) {
        return connections.get(direction);
    }

    public Map<String, Region> getConnections() {
        return connections;
    }
}
