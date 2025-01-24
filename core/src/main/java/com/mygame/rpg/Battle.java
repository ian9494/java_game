package com.mygame.rpg;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
public class Battle {
    // objects
    private Character player;
    private Character enemy;

    private boolean playerTurn;
    private boolean isBattleOver;

    // 戰鬥進行記錄
    private final List<String> battleLogs = new ArrayList<>();
    private int currentLogIndex = 0;

    public Battle(Character player, Character enemy) {
        Gdx.app.log("battle", "enter battle");
        this.player = player;
        this.enemy = enemy;
        this.isBattleOver = false;

        // battleLogs.add(attacker.getName() + " attacks " + attacker.getName() + " for 10 damage.");
        // battleLogs.add(defender.getName() + " attacks " + attacker.getName() + " for 8 damage.");
        // battleLogs.add(attacker.getName() + " uses a special move! " + defender.getName() + " takes 20 damage.");
        // battleLogs.add(defender.getName() + " is defeated!");
    }

    // 對換角色
    public void switchRoles() {
        Character temp = player;
        player = enemy;
        enemy = temp;
    }

    public Character getPlayer() {
        return player;
    }

    public Character getEnemy() {
        return enemy;
    }

    public boolean isBattleOver() {
        return !player.isAlive() || !enemy.isAlive();
    }

    // calculate damage
    public int calculateDamage(Character attacker, Character defender) {
        int damage = attacker.getAtk() - defender.getDef();
        return Math.max(1, damage); // make attack do at least 1 damage
    }

    // player do attack commend
    public void doAttack() {
        Gdx.app.log("battle:doAttack", "do an attack");
        int damage = calculateDamage(player, enemy);
        String action = player.getName() + " attacks " + enemy.getName() + " for " + damage + " damage.";
        battleLogs.add(action);
        enemy.takeDamage(damage);
    }

    // player do defend commend
    public void doDefend() {
        String action = player.getName() + " defends, reducing damage.";
        battleLogs.add(action);
        // 添加防禦邏輯
    }

    // show battle logs
    public String getNextLog(){
        if (currentLogIndex < battleLogs.size()) {
            return battleLogs.get(currentLogIndex++);
        }
        return null; //如果沒有log
    }

    // run one round of battle
    public void battleTurn(Character attacker, Character defender) {
        Gdx.app.log("battle", "start new round");

        // detect if battle is over
        if (isBattleOver()) return;


        // Check if player is defeated
        if (!attacker.isAlive()) {
            System.out.println(attacker.getName() + " 被擊倒了!");
        }
    }

    public String getBattleState() {
        StringBuilder state = new StringBuilder();
        state.append("Player: ").append(player.getName()).append(" HP: ").append(player.getHp()).append("\n");
        state.append("Enemy: ").append(enemy.getName()).append(" HP: ").append(enemy.getHp()).append("\n");
        return state.toString();
    }

}
