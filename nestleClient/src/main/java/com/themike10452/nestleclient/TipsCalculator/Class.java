package com.themike10452.nestleclient.TipsCalculator;

/**
 * Created by Mike on 6/24/2014.
 */
public class Class implements AnElement {
    private String name;
    private int level;
    private boolean asPercentage;
    private double share;

    public Class(String ClassName, int ClassLevel, boolean asPercentage, double ClassShare) {
        name = ClassName.toUpperCase();
        level = ClassLevel;
        share = ClassShare;
        this.asPercentage = asPercentage;
    }

    public String getTag() {
        return level + ":" + asPercentage + ":" + name + ":" + share;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public Class getSuperClass() {
        return this;
    }

    public String getName() {
        return name;
    }

    public double getShare() {
        return share;
    }

    public boolean isByPercentage() {
        return asPercentage;
    }

    public double getCalculatedShare(double total) {
        return total * share / 100;
    }

    public double getCalculatedShare(double total, double percentage) {
        return total * percentage / 100;
    }

    private int booleanToInt(boolean b) {
        return b ? 1 : 0;
    }

}
