package com.example.hmshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hmshop.R;
import com.example.hmshop.model.Product;

import java.util.List;

public class ProductAdapter  extends BaseAdapter {
    List<Product> array;
    Context context;

    public ProductAdapter(Context context, List<Product> array) {

        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public class ViewHolder{
        TextView producttxt;
        ImageView imageimg;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_product,null);
            viewHolder.producttxt = view.findViewById(R.id.item_nameproduct);
            viewHolder.imageimg = view.findViewById(R.id.item_image);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();


        }
        viewHolder.producttxt.setText(array.get(i).getNameproduct());
        Glide.with(context).load(array.get(i).getImage()).into(viewHolder.imageimg);


        return view;
    }
}
