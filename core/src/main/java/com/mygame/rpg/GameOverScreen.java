package com.mygame.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygame.rpg.Player;

public class GameOverScreen implements Screen {
    private final RPGGame game;
    private final Player player;
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;
    private Skin skin;

    public GameOverScreen(RPGGame game, Player player) {
        this.game = game;
        this.player = player;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.stage = new Stage();
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));


        // 設置 UI
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        int lostGold = Math.round(player.getGold()*0.2f);
        int lostExp = Math.round(player.getExp()*0.2f);

        Label messageLabel = new Label("You have been defeated!", skin);
        Label goldPenaltyLabel = new Label("Gold lost: " + lostGold, skin);
        Label expPenaltyLabel = new Label("Exp lost: " + lostExp, skin);

        // 懲罰機制
        player.applyDeathPenalty();

        TextButton exitButton = new TextButton("Exit to Menu", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.add(messageLabel).pad(10).row();
        table.add(goldPenaltyLabel).pad(10).row();
        table.add(expPenaltyLabel).pad(10).row();
        table.add(exitButton).pad(10);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        stage.dispose();
        skin.dispose();
    }
}

