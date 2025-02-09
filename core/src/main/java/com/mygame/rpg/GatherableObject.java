package com.mygame.rpg;

import java.util.List;
import java.util.Random;

public class GatherableObject {
    private String objectName;
    private String chineseName;
    private String description;
    private String iconPath;
    private List<DropItem> dropItems;

    public GatherableObject(String objectName, String chineseName, String description, String iconPath, List<DropItem> dropItems) {
        this.objectName = objectName;
        this.chineseName = chineseName;
        this.description = description;
        this.iconPath = iconPath;
        this.dropItems = dropItems;
    }

    public String getObjectName() { return objectName; }
    public String getChineseName() { return chineseName; }
    public String getDescription() { return description; }
    public String getIconPath() { return iconPath; }
    public List<DropItem> getDropItems() { return dropItems; }

    public DropItem getRandomDropItem() {
        Random random = new Random();
        for (DropItem drop : dropItems) {
            if (random.nextInt(100) < drop.getDropRate()) { // 確定掉落
                return drop;
            }
        }
        return null;
    }
}
