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
public class PersonsManager {

    private static String key = "0x0_person_";
    private static String errorKey = "n/a";
    private static PersonsManager instance;
    private static Context context;
    private static ArrayList<Person> personsArray;
    private Comparator<AnElement> personComparator = new Comparator<AnElement>() {
        @Override
        public int compare(AnElement person, AnElement person2) {
            String a1 = Integer.toString(person.getLevel());
            char a2 = person.getSuperClass().getName().charAt(0);
            char a3 = person.getName().charAt(0);

            String b1 = Integer.toString(person2.getLevel());
            char b2 = person2.getSuperClass().getName().charAt(0);
            char b3 = person2.getName().charAt(0);

            return (a1 + a2 + a3 + "").compareTo(b1 + b2 + b3 + "");
        }
    };

    public PersonsManager(Context context) {
        this.context = context;
        personsArray = getPersons();
    }

    public static PersonsManager getInstance(Context context) {
        if (instance == null)
            instance = new PersonsManager(context);

        return instance;
    }

    public static int getPersonCounter() {
        return personsArray.size();
    }

    public PersonsManager refreshInstance() {
        personsArray = getPersons();
        return this;
    }

    public boolean addPerson(Person person) {
        SharedPreferences preferences = context.getSharedPreferences("Persons", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int personCounter = 0;
        String s;
        while (!(s = preferences.getString(key + personCounter, errorKey)).equals(errorKey)) {
            if (s.split(":")[1].equalsIgnoreCase(person.getName()))
                return false;
            personCounter++;
        }
        editor.putString(key + personCounter, person.getTag());
        Collections.sort(personsArray, personComparator);
        editor.apply();
        refreshInstance();
        MainActivity2.getInstance().updateListView();
        return true;
    }

    public boolean removePersonAt(int index) {

        SharedPreferences preferences = context.getSharedPreferences("Persons", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String info = getPersonsArrayOfStrings().get(index);
        String s;
        boolean found = false;
        int personCounter = 0;

        do {

            s = preferences.getString(key + personCounter, errorKey);

            if (s.equals(errorKey))
                break;

            if (info.equalsIgnoreCase(s.split(":")[1])) {
                found = true;
                String z;
                if (!(z = preferences.getString(key + (personCounter + 1), "n/a")).equals("n/a")) {
                    editor.putString(key + personCounter, z);
                    editor.putString(key + (personCounter + 1), s);
                    editor.apply();
                } else {
                    editor.remove(key + personCounter);
                    break;
                }
            }

            personCounter++;

        } while (true);

        editor.apply();

        refreshInstance();

        MainActivity2.getInstance().updateListView();

        if (found && ClassManager.getInstance(context).findClassByName(info) != null) {
            ClassManager.getInstance(context).removeClass(ClassManager.getInstance(context).findClassByName(info));
        }
        return found;
    }

    public boolean removePerson(String name) {
        Log.d("TAG", "removing " + name);
        SharedPreferences preferences = context.getSharedPreferences("Persons", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String info = name;
        String s;
        boolean found = false;
        int personCounter = 0;
        do {
            s = preferences.getString(key + personCounter, errorKey);
            if (s.equals(errorKey))
                break;

            if (info.equalsIgnoreCase(s.split(":")[1])) {
                found = true;
                String z;
                if (!(z = preferences.getString(key + (personCounter + 1), "n/a")).equals("n/a")) {
                    editor.putString(key + personCounter, z);
                    editor.putString(key + (personCounter + 1), s);
                    editor.apply();
                } else {
                    editor.remove(key + personCounter);
                    break;
                }
            }
            personCounter++;
        } while (true);
        editor.apply();
        refreshInstance();
        MainActivity2.getInstance().updateListView();
        if (found && ClassManager.getInstance(context).findClassByName(info) != null) {
            ClassManager.getInstance(context).removeClass(ClassManager.getInstance(context).findClassByName(info));
        }
        return found;
    }

    private ArrayList<Person> getPersons() {
        SharedPreferences preferences = context.getSharedPreferences("Persons", Context.MODE_PRIVATE);
        ArrayList<Person> list = new ArrayList<Person>();
        int personCounter = 0;
        String s;

        while (!(s = preferences.getString(key + personCounter, errorKey)).equals(errorKey)) {
            Log.d("TAG", "s=" + s);
            String[] info = s.split(":");
            Log.d("TAG", "searching for class:" + info[2]);
            Class hisClass = ClassManager.getInstance(context).findClassByName(info[2]);

            if (hisClass != null)
                list.add(new Person(info[1], hisClass));
            else
                Toast.makeText(context, "Error while processing " + info[1], Toast.LENGTH_SHORT).show();

            personCounter++;
        }

        if (list.size() > 0)
            Collections.sort(list, personComparator);

        return list;
    }

    public ArrayList<String> getPersonsArrayOfStrings() {
        ArrayList<String> list = new ArrayList<String>();
        for (Person person : personsArray)
            list.add(person.getName());

        return list;
    }

    public ArrayList<Person> getPersonsArray() {
        return personsArray;
    }

    public void updatePersonsWithClass(Class oldClass, Class newClass) {
        SharedPreferences preferences = context.getSharedPreferences("Persons", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String s;
        int localCounter = 0;
        while (!(s = preferences.getString(key + localCounter, errorKey)).equals(errorKey)) {
            if (s.split(":")[2].equalsIgnoreCase(oldClass.getName())) {
                String tmp = s.split(":")[0];
                tmp += ":" + s.split(":")[1];
                tmp += ":" + newClass.getName();
                editor.putString(key + localCounter, tmp);

                editor.apply();
            }
            localCounter++;
        }
        refreshInstance();
    }

    public List<Person> getPersonsFromSuperClass(Class superClass) {
        List<Person> list = new ArrayList<Person>();

        for (Person person : personsArray)
            if (person.getSuperClass() == superClass)
                list.add(person);

        return list.size() == 0 ? null : list;
    }

    public List<Person> getPersonsWithLevel(int level) {
        List<Person> list = new ArrayList<Person>();

        for (Person person : personsArray)
            if (person.getLevel() == level)
                list.add(person);

        return list.size() == 0 ? null : list;
    }

}
