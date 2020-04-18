package com.example.sudohelp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.List;

public class arrayAdapter extends ArrayAdapter<Cards> {
    Context context;

    public arrayAdapter(Context context, int resourceId, List<Cards> items) {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Cards card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.subj2);
        TextView date = (TextView)  convertView.findViewById(R.id.date2);
        TextView loc = (TextView)  convertView.findViewById(R.id.loc2);
        TextView lang = (TextView)  convertView.findViewById(R.id.lang2);
        TextView status = (TextView) convertView.findViewById(R.id.status);
        name.setText(card_item.getSubject());
        date.setText(card_item.getDate());
        loc.setText(card_item.getLocation());
        lang.setText(card_item.getLanguage());
        status.setText(card_item.getStatus());
        return convertView;
    }
}
