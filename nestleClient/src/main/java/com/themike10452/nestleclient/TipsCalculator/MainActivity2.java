package com.themike10452.nestleclient.TipsCalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.themike10452.nestleclient.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity2 extends Activity {


    private static MainActivity2 instance;
    private static int lastLVL;
    private static int lastSC;
    private View.OnClickListener OnAddPerson = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (ClassManager.getInstance(getThisContext()).getSuperClassArray().size() < 1) {
                toast("Create a SuperClass first");
                return;
            }

            final Dialog dialog = new Dialog(getThisContext());
            dialog.setTitle("Add person");
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.add_person_dialog);
            dialog.show();
            dialog.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String hisName = ((EditText) dialog.findViewById(R.id.__name)).getText().toString();
                    String className = ((Spinner) dialog.findViewById(R.id.__superClass)).getSelectedItem().toString();
                    if (hisName.length() > 0) {
                        Class hisClass = ClassManager.getInstance(getThisContext()).findClassByName(className);
                        if (hisClass != null)
                            if (PersonsManager.getInstance(getThisContext()).addPerson(new Person(hisName, hisClass))) {
                                toast(hisName + " added successfully");
                                lastSC = ((Spinner) dialog.findViewById(R.id.__superClass)).getSelectedItemPosition();
                                dialog.dismiss();
                            } else {
                                toast(hisName + " already exists");
                            }
                        else
                            toast("Failed to add " + hisName);
                    } else {
                        toast("Blank name, dismissed");
                        dialog.dismiss();
                    }
                }
            });
            ((Spinner) dialog.findViewById(R.id.__superClass)).setAdapter(new ArrayAdapter<String>(getThisContext(), android.R.layout.simple_spinner_dropdown_item, ClassManager.getInstance(getThisContext()).getSuperClassArrayOfStrings()));
            ((Spinner) dialog.findViewById(R.id.__superClass)).setSelection(lastSC);

            dialog.findViewById(R.id.__name).requestFocus();
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        }
    };
    private static boolean ScWasChecked;
    private ListView listView;

    public static MainActivity2 getInstance() {
        return instance;
    }

    private void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private Context getThisContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        listView = (ListView) findViewById(R.id.listView);
        lastLVL = 0;
        lastSC = 0;
        ScWasChecked = true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Button addPerson = (Button) findViewById(R.id.addPerson);
        addPerson.setOnClickListener(OnAddPerson);
        updateListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            final Dialog dialog = new Dialog(getThisContext());
            dialog.setTitle("Settings");
            dialog.setContentView(R.layout.layout_settings);
            ListView settings = (ListView) dialog.findViewById(R.id.settings_listView);
            final String[] array = getResources().getStringArray(R.array.tips_settings);
            settings.setAdapter(new ArrayAdapter<String>(getThisContext(), R.layout.settings_listview_item, R.id.settings_textView, array));
            settings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 0: {
                            final Dialog dialog1 = new Dialog(getThisContext());
                            dialog1.setTitle(array[i]);
                            dialog1.setContentView(R.layout.add_class_dialog);
                            ((Spinner) dialog1.findViewById(R.id.__lvl))
                                    .setAdapter(new ArrayAdapter<String>(getThisContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.class_levels)));
                            ((Spinner) dialog1.findViewById(R.id.__lvl))
                                    .setSelection(lastLVL);
                            ((CheckBox) dialog1.findViewById(R.id.hasDesc_checkBox)).setChecked(ScWasChecked);
                            ((CheckBox) dialog1.findViewById(R.id.hasDesc_checkBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    ((EditText) dialog1.findViewById(R.id.share_editText)).setHint(b ? "Shares" : "Percentage");
                                    ((EditText) dialog1.findViewById(R.id.share_editText)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(b ? 4 : 2)});
                                    ScWasChecked = b;
                                }
                            });
                            dialog1.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String name = ((TextView) dialog1.findViewById(R.id.__name)).getText().toString();
                                    if (name.length() > 0) {
                                        int level = ((Spinner) dialog1.findViewById(R.id.__lvl)).getSelectedItemPosition();
                                        lastLVL = level;
                                        boolean isSuper = ((CheckBox) dialog1.findViewById(R.id.hasDesc_checkBox)).isChecked();
                                        double share;
                                        try {
                                            share = Double.parseDouble(((TextView) dialog1.findViewById(R.id.share_editText)).getText().toString());
                                        } catch (Exception e) {
                                            toast("Invalid share");
                                            return;
                                        }
                                        if (!ClassManager.getInstance(getThisContext()).addClass(new Class(name, level, !isSuper, share)))
                                            toast("This name is already used");
                                        else {
                                            dialog1.dismiss();
                                            toast(name + " added successfully");
                                        }
                                    } else {
                                        toast("Empty name, discarded");
                                        dialog1.dismiss();
                                    }
                                }
                            });
                            dialog1.show();
                            break;
                        }

                        case 1: {
                            final Dialog dialog1 = new Dialog(getThisContext());
                            dialog1.setTitle("Edit an existing Class");
                            dialog1.setContentView(R.layout.layout_settings);
                            dialog1.show();
                            final List<String> list = ClassManager.getInstance(getThisContext()).getClassArrayOfStrings();
                            list.addAll(ClassManager.getInstance(getThisContext()).getSuperClassArrayOfStrings());
                            final ListView listView1 = (ListView) dialog1.findViewById(R.id.settings_listView);
                            listView1.setAdapter(new ArrayAdapter<String>(getThisContext(), R.layout.settings_listview_item, R.id.settings_textView, list));
                            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                                    final Dialog dialog2 = new Dialog(getThisContext());
                                    dialog2.setTitle(list.get(i));
                                    dialog2.setContentView(R.layout.add_class_dialog);

                                    final ClassManager manager = ClassManager.getInstance(getThisContext());
                                    final Class concernedClass = manager.findClassByName(list.get(i));

                                    final TextView _name = (TextView) dialog2.findViewById(R.id.__name);
                                    final CheckBox isSuperClass = (CheckBox) dialog2.findViewById(R.id.hasDesc_checkBox);
                                    final EditText _share = (EditText) dialog2.findViewById(R.id.share_editText);
                                    final Spinner _lvl = (Spinner) dialog2.findViewById(R.id.__lvl);

                                    _name.setText(list.get(i));
                                    isSuperClass.setChecked(!manager.findClassByName(list.get(i)).isByPercentage());
                                    _share.setText(concernedClass.getShare() + "");

                                    _lvl.setAdapter(new ArrayAdapter<String>(getThisContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.class_levels)));

                                    _lvl.setSelection(concernedClass.getLevel());

                                    dialog2.show();

                                    dialog2.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (manager.removeClass(concernedClass)) {
                                                String newName = _name.getText().toString();
                                                if (newName.trim().length() > 0) {
                                                    Class newClass = new Class(_name.getText().toString(), _lvl.getSelectedItemPosition(), !isSuperClass.isChecked(), Double.parseDouble(_share.getText().toString()));
                                                    if (manager.addClass(newClass)) {
                                                        PersonsManager.getInstance(getThisContext()).updatePersonsWithClass(concernedClass, newClass);
                                                        toast("Class edited successfully");
                                                        dialog2.dismiss();
                                                        dialog1.dismiss();
                                                        MainActivity2.getInstance().updateListView();
                                                    } else {
                                                        toast("This name is already used");
                                                    }
                                                } else {
                                                    toast("Invalid name");
                                                }

                                            } else {
                                                toast("Failed to edit this Class");
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        }
                        case 2: {
                            final ClassManager manager = ClassManager.getInstance(getThisContext());
                            final List<String> list = manager.getClassArrayOfStrings();
                            list.addAll(manager.getSuperClassArrayOfStrings());
                            String[] str = new String[list.size()];
                            final ArrayList<Class> classList = new ArrayList<Class>();
                            new AlertDialog.Builder(getThisContext())
                                    .setMultiChoiceItems(list.toArray(str), null, new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                            if (b)
                                                classList.add(ClassManager.getInstance(getThisContext()).findClassByName(list.get(i)));
                                            else
                                                try {
                                                    classList.remove(classList.indexOf(ClassManager.getInstance(getThisContext()).findClassByName(list.get(i))));
                                                } catch (Exception ignored) {
                                                }
                                        }
                                    })
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            for (Class aClass : classList) {
                                                if (PersonsManager.getInstance(getThisContext()).getPersonsFromSuperClass(aClass) == null)
                                                    ClassManager.getInstance(getThisContext()).removeClass(aClass);
                                                else
                                                    toast("This SuperClass has children, delete them first");
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .setTitle("Delete Classes")
                                    .show();
                        }
                    }
                }
            });

            dialog.show();
            return true;

        } else if (id == R.id.action_calculate) {
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(findViewById(R.id.total).getWindowToken(), 0);
                Calculator.calculate(this, Double.parseDouble(((TextView) findViewById(R.id.total)).getText().toString()));
            } catch (NumberFormatException e) {
                toast("You inserted an invalid amount");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to leave?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        leave();
                    }
                })
                .setNegativeButton("No, stay", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void leave() {
        finish();
    }

    public void updateListView() {
        List list = PersonsManager.getInstance(this).getPersonsArray();
        listView.setAdapter(new mListView(getThisContext(), R.layout.tips_list_item, list));
        ((TextView) findViewById(R.id.count)).setText(PersonsManager.getInstance(getThisContext()).getPersonsArray().size() + "");
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tmp = PersonsManager.getInstance(getThisContext()).getPersonsArrayOfStrings().get(i);
                if (PersonsManager.getInstance(getThisContext()).removePersonAt(i)) {
                    toast(tmp + " removed");
                    return true;
                } else {
                    toast("Failed to remove " + tmp);
                    return false;
                }
            }
        });
    }

    public void showResults(final double total, double rest) {

        final List<Person> list = PersonsManager.getInstance(this).getPersonsArray();

        new AlertDialog.Builder(this)
                .setAdapter(new ResultsListAdapter(this, R.layout.results_listitem, list, total), null)
                .setTitle("Results - Total = " + total)
                .setCancelable(true)
                .setNeutralButton("Rest = " + rest, null)
                .show();

        updateListView();
    }

}
