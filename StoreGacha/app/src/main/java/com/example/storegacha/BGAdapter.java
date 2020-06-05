package com.example.storegacha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BGAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<Background>list_bg;


    public BGAdapter(Context context, int layout, ArrayList<Background> list_bg) {
        this.context = context;
        this.layout = layout;
       this.list_bg = list_bg;
    }

    @Override
    public int getCount() {
        return list_bg.size();
    }

    @Override
    public Object getItem(int position) {
        return list_bg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder
    {
        ImageView img;
        TextView txt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.background_item,null);
            viewHolder.img = convertView.findViewById(R.id.srcImage);
            viewHolder.txt = convertView.findViewById(R.id.nameImg);
            convertView.setTag(viewHolder);
        }
        else viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.img.setImageResource(list_bg.get(position).getImg());
        viewHolder.txt.setText(list_bg.get(position).name);
        return convertView;
    }
}
