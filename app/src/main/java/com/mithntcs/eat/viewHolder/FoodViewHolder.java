package com.mithntcs.eat.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mithntcs.eat.InterFace.ItemClickListener;
import com.mithntcs.eat.R;


/**
 * Created by Mithlesh Kumar Sharma on 15,March,2020
 * NTCS Company
 */
public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textView_food;
    public ImageView imageView_food;
    private ItemClickListener itemClickListener;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        textView_food=(TextView)itemView.findViewById(R.id.food_name);
        imageView_food=(ImageView)itemView.findViewById(R.id.image_food);
        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
