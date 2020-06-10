package com.project.arcadedestroyer.Controller;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.arcadedestroyer.Method.Item;
import com.project.arcadedestroyer.R;

import java.util.ArrayList;

class StoreItemAdapter extends BaseAdapter {

    ArrayList<Item> StoreItemList;

    public StoreItemAdapter(ArrayList<Item> itemList){
        this.StoreItemList = itemList;
    }

    @Override
    public int getCount() {
        //Cần trả về số phần tử mà ListView hiện thị
        return StoreItemList.size();
    }

    @Override
    public Object getItem(int position) {
        //Cần trả về đối tượng dữ liệu phần tử ở vị trí position
        return StoreItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID liên quan đến phần tử ở vị trí position
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View hiện thị phần tử, nếu là null cần tạo mới
        //(có thể nạp từ layout bằng View.inflate)

        //Cuối cùng là gán dữ liệu ở vị trí possition vào View và trả về đối
        //tượng View này

        View viewProduct;
        if (convertView == null) {
            viewProduct = View.inflate(parent.getContext(), R.layout.listview_item_layout, null);
        } else viewProduct = convertView;

        //Bind sữ liệu phần tử vào View
        Item product = (Item) getItem(position);
        ((ImageView) viewProduct.findViewById(R.id.item_thumb)).setBackgroundResource(product.getResource());
        ((TextView) viewProduct.findViewById(R.id.item_name)).setText(product.getName());

        return viewProduct;
    }
}