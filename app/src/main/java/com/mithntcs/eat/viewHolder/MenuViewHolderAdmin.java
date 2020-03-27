package com.mithntcs.eat.viewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.service.Common;
import com.mithntcs.eat.Common.Comman;
import com.mithntcs.eat.InterFace.ItemClickListener;
import com.mithntcs.eat.R;

/**
 * Created by Mithlesh Kumar Sharma on 23,March,2020
 * NTCS Company
 */
public class MenuViewHolderAdmin extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView textView_menu;
    public ImageView imageView_menu;
    private ItemClickListener itemClickListener;

    public MenuViewHolderAdmin(@NonNull View itemView) {
        super(itemView);
        textView_menu = (TextView) itemView.findViewById(R.id.menu_name);
        imageView_menu = (ImageView) itemView.findViewById(R.id.image_menu);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Select the action");
        menu.add(0,0,getAdapterPosition(), Comman.UPDATE);
        menu.add(0,1,getAdapterPosition(), Comman.DELETE);

    }
}