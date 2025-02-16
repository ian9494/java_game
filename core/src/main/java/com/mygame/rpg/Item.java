package com.mygame.rpg;

import java.util.List;
import com.badlogic.gdx.Gdx;

public class Item {
    private String itemID;
    private String name;
    private String chineseName;
    private String rarity;
    private String type;
    private int price;
    private int maxStack;
    private String description;
    private int quantity;
    private boolean stackable;

    private List<Effect> effects;

    private ItemManager itemManager = ItemManager.getInstance();

    public Item() {}

    public Item(String itemID, int quantity) {
        this.itemID = itemID;
        this.quantity = quantity;
    }

    public String getItemID() { return itemID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }

    public void setItemName() {
        this.name = ItemManager.getInstance().getItemName(itemID);
        this.description = ItemManager.getInstance().getItemDescription(itemID);
    }

    public void setChineseName() {
        this.chineseName = ItemManager.getInstance().getItemChineseName(itemID);
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public void removeQuantity(int amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }
}
