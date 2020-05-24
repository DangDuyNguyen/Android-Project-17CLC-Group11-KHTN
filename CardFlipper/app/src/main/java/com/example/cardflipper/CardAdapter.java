package com.example.cardflipper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends BaseAdapter {
    private Context context;
    private List<Card> deck;
    int column_width , column_height ;
    public  ArrayList<Boolean> itemClickable = new ArrayList<Boolean>();

    public CardAdapter(Context context, List<Card> deck, int WIDTH, int HEIGHT) {
        this.context = context;
        this.deck = deck;
        column_height = HEIGHT;
        column_width = WIDTH;
        for (int i = 0; i < this.deck.size();i++)
            itemClickable.add(true);
    }

    @Override
    public int getCount() {
        return deck.size();
    }

    @Override
    public Object getItem(int position) {
        return deck.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView img;
        LinearLayout lin;
    }

    @Override
    public boolean areAllItemsEnabled() {
        if (itemClickable.contains(true))
            return false;
        else return true;
    }


    @Override
    public boolean isEnabled(int position) {
        return itemClickable.get(position);
    }

    public void setItemClickable(int position,Boolean typeValue){
        itemClickable.set(position,typeValue);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.card_layout,null);
            convertView.setMinimumHeight(column_height);
            viewHolder.img = convertView.findViewById(R.id.card);
            viewHolder.lin = convertView.findViewById(R.id.lin);
            convertView.setTag(viewHolder);
        }
        else viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.img.setImageResource(deck.get(position).DownCard());
        android.widget.AbsListView.LayoutParams parms = new android.widget.AbsListView.LayoutParams(column_width, column_height);
        viewHolder.lin.setLayoutParams(parms);
        return convertView;
    }
}
