package com.mygame.rpg;

import java.util.ArrayList;
import java.util.List;
public class Battle {
    // objects
    private Character attacker;
    private Character defender;

    private boolean isBattleOver;

    // 戰鬥進行記錄
    private final List<String> battleLogs = new ArrayList<>();
    private int currentLogIndex = 0;

    public Battle(Character player1, Character player2) {
        this.attacker = player1;
        this.defender = player2;
        this.isBattleOver = false;

        // battleLogs.add(attacker.getName() + " attacks " + attacker.getName() + " for 10 damage.");
        // battleLogs.add(defender.getName() + " attacks " + attacker.getName() + " for 8 damage.");
        // battleLogs.add(attacker.getName() + " uses a special move! " + defender.getName() + " takes 20 damage.");
        // battleLogs.add(defender.getName() + " is defeated!");
    }

    // 對換角色
    public void switchRoles() {
        Character temp = attacker;
        attacker = defender;
        defender = temp;
    }

    public Character getAttacker() {
        return attacker;
    }

    public Character getDefender() {
        return defender;
    }

    public boolean isBattleOver() {
        return isBattleOver;
    }

    // calculate damage
    public int calculateDamage(Character attacker, Character defender) {
        int damage = attacker.getAtk() - defender.getDef();
        return Math.max(1, damage); // make attack do at least 1 damage
    }

    // do one attack
    public void performAttack(Character attacker, Character defender) {
        int damage = calculateDamage(attacker, defender);
        defender.takeDamage(damage);

        battleLogs.add(attacker.getName() + " 進行了一次攻擊\n對 " + defender.getName() + " 造成了 " + damage + " 點傷害\n");
        System.out.println(attacker.getName() + " 進行了一次攻擊\n對 " + defender.getName() + " 造成了 " + damage + " 點傷害\n");
    }

    // player do attack commend
    public void doAttack() {
        int damage = calculateDamage(attacker, defender);
        String action = attacker.getName() + " attacks " + defender.getName() + " for " + damage + " damage.";
        battleLogs.add(action);
        defender.takeDamage(damage);
    }

    // player do defend commend
    public void doDefend() {
        String action = attacker.getName() + " defends, reducing damage.";
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
        // detect if battle is over
        if (!attacker.isAlive() || !defender.isAlive()) {
            System.out.println("Battle is over.");
            return;
        }

        // Player's turn
        performAttack(attacker, defender);

        // Check if enemy is defeated
        if (!defender.isAlive()) {
            System.out.println(defender.getName() + " 被擊倒了!");
            return;
        }

        // Enemy's turn
        performAttack(defender, attacker);

        // Check if player is defeated
        if (!attacker.isAlive()) {
            System.out.println(attacker.getName() + " 被擊倒了!");
        }
    }

    public String getBattleState() {
        StringBuilder state = new StringBuilder();
        state.append("Player: ").append(attacker.getName()).append(" HP: ").append(attacker.getHp()).append("\n");
        state.append("Enemy: ").append(defender.getName()).append(" HP: ").append(defender.getHp()).append("\n");
        return state.toString();
    }

}
