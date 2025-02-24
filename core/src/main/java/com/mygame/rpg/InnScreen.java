package com.mygame.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.ScreenUtils;

public class InnScreen implements Screen {
    private final RPGGame game;
    private final Player player;
    private final Inn inn;
    private final Stage stage;
    private final Skin skin;

    private Label goldLabel;
    private Label messageLabel;

    public InnScreen(RPGGame game, Player player, Inn inn) {
        this.game = game;
        this.player = player;
        this.inn = inn;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label innLabel = new Label("Inn", skin);
        table.add(innLabel).padBottom(20);
        table.row();

        goldLabel = new Label("Gold: " + player.getGold(), skin);
        table.add(goldLabel).padBottom(20);
        table.row();

        messageLabel = new Label("", skin);
        table.add(messageLabel).padBottom(20);
        table.row();

        TextButton basicRoomButton = new TextButton("normal room - 10 gold", skin);
        basicRoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                boolean success = inn.rest(player, false);
                if (success) {
                    messageLabel.setText("You have rested in the normal room.");
                } else {
                    messageLabel.setText("You don't have enough gold to rest in the normal room.");
                }
                goldLabel.setText("Gold: " + player.getGold());
            }
        });
        table.add(basicRoomButton).padBottom(10);
        table.row();

        TextButton premiumRoomButton = new TextButton("premium room - 50 gold", skin);
        premiumRoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                boolean success = inn.rest(player, true);
                if (success) {
                    messageLabel.setText("You have rested in the premium room.");
                } else {
                    messageLabel.setText("You don't have enough gold to rest in the premium room.");
                }
                goldLabel.setText("Gold: " + player.getGold());
            }
        });
        table.add(premiumRoomButton).padBottom(10);
        table.row();

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
