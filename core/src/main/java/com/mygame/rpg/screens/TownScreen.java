package com.mygame.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygame.rpg.character.Player;
import com.mygame.rpg.core.RPGGame;
import com.mygame.rpg.location.Inn;
import com.mygame.rpg.location.townShop;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.List;

public class TownScreen implements Screen {
    private final RPGGame game;
    private final Player player;
    private final Stage stage;
    private final Skin skin;

    private final townShop townShop;

    public TownScreen(RPGGame game, Player player) {
        this.game = game;
        this.player = player;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        this.townShop = new townShop("json/shopInventory.json"); // 初始化 townShop 並傳入 JSON 檔案路徑

        createButtons();
    }

    private void createButtons() {
        TextButton shopButton = new TextButton("enter shop", skin);
        shopButton.setSize(200, 50);
        shopButton.setPosition(300, 400);
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new ShopScreen(game, player, townShop));
            }
        });

        TextButton innButton = new TextButton("enter Inn", skin);
        innButton.setSize(200, 50);
        innButton.setPosition(300, 300);
        innButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new InnScreen(game, player, new Inn()));
            }
        });

        TextButton backButton = new TextButton("back to Main Menu", skin);
        backButton.setSize(200, 50);
        backButton.setPosition(300, 200);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(shopButton);
        stage.addActor(innButton);
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void hide() {
        // Gdx.input.setInputProcessor(null);
    }
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
