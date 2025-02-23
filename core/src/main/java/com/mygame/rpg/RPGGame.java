package com.mygame.rpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class RPGGame extends Game {
    private SpriteBatch batch;
    private Player player;
    private Monster enemy;

    private BattleScreen battleScreen;

    public Player getPlayer() {return player;}
    public Monster getEnemy() {return enemy;}

    public void setBattleScreen(BattleScreen battleScreen) {
        this.battleScreen = battleScreen;
    }

    public BattleScreen getBattleScreen() {
        return battleScreen;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        // Gdx.app.setApplicationLogger(new FileLogger("game_log.txt")); // 設置日誌文件

        // 初始化角色
        player = Player.loadFromFile("save/player.json");

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
