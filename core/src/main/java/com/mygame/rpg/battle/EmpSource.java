package com.mygame.rpg.battle;

public class EmpSource {
    private int empBar; // 能量進度條
    private int empSpeed; // 能量條前進速度

    private int empPool; // 能量池
    private int maxEmpPool = 100; // 最大能量池
    private final int naturalEmpRegen = 10; // 每回合自然回復的能量值

    public EmpSource() {
        this.empPool = maxEmpPool; // 初始化能量池為最大值
        this.empBar = 0;
        this.empSpeed = 25; // 每回合能量條前進速度
    }

    // 更新能量條
    public void incrementEmpBar() {
        empBar += empSpeed;
        if (empBar >= 100) {
            empBar -= 100;
            if (empPool < maxEmpPool) {
                recoverEmpNatural();
            }
        }
    }

    // 自然恢復能量
    public void recoverEmpNatural() {
        empPool += naturalEmpRegen;
        if (empPool > maxEmpPool) {
            empPool = maxEmpPool;
        }
    }

    // 使用能量
    public void consume(int emp) {
        if (empPool < emp) return; // 能量不足
        empPool -= emp;
    }

    // 檢查能量是否足夠
    public boolean canCast(int emp) {
        return empPool >= emp; // 能量足夠
    }

    public int getEmpPool() {return empPool; }
    public void setEmpPool(int empPool) {this.empPool = empPool; }
    public int getMaxEmpPool() {return maxEmpPool; }
    public void setMaxEmpPool(int maxEmpPool) {this.maxEmpPool = maxEmpPool; }
}
