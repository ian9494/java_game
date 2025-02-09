package com.mygame.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.List;

public class CharacterScreen implements Screen {
    private final RPGGame game;
    private final Player player;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final BitmapFont largeFont;
    private final OrthographicCamera camera;
    private final Stage stage;
    private final Skin skin;

    public CharacterScreen(RPGGame game, Player player) {
        this.game = game;
        this.player = player;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);
        this.stage = new Stage(new ScreenViewport());
        // this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        // 設定字體
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts\\NotoSansTC-Regular.ttf"));

        // 24吋字體
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18; // 字體大小
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


        Gdx.input.setInputProcessor(stage);

        createButtons();
    }

    private void createButtons() {
        // 返回主選單按鈕
        TextButton backButton = new TextButton("Back", skin, "default");
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

        // 顯示背包
        font.draw(batch, "Inventory:", 300, 550);
        List<DropItem> inventory = player.getInventory();
        int y = 220;
        if (inventory.isEmpty()) {
            font.draw(batch, "No items.", 100, y);
        } else {
            for (DropItem item : inventory) {
                font.draw(batch, "- " + item, 100, y);
                y -= 25;
            }
        }

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
