package com.mithntcs.eat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mithntcs.eat.Common.Comman;
import com.mithntcs.eat.Database.Database;
import com.mithntcs.eat.model.Order;
import com.mithntcs.eat.model.Request;
import com.mithntcs.eat.viewHolder.CartAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    FButton btnPlace;

    List<Order> cart=new ArrayList<>();
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        txtTotalPrice=(TextView)findViewById(R.id.total);
        btnPlace=(FButton)findViewById(R.id.btn_placeOrder);

        recyclerView=(RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (!(txtTotalPrice.getText().equals("Rs.0"))){
                   showAlertDialog();
               }else {
                   Toast.makeText(Cart.this, "First add a Cart", Toast.LENGTH_SHORT).show();
               }
            }
        });
        loadListFood();
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(Cart.this);
        builder.setTitle("One more step!");
        builder.setMessage("Enter your address: ");

        final EditText edtAddress=new EditText(Cart.this);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        builder.setView(edtAddress); //add editText to alert dialog;
        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Request request=new Request(Comman.currentUser.getPhone()
                        ,Comman.currentUser.getName(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart);
                //submit to firebase
                //we will using System.currentMilli to key
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                //delete cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank you,Order Place", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void loadListFood() {

        cart=new Database(this).getCarts();
        cartAdapter=new CartAdapter(cart,this);
        recyclerView.setAdapter(cartAdapter);

        int totalPrice=0;
        for (Order order:cart)
            totalPrice+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale=new Locale("en","US");
        NumberFormat numberFormat=NumberFormat.getNumberInstance(locale);

        txtTotalPrice.setText(String.format("%s%s", getString(R.string.rs), numberFormat.format(totalPrice)));
    }
}
