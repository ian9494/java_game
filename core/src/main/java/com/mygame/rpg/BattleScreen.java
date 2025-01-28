package com.mygame.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class BattleScreen implements Screen {
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final RPGGame game;
    private final Battle battle;
    private Stage stage;
    private Skin skin;

    private void createButtons() {
        // 攻擊按鈕
        TextButton attackButton = new TextButton("Attack", skin);
        attackButton.setSize(150, 50);
        attackButton.setPosition(50, 50);
        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // 執行攻擊邏輯
                battle.doAttack();

                // 打印行動序列
                Gdx.app.log("BattleLog", "=== Current Action Sequence ===");
                for (String log : battle.getBattleLogs()) {
                    Gdx.app.log("BattleLog", log);
                }
                Gdx.app.log("BattleLog", "==============================\n");
            }
        });

        // 防禦按鈕
        TextButton defendButton = new TextButton("Defend", skin);
        defendButton.setSize(150, 50);
        defendButton.setPosition(220, 50);
        defendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // 執行防禦邏輯
                battle.doDefend();
            }
        });

        // 添加按鈕到舞台
        stage.addActor(attackButton);
        stage.addActor(defendButton);
    }

    public BattleScreen(RPGGame game, Battle battle) {
        this.game = game;
        this.battle = battle;
        this.batch = new SpriteBatch();

        // 設置高解析度字體
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24; // 字體大小
        parameter.magFilter = Texture.TextureFilter.Linear; // 高品質濾波
        parameter.minFilter = Texture.TextureFilter.Linear; // 高品質濾波
        font = generator.generateFont(parameter);
        generator.dispose();

        // 設置相機
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600); // 設置螢幕大小

        // 設置舞台和皮膚
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json")); // 確保 assets 資料夾中有 uiskin.json 和相關檔案

        Gdx.input.setInputProcessor(stage);

        createButtons();
    }

    public void render(float delta) {
        // 清除畫面
        ScreenUtils.clear(0, 0, 0, 1); // 黑色背景

        // 更新行動條
        if (!battle.isWaitingForPlayerAction()) {
            battle.updateActionBar();
        }

        // 更新相機
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // 繪製戰鬥狀態
        font.draw(batch, battle.getBattleState(), 50, 550); // 顯示場上資訊
        font.draw(batch, "Last Action: " + battle.getLastLog(), 50, 400);

        // 結束戰鬥
        if (battle.isBattleOver()) {
            Gdx.app.log("BattleScreen", "go main menu");
            font.draw(batch, "Battle Over! Winner: " + (battle.getPlayer().isAlive() ? battle.getPlayer().getName() : battle.getEnemy().getName()) + " press ENTER to return", 50, 50);
            game.setScreen(new MainMenuScreen(game));
        }

        batch.end();

        // 更新並繪製舞台
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}
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
    }
}
