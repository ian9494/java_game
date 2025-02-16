package com.mygame.rpg;

import java.util.Random;

public class DropItem {
    private String itemID;
    private String name;
    private String chineseName;
    private String description;
    // 掉落數量的範圍
    private int minDrop;
    private int maxDrop;

    private int dropRate; // 掉落機率（百分比）

    public DropItem(String itemID, int minDrop, int maxDrop, int dropRate) {
        this.itemID = itemID;
        this.minDrop = minDrop;
        this.maxDrop = maxDrop;
        this.dropRate = dropRate;
    }

    public String getItemID() { return itemID; }
    public String getName() {return name; }
    public int getDropRate() { return dropRate; }

    // 從item.json取得item name
    public void setItemName(String name, String chineseName, String description) {
        this.name = name;
        this.chineseName = chineseName;
        this.description = description;
    }

    // 取得隨機掉落數量
    public int getRandomDropCount() {
        if (maxDrop == minDrop) {
            return minDrop;
        }
        Random random = new Random();
        return minDrop + random.nextInt(maxDrop - minDrop + 1);
    }
}
