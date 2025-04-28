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
    private static Game instance;
    private SpriteBatch batch;
    private Player player;
    private Monster enemy;
    private GameState state;

    private BattleScreen battleScreen;

    public static RPGGame getInstance() {return (RPGGame) instance;}
    public Player getPlayer() {return player;}
    public Monster getEnemy() {return enemy;}

    public void setState(GameState newState) {
        state = newState;
        Gdx.app.log("Game-State", "Game state changed to " + state);
    }

    public void startBattle(BattleScreen battleScreen) {
        this.battleScreen = battleScreen;
        setScreen(battleScreen);
    }

    public void endBattle() {
        setScreen(new MainMenuScreen(this));
    }

    public void playerDeath() {
        setScreen(new GameOverScreen(this, player));
    }

    @Override
    public void create() {
        instance = this;
        batch = new SpriteBatch();
        // Gdx.app.setApplicationLogger(new FileLogger("game_log.txt")); // 設置日誌文件

        // 初始化角色
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
    public void dispose() {
        player.saveToFile("save/player.json"); // 關閉遊戲時自動存檔
        batch.dispose();
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }
}
