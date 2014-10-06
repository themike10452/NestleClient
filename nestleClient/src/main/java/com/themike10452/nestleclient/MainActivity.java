package com.themike10452.nestleclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.themike10452.nestleclient.TipsCalculator.MainActivity2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity {

    public static MainActivity instance;
    public static SQLiteDatabase database;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private Button save1;
    private String date;
    private EditText editText_id4, editText_id6, editText_id7, editText_id8;
    private EditText[] oEditTexts, nEditTexts;
    private final View.OnClickListener save1OnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            final String[] ids = {date + "b4", date + "b6", date + "b7", date + "b8"};
            final EditText[] oeditTexts = oEditTexts;
            final EditText[] neditTexts = nEditTexts;
            boolean allGood = true;
            for (View v : neditTexts)
                if (!determine(v, false)) {
                    allGood = false;
                    break;
                }
            if (allGood)
                for (int i = 0; i < ids.length; i++)
                    try {
                        String[] data = new String[]{ids[i], date, oeditTexts[i].getText().toString(), neditTexts[i].getText().toString(), "null"};
                        mSQLiteOpenHelper.insertIntoTable(database, mSQLiteOpenHelper.NESTLE_TABLE_NAME, data);
                        Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                    } catch (SQLiteConstraintException sce) {
                        final int j = i;
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Data already exists")
                                .setMessage(oeditTexts[i].getTag().toString().split("\\|")[0].split(":")[0] + " is already defined. Do you want to overwrite the currenrt data?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int ind) {
                                        String s1, s2;
                                        int i;
                                        try {
                                            i = Integer.parseInt(s2 = neditTexts[j].getText().toString()) - Integer.parseInt(s1 = oeditTexts[j].getText().toString());
                                            mSQLiteOpenHelper.updateTable(database, mSQLiteOpenHelper.NESTLE_TABLE_NAME, ids[j], new String[]{date, s1, s2, Integer.toString(i)});
                                        } catch (NumberFormatException e) {
                                            s1 = oeditTexts[j].getText().toString();
                                            s2 = neditTexts[j].getText().toString();
                                            mSQLiteOpenHelper.updateTable(database, mSQLiteOpenHelper.NESTLE_TABLE_NAME, ids[j], new String[]{date, s1, s2, "null"});
                                        }
                                    }
                                })
                                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
        }
    };
    private boolean firstLaunch = true;
    private EditText editText_id4n, editText_id6n, editText_id7n, editText_id8n;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;
    }

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            if (android.os.Build.VERSION.SDK_INT >= 18)
                getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        } catch (Exception ignoredException) {
        } catch (Error ignoredError) {
        }

        SQLiteOpenHelper helper = new mSQLiteOpenHelper(this);
        database = helper.getWritableDatabase();

        editText_id4 = (EditText) findViewById(R.id.editText_id4);
        editText_id6 = (EditText) findViewById(R.id.editText_id6);
        editText_id7 = (EditText) findViewById(R.id.editText_id7);
        editText_id8 = (EditText) findViewById(R.id.editText_id8);

        editText_id4n = (EditText) findViewById(R.id.editText_id4n);
        editText_id6n = (EditText) findViewById(R.id.editText_id6n);
        editText_id7n = (EditText) findViewById(R.id.editText_id7n);
        editText_id8n = (EditText) findViewById(R.id.editText_id8n);

        oEditTexts = new EditText[]{editText_id4, editText_id6, editText_id7, editText_id8};
        nEditTexts = new EditText[]{editText_id4n, editText_id6n, editText_id7n, editText_id8n};

        for (EditText et : nEditTexts) {
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    determine(view, b);
                }
            });
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    determine(textView, false);
                    return false;
                }
            });
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name);

        String[] segments = {"Nestlé Client", "Tips Calculator", "Empty"};

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.navigation_drawer_item, segments));
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(getActivity(), MainActivity2.class));
                        break;
                    case 2:

                }
            }
        });

        save1 = (Button) findViewById(R.id.button_save1);
        save1.setOnClickListener(save1OnClickListener);
        updateView(null);
        calculate();
        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        final ArrayList<String> list0 = mSQLiteOpenHelper.getListOfDays(database);
        spinner1.setAdapter(new mCustomListAdapter(this, android.R.layout.simple_spinner_dropdown_item, list0));
        { // automatically choose the best day to resume a session from
            List<String> ls = mSQLiteOpenHelper.getListOfDays(database);
            if (ls.indexOf(date) > 0)
                spinner1.setSelection(ls.indexOf(date) - 1);
            else
                spinner1.setSelection(ls.size() - 1);
        }
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
                .setNegativeButton("No, stay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    protected void leave() {
        super.onBackPressed();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public void updateView(String localDate) {
        TextView date = (TextView) findViewById(R.id.textView_date);
        date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()).trim());
        this.date = new SimpleDateFormat("ddMMyyyy").format(new Date()).trim();
        ArrayList<String> list = (localDate == null) ? mSQLiteOpenHelper.selectFromTable(database, mSQLiteOpenHelper.NESTLE_TABLE_NAME, this.date + "__") : mSQLiteOpenHelper.selectFromTable(database, mSQLiteOpenHelper.NESTLE_TABLE_NAME, localDate + "__");
        if (list.size() > 0)
            for (int i = 0; i < nEditTexts.length; i++) {
                String[] s = (list.get(i)).split(":");
                if (!s[0].equals("null"))
                    oEditTexts[i].setText(s[0]);
                if (!s[1].equals("null"))
                    nEditTexts[i].setText(s[1]);
            }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        int id = item.getItemId();
        switch (id) {
            case R.id.action_accept:
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(findViewById(R.id.relativeLayout_Buffet).getWindowToken(), 0);
                calculate();
                break;
            case R.id.action_discard:
                new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setTitle("Discard")
                        .setMessage("Clear all entries?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                clearFields();
                                calculate();
                            }
                        })
                        .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void display(View view, int i) {
        try {
            TextView res = (TextView) (findViewById(R.id.relativeLayout_BuffetResults)).findViewWithTag(view.getTag().toString().trim().split("\\|")[0]);
            res.setText(Integer.toString(i));
        } catch (NullPointerException ignored) {
        }
    }

    private void display(View view, String msg) {
        try {
            TextView res = (TextView) (findViewById(R.id.relativeLayout_BuffetResults)).findViewWithTag(view.getTag().toString().trim().split("\\|")[0]);
            res.setText(msg);
        } catch (NullPointerException ignored) {
        }
    }

    private boolean determine(View view, boolean focused) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout_Buffet);
        EditText subj = (EditText) view;
        String[] tag = view.getTag().toString().trim().split("\\|");
        EditText comp = (EditText) relativeLayout.findViewWithTag(tag[1] + "|" + tag[0]);

        if (!focused) {
            int i;
            try {
                i = Integer.parseInt(comp.getText().toString()) - Integer.parseInt(subj.getText().toString());
            } catch (NumberFormatException e) {
                i = 0;
            }

            if (i > 0 && subj.getText().toString().length() > 0) {
                Toast.makeText(getApplicationContext(), "Wrong value " + tag[0].split(":")[0], Toast.LENGTH_SHORT).show();
                ((EditText) view).setText("");
                display(view, "?");
                return false;
            } else {
                if (comp.getText().length() == 0) {
                    subj.setText("");
                    display(view, "");
                } else if (subj.getText().toString().length() > 0)
                    display(view, -i);
                return true;
            }
        } else {
            if (comp.getText().toString().length() < 1) {
                if (view != null)
                    view.clearFocus();
                return true;
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            return true;
            //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void clearFields() {
        for (TextView v : nEditTexts)
            v.setText("");
        for (TextView v : oEditTexts)
            v.setText("");
    }

    public void calculate() {
        for (View v : nEditTexts)
            determine(v, false);
    }

    public void resumeFromSession(String date) {
        ArrayList<String> list = mSQLiteOpenHelper.selectFromTable(database, mSQLiteOpenHelper.NESTLE_TABLE_NAME, date + "__");
        try {
            clearFields();
            for (int i = 0; i < oEditTexts.length; i++)
                oEditTexts[i].setText(list.get(i).split(":")[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    private Activity getActivity() {
        return this;
    }
}
