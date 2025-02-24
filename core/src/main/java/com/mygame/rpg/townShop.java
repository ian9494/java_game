package com.mygame.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;
import java.util.List;

public class townShop {
    private List<Item> shopInventory;

    public townShop(String jsonFilePath) {
        this.shopInventory = loadShopInventoryFromJson(jsonFilePath);
    }

    private List<Item> loadShopInventoryFromJson(String jsonFilePath) {
        Json json = new Json();
        ArrayList<JsonValue> jsonData = json.fromJson(ArrayList.class, Gdx.files.internal(jsonFilePath).readString());
        List<Item> inventory = new ArrayList<>();
        for (JsonValue itemValue : jsonData) {
            Item item = new Item(itemValue.getString("itemID"), itemValue.getString("name"), itemValue.getInt("price"));
            inventory.add(item);
        }
        return inventory;
    }

    public List<Item> getInventory() {
        return shopInventory;
    }

    // buy item from shop
    public boolean buyItem(Player player, int itemIndex, int quantity) {
        Item item = shopInventory.get(itemIndex);
        if (player.getGold() >= item.getPrice()) {
            player.setGold(player.getGold() - item.getPrice());
            player.addItem(item.getItemID(), 1);
            System.out.println("You bought " + item.getName() + " for " + item.getPrice() + " gold.");
            return true;
        } else {
            System.out.println("You don't have enough gold to buy " + item.getName());
            return false;
        }
    }

    // sell item to shop
    public void sellItem(Player player, int itemIndex, int quantity) {
        Item item = player.getInventory().get(itemIndex);
        player.setGold(player.getGold() + item.getPrice());
        player.removeItem(item.getItemID(), quantity);
        System.out.println("You sold " + item.getName() + " for " + item.getPrice() + " gold.");
    }

    // display shop inventory
    public void displayShopInventory() {
        System.out.println("Shop Inventory:");
        for (int i = 0; i < shopInventory.size(); i++) {
            Item item = shopInventory.get(i);
            System.out.println(i + ". " + item.getName() + " - " + item.getPrice() + " gold");
        }
    }
}
