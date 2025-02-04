package com.mygame.rpg;

import java.util.List;

public class Location {
    private int id;
    private String name;
    private String chineseName;
    private boolean isTown;
    private List<String> gatherableItems;
    private List<String> possibleEnemies;
    private List<Integer> connections; // **使用 ID 來存放相鄰地區**

    public Location(int id, String name, String chineseName, boolean isTown, List<String> gatherableItems, List<String> possibleEnemies, List<Integer> connections) {
        this.id = id;
        this.name = name;
        this.chineseName = chineseName;
        this.isTown = isTown;
        this.gatherableItems = gatherableItems;
        this.possibleEnemies = possibleEnemies;
        this.connections = connections;
    }

    public int getId() {
        return id;
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

    public List<Integer> getConnections() {
        return connections;
    }
}
