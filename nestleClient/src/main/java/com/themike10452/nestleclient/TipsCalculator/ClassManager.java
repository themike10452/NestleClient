package com.themike10452.nestleclient.TipsCalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Mike on 6/24/2014.
 */
public class ClassManager {

    public static final Comparator<Class> classComparator = new Comparator<Class>() {
        @Override
        public int compare(Class aClass, Class aClass2) {
            return aClass.getTag().compareTo(aClass2.getTag());
        }
    };
    private static final String key = "Class_";
    private static final String errorKey = "n/a";
    private static ClassManager instance;
    private List<Class> classArray;
    private List<Class> superClassArray;
    private Context context;

    public ClassManager(Context context) {
        this.context = context;
        classArray = getClasses();
        superClassArray = getSuperClasses();
    }

    public static ClassManager getInstance(Context context) {
        if (instance == null)
            instance = new ClassManager(context);

        return instance;
    }

    public void refreshInstance() {
        classArray = getClasses();
        superClassArray = getSuperClasses();
    }

    private ArrayList<Class> getClasses() {
        SharedPreferences preferences = context.getSharedPreferences("Classes", Context.MODE_PRIVATE);
        ArrayList<Class> list = new ArrayList<Class>();
        int ClassCounter = 0;
        String s;
        do {
            s = preferences.getString(key + ClassCounter, errorKey);
            if (!s.equals(errorKey)) {
                String[] info = s.split(":");
                if (info.length > 3)
                    list.add(new Class(info[2], Integer.parseInt(info[0]), Boolean.parseBoolean(info[1]), Double.parseDouble(info[3])));
                else
                    Toast.makeText(context, "Found a corrupted line " + s, Toast.LENGTH_SHORT).show();
            } else {
                break;
            }
            ClassCounter++;
        } while (true);

        if (list.size() > 0)
            Collections.sort(list, classComparator);
        return list;
    }

    private ArrayList<Class> getSuperClasses() {
        SharedPreferences SuperPreferences = context.getSharedPreferences("SuperClasses", Context.MODE_PRIVATE);
        ArrayList<Class> list = new ArrayList<Class>();
        int SuperClassCounter = 0;
        String s;
        do {
            s = SuperPreferences.getString(key + SuperClassCounter, errorKey);
            if (!s.equals(errorKey)) {
                String[] info = s.split(":");
                if (info.length > 3)
                    list.add(new Class(info[2], Integer.parseInt(info[0]), Boolean.parseBoolean(info[1]), Double.parseDouble(info[3])));
            } else {
                break;
            }
            SuperClassCounter++;
        } while (true);

        if (list.size() > 0)
            Collections.sort(list, classComparator);
        return list;
    }

    public int getClassCount() {
        return classArray.size();
    }

    public int getSuperClassCount() {
        return superClassArray.size();
    }

    public List<Class> getClassArray() {
        return classArray;
    }

    public List<Class> getSuperClassArray() {
        return superClassArray;
    }

    public List<String> getClassArrayOfStrings() {
        List<String> list = new ArrayList<String>();
        for (Class aClass : classArray)
            list.add(aClass.getName());
        return list;
    }

    public List<String> getSuperClassArrayOfStrings() {
        List<String> list = new ArrayList<String>();
        for (Class aClass : superClassArray)
            list.add(aClass.getName());
        return list;
    }

    public int findClassIdByTag(Class aClass) {
        for (int i = 0; i < classArray.size(); i++) {
            if (classArray.get(i).getTag().equals(aClass.getTag())) {
                return i;
            }
        }
        for (int i = 0; i < superClassArray.size(); i++) {
            if (superClassArray.get(i).getTag().equals(aClass.getTag())) {
                return i;
            }
        }
        return -1;
    }

    public int findClassIdByName(String name) {
        for (int i = 0; i < classArray.size(); i++) {
            if (classArray.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        for (int i = 0; i < superClassArray.size(); i++) {
            if (superClassArray.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public Class findClassByName(String name) {

        for (int i = 0; i < classArray.size(); i++) {
            if (classArray.get(i).getName().equalsIgnoreCase(name)) {
                return classArray.get(i);
            }
        }
        for (int i = 0; i < superClassArray.size(); i++) {
            if (superClassArray.get(i).getName().equalsIgnoreCase(name)) {
                return superClassArray.get(i);
            }
        }

        Log.d("TAG", name + " not found !!!");

        return null;
    }

    public boolean addClass(Class aClass) {

        SharedPreferences localPrefs = context.getSharedPreferences("Classes", Context.MODE_PRIVATE);

        if (findClassIdByName(aClass.getName()) >= 0)
            return false;

        if (!aClass.isByPercentage())
            localPrefs = context.getSharedPreferences("SuperClasses", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = localPrefs.edit();
        int localCounter = 0;
        while (!(localPrefs.getString(key + localCounter, errorKey)).equals(errorKey)) {
            localCounter++;
        }

        editor.putString(key + localCounter, aClass.getTag()).apply();

        refreshInstance();

        if (aClass.isByPercentage()) {
            //superClassArray.add(aClass);
            //Collections.sort(superClassArray, classComparator);
            //} else {
            //classArray.add(aClass);
            //Collections.sort(classArray, classComparator);
            PersonsManager.getInstance(context).addPerson(new Person(aClass.getName(), aClass));
        }

        MainActivity2.getInstance().updateListView();
        return true;
    }

    public boolean removeClass(Class aClass) {
        try {
            String localKey = aClass.isByPercentage() ? "Classes" : "SuperClasses";
            SharedPreferences localPrefs = context.getSharedPreferences(localKey, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = localPrefs.edit();
            String name = aClass.getName();
            String s;
            boolean found = false;
            int ClassCounter = 0;
            do {

                s = localPrefs.getString(key + ClassCounter, errorKey);

                if (s.equals(errorKey)) {
                    break;
                }

                if (name.equalsIgnoreCase(s.split(":")[2])) {
                    found = true;
                    String z;
                    if (!(z = localPrefs.getString(key + (ClassCounter + 1), errorKey)).equals(errorKey)) {
                        editor.putString(key + ClassCounter, z);
                        editor.putString(key + (ClassCounter + 1), s);
                        editor.apply();
                    } else {
                        editor.remove(key + ClassCounter);
                        editor.apply();
                        break;
                    }
                }
                ClassCounter++;
            } while (true);

            editor.apply();

            if (found && aClass.isByPercentage()) {
                return PersonsManager.getInstance(context).removePerson(name);
            } else {
                return found;
            }
        } finally {
            superClassArray = getSuperClasses();
            classArray = getClasses();
        }
    }
}
