package com.mygame.rpg.item;

import java.util.*;
import java.util.stream.Collectors;

public class StagePathUtils {

    // 是否是同一條分支（只要有相同前綴即可）
    public static boolean isSameBranch(List<Integer> a, List<Integer> b) {
        int min = Math.min(a.size(), b.size());
        for (int i = 0; i < min; i++) {
            if (!a.get(i).equals(b.get(i))) return false;
        }
        return true;
    }

    // 是否是從屬分支（b 是否為 a 的子節點）
    public static boolean isSubBranchOf(List<Integer> base, List<Integer> candidate) {
        if (candidate.size() <= base.size()) return false;
        for (int i = 0; i < base.size(); i++) {
            if (!base.get(i).equals(candidate.get(i))) return false;
        }
        return true;
    }

    // 回傳上一層的路徑，例如 [1,1,2] -> [1,1]
    public static List<Integer> getParentStagePath(List<Integer> stage) {
        if (stage.size() <= 1) return null;
        return new ArrayList<>(stage.subList(0, stage.size() - 1));
    }

    // 將 List<Integer> 轉換為 "1-1-2" 這樣的字串
    public static String formatStagePath(List<Integer> stage) {
        return stage.stream().map(String::valueOf).collect(Collectors.joining("-"));
    }

    // 回傳分支深度
    public static int getDepth(List<Integer> stage) {
        return stage.size();
    }

    // 將 "1-2-3" 轉換為 List<Integer>
    public static List<Integer> parseStagePath(String str) {
        String[] parts = str.split("-");
        List<Integer> result = new ArrayList<>();
        for (String part : parts) {
            result.add(Integer.parseInt(part));
        }
        return result;
    }
}

