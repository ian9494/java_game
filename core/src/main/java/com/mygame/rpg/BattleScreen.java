package com.mygame.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;

public class BattleScreen implements Screen {
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final Battle battle; // 引入你的 Battle 類

    public BattleScreen(Battle battle) {
        this.battle = battle;
        this.batch = new SpriteBatch();

        // 設置高解析度字體
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48; // 字體大小
        parameter.magFilter = Texture.TextureFilter.Linear; // 高品質濾波
        parameter.minFilter = Texture.TextureFilter.Linear; // 高品質濾波
        font = generator.generateFont(parameter);
        generator.dispose();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600); // 設置螢幕大小
    }

    public void render(float delta) {
        // 清除畫面
        ScreenUtils.clear(0, 0, 0, 1); // 黑色背景

        // 更新相機
        camera.update();

        // 繪製文字
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        font.getData().setScale(1);
        font.draw(batch, "Hello Battle Screen!", 100, 100); // 顯示文字

        font.draw(batch, "Player: " + battle.getAttacker().getName(), 50, 550);
        font.draw(batch, "HP: " + battle.getAttacker().getHp(), 50, 500);
        font.draw(batch, "Enemy: " + battle.getDefender().getName(), 500, 550);
        font.draw(batch, "HP: " + battle.getDefender().getHp(), 500, 500);

        batch.end();
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
