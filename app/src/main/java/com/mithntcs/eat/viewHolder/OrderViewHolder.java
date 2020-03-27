package com.mithntcs.eat.viewHolder;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mithntcs.eat.InterFace.ItemClickListener;
import com.mithntcs.eat.R;


/**
 * Created by Mithlesh Kumar Sharma on 18,March,2020
 * NTCS Company
 */
public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderID,txtOrderStatus,txtOrderAddress,txtOrderPhone;
    public ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderStatus=(TextView)itemView.findViewById(R.id.order_status);
        txtOrderID=(TextView)itemView.findViewById(R.id.order_id);
        txtOrderAddress=(TextView)itemView.findViewById(R.id.order_address);
        txtOrderPhone=(TextView)itemView.findViewById(R.id.order_phone);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

       // itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
