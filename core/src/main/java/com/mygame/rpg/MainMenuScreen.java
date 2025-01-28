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

public class MainMenuScreen implements Screen {
    private final RPGGame game;
    // 移除未使用的 batch 變數
    // private final SpriteBatch batch;
    private final BitmapFont font;
    private final BitmapFont largeFont;
    private final OrthographicCamera camera;

    private Stage stage; // 用於管理按鈕的舞台
    private Skin skin; // 按鈕的樣式

    private Label locationLabel;
    private Label hpLabel;
    private Label levelLabel;

    public MainMenuScreen(RPGGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // 設定舞台為輸入處理器

        // this.batch = new SpriteBatch(); // 移除未使用的 batch 變數

        // 設定字體
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts\\NotoSansTC-Regular.ttf"));

        // 24吋字體
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24; // 字體大小
        parameter.characters = "角色預覽現在事件地圖資訊互移動按鈕聊天室探索"; // 添加需要顯示的中文字符
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

        // 建立48吋標籤用的字體模式
        Label.LabelStyle largeLabelStyle = new Label.LabelStyle();
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

        hpLabel = new Label("HP : 100", largeLabelStyle);
        hpLabel.setPosition(200, 500);

        levelLabel = new Label("Level : 1", largeLabelStyle);
        levelLabel.setPosition(200, 420);

        stage.addActor(locationLabel);
        stage.addActor(hpLabel);
        stage.addActor(levelLabel);
    }

    // 創建按鈕
    private void createButtons() {
        // 探索按鈕
        TextButton exploreButton = new TextButton("探索", skin, "default");
        exploreButton.setSize(200, 50);
        exploreButton.setPosition(1250, 400);
        exploreButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // 進入探索邏輯
                Gdx.app.log("MainMenuScreen", "Exploring...");
                Character player = game.getPlayer();
                Character enemy = new Character("Monster", 50, 10, 10, 10, 10);
                game.setScreen(new BattleScreen(game, new Battle(player, enemy)));
            }
        });

        // 檢視角色按鈕
        TextButton characterButton = new TextButton("角色", skin);
        characterButton.setSize(200, 50);
        characterButton.setPosition(1250, 300);
        characterButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // 進入角色檢視畫面
                System.out.println("Viewing Character...");
                // game.setScreen(new CharacterScreen(game, player)); // 假設有 CharacterScreen
            }
        });

        // 移動按鈕
        TextButton moveButton = new TextButton("移動", skin);
        moveButton.setSize(200, 50);
        moveButton.setPosition(1250, 200);
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 繪製labels
        locationLabel.setText("Location: " + game.getPlayer().getLocation());
        hpLabel.setText("HP: " + game.getPlayer().getHp());
        levelLabel.setText("Level: " + game.getPlayer().getLV());

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
