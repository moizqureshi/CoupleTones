package com.example.moizqureshi.coupletones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

    /*
        Adapter for dynamically constructing the listview
     */
public class MyListAdapter extends ArrayAdapter<String> {

    private int layout;
    private List<String> myObjects;

    public MyListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        myObjects = objects;
        layout = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mainViewHolder = null;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
            viewHolder.button = (Button) convertView.findViewById(R.id.list_item_botton);
            convertView.setTag(viewHolder);
        }

        mainViewHolder = (ViewHolder) convertView.getTag();
        mainViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Button was clicked for list item " + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });
        mainViewHolder.title.setText(getItem(position));

        return convertView;
    }

    protected class ViewHolder {

        TextView title;
        Button button;
    }
}