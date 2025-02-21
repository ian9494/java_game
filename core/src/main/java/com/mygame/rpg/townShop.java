package com.mygame.rpg;

import java.util.List;

public class townShop {
    private List<Item> shopInventory;

    public townShop(List<Item> shopInventory) {
        this.shopInventory = shopInventory;
    }

    // buy item from shop
    public void buyItem(Player player, int itemIndex) {
        Item item = shopInventory.get(itemIndex);
        if (player.getGold() >= item.getPrice()) {
            player.setGold(player.getGold() - item.getPrice());
            player.addItem(item.getItemID(), 1);
            System.out.println("You bought " + item.getName() + " for " + item.getPrice() + " gold.");
        } else {
            System.out.println("You don't have enough gold to buy " + item.getName());
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
