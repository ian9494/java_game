package com.mygame.rpg;

public class GameInitializer {
    public static WorldMap createWorldMap() {
        WorldMap worldMap = new WorldMap();

        Region forest = new Region("森林", "一片茂密的森林", "forest_icon.png");
        Region village = new Region("村莊", "一個安靜的小村莊", "village_icon.png");
        Region castle = new Region("城堡", "宏偉的城堡，國王居住的地方", "castle_icon.png");

        worldMap.addRegion(forest);
        worldMap.addRegion(village);
        worldMap.addRegion(castle);

        // worldMap.connectRegions("森林", "村莊");
        // worldMap.connectRegions("村莊", "城堡");

        return worldMap;
    }
}

