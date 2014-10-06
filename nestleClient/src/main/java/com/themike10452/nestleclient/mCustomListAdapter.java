package com.themike10452.nestleclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mike on 6/17/2014.
 */
public class mCustomListAdapter extends ArrayAdapter {

    private Context context;
    private List list;

    public mCustomListAdapter(Context context, int resource, List<?> items) {
        super(context, resource, items);
        this.context = context;
        list = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listviewitem, null);
        }

        if (convertView != null) {
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            Button button1 = (Button) convertView.findViewById(R.id.button1);
            textView.setText(list.get(position) + "");
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context)
                            .setCancelable(true)
                            .setTitle((String) list.get(position))
                            .setMessage(String.format("How to load values from %s ?", list.get(position)))
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setNegativeButton("Load all values", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MainActivity mainInstance = MainActivity.getInstance();
                                    mainInstance.updateView((String) list.get(position));
                                    mainInstance.calculate();
                                }
                            })
                            .setPositiveButton("Continue from this session", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MainActivity mainInstance = MainActivity.getInstance();
                                    mainInstance.clearFields();
                                    mainInstance.resumeFromSession((String) list.get(position));
                                    mainInstance.calculate();
                                }
                            })
                            .show();
                }
            });
        }
        return convertView;
    }
}
