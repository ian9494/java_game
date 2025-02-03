package com.mygame.rpg;

import java.util.List;

public class Location {
    private String name;
    private String chineseName;
    private boolean isTown;
    private List<String> gatherableItems;
    private List<String> possibleEnemies;
    private List<String> connections; // **新增相鄰地區**

    public Location(String name, String chineseName, boolean isTown, List<String> gatherableItems, List<String> possibleEnemies, List<String> connections) {
        this.name = name;
        this.chineseName = chineseName;
        this.isTown = isTown;
        this.gatherableItems = gatherableItems;
        this.possibleEnemies = possibleEnemies;
        this.connections = connections;
    }

    public String getName() {
        return name;
    }

    public String getChineseName() {
        return chineseName;
    }

    public boolean isTown() {
        return isTown;
    }

    public List<String> getGatherableItems() {
        return gatherableItems;
    }

    public List<String> getPossibleEnemies() {
        return possibleEnemies;
    }

    public List<String> getConnections() {
        return connections;
    }
}
