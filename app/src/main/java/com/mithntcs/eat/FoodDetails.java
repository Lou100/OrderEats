package com.mithntcs.eat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mithntcs.eat.Database.Database;
import com.mithntcs.eat.model.Food;
import com.mithntcs.eat.model.Order;
import com.squareup.picasso.Picasso;

public class FoodDetails extends AppCompatActivity {

    TextView food_name,food_price,food_dis;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberBtn;

    String foodID="";

    FirebaseDatabase database;
    DatabaseReference food_details;
    Food food;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        database=FirebaseDatabase.getInstance();
        food_details=database.getReference("Food");

        food_name=(TextView)findViewById(R.id.food_names);
        food_price=(TextView)findViewById(R.id.food_price);
        food_dis=(TextView)findViewById(R.id.food_description);
        food_image=(ImageView)findViewById(R.id.img_food);

        btnCart=(FloatingActionButton)findViewById(R.id.btnCart);


        numberBtn=(ElegantNumberButton)findViewById(R.id.number_btn);

        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsingAppbar);



        if (getIntent()!=null)
            foodID=getIntent().getStringExtra("FoodID");

        if (!(foodID.isEmpty()) && foodID!=null) {

            getFoodDetails(foodID);

        }
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Database(getBaseContext()).addToCart(new Order(
                        foodID,
                        food.getName(),
                        numberBtn.getNumber(),
                        food.getPrice(),
                        food.getDiscount())
                );

                Toast.makeText(FoodDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getFoodDetails(String foodID) {

        food_details.child(foodID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                food=dataSnapshot.getValue(Food.class);
                //set Image
                assert food != null;
                Picasso.get().load(food.getImage()).into(food_image);

                collapsingToolbarLayout.setTitle(food.getName());

                food_price.setText(food.getPrice());
                food_name.setText(food.getName());
                food_dis.setText(food.getDiscription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
