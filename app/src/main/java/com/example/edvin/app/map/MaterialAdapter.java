package com.example.edvin.app.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edvin.app.R;
import com.example.edvin.app.models.Material;

public class MaterialAdapter extends BaseAdapter {

    private Context context;
    private final int[] imageID;

    public MaterialAdapter(Context context, int[] imageID) {
        this.context = context;
        this.imageID = imageID;

    }


    @Override
    public int getCount() {
        return imageID.length;
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
        View grid;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(context);
            grid = inflater.inflate(R.layout.material_grid_item, null);
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
            imageView.setImageResource(imageID[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}



