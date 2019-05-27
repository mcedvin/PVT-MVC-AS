package com.example.edvin.app.map;

import android.widget.BaseAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edvin.app.R;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> imageIDs;
    private List<String> titles;
    private List<String> descriptions;

    public ReportAdapter(Context context, List<Integer> imageIDs, List<String> titles, List<String> descriptions) {
        this.context = context;
        this.imageIDs = imageIDs;
        this.titles = titles;
        this.descriptions = descriptions;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            item = new View(context);
            item = inflater.inflate(R.layout.report_listview_item, null);
            ImageView imageView = (ImageView) item.findViewById(R.id.listview_image_report);
            TextView title = (TextView) item.findViewById(R.id.listview_item_title_report);
            TextView description = (TextView) item.findViewById(R.id.listview_item_short_description_report);

            imageView.setImageResource(imageIDs.get(position));
            title.setText(titles.get(position));
            description.setText(descriptions.get(position));

        } else {
            item = (View) convertView;
        }

        return item;
    }
}




