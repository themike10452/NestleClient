package com.themike10452.nestleclient.TipsCalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.themike10452.nestleclient.R;

import java.util.List;

/**
 * Created by Mike on 6/30/2014.
 */
public class ResultsListAdapter extends ArrayAdapter<Person> {

    private static final String au = " a.u";
    private List<Person> objects;
    private double TOTAL;

    public ResultsListAdapter(Context context, int resource, List<Person> objects, final double total) {
        super(context, resource, objects);
        this.objects = objects;
        TOTAL = total;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.results_listitem, parent, false);

        ((TextView) convertView.findViewById(R.id.NAME)).setText(objects.get(position).getName());
        ((TextView) convertView.findViewById(R.id.PERC)).setText("~" + Calculator.round(objects.get(position).getCalculatedShare() / TOTAL * 100) + "%");
        ((TextView) convertView.findViewById(R.id.AMOUNT)).setText(objects.get(position).getCalculatedShare() + au);

        return convertView;
    }
}
