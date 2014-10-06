package com.themike10452.nestleclient.TipsCalculator;

import android.app.AlertDialog;
import android.content.Context;

import com.themike10452.nestleclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 6/30/2014.
 */
public class Calculator {

    public static void calculate(Context context, double TOTAL) {

        final double total = TOTAL;

        int maxLevel = context.getResources().getStringArray(R.array.class_levels).length - 1;
        List<Person> delayed = new ArrayList<Person>();

        for (int thisLevel = 0; thisLevel <= maxLevel; thisLevel++) {

            double sumOfThisLevel = 0;
            double sumOfDelayedShares = 0;
            List<Person> persons = PersonsManager.getInstance(context).getPersonsWithLevel(thisLevel);

            if (persons == null)
                continue;

            delayed.clear();

            for (Person person : persons) {
                if (person.getSuperClass().isByPercentage()) {
                    sumOfThisLevel += person.setCalculatedShare(round(TOTAL / 100 * person.getShare()));
                } else {
                    delayed.add(person);
                    sumOfDelayedShares += person.getShare();
                }
            }

            double newTotal = TOTAL - sumOfThisLevel;

            for (Person person : delayed) {
                double percentage = person.getShare() / sumOfDelayedShares;
                sumOfThisLevel += person.setCalculatedShare(round(percentage * newTotal));
            }

            TOTAL -= sumOfThisLevel;

        }

        if (TOTAL >= 0) MainActivity2.getInstance().showResults(total, TOTAL);
        else new AlertDialog.Builder(context)
                .setTitle("Notice")
                .setMessage("This amount of money can not be divided respecting your current configuration. An extra " + -TOTAL + " p.d.u is needed")
                .setCancelable(false)
                .setNeutralButton("OK", null)
                .show();
    }

    public static double round(double toRound) {

        double newDouble = Math.round(toRound);

        return toRound - newDouble < 0 ? newDouble - 1 : newDouble;

    }


}
