package com.mygame.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CharacterScreen implements Screen {
    private final RPGGame game;
    private final Player player;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final Stage stage;
    private final Skin skin;

    public CharacterScreen(RPGGame game, Player player) {
        this.game = game;
        this.player = player;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        Gdx.input.setInputProcessor(stage);

        createButtons();
    }

    private void createButtons() {
        // 返回主選單按鈕
        TextButton backButton = new TextButton("Back", skin);
        backButton.setSize(200, 50);
        backButton.setPosition(300, 50);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game)); // 返回主選單
            }
        });

        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // 顯示角色詳細數據
        font.draw(batch, "Character Stats", 300, 550);
        font.draw(batch, "Name: " + player.getName(), 100, 500);
        font.draw(batch, "Level: " + player.getLV(), 100, 470);
        font.draw(batch, "EXP: " + player.getExp() + "/" + player.getExpToNextLV(), 100, 440);
        font.draw(batch, "HP: " + player.getHp() + "/" + player.getMaxHp(), 100, 410);
        font.draw(batch, "MP: " + player.getMp() + "/" + player.getMaxMp(), 100, 380);
        font.draw(batch, "ATK: " + player.getAtk(), 100, 350);
        font.draw(batch, "DEF: " + player.getDef(), 100, 320);
        font.draw(batch, "SPD: " + player.getSpd(), 100, 290);

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        stage.dispose();
    }
}
