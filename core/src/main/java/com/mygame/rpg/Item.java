package com.mygame.rpg;

public class Item {
    private String itemID;
    private String name;
    private String description;
    private int quantity;

    public Item(String itemID, String name, String description, int quantity) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }

    public String getItemID() { return itemID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public void removeQuantity(int amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }
}
