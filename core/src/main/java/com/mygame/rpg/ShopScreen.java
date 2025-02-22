package com.mygame.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.List;

public class ShopScreen implements Screen {
    private final RPGGame game;
    private final Player player;
    private final townShop shop;
    private final Stage stage;
    private final Skin skin;

    public ShopScreen(RPGGame game, Player player, townShop shop) {
        this.game = game;
        this.player = player;
        this.shop = shop;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label shopLabel = new Label("Shop", skin);
        table.add(shopLabel).padBottom(20);
        table.row();

        List<Item> shopItems = shop.getInventory();
        for (Item item : shopItems) {
            TextButton buyButton = new TextButton("Buy " + item.getName() + " - " + item.getPrice() + " gold", skin);
            buyButton.addListener(new ClickListener() {
                @Override
                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    int itemIndex = shop.getInventory().indexOf(item);
                    shop.buyItem(player, itemIndex, 1);
                }
            });
            table.add(buyButton).padBottom(10);
            table.row();
        }

        TextButton backButton = new TextButton("return town", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new TownScreen(game, player));
            }
        });
        table.add(backButton).padTop(20);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void hide() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
