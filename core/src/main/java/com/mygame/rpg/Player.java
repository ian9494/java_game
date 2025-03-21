package com.mygame.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player extends Character {
    private int exp;
    private int expToNextLV;
    private int gold;

    private Map<String, Item> inventory;

    public int getExpToNextLV() { return expToNextLV; }
    public int getExp() { return exp; }
    public int getGold() { return gold; }
    public int addGold(int amount) { return gold += amount; }
    public void setGold(int gold) { this.gold = gold; }
    public Map<String, Item> getInventory() { return inventory; }

    // 必須有無參數建構子，否則會無法建立物件
    public Player() {
        super("Default", 1); // 設定預設角色名稱與等級
        this.exp = 0;
        this.expToNextLV = 100;
        this.LocationID = 1;
        this.inventory = new HashMap<>();
    }

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
    public String addItem(String itemID, int amount) {
        Item addingItem = new Item(itemID, amount);
        addingItem.setItemInfo();
        if (inventory.containsKey(itemID)) {
            inventory.get(itemID).addQuantity(amount);
        } else {
            inventory.put(itemID, addingItem);
        }
        Gdx.app.log("Player-inventory", name + " gets x" + amount + " " + addingItem.getName());
        return addingItem.getName();
    }

    // 移除物品
    public void removeItem(String itemID, int amount) {
        if (inventory.containsKey(itemID)) {
            Item item = inventory.get(itemID);
            item.removeQuantity(amount);
            if (item.getQuantity() == 0) {
                inventory.remove(itemID);
            }
            Gdx.app.log("Player-inventory", "removed " + item.getName() + " x" + amount);
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

    // 消耗金幣
    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        } else {
            Gdx.app.log("Player - Spend gold", "Not enough gold!");
            return false;
        }
    }

    // 死亡懲罰
    public void applyDeathPenalty() {
        int lostGold = gold / 5; // 例如損失 20% 金錢
        gold -= lostGold;
        if (gold < 0) {
            gold = 0;
        }
        Gdx.app.log("Player - Death penalty", "Lost " + lostGold + " gold");

        int lostExp = exp / 5; // 例如損失 20% 經驗
        exp -= lostExp;
        if (exp < 0) {
            exp = 0;
        }
        Gdx.app.log("Player - Death penalty", "Lost " + lostExp + " exp");
    }

    // 恢復 HP 與 MP
    public void restoreHPMP(int percentage) {
        hp = Math.min(hp + maxHp * percentage / 100, maxHp);
        mp = Math.min(mp + maxMp * percentage / 100, maxMp);
    }

    // 使用道具
    public void useItem(String itemID) {
        if (inventory.containsKey(itemID)) {
            Item item = inventory.get(itemID);
            item.useItem(this); // 讓道具的效果作用在玩家身上
            item.removeQuantity(1); // 消耗 1 個道具
            if (item.getQuantity() == 0) {
                inventory.remove(itemID);
            }
            Gdx.app.log("Player-inventory", "used " + item.getName());
        } else {
            Gdx.app.log("Player-inventory", "do not have item: " + itemID);
        }
    }

    public void addTemporaryBuff(String type, int value, int duration) {
        switch (type) {
            case "atk":
                this.Atk += value;
                break;

            case "def":
                this.Def += value;
                break;

            case "spd":
                this.Spd += value;
                break;

            default:
                Gdx.app.log("Player - Buff", "Unknown buff type: " + type);
        }
    }

    public void respawn() {
        this.hp = this.maxHp; // 將 HP 重置為最大值
        // 其他需要重置的狀態
    }

    public void returnToTown() {
        // 傳送玩家到城鎮的邏輯
        Gdx.app.log("player", "returning to town");
        // 設定為城鎮ID
        // TODO 尋找最近的城鎮ID
        this.LocationID = 1;
    }

    // 移動到新地點
    public void setLocationID(int locationID) {
        this.LocationID = locationID;
    }

    // 存檔到json
    public void saveToFile(String fileName) {
        Json json = new Json();
        String playerData = json.prettyPrint(this); // 轉成可讀 JSON 格式

        FileHandle file = Gdx.files.local(fileName);
        file.writeString(playerData, false);
        Gdx.app.log("Player - Save file", "file saved: " + fileName);
    }

    // 從json載入
    public static Player loadFromFile(String fileName) {
        FileHandle file = Gdx.files.local(fileName);
        if (!file.exists()) {
            Gdx.app.log("Player - Load file", "file don't exist: " + fileName);
            return new Player("Hero");
        }

        Json json = new Json();
        return json.fromJson(Player.class, file.readString());
    }
}
