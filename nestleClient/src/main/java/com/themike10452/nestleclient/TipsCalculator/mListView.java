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
 * Created by Mike on 6/22/2014.
 */
public class mListView extends ArrayAdapter<Person> {

    private static List<Person> list;
    private static mListView instance;

    public mListView(Context context, int resource, List<Person> objects) {
        super(context, resource, objects);
        list = objects;
        instance = this;
    }

    public static mListView getInstance() {
        return instance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tips_list_item, parent, false);

        ((TextView) convertView.findViewById(R.id._name)).setText((list.get(position)).getName());


        if (list.get(position).getSuperClass().isByPercentage()) {

            ((TextView) convertView.findViewById(R.id._pos)).setText(
                    list.get(position).getSuperClass().getName() +
                            " (" + list.get(position).getShare() + "%)"
            );

        } else {

            ((TextView) convertView.findViewById(R.id._pos)).setText(
                    list.get(position).getSuperClass().getName() +
                            " (" + list.get(position).getShare() + ")"
            );

        }

        if (list.get(position).getCalculatedShare() >= 0) {
            ((TextView) convertView.findViewById(R.id.share)).setText(list.get(position).getCalculatedShare() + "");
        }

        int[] colors = new int[]{R.color.c1, R.color.c2, R.color.c3, R.color.c4};
        try {
            convertView.setBackgroundResource(colors[list.get(position).getLevel()]);
        } catch (Exception ignored) {
        }

        return convertView;
    }

    public void clearResults() {

    }

}
