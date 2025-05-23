package com.mygame.rpg.screens;

import com.mygame.rpg.item.Skill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygame.rpg.battle.Battle;
import com.mygame.rpg.battle.DropItem;
import com.mygame.rpg.character.Player;
import com.mygame.rpg.core.RPGGame;
import com.mygame.rpg.item.Item;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import java.util.Map;
import java.util.List;

public class BattleScreen implements Screen {
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private RPGGame game;
    private final Battle battle;
    private Stage stage;
    private Skin skin;

    private TextButton attackButton;
    private TextButton useSkillButton;
    private TextButton useItemButton;
    private TextButton confirmButton;

    private Label rewardLabel;
    private TextButton continueButton;
    private boolean showRewards = false;

    private Label levelUpLabel;
    private TextButton levelUpOkButton;
    private boolean isLevelUpVisible = false;

    private Label empLabel;

    private Window itemWindow;
    private ScrollPane itemScroll;
    private VerticalGroup itemList;

    private Window skillWindow;
    private ScrollPane skillScroll;
    private VerticalGroup skillList;

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
            }
        });

        // 技能按鈕
        useSkillButton = new TextButton("Use Skill", skin);
        useSkillButton.setSize(150, 50);
        useSkillButton.setPosition(250, 50);
        useSkillButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // 使用技能
                showSkillMenu();
            }
        });

        // 確認按鈕
        confirmButton = new TextButton("Confirm", skin);
        confirmButton.setSize(150, 50);
        confirmButton.setPosition(400, 50);
        confirmButton.setVisible(false);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                battle.onPlayerConfirm();
                confirmButton.setVisible(false);
                // attackButton.setVisible(true);
                // useItemButton.setVisible(true);
                Gdx.app.log("battleScreen", "show attack button");
            }
        });

        // 使用物品按鈕
        useItemButton = new TextButton("Use Item", skin);
        useItemButton.setSize(150, 50);
        useItemButton.setPosition(450, 50);
        useItemButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showItemMenu();
            }
        });
        stage.addActor(useItemButton);

        // 初始化物品選單
        itemList = new VerticalGroup();
        itemScroll = new ScrollPane(itemList, skin);
        itemWindow = new Window("Select Items", skin);
        itemWindow.setSize(300, 400);
        itemWindow.setPosition(750, 100);
        itemWindow.setVisible(false);
        itemWindow.add(itemScroll);
        stage.addActor(itemWindow);


        // 添加按鈕到舞台
        stage.addActor(attackButton);
        stage.addActor(confirmButton);
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

    private void showItemMenu() {
        itemList.clear();
        Map<String, Item> inventory = battle.getPlayer().getItemInventory();

        boolean hasUsableItem = false;

        for (Map.Entry<String, Item> entry : inventory.entrySet()) {
            Item item = entry.getValue();

            if (!"consumable".equals(item.getType()) || item.getQuantity() <= 0) {
                continue;
            }

            hasUsableItem = true;
            TextButton itemButton = new TextButton(item.getName() + " x" + item.getQuantity(), skin);
            itemButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    useItemInBattle(item);
                    itemWindow.setVisible(false);
                }
            });
            itemList.addActor(itemButton);
        }
        if (!hasUsableItem) {
            itemList.addActor(new Label("No usable item", skin));
        }

        TextButton closeButton = new TextButton("Close", skin);
            closeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    itemWindow.setVisible(false);
                }
            });
        itemList.addActor(closeButton);

        itemWindow.setVisible(true);
    }

    // 顯示技能選單
    private void showSkillMenu() {
        skillList.clear();
        Player player = battle.getPlayer();

        // 確保玩家有裝備武器
        if (player.getEquippedWeapon() == null) {
            skillList.addActor(new Label("No weapon equipped", skin));
            return;
        }

        // 確保玩家裝備武器有技能
        if (player.getEquippedWeapon().getAvailableSkillIds().isEmpty()) {
            skillList.addActor(new Label("No available skills", skin));
        }

        List<Skill> skills = player.getEquippedWeapon().getAvailableSkillIds();

        for (Skill skill : skills) {
            TextButton skillButton = new TextButton(skill.getName(), skin);
            skillButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // 使用技能
                    battle.useSkill(skill);
                    skillWindow.setVisible(false);
                }
            });
            skillList.addActor(skillButton);
        }

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skillWindow.setVisible(false);
            }
        });
        skillList.addActor(closeButton);

        skillWindow.setVisible(true);
    }

    // 顯示環境魔力狀態
    private void showEmpStatus() {
        empLabel = new Label("EMP : 10", skin);
        empLabel.setAlignment(Align.right);
        Table topBar = new Table();
        topBar.top().right();
        topBar.setFillParent(true);
        topBar.add(empLabel).pad(10);
        stage.addActor(topBar);
    }


    // 使用物品
    private void useItemInBattle(Item item) {
        battle.getPlayer().useItem(item.getItemID());
        Gdx.app.log("BattleScreen", "use item: " + item.getName());

    }

    // 顯示升級訊息
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
        showEmpStatus();
    }

    public void render(float delta) {
        // 清除畫面
        ScreenUtils.clear(0, 0, 0, 1); // 黑色背景

        // 更新行動條
        if (!battle.isWaitingForPlayerAction()) {
            battle.updateActionBar();
        }

        if (battle.isWaitingForPlayerConfirmation()){
            confirmButton.setVisible(true);
            attackButton.setVisible(false);
            useItemButton.setVisible(false);
        } else {
            confirmButton.setVisible(false);
            attackButton.setVisible(true);
            useItemButton.setVisible(true);
        }

        // 更新相機
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // 繪製戰鬥狀態
        font.draw(batch, battle.getBattleState(), 50, 550); // 顯示場上資訊
        empLabel.setText("EMP: " + battle.getEmp().getEmpPool() + "/" + battle.getEmp().getMaxEmpPool());
        font.draw(batch, "Last Action: " + battle.getLastLog(), 50, 400);

        createRewardUI();
        createLevelUpUI();

        if (isLevelUpVisible) {
            rewardLabel.setVisible(false); // **如果顯示升級 UI，隱藏獎勵 UI**
            continueButton.setVisible(false);

            attackButton.setVisible(false);

            batch.end();
            stage.act(delta);
            stage.draw();
            return;
        }

        // 結束戰鬥
        if (battle.isBattleOver() && !isLevelUpVisible) {

            if (!showRewards) {
                Gdx.app.log("battleScreen", "showing rewards");
                showRewards = true;

                int expGained = battle.getEnemy().getExpReward();
                int goldGained = battle.getEnemy().getGoldReward();
                List<DropItem> itemRewards = battle.getItemReward();

                attackButton.setVisible(false);

                StringBuilder rewardText = new StringBuilder("Battle ended\n" +
                                                             "Gained exp: " + expGained + "\n" +
                                                             "Gained gold: " + goldGained);
                if (itemRewards != null) {
                    for (DropItem item : itemRewards) {
                        rewardText.append("\n").append(item.getName()).append(" x").append(item.getRandomDropCount());
                    }
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
