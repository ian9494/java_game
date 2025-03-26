package com.mygame.rpg.battle;

public class Effect {
    private String type;
    private int value;
    private int duration;

    public Effect() {
        // default constructor
    }

    public void setEffect(String type, int value) {
        this.type = type;
        this.value = value;
        this.duration = 0;
    }

    public void setEffect(String type, int value, int duration) {
        this.type = type;
        this.value = value;
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public int getDuration() {
        return duration;
    }
}
