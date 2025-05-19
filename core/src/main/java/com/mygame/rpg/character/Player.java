package com.mygame.rpg.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygame.rpg.item.EquipSlot;
import com.mygame.rpg.item.Equipment;
import com.mygame.rpg.item.EquipmentDatabase;
import com.mygame.rpg.item.Item;
import com.mygame.rpg.item.Skill;
import com.mygame.rpg.item.SkillDatabase;
import com.mygame.rpg.item.Weapon;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ietf.jgss.GSSContext;

public class Player extends Character {
    private int exp;
    private int expToNextLV;
    private int gold;

    private Map<EquipSlot, Equipment> equippedItems = new HashMap<>(); // 裝備的物品
    private Weapon equippedWeapon; // 裝備的武器

    private Map<String, Item> itemInventory; // 道具背包
    private Map<String, Equipment> equipmentInventory; // 裝備背包
    private Map<String, Weapon> weaponInventory; // 武器背包

    public int getExpToNextLV() { return expToNextLV; }
    public int getExp() { return exp; }
    public int getGold() { return gold; }

    public int addGold(int amount) { return gold += amount; }
    public void setGold(int gold) { this.gold = gold; }
    public Map<String, Item> getItemInventory() { return itemInventory; }

    // 必須有無參數建構子，否則會無法建立物件
    public Player() {
        super("Default", 1); // 設定預設角色名稱與等級
        this.exp = 0;
        this.expToNextLV = 100;
        this.LocationID = 1;
        this.itemInventory = new HashMap<>();
        this.equipmentInventory = new HashMap<>();
        this.weaponInventory = new HashMap<>();
    }

    public Player(String name) {
        super(name, 1); // 玩家初始等級 1
        this.exp = 0;
        this.LocationID = 1; // 初始位置為 1
        updateStats(); // 依據等級計算屬性
        if (equipmentInventory == null) {equipmentInventory = new HashMap<>(); }
        if (weaponInventory == null) {weaponInventory = new HashMap<>(); }
        if (itemInventory == null) {
            itemInventory = new HashMap<>();
        }
        this.itemInventory = new HashMap<>();
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

    // 獲得裝備物品
    public List<Equipment> getEquipmentItem(EquipSlot slot) {
        List<Equipment> result = new ArrayList<>();
        for (Equipment eq : equipmentInventory.values()) {
            if (eq.getSlot() == slot) {
                result.add(eq);
            }
        }
        return result;
    }

    // 獲得武器
    public List<Weapon> getWeapons() {
        return new ArrayList<>(weaponInventory.values());
    }

    // 裝備武器
    public void equipWeapon(Weapon weapon) {
        if (!weaponInventory.containsKey(weapon.getEquipmentID())) {
            Gdx.app.log("Player - Inventory", "Weapon not found in inventory: " + weapon.getName());
            return;
        }

        // 如果已經有裝備，先卸下
        if (equippedWeapon != null) {
            unEquipWeapon();
        }

        // 從武器背包中移除 → 裝上武器
        weaponInventory.remove(weapon.getEquipmentID());
        equippedWeapon = weapon;
        updateAvailableSkills();
        Gdx.app.log("Player - Inventory", "Equipped " + weapon.getName() + " as weapon");
    }

    // 裝備物品
    public void equipItem(EquipSlot slot, Equipment item) {
        boolean found = false;

        // 如果是武器，檢查是否在武器背包中
        if (slot == EquipSlot.WEAPON && item instanceof Weapon) {
            if (!weaponInventory.containsKey(item.getEquipmentID())) {
                Gdx.app.log("Player - Inventory", "Weapon not found in inventory: " + item.getName());
                return;
            }
            weaponInventory.remove(item.getEquipmentID());
            found = true;
        // 如果是裝備，檢查是否在裝備背包中
        } else {
            if (!equipmentInventory.containsKey(item.getEquipmentID())) {
                Gdx.app.log("Player - Inventory", "Item not found in inventory: " + item.getName());
                return;
            }
            equipmentInventory.remove(item.getEquipmentID());
            found = true;
        }

        // 如果該欄位已經有裝備，先卸下
        if (found) {
        if (equippedItems.containsKey(slot)) {
            if (equippedItems.get(slot) instanceof Weapon) {
                unEquipWeapon();
            } else {
                unEquipItem(slot);
            }
        }

        equippedItems.put(slot, item);
        updateAvailableSkills();
        Gdx.app.log("Player - Inventory", "Equipped " + item.getName() + " in " + slot);
        }
    }

    // 卸下武器
    public void unEquipWeapon() {
        if (equippedWeapon != null) {
            // 將武器從裝備欄中移除
            Weapon wp = equippedWeapon;
            equippedWeapon = null;
            weaponInventory.put(wp.getEquipmentID(), wp);
            Gdx.app.log("Player - Inventory", "Unequipped " + wp.getName() + " from weapon slot");
        } else {
            Gdx.app.log("Player - Inventory", "No weapon equipped");
        }
    }

    // 卸下裝備
    public void unEquipItem(EquipSlot slot) {
        if (equippedItems.containsKey(slot)) {
            Equipment item = equippedItems.remove(slot);
            equipmentInventory.put(item.getEquipmentID(), item);
            Gdx.app.log("Player - Inventory", "Unequipped " + item.getName() + " from " + slot);
        } else {
            Gdx.app.log("Player - Inventory", "No item equipped in " + slot);
        }
    }

    // 獲得裝備物品
    public Equipment getEquipmentBySlot(EquipSlot slot) {
        return equippedItems.get(slot);
    }

    // 獲得裝備的武器
    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    // 更新可用技能
    public void updateAvailableSkills() {
        List<String> allowedTypes = new ArrayList<>();

        for (Equipment equipment : equippedItems.values()) {
            allowedTypes.addAll(equipment.getAllowedSkillTypes());
        }
    }

    // 所有技能冷卻一次
    public void tickCooldown() {
        if (equippedWeapon == null) {
            Gdx.app.log("Player - Inventory", "No weapon equipped");
            return;
        }
        List<Skill> skills = equippedWeapon.getAvailableSkillIds();
        for (Skill skill : skills) {
            skill.tickCooldown();
        }
    }

    // 獲得所有物品ID 包含裝備與道具
    public List<String> getAllInventoryIDs() {
        List<String> IDs = new ArrayList<>();
        IDs.addAll(itemInventory.keySet());
        IDs.addAll(equipmentInventory.keySet());
        IDs.addAll(weaponInventory.keySet());
        return IDs;
    }

    // 獲得物品
   public String addItem(String itemID, int amount) {
        // 如果是武器，用 Weapon 物件
        if (itemID.startsWith("3")) {
            Weapon weapon = EquipmentDatabase.getWeaponByID(itemID);
            weapon.setCurrentStage(new ArrayList<>(Arrays.asList(1))); // 設定當前階段
            // weapon.setSkillMastery(Map.of("SLASH", 10));
            Gdx.app.log("Player - Inventory", "weapon status:" + weapon.getCurrentStage());
            weaponInventory.put(itemID, weapon);
            Gdx.app.log("Player - Inventory", "Added weapon: " + weapon.getName());
            return weapon.getName();
        }

        // 如果是裝備，用 Equipment 物件
        else if (itemID.startsWith("4") || itemID.startsWith("5") || itemID.startsWith("6") || itemID.startsWith("7")) {
            Equipment equipment = EquipmentDatabase.getEquipmentByID(itemID);
            Gdx.app.log("Player - Inventory", "Adding equipment: " + equipment.getName());
            equipmentInventory.put(itemID, equipment);
            Gdx.app.log("Player - Inventory", "Added equipment: " + equipment.getName());
            return equipment.getName();
        }

        // 如果是道具，用 Item 物件
        else if (itemID.startsWith("1") || itemID.startsWith("2")) {
            Gdx.app.log("Player - Inventory", "Adding item: " + itemID);
            Item addingItem = new Item(itemID, amount);
            addingItem.setItemInfo();
            Gdx.app.log("Player - Inventory", "Adding item: " + addingItem.getName());
            if (itemInventory.containsKey(itemID)) {
                itemInventory.get(itemID).addQuantity(amount);
            } else {
                itemInventory.put(itemID, addingItem);
            }
            Gdx.app.log("Player-inventory", name + " gets x" + amount + " " + addingItem.getName());
            return addingItem.getName();
        }

        // 如果物品ID不符合
        Gdx.app.log("Player - Inventory", "Invalid item ID: " + itemID);
        return "Invalid item ID";
    }

    // 移除物品
    public void removeItem(String itemID, int amount) {
        if (itemInventory.containsKey(itemID)) {
            Item item = itemInventory.get(itemID);
            item.removeQuantity(amount);
            if (item.getQuantity() == 0) {
                itemInventory.remove(itemID);
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
        if (itemInventory.containsKey(itemID)) {
            Item item = itemInventory.get(itemID);
            item.useItem(this); // 讓道具的效果作用在玩家身上
            item.removeQuantity(1); // 消耗 1 個道具
            if (item.getQuantity() == 0) {
                itemInventory.remove(itemID);
            }
            Gdx.app.log("Player-inventory", "used " + item.getName());
        } else {
            Gdx.app.log("Player-inventory", "do not have item: " + itemID);
        }
    }

    // 添加臨時增益效果
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

    // 重置玩家狀態
    // 例如死亡後重生
    public void respawn() {
        this.hp = this.maxHp; // 將 HP 重置為最大值
        // 其他需要重置的狀態
    }

    // 傳送回城鎮
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
        json.setOutputType(JsonWriter.OutputType.json);
        json.setTypeName("class");

        json.addClassTag("Weapon", Weapon.class);

        // ✅ 告訴 Json「Map 內的 value 是 Weapon」
        json.setElementType(Player.class, "weaponInventory", Weapon.class);

        String playerData = json.prettyPrint(this);
        FileHandle file = Gdx.files.local(fileName);
        file.writeString(playerData, false);
        Gdx.app.log("Player - Save file", "file saved: " + fileName);
    }

    // 載入 json
    public static Player loadFromFile(String fileName) {
        FileHandle file = Gdx.files.local(fileName);
        if (!file.exists()) {
            Gdx.app.log("Player - Load file", "file don't exist: " + fileName);
            return new Player("Hero");
        }

        Json json = new Json();
        json.setTypeName("class");
        json.addClassTag("Weapon", Weapon.class);
        json.setElementType(Player.class, "weaponInventory", Weapon.class);

        return json.fromJson(Player.class, file.readString());
    }
}
