package com.mygame.rpg;

import java.util.List;
import java.util.Arrays;
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

    public Item(String itemID, String name, int price) {
        this.itemID = itemID;
        this.name = name;
        this.price = price;
    }

    // Getters and setters
    public String getItemID() { return itemID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getType() { return type; }

    // 設定物品名稱
    public void setItemInfo() {
        this.name = ItemManager.getInstance().getItemName(itemID);
        this.description = ItemManager.getInstance().getItemDescription(itemID);
        this.type = ItemManager.getInstance().getItemType(itemID);

        Effect effect = ItemManager.getInstance().getItemEffect(itemID);
        if (effect != null) {
            this.effects = Arrays.asList(effect);
        }
    }

    // 設定中文名稱
    public void setChineseName() {
        this.chineseName = ItemManager.getInstance().getItemChineseName(itemID);
    }

    // 增加物品數量
    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    // 移除物品數量
    public void removeQuantity(int amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }

    // 使用物品
    public void useItem(Player player) {
        Gdx.app.log("Item", "Using item: " + name);
        if (effects != null) {
            Gdx.app.log("Item", "Item has effects: " + effects.size());
            for (Effect effect : effects) {
                switch (effect.getType()) {
                    case "hp_restore":
                        player.setHp(player.getHp() + effect.getValue());
                        break;

                    case "mp_restore":
                        player.setMp(player.getMp() + effect.getValue());
                        break;

                    case "atk_boost":
                        player.addTemporaryBuff("atk", effect.getValue(), effect.getDuration());
                        break;

                    case "def_boost":
                        player.addTemporaryBuff("def", effect.getValue(), effect.getDuration());
                        break;

                    case "spd_boost":
                        player.addTemporaryBuff("spd", effect.getValue(), effect.getDuration());
                        break;

                    case "gold_range":
                        int minGold = effect.getValue();
                        int maxGold = effect.getDuration();
                        int earnedGold = minGold + (int)(Math.random() * (maxGold - minGold));
                        player.setGold(player.addGold(earnedGold));
                        Gdx.app.log("Item", "Earned gold: " + earnedGold);
                        break;

                    default:
                        System.out.println("Unknown effect type: " + effect.getType());
                }
            }
        }
    }

}
