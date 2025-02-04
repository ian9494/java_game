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
    private Enemy enemy;

    public Player getPlayer() {return player;}
    public Enemy getEnemy() {return enemy;}


    @Override
    public void create() {
        batch = new SpriteBatch();

        // 初始化角色
        player = new Player("Hero");
        enemy = new Enemy("Goblin", 80, 30, 15, 5, 10, 100, null);

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
        batch.dispose();
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }
}
