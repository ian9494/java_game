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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygame.rpg.RPGGame;

public class BattleScreen implements Screen {
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private RPGGame game;
    private final Battle battle;
    private Stage stage;
    private Skin skin;

    private TextButton attackButton;
    private TextButton defendButton;
    private TextButton useItemButton;

    private Label rewardLabel;
    private TextButton continueButton;
    private boolean showRewards = false;

    private Label levelUpLabel;
    private TextButton levelUpOkButton;
    private boolean isLevelUpVisible = false;

    // Removed the constructor to ensure all final fields are initialized


    private void createButtons() {
        // 攻擊按鈕
        attackButton = new TextButton("Attack", skin);
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
        defendButton = new TextButton("Defend", skin);
        defendButton.setSize(150, 50);
        defendButton.setPosition(220, 50);
        defendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // 執行防禦邏輯
                battle.doDefend();
            }
        });

        useItemButton = new TextButton("Use Item", skin);
        useItemButton.setSize(150, 50);
        useItemButton.setPosition(400, 50);
        useItemButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                battle.getPlayer().useItem("102"); // 測試使用 低階HP藥水
            }
        });
stage.addActor(useItemButton);


        // 添加按鈕到舞台
        stage.addActor(attackButton);
        stage.addActor(defendButton);
    }

    private void createRewardUI() {
        // 設定獎勵標籤
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        rewardLabel = new Label("", labelStyle);
        rewardLabel.setPosition(200, 400);
        rewardLabel.setVisible(false);
        stage.addActor(rewardLabel);

        // 設定繼續按鈕
        continueButton = new TextButton("back", skin);

        continueButton.setSize(200, 50);
        continueButton.setPosition(300, 250);
        continueButton.setVisible(false);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.log("BattleScreen", "go main menu");
                game.setScreen(new MainMenuScreen(game)); // 返回主選單
            }
        });
        stage.addActor(continueButton);
    }

    private void createLevelUpUI() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // 升級訊息標籤
        levelUpLabel = new Label("", labelStyle);
        levelUpLabel.setPosition(700, 400);
        levelUpLabel.setVisible(false);
        stage.addActor(levelUpLabel);

        // OK 按鈕
        levelUpOkButton = new TextButton("confirm", skin);
        levelUpOkButton.setSize(150, 50);
        levelUpOkButton.setPosition(800, 250);
        levelUpOkButton.setVisible(false);
        levelUpOkButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                levelUpLabel.setVisible(false);
                levelUpOkButton.setVisible(false);
                isLevelUpVisible = false;
            }
        });

        stage.addActor(levelUpOkButton);
    }

    public void showLevelUpMessage(String message) {
        levelUpLabel.setText(message);
        levelUpLabel.setVisible(true);
        levelUpOkButton.setVisible(true);
        isLevelUpVisible = true;
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

        createRewardUI();
        createLevelUpUI();

        if (isLevelUpVisible) {
            rewardLabel.setVisible(false); // **如果顯示升級 UI，隱藏獎勵 UI**
            continueButton.setVisible(false);

            attackButton.setVisible(false);
            defendButton.setVisible(false);

            batch.end();
            stage.act(delta);
            stage.draw();
            return;
        }

        // 結束戰鬥
        if (battle.isBattleOver() && !isLevelUpVisible) {

            if (!showRewards) {
                Gdx.app.log("battleScreen", "showRewards");
                showRewards = true;

                int expGained = battle.getEnemy().getExpReward();
                DropItem itemRewards = battle.getItemReward();

                attackButton.setVisible(false);
                defendButton.setVisible(false);

                StringBuilder rewardText = new StringBuilder("Battle ended\n" +
                                                             "Gained exp: " + expGained);
                if (itemRewards != null) {
                    rewardText.append("\nGet item: ").append(itemRewards.getName());
                } else {
                    rewardText.append("\nNo item found");
                }

                rewardLabel.setText(rewardText.toString());
                rewardLabel.setVisible(true);
                continueButton.setVisible(true);
            }

            // font.draw(batch, "Battle Over! Winner: " + (battle.getPlayer().isAlive() ? battle.getPlayer().getName() : battle.getEnemy().getName()) + " press ENTER to return", 50, 50);
            // game.setScreen(new MainMenuScreen(game));
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
