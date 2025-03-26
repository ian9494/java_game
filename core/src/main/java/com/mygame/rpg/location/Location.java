package com.mygame.rpg.location;

import com.badlogic.gdx.Gdx;
import java.util.List;
import java.util.ArrayList;

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
    private List<String> possibleMonsters;

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
    public Location(int id, String name, String chineseName, String description, boolean isTown, List<String> gatherableItems, List<String> possibleMonsters, List<Integer> connections, List<String> weathers, List<String> daytimes) {
        this.id = id;
        this.name = name;
        this.chineseName = chineseName;
        this.description = description;
        this.isTown = isTown;
        this.gatherableItems = gatherableItems;
        this.possibleMonsters = new ArrayList<>();
        this.connections = connections;
        this.weathers = weathers;
        this.daytimes = daytimes;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getChineseName() { return chineseName; }
    public boolean isTown() { return isTown; }
    public List<String> getGatherableItems() { return gatherableItems; }
    public List<String> getPossibleMonsters() { return possibleMonsters; }
    public List<Integer> getConnections() { return connections; }
    public List<String> getWeathers() { return weathers; }
    public List<String> getDaytimes() { return daytimes; }
    public String getDescription() { return description; }

    public void printPossibleMonsters() {
        if (possibleMonsters == null) {
        }
        else if (possibleMonsters.isEmpty()) {
            Gdx.app.log("location", "possibleMonsters is empty");
        }
        else {
            for (String monster : possibleMonsters) {
                // Gdx.app.log("location", monster);
            }
        }
    }
}
