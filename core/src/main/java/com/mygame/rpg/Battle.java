package com.mygame.rpg;
public class Battle {
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
}
