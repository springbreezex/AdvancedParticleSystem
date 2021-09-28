package me.springbreezex.advancedparticlesystem.utils;

import scalc.SCalcBuilder;

public class FormulaCalculator {
    public static int getIntegerByT(String formula, int t) {
        return SCalcBuilder.doubleInstance().expression(formula).build().parameter("t",t).calc().intValue();
    }
    public static int getIntegerByT(String formula, float t) {
        return SCalcBuilder.doubleInstance().expression(formula).build().parameter("t",t).calc().intValue();
    }
    public static float getFloatByT(String formula, int t) {
        return SCalcBuilder.doubleInstance().expression(formula).build().parameter("t",t).calc().floatValue();
    }
    public static float getFloatByT(String formula, float t) {
        return SCalcBuilder.doubleInstance().expression(formula).build().parameter("t",t).calc().floatValue();
    }

    public static int getIntegerByTBetween0and255(String formula, int t) {
        int retrieve = SCalcBuilder.doubleInstance().expression(formula).build().parameter("t",t).calc().intValue();
        if (retrieve>255) return 255;
        if (retrieve<0) return 0;
        return retrieve;
    }
}
