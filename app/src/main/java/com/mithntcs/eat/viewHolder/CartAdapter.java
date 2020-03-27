package com.mithntcs.eat.viewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.mithntcs.eat.InterFace.ItemClickListener;
import com.mithntcs.eat.R;
import com.mithntcs.eat.model.Order;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mithlesh Kumar Sharma on 16,March,2020
 * NTCS Company
 */

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView txt_cartName,item_price;
    ImageView imgcartCount;

    ItemClickListener itemClickListener;

    public void setTxt_cartName(TextView txt_cartName) {
        this.txt_cartName = txt_cartName;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cartName=(TextView)itemView.findViewById(R.id.cart_item_name);
        item_price=(TextView)itemView.findViewById(R.id.cart_item_price);
        imgcartCount=(ImageView) itemView.findViewById(R.id.cart_item_count);

    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listdata=new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.cart_layout,parent,false);


        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        TextDrawable textDrawable=TextDrawable.builder().buildRound(""+listdata.get(position).getQuantity(), Color.RED);
                holder.imgcartCount.setImageDrawable(textDrawable);

        Locale locale=new Locale("en","US");
        NumberFormat numberFormat=NumberFormat.getNumberInstance(locale);
        int price=(Integer.parseInt(listdata.get(position).getPrice()))*(Integer.parseInt(listdata.get(position).getQuantity()));
        holder.item_price.setText("Rs."+numberFormat.format(price));
        holder.txt_cartName.setText(listdata.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }
}
