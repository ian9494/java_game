package com.mygame.rpg;

import com.badlogic.gdx.Gdx;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player extends Character {
    private int exp;
    private int expToNextLV;

    private Map<String, Item> inventory;

    public int getExpToNextLV() { return expToNextLV; }
    public int getExp() { return exp; }
    public Map<String, Item> getInventory() { return inventory; }

    public Player(String name) {
        super(name, 1); // 玩家初始等級 1
        this.exp = 0;
        this.LocationID = 1; // 初始位置為 1
        updateStats(); // 依據等級計算屬性
        this.inventory = new HashMap<>();
    }

    // 根據等級計算數值
    public void updateStats() {
        Gdx.app.log("Player - Status", "Updating stats for " + name + "...");
        this.maxHp = (100 + (LV - 1) * 20);
        this.maxMp = 50 + (LV - 1) * 10;
        this.Atk = 10 + (LV - 1) * 3;
        this.Def = 5 + (LV - 1) * 2;
        this.Spd = 20;
        this.hp = maxHp;
        this.mp = maxMp;
        this.expToNextLV = LV * (LV * 20);
        Gdx.app.log("Player - Status", "expToNextLV: " + expToNextLV);
    }

    // 獲得經驗
    public void gainExp(int amount) {
        exp += amount;
        while (exp >= expToNextLV) {
            LVUp();
        }
    }

    // 獲得物品
    public void addItem(String itemID, int amount) {
        Item addingItem = new Item(itemID, amount);
        addingItem.setItemName();
        if (inventory.containsKey(itemID)) {
            inventory.get(itemID).addQuantity(amount);
        } else {
            inventory.put(itemID, addingItem);
        }
        Gdx.app.log("Player-inventory", name + " x" + amount + " " + addingItem.getName());
    }

    // 移除物品
    public void removeItem(String itemID, int amount) {
        if (inventory.containsKey(itemID)) {
            Item item = inventory.get(itemID);
            item.removeQuantity(amount);
            if (item.getQuantity() == 0) {
                inventory.remove(itemID);
            }
            System.out.println("移除了 " + item.getName() + " x" + amount);
        }
    }

    // 升級
    private void LVUp() {
        exp -= expToNextLV;
        LV++;
        expToNextLV += LV * 20; // 升級所需經驗增加
        updateStats(); // 重新計算屬性
        Gdx.app.log("Player - Status", name + " Leveled up to " + LV + "!");
    }

    // 移動到新地點
    public void setLocationID(int locationID) {
        this.LocationID = locationID;
    }
}
