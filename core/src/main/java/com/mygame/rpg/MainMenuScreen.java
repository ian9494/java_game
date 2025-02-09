package com.mygame.rpg;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Gdx;

import java.util.Random;
import java.util.List;

public class MainMenuScreen implements Screen {
    private final RPGGame game;
    private final Player player;
    private final LocationManager locationManager;

    private final int uiButtonPos = 1250;

    private final BitmapFont font;
    private final BitmapFont largeFont;
    private final Label.LabelStyle labelStyle;
    private final Label.LabelStyle largeLabelStyle;
    private final OrthographicCamera camera;

    private Stage stage; // 用於管理按鈕的舞台
    private Skin skin; // 按鈕的樣式

    private Label locationLabel;
    private Label hpLabel;
    private Label levelLabel;
    private Label moveToLabel;

    public MainMenuScreen(RPGGame game) {
        this.game = game;
        this.player = game.getPlayer();
        this.locationManager = new LocationManager();

        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // 設定舞台為輸入處理器

        // this.batch = new SpriteBatch(); // 移除未使用的 batch 變數

        // 設定字體
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts\\NotoSansTC-Regular.ttf"));

        // 24吋字體
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24; // 字體大小
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "角色預覽現在事件地圖資訊互移動按鈕聊天室探索"; // 添加需要顯示的中文字符
        parameter.magFilter = Texture.TextureFilter.Linear; // 高品質濾波
        parameter.minFilter = Texture.TextureFilter.Linear; // 高品質濾波
        this.font = generator.generateFont(parameter);

        // 48吋字體
        FreeTypeFontGenerator.FreeTypeFontParameter largeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        largeFontParameter.size = 48;
        this.largeFont = generator.generateFont(largeFontParameter);

        generator.dispose();

        // 建立skin然後添加字體
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.add("font", font, BitmapFont.class);
        skin.add("large-font", largeFont, BitmapFont.class);

        // 建立24吋標籤用的字體模式
        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // 建立48吋標籤用的字體模式
        largeLabelStyle = new Label.LabelStyle();
        largeLabelStyle.font = largeFont;

        // 建立按鈕用的字體模式
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = this.font; // 使用生成的中文字體
        style.up = skin.newDrawable("default-round", Color.DARK_GRAY);
        style.down = skin.newDrawable("default-round-down", Color.DARK_GRAY);
        style.checked = skin.newDrawable("default-round", Color.BLUE);
        style.over = skin.newDrawable("default-round", Color.LIGHT_GRAY);
        skin.add("default", style);

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);

        // 添加到布局表格
        Table table = new Table();
        table.setFillParent(true);
        table.pad(10);
        stage.addActor(table);

        createLabels(largeLabelStyle);
        createButtons();
    }

    // 創建標籤
    private void createLabels(Label.LabelStyle largeLabelStyle) {
        // Labels
        locationLabel = new Label("Location: 未知", largeLabelStyle);
        locationLabel.setPosition(1250, 600);

        moveToLabel = new Label("Move to:", labelStyle);
        moveToLabel.setPosition(1250, 250);

        hpLabel = new Label("HP : 100", largeLabelStyle);
        hpLabel.setPosition(200, 500);

        levelLabel = new Label("Level : 1", largeLabelStyle);
        levelLabel.setPosition(200, 420);

        stage.addActor(locationLabel);
        stage.addActor(hpLabel);
        stage.addActor(levelLabel);
        stage.addActor(moveToLabel);
    }

    // 創建按鈕
    private void createButtons() {
        // 探索按鈕
        TextButton exploreButton = new TextButton("Explore", skin);
        exploreButton.setSize(200, 50);
        exploreButton.setPosition(uiButtonPos, 400);
        exploreButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Location currentLocation = locationManager.getLocationByID(player.getLocationID());

                // 如果是城鎮，則無法探索
                if (currentLocation.isTown()) {
                    Gdx.app.log("Main menu-explore", "This is a town. No exploration possible.");
                    return;
                }

                Gdx.app.log("Main_menu-explore", "Exploring...");

                // 隨機遇敵或採集
                Random random = new Random();
                int eventType = random.nextInt(2); // 0: 採集, 1: 遭遇敵人
                Gdx.app.log("Main_menu-explore", "Event type: " + eventType);
                List<GatherableObject> gatherableObjects = locationManager.getGatherablesObjects(player.getLocationID());

                if (gatherableObjects.isEmpty()) {
                    Gdx.app.log("Main_menu-explore", "No gatherable objects found.");
                }

                // 採集
                if (eventType == 0) {
                    Gdx.app.log("Main_menu-explore", "Gathering...");

                    for (GatherableObject object : gatherableObjects) {
                        Gdx.app.log("Main_menu-explore", "You found a " + object.getObjectName());
                        for (DropItem dropItem : object.getDropItems()) {
                            int dropChance = random.nextInt(100);
                            if (dropChance < dropItem.getDropRate()) {
                                int dropCount = dropItem.getRandomDropCount();
                                player.addItem(dropItem.getItemID(), dropCount);
                                Gdx.app.log("Main_menu-explore", "You got " + dropCount + " " + dropItem.getItemID());
                            }
                        }
                    }
                // 遭遇敵人
                 } //else if (!currentLocation.getPossibleEnemies().isEmpty()) {
                //     String enemyName = currentLocation.getPossibleEnemies().get(random.nextInt(currentLocation.getPossibleEnemies().size()));
                //     Enemy enemy = new Enemy(enemyName, 50, 0, 10, 5, 10, 100, null);
                //     game.setScreen(new BattleScreen(game, new Battle(player, enemy)));
                // }
            }
        });

        // 檢視角色按鈕
        TextButton characterButton = new TextButton("角色", skin);
        characterButton.setSize(200, 50);
        characterButton.setPosition(uiButtonPos, 300);
        characterButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // 進入角色檢視畫面
                Gdx.app.log("Main Menu", "Viewing Character...");
                game.setScreen(new CharacterScreen(game, game.getPlayer())); // 假設有 CharacterScreen
            }
        });

        // 移動按鈕
        List<Integer> connections = locationManager.getConnections(player.getLocationID());
        int yPosition = 200;
        for (int locationID : connections) {
            String locationName = locationManager.getLocationName(locationID);
            TextButton moveButton = new TextButton(locationName, skin);
            moveButton.setSize(200, 50);
            moveButton.setPosition(uiButtonPos, yPosition);
            moveButton.addListener(new ClickListener() {
                @Override
                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    player.setLocationID(locationID);
                    refreshButtons();
                    updateLocationLabel();
                    Gdx.app.log("Main Menu-buttons", "Moved to " + locationName);
                }
            });
            stage.addActor(moveButton);
            yPosition -= 60;
        }

        // 添加按鈕到舞台
        stage.addActor(exploreButton);
        stage.addActor(characterButton);
    }

    // 更新位置標籤
    private void updateLocationLabel() {
        locationLabel.setText("Location:\n" + locationManager.getLocationName(player.getLocationID()));
    }

    // 更新角色標籤
    private void refreshButtons() {
        // 移除所有按鈕
        stage.clear();
        createLabels(largeLabelStyle);
        createButtons();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 繪製labels
        locationLabel.setText("Location: " + game.getPlayer().getLocationID());
        hpLabel.setText("HP: " + game.getPlayer().getHp());
        levelLabel.setText("Level: " + game.getPlayer().getLV());

        updateLocationLabel();
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
        // batch.dispose(); // 移除未使用的 batch 變數
        font.dispose();
        stage.dispose();
    }
}
