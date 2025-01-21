package com.mygame.rpg;

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
import com.badlogic.gdx.Gdx;

public class MainMenuScreen implements Screen {
    private final RPGGame game;
    private final Character player;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final OrthographicCamera camera;

    private Stage stage; // 用於管理按鈕的舞台
    private Skin skin; // 按鈕的樣式

    public MainMenuScreen(RPGGame game, Character player) {
        this.game = game;
        this.player = player;

        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);

        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json")); // 確保 assets 有 uiskin.json

        Gdx.input.setInputProcessor(stage); // 設定舞台為輸入處理器
        createButtons();
    }

    private void createButtons() {
        // 探索按鈕
        TextButton exploreButton = new TextButton("Explore", skin);
        exploreButton.setSize(200, 50);
        exploreButton.setPosition(50, 400);
        exploreButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // 進入探索邏輯
                System.out.println("Exploring...");
                // game.setScreen(new ExploreScreen(game, player)); // 假設有 ExploreScreen
            }
        });

        // 檢視角色按鈕
        TextButton characterButton = new TextButton("Character", skin);
        characterButton.setSize(200, 50);
        characterButton.setPosition(50, 300);
        characterButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // 進入角色檢視畫面
                System.out.println("Viewing Character...");
                // game.setScreen(new CharacterScreen(game, player)); // 假設有 CharacterScreen
            }
        });

        // 移動按鈕
        TextButton moveButton = new TextButton("Move", skin);
        moveButton.setSize(200, 50);
        moveButton.setPosition(50, 200);
        moveButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // 進入地圖選擇畫面
                System.out.println("Moving to a new location...");
                // game.setScreen(new MapScreen(game, player)); // 假設有 MapScreen
            }
        });

        // 添加按鈕到舞台
        stage.addActor(exploreButton);
        stage.addActor(characterButton);
        stage.addActor(moveButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // 繪製角色資訊
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Location: " + player.getLocation(), 50, 550);
        font.draw(batch, "HP: " + player.getHp(), 50, 520);
        font.draw(batch, "Level: " + player.getLV(), 50, 490);
        batch.end();

        // 繪製按鈕
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
