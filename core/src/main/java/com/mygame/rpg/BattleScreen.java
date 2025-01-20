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
    private final Battle battle;
    private Stage stage;
    private Skin skin;

    private String currentBattleLog = ""; //現在戰鬥事件
    private float elapsedTime = 0;
    private final float DISPLAY_INTERVAL = 1.5f; // 每個動作顯示時間

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


    public BattleScreen(Battle battle) {
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

        // input logs
        elapsedTime += delta;
        if (elapsedTime >= DISPLAY_INTERVAL && !battle.isBattleOver()){
            currentBattleLog = battle.getNextLog();
            elapsedTime = 0;
        }

        // 清除畫面
        ScreenUtils.clear(0, 0, 0, 1); // 黑色背景

        // 更新相機
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // 繪製文字
        font.getData().setScale(1);
        font.draw(batch, "Hello Battle Screen!", 100, 100); // 顯示文字

        font.draw(batch, battle.getBattleState(), 50, 550); // 顯示場上資訊

        if (currentBattleLog != null) {

            font.draw(batch, currentBattleLog, 50, 200); // 顯示目前的動作
        }

        if (battle.isBattleOver()) {
            font.draw(batch, "Battle Over! Winner: " +
                    (battle.getAttacker().isAlive() ? battle.getAttacker().getName() : battle.getDefender().getName()), 50, 50);
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
