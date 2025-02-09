package com.mygame.rpg;

public class Effect {
    private String type;
    private int value;

    public Effect(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public String getType() { return type; }
    public int getValue() { return value; }
}
