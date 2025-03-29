package com.mygame.rpg.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.badlogic.gdx.Gdx;
import com.mygame.rpg.character.Character;
import com.mygame.rpg.character.Monster;
import com.mygame.rpg.character.Player;
import com.mygame.rpg.core.RPGGame;

public class Battle {
    // objects
    private Player player;
    private Monster enemy;

    // battle state
    // private boolean playerTurn;
    // private boolean isBattleOver;
    private boolean battleOver;
    private boolean waitingForPlayerAction; // 新增布林值來標記是否等待玩家操作
    private boolean waitingForPlayerConfirmation; // 新增布林值來標記是否等待玩家確認


    private String battleResult;
    private List<DropItem> itemReward;

    // 行動隊列
    private final PriorityQueue<Character> actionQueue;
    private final int actionThreshold = 100;

    // 戰鬥進行記錄
    private final List<String> battleLogs;
    private int currentLogIndex = 0;

    public Battle(Player player, Monster enemy) {
        Gdx.app.log("battle", "enter battle");
        this.player = player;
        this.enemy = enemy;
        // this.isBattleOver = false;
        this.waitingForPlayerAction = false; // 初始化為 false

        this.battleLogs = new ArrayList<>();
        this.actionQueue = new PriorityQueue<>((a, b) -> Integer.compare(b.getActionBar(), a.getActionBar())); // 速度高者優先

        // 初始化角色行動條
        player.resetActionBar();
        enemy.resetActionBar();
        actionQueue.add(player);
        actionQueue.add(enemy);
    }

    // 執行戰鬥
    private void processTurn(Character character) {
        if (character == enemy) {
            // 敵人行動
            int damage = calculateDamage(enemy, player);
            player.takeDamage(damage);

            waitingForPlayerConfirmation = true; // 等待玩家操作

            Gdx.app.log("BattleLog", "waitingForPlayerConfirm: " + waitingForPlayerAction + "\n");
            Gdx.app.log("BattleLog", enemy.getName() + " attacks " + player.getName() + " for " + damage + " damage.");
            Gdx.app.log("BattleLog", player.getName() + " remaining HP: " + player.getHp() + "\n\n");

            if (!actionQueue.contains(enemy)){
                enemy.resetActionBar();
                enemy.setReadyToAct(false);
                actionQueue.add(enemy);
            }

        }

        // 檢查戰鬥是否結束
        if (isBattleOver()) {
            // isBattleOver = true;
            String winner = player.isAlive() ? player.getName() : enemy.getName();
            Gdx.app.log("BattleLog", "Battle Over! Winner: " + winner);
        }
    }

    public void onPlayerConfirm() {
        Gdx.app.log("Battle", "Player confirmed. Resuming action bar update.");
        if (waitingForPlayerConfirmation) {
            waitingForPlayerConfirmation = false; // 更新狀態
            updateActionBar(); // 繼續更新行動條
        }
    }

    // update action bar
    public void updateActionBar() {
        if (waitingForPlayerAction || waitingForPlayerConfirmation) {
            // Gdx.app.log("battleLog", "Pausing action bar update waiting for player action");
            return; // 如果正在等待玩家操作，則不更新行動條
        }

        for (Character character : actionQueue) {
            // for (Character c : actionQueue) {
            //     Gdx.app.log("BattleLog - updateActionBar", c.getName() + " action bar: " + c.getActionBar());
            // }
            // Gdx.app.log("BattleLog - updateActionBar", "\n");
            character.incrementActionBar(character.getSpd());

            if (character.getActionBar() >= actionThreshold) {
                // Gdx.app.log("BattleLog - updateActionBar", "Character " + character.getName() + " is ready to act.");
                character.setReadyToAct(true); // 設置角色為準備行動狀態
                actionQueue.remove(character);

                if (character == player) {
                    waitingForPlayerAction = true; // 如果是玩家，設置等待玩家操作
                    // Gdx.app.log("BattleLog - updateActionBar", "should wait for player action");
                } else {
                    processTurn(character); // 自動執行敵人的行動
                    character.resetActionBar();
                    character.setReadyToAct(false); // 重置行動狀態
                    if (!actionQueue.contains(character)) {
                        actionQueue.add(character);
                    }
                }
                break;
            }
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
    public Player getPlayer() {return player;}
    public Monster getEnemy() {return enemy;}

    // 判斷戰鬥是否結束
    public boolean isBattleOver() {
        if (!enemy.isAlive()) {
            if (!battleOver && player.isAlive()) {
                int expGained = enemy.getExpReward();
                int goldGained = enemy.getGoldReward();
                player.addGold(goldGained);
                player.gainExp(expGained);

                itemReward = enemy.getRandomDrop();
                for (DropItem item : itemReward) {
                    Gdx.app.log("battle - isBattleOver", "getting drops " + item.getItemID() + " " + item.getName() + " " + item.getDropRate());
                    if (item != null) {
                        player.addItem(item.getItemID(), item.getRandomDropCount()); // 獲得掉落物品
                    }
                }

                battleResult = player.getName() + " defeated " + enemy.getName() + "! Gained " + expGained + " EXP.";
                battleOver = true;
            }
            return true;
        } else if (!player.isAlive()) {
            if (!battleOver) {
                battleResult = player.getName() + " was defeated by " + enemy.getName() + ".";
                battleOver = true;
                // 呼叫重生玩家的方法
                respawnPlayer();
            }
            return true;
        }
        return false;
    }

    // 重生玩家並返回城鎮
    private void respawnPlayer() {
        Gdx.app.log("battle", "respawning player and returning to town");
        player.respawn(); // 重置玩家的狀態
        // 這裡假設有一個方法可以將玩家傳送到城鎮
        player.returnToTown();
        RPGGame.getInstance().playerDeath();; // 結束戰鬥

    }

    public boolean isWaitingForPlayerConfirmation() {
        return waitingForPlayerConfirmation;
    }

    // get battle result
    public String getBattleResult() {
        return battleResult;
    }

    // get item reward
    public List<DropItem> getItemReward() {
        return itemReward;
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

        player.resetActionBar();
        player.setReadyToAct(false);

        actionQueue.add(player);
        waitingForPlayerAction = false; // 玩家操作完成，繼續更新行動條
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

    // get all battle logs
    public List<String> getBattleLogs() {
        return new ArrayList<>(battleLogs); // 回傳所有行動日誌
    }

    public String getBattleState() {
        StringBuilder state = new StringBuilder();
        state.append("Player: ").append(player.getName()).append(" HP: ").append(player.getHp()).append("\n");
        state.append("Enemy: ").append(enemy.getName()).append(" HP: ").append(enemy.getHp()).append("\n");
        return state.toString();
    }

    // 新增方法來檢查是否正在等待玩家操作
    public boolean isWaitingForPlayerAction() {
        return waitingForPlayerAction;
    }
}
