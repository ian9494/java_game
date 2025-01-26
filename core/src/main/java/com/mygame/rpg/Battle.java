package com.mygame.rpg;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.badlogic.gdx.Gdx;
public class Battle {
    // objects
    private Character player;
    private Character enemy;

    // battle state
    private boolean playerTurn;
    private boolean isBattleOver;

    // 行動隊列
    private final PriorityQueue<Character> actionQueue;
    private final int actionThreshold = 100;

    // 戰鬥進行記錄
    private final List<String> battleLogs;
    private int currentLogIndex = 0;

    public Battle(Character player, Character enemy) {
        Gdx.app.log("battle", "enter battle");
        this.player = player;
        this.enemy = enemy;
        this.isBattleOver = false;

        this.battleLogs = new ArrayList<>();
        this.actionQueue = new PriorityQueue<>((a, b) -> Integer.compare(b.getActionBar(), a.getActionBar())); // 速度高者優先

        // 初始化角色行動條
        player.resetActionBar();
        enemy.resetActionBar();
        actionQueue.add(player);
        actionQueue.add(enemy);
    }

    // update action bar
    public void updateActionBar(float deltaTime) {
        for (Character character : actionQueue) {
            character.incrementActionBar(character.getSpd() * deltaTime); // 根據速度增加行動條
        }
    }

    // 確認下一位行動者
    public Character getNextActionCharacter() {
        for (Character character : actionQueue) {
            if (character.getActionBar() >= actionThreshold) {
                character.resetActionBar(); // 重置行動條
                return character;
            }
        }
        return null; // 沒有人達到行動條要求
    }

    // get player and enemy
    public Character getPlayer() {
        return player;
    }

    public Character getEnemy() {
        return enemy;
    }

    // 判斷戰鬥是否結束
    public boolean isBattleOver() {
        return actionQueue.stream().anyMatch(c -> !c.isAlive());
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

    // log action
    public void logAction(String log) {
        battleLogs.add(log);
    }

    // get last log
    public String getLastLog() {
        return battleLogs.isEmpty() ? "" : battleLogs.get(battleLogs.size() - 1);
    }

    // run one round of battle
    public void battleTurn(Character player, Character enemy) {
        Gdx.app.log("battle", "start new round");

        // detect if battle is over
        if (isBattleOver()) return;

        // character's turn
        if (playerTurn) {
            int damage = calculateDamage(player, enemy);
            enemy.takeDamage(damage);
            battleLogs.add(player.getName() + "對" + enemy.getName() + "造成了" + damage + "點傷害");
        }
        // enemy's turn
        else {
            int damage = calculateDamage(enemy, player);
            player.takeDamage(damage);
            battleLogs.add(enemy.getName() + "對" + player.getName() + "造成了" + damage + "點傷害");
        }
        // switch side
        playerTurn = !playerTurn;
    }

    public String getBattleState() {
        StringBuilder state = new StringBuilder();
        state.append("Player: ").append(player.getName()).append(" HP: ").append(player.getHp()).append("\n");
        state.append("Enemy: ").append(enemy.getName()).append(" HP: ").append(enemy.getHp()).append("\n");
        return state.toString();
    }

}
