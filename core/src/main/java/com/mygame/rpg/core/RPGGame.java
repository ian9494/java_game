package com.mygame.rpg.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygame.rpg.character.Monster;
import com.mygame.rpg.character.Player;
import com.mygame.rpg.item.EquipmentDatabase;
import com.mygame.rpg.item.Skill;
import com.mygame.rpg.item.SkillDatabase;
import com.mygame.rpg.item.SkillTreeData;
import com.mygame.rpg.screens.BattleScreen;
import com.mygame.rpg.screens.GameOverScreen;
import com.mygame.rpg.screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class RPGGame extends Game {
    private static Game instance; // 用於獲取當前遊戲實例
    private SpriteBatch batch; // 用於渲染精靈的批處理器
    private Player player; // 玩家角色
    private Monster enemy; // 目前的敵人

    private BattleScreen battleScreen;

    public static RPGGame getInstance() {return (RPGGame) instance;}
    public Player getPlayer() {return player;}
    public Monster getEnemy() {return enemy;}

    // 設置戰鬥場景
    public void startBattle(BattleScreen battleScreen) {
        this.battleScreen = battleScreen;
        setScreen(battleScreen);
    }

    // 結束戰鬥 (回到主菜單)
    public void endBattle() {
        setScreen(new MainMenuScreen(this));
    }

    // 玩家死亡 (顯示 GameOver 畫面)
    public void playerDeath() {
        setScreen(new GameOverScreen(this, player));
    }

    @Override
    // 設置遊戲的初始狀態 在執行初始時調用
    // 這裡可以進行一些初始化操作，比如加載資源、設置初始狀態等
    public void create() {
        instance = this;
        batch = new SpriteBatch();
        // Gdx.app.setApplicationLogger(new FileLogger("game_log.txt")); // 設置日誌文件

        // 初始化資料讀取
        player = Player.loadFromFile("save/player.json");
        EquipmentDatabase.loadFromJson("json/data/equipment.json");

        SkillDatabase.loadAllSkillTrees("json/data/skills");

        // 測試讀取
        SkillTreeData tree = SkillDatabase.getSkillTree("one-handed-sword");
        if (tree != null) {
            Gdx.app.log("Test", "Loaded one-handed-sword skill tree with " + tree.getStages().size() + " stages.");
        } else {
            Gdx.app.log("Skill", "Failed to load one-handed-sword skill tree.");
        }


        // 設置 BattleScreen 為當前屏幕
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        // 將控制權交給當前的 Screen
        super.render();
    }

    @Override
    // 在遊戲關閉時釋放資源 以及保存遊戲狀態
    public void dispose() {
        player.saveToFile("save/player.json"); // 關閉遊戲時自動存檔
        batch.dispose();
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }
}
