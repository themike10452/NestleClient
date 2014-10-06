package com.themike10452.nestleclient.TipsCalculator;

/**
 * Created by Mike on 6/26/2014.
 */
public class Person implements AnElement {

    private Class superClass;
    private String name;
    private double calculatedShare;

    public Person(String name, Class hisClass) {
        this.name = name.toUpperCase();
        superClass = hisClass;
        calculatedShare = -1;
    }

    public Class getSuperClass() {
        return superClass;
    }

    public void setSuperClass(Class aClass) {
        superClass = aClass;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return superClass.getLevel() + ":" + name + ":" + superClass.getName();
    }

    public double getShare() {
        return superClass.getShare();
    }

    public int getLevel() {
        return superClass.getLevel();
    }

    public double setCalculatedShare(double calculatedShare) {
        this.calculatedShare = calculatedShare;
        return calculatedShare;
    }

    public double getCalculatedShare() {
        return calculatedShare;
    }
}
