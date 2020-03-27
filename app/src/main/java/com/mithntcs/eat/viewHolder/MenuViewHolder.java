package com.mithntcs.eat.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mithntcs.eat.InterFace.ItemClickListener;
import com.mithntcs.eat.R;


/**
 * Created by Mithlesh Kumar Sharma on 14,March,2020
 * NTCS Company
 */
public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textView_menu;
    public ImageView imageView_menu;
    private ItemClickListener itemClickListener;
    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        textView_menu=(TextView)itemView.findViewById(R.id.menu_name);
        imageView_menu=(ImageView)itemView.findViewById(R.id.image_menu);
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
