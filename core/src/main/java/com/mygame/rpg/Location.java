package com.mygame.rpg;

import java.util.List;

public class Location {
    private int id;
    private String name;
    private String chineseName;
    private String description;
    private boolean isTown;

    private List<String> weathers;
    private List<String> daytimes;
    private List<Integer> connections; // **使用 ID 來存放相鄰地區**
    private List<String> gatherableItems;
    private List<String> possibleEnemies;

    // 載入城鎮地區
    public Location(int id, String name, String chineseName, String description, boolean isTown, List<Integer> connections) {
        this.id = id;
        this.name = name;
        this.chineseName = chineseName;
        this.description = description;
        this.isTown = isTown;
        this.connections = connections;
    }

    // 載入非城鎮地區
    public Location(int id, String name, String chineseName, String description, boolean isTown, List<String> gatherableItems, List<String> possibleEnemies, List<Integer> connections, List<String> weathers, List<String> daytimes) {
        this.id = id;
        this.name = name;
        this.chineseName = chineseName;
        this.description = description;
        this.isTown = isTown;
        this.gatherableItems = gatherableItems;
        this.possibleEnemies = possibleEnemies;
        this.connections = connections;
        this.weathers = weathers;
        this.daytimes = daytimes;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getChineseName() { return chineseName; }
    public boolean isTown() { return isTown; }
    public List<String> getGatherableItems() { return gatherableItems; }
    public List<String> getPossibleEnemies() { return possibleEnemies; }
    public List<Integer> getConnections() { return connections; }
    public List<String> getWeathers() { return weathers; }
    public List<String> getDaytimes() { return daytimes; }
    public String getDescription() { return description; }

}
