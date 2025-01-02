package com.mygame.rpg;

public class Battle {
    //objects
    private Character attacker;
    private Character defender;
    private boolean isBattleOver;

    public Battle(Character attacker, Character defender) {
        this.attacker = attacker;
        this.defender = defender;
        this.isBattleOver = false;
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

        System.out.println(attacker.getName() + " 進行了一次攻擊\n對 " + defender.getName() + " 造成了 " + damage + " 點傷害\n");
    }

    // run one round of battle
    public void battleTurn(Character player, Character enemy) {
        if (!player.isAlive() || !enemy.isAlive()) {
            System.out.println("Battle is over.");
            return;
        }

        // Player's turn
        performAttack(player, enemy);

        // Check if enemy is defeated
        if (!enemy.isAlive()) {
            System.out.println(enemy.getName() + " 被擊倒了!");
            return;
        }

        // Enemy's turn
        performAttack(enemy, player);

        // Check if player is defeated
        if (!player.isAlive()) {
            System.out.println(player.getName() + " 被擊倒了!");
        }
    }

    public String getBattleState() {
        StringBuilder state = new StringBuilder();
        state.append("Player: ").append(attacker.getName()).append(" HP: ").append(attacker.getHp()).append("\n");
        state.append("Enemy: ").append(defender.getName()).append(" HP: ").append(defender.getHp()).append("\n");
        return state.toString();
    }

}
