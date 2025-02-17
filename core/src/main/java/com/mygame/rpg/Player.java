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

    private Map<String, Item> inventory;

    public int getExpToNextLV() { return expToNextLV; }
    public int getExp() { return exp; }
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
        addingItem.setItemName();
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
            System.out.println("移除了 " + item.getName() + " x" + amount);
        }
    }

    // 升級
    private void LVUp() {
        // 紀錄上一級資訊以便做比對
        int oldMaxHp = maxHp;
        int oldMaxMp = maxMp;
        int oldAtk = Atk;
        int oldDef = Def;

        exp -= expToNextLV;
        LV++;
        expToNextLV += LV * 20; // 升級所需經驗增加

        updateStats(); // 重新計算屬性`

        // 編輯升級資訊
        StringBuilder levelUpMessage = new StringBuilder();
        levelUpMessage.append("Level up to ").append(LV).append("!\n");
        levelUpMessage.append("HP: ").append(oldMaxHp).append(" -> ").append(maxHp).append("\n");
        levelUpMessage.append("MP: ").append(oldMaxMp).append(" -> ").append(maxMp).append("\n");
        levelUpMessage.append("ATK: ").append(oldAtk).append(" -> ").append(Atk).append("\n");
        levelUpMessage.append("DEF: ").append(oldDef).append(" -> ").append(Def).append("\n");

        Gdx.app.log("Player - LevelUp", levelUpMessage.toString());

        // 觸發 UI 顯示升級訊息
        RPGGame game = (RPGGame) Gdx.app.getApplicationListener();
        game.getBattleScreen().showLevelUpMessage(levelUpMessage.toString());
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
        Gdx.app.log("Player - Save file", "存檔完成: " + fileName);
    }

    // 從json載入
    public static Player loadFromFile(String fileName) {
        FileHandle file = Gdx.files.local(fileName);
        if (!file.exists()) {
            Gdx.app.log("Player - Load file", "存檔不存在，即將創建新角色");
            return new Player("Hero");
        }

        Json json = new Json();
        return json.fromJson(Player.class, file.readString());
    }
}
