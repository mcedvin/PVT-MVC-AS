package com.example.edvin.app.map;

import android.app.Activity;
import android.util.Log;
import android.widget.BaseAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edvin.app.R;

import java.util.List;

public class ReportAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> imageIDs;
    private List<String> titles;
    private String TAG = "ReportAdapter";

    public ReportAdapter(Context context, List<Integer> imageIDs, List<String> titles) {
        this.context = context;
        this.imageIDs = imageIDs;
        this.titles = titles;

        Log.d(TAG, titles.toString());
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
        View item = null;


        if (convertView == null) {


            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(R.layout.report_listview_item, parent, false);


        } else {
            item = (View) convertView;
        }

        ImageView imageView = (ImageView) item.findViewById(R.id.listview_image_report);
        TextView title = (TextView) item.findViewById(R.id.listview_item_title_report);

        imageView.setImageResource(imageIDs.get(position));
        title.setText(titles.get(position));

        return item;
    }

    public List<Integer> getImageIDs() {
        return imageIDs;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setImageIDs(List<Integer> imageIDs) {
        this.imageIDs = imageIDs;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }
}




