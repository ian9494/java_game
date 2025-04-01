package com.mygame.rpg.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.List;

import com.mygame.rpg.character.Player;
import com.mygame.rpg.item.EquipSlot;
import com.mygame.rpg.item.Equipment;
import com.mygame.rpg.core.RPGGame;
import com.mygame.rpg.screens.CharacterScreen;

public class EquipmentEditScreen implements Screen {
    private final RPGGame game;
    private final Player player;
    private Stage stage;
    private Skin skin;
    private SelectBox<EquipSlot> slotSelector;
    private Table equipmentTable;

    public EquipmentEditScreen(RPGGame game, Player player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label title = new Label("Edit Equipment", skin);
        slotSelector = new SelectBox<>(skin);
        slotSelector.setItems(EquipSlot.values());

        slotSelector.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateEquipmentList();
            }
        });

        equipmentTable = new Table();
        updateEquipmentList();

        TextButton back = new TextButton("Back", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CharacterScreen(game, player));
            }
        });

        root.top().pad(10);
        root.add(title).colspan(2).center().row();
        root.add(new Label("Slot:", skin)).left();
        root.add(slotSelector).left().row();
        root.add(new Label("Available Equipment:", skin)).colspan(2).left().row();
        root.add(equipmentTable).colspan(2).left().padTop(5).row();
        root.add(back).colspan(2).center().padTop(20);
    }

    private void updateEquipmentList() {
        equipmentTable.clear();
        EquipSlot selectedSlot = slotSelector.getSelected();
        List<Equipment> available = player.getEquippedItem(selectedSlot);

        if (available.isEmpty()) {
            equipmentTable.add(new Label("No equipment available.", skin)).left().row();
            return;
        }

        for (Equipment eq : available) {
            TextButton equipBtn = new TextButton(eq.getName(), skin);
            equipBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.equipItem(selectedSlot, eq);
                    updateEquipmentList(); // Refresh list
                }
            });
            equipmentTable.add(equipBtn).left().row();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
