package com.mygame.rpg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldMapRenderer {
    private Texture backgroundTexture; // 地圖背景
    private WorldMap worldMap;

    public WorldMapRenderer(WorldMap worldMap, String backgroundPath) {
        this.worldMap = worldMap;
        this.backgroundTexture = new Texture(backgroundPath);
    }

    public void render(SpriteBatch batch) {
        // 繪製背景
        batch.draw(backgroundTexture, 0, 0);

        // 繪製每個區域的圖標
        for (Region region : worldMap.getAllRegions().values()) {
            Texture regionIcon = new Texture(region.getIconPath());
            // 假設區域的位置由其名稱對應的座標設定
            int x = 0; // 設定區域的X座標
            int y = 0; // 設定區域的Y座標
            batch.draw(regionIcon, x, y, 64, 64); // 假設圖標大小為64x64像素
        }
    }
}
