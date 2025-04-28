package com.mygame.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygame.rpg.character.Player;
import com.mygame.rpg.core.RPGGame;
import com.mygame.rpg.item.EquipSlot;
import com.mygame.rpg.item.Equipment;
import com.mygame.rpg.item.Item;
import com.mygame.rpg.item.Skill;
import com.mygame.rpg.item.SkillDatabase;

import java.util.Map;
import java.util.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.w3c.dom.Text;

public class CharacterScreen implements Screen {
    private final RPGGame game;
    private final Player player;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final BitmapFont largeFont;
    private final OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

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
        createInventoryUI();
        createEquipmentUI();
    }

    // 創建互動按鈕
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

    // 創建背包UI
    private void createInventoryUI() {
        int y = 500;
        for (Map.Entry<String, Item> entry : player.getItemInventory().entrySet()) {
            Item item = entry.getValue();
            item.setItemInfo();

            // 過濾"material" 類型的物品
            if (item.getType().equals("material")) {
                continue;
            }
            TextButton useButton = new TextButton(item.getName() + " x" + item.getQuantity(), skin);
            useButton.setSize(250, 50);
            useButton.setPosition(1200, y);
            y -= 60;

            useButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.useItem(item.getItemID());
                }
            });

            stage.addActor(useButton);
            }
    }

    // 創建裝備UI
    private void createEquipmentUI() {
        Label weaponLabel = new Label("Weapon: " + getEquipName(EquipSlot.WEAPON), skin);
        Label headLabel = new Label("Head: " + getEquipName(EquipSlot.HEAD), skin);
        Label bodyLabel = new Label("Body: " + getEquipName(EquipSlot.BODY), skin);
        Label legsLabel = new Label("Legs: " + getEquipName(EquipSlot.LEGS), skin);
        Label accessoryLabel = new Label("Accessory: " + getEquipName(EquipSlot.ACCESSORY), skin);

        TextButton editButton = new TextButton("Edit Equipment", skin);
        editButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // 這裡可以添加編輯裝備的邏輯
                Gdx.app.log("CharacterScreen", "Editing Equipment");
                game.setScreen(new EquipmentEditScreen(game, player));
            }
        });
        editButton.setPosition(75, 50);
        editButton.setSize(200, 50);
        stage.addActor(editButton);

        // 設定標籤的位置
        int equipmentLabelHeight = 700;
        weaponLabel.setPosition(450, equipmentLabelHeight);
        headLabel.setPosition(450, equipmentLabelHeight - 50);
        bodyLabel.setPosition(450, equipmentLabelHeight - 100);
        legsLabel.setPosition(450, equipmentLabelHeight - 150);
        accessoryLabel.setPosition(450, equipmentLabelHeight - 200);

        // 設定標籤的字體樣式
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        weaponLabel.setStyle(labelStyle);
        headLabel.setStyle(labelStyle);
        bodyLabel.setStyle(labelStyle);
        legsLabel.setStyle(labelStyle);
        accessoryLabel.setStyle(labelStyle);
        // 設定標籤的顏色
        weaponLabel.setColor(Color.WHITE);
        headLabel.setColor(Color.WHITE);
        bodyLabel.setColor(Color.WHITE);
        legsLabel.setColor(Color.WHITE);
        accessoryLabel.setColor(Color.WHITE);
        // 設定標籤的大小
        weaponLabel.setFontScale(2f);
        headLabel.setFontScale(2f);
        bodyLabel.setFontScale(2f);
        legsLabel.setFontScale(2f);
        accessoryLabel.setFontScale(2f);

        stage.addActor(weaponLabel);
        stage.addActor(headLabel);
        stage.addActor(bodyLabel);
        stage.addActor(legsLabel);
        stage.addActor(accessoryLabel);
    }

    // 創建技能瀏覽UI
    private void createSkillUI() {
        Table skillsTable = new Table();
        skillsTable.top().left();
        skillsTable.setPosition(1100, 300);
        skillsTable.setFillParent(false);

        List<Skill> skills = SkillDatabase.getSkillByCategory("one-handed-sword");

        for (Skill skill : skills) {
            Label skillLabel = new Label(
            "- " + skill.getName() + " (EMP: " + skill.getEmpCost() + ")",
            skin);
            skillsTable.add(skillLabel).left().row();
        }
        skillsTable.pack();
        stage.addActor(skillsTable);
    }


    // 獲取裝備名稱
    private String getEquipName(EquipSlot slot) {
        Equipment eq = player.getEquipmentBySlot(slot);
        return eq != null ? eq.getName() : "None";
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(delta);
        stage.draw();

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
        font.draw(batch, "Gold: " + player.getGold(), 100, 260);

        // 顯示背包
        font.draw(batch, "Inventory:", 400, 500);
        Map<String, Item> inventory = player.getItemInventory();
        int y = 470;
        if (inventory.isEmpty()) {
            font.draw(batch, "No items.", 400, y);
        } else {
            for (Map.Entry<String, Item> entry : inventory.entrySet()) {
                Item item = entry.getValue();
                font.draw(batch, "- " + item.getName() + " ×" + item.getQuantity(), 400, y);
                y -= 25;
            }
        }

        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void show() {
        // stage = new Stage(new ScreenViewport());
        // Gdx.input.setInputProcessor(stage);

        // skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Table rootTable = new Table();
        // rootTable.setFillParent(true);
        // stage.addActor(rootTable);

        createSkillUI();
    }

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
