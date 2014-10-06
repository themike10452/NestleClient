package com.themike10452.nestleclient;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Mike on 6/9/2014.
 */
public class Stack {
    private static ArrayList<String> arrayList = new ArrayList();

    public static void push(String data) {
        arrayList.add(data);
        Log.d("TAG", "added " + data);
    }

    public static String pop() {
        if (arrayList.size() > 0) {
            try {
                return arrayList.get(arrayList.size() - 1);
            } finally {
                arrayList.remove(arrayList.size() - 1);
            }
        } else
            return null;
    }

}
