package com.mithntcs.eat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mithntcs.eat.InterFace.ItemClickListener;
import com.mithntcs.eat.model.Food;
import com.mithntcs.eat.viewHolder.FoodViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Food_list extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference food_list;
    RecyclerView recyclerView_food;
    RecyclerView.LayoutManager layoutManager;
    String categoryId="";
    FirebaseRecyclerAdapter<Food, FoodViewHolder> food_adepter;
    //for search
    FirebaseRecyclerAdapter<Food, FoodViewHolder> search_food_adepter;
    List<String> suggestList=new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        database=FirebaseDatabase.getInstance();
        food_list=database.getReference("Food");

        //Load menu
        recyclerView_food=(RecyclerView)findViewById(R.id.recycler_food);
        recyclerView_food.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView_food.setLayoutManager(layoutManager);

        if (getIntent()!=null)
            categoryId=getIntent().getStringExtra("CategoryId");

        if (!(categoryId.isEmpty()) && categoryId!=null) {

            LoadFoodList(categoryId);
            //Toast.makeText(this, "cat"+categoryId, Toast.LENGTH_SHORT).show();
        }

        //for search view
        materialSearchBar=(MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter Your Food");

        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                List<String> suggest=new ArrayList<>();
                for(String search:suggestList){

                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                    materialSearchBar.setLastSuggestions(suggest);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                if (!enabled)
                    recyclerView_food.setAdapter(food_adepter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {

        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(food_list.orderByChild("Name").equalTo(text.toString()), Food.class)
                        .build();
        search_food_adepter=
                new FirebaseRecyclerAdapter<Food, FoodViewHolder>
                        (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {

                        holder.textView_food.setText(model.getName());
                        Picasso.get().load(model.getImage()).into(holder.imageView_food);
                        final Food local=model;
                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                Toast.makeText(Food_list.this, ""+local.getName(), Toast.LENGTH_SHORT).show();
                                Intent foodDetails=new Intent(getApplicationContext(),FoodDetails.class);
                                foodDetails.putExtra("FoodID",search_food_adepter.getRef(position).getKey());//send food id to new activity
                                startActivity(foodDetails);

                            }
                        });
                    }
                    @NonNull
                    @Override
                    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.food_items, parent, false);
                        return new FoodViewHolder(view);
                    }
                } ;

        Log.d("TAG", String.valueOf(search_food_adepter.getItemCount()));
        search_food_adepter.startListening();
        recyclerView_food.setAdapter(search_food_adepter);

    }

    private void loadSuggest() {

        food_list.orderByChild("MenuID").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Food item=snapshot.getValue(Food.class);
                            suggestList.add(item.getName());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void LoadFoodList(String categoryId) {

        // Toast.makeText(this, "cat"+categoryId, Toast.LENGTH_SHORT).show();

        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(food_list.orderByChild("MenuID").equalTo(categoryId), Food.class)
                        .build();
        food_adepter=
                new FirebaseRecyclerAdapter<Food, FoodViewHolder>
                        (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {

                        holder.textView_food.setText(model.getName());
                        Picasso.get().load(model.getImage()).into(holder.imageView_food);
                        final Food local=model;
                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                Toast.makeText(Food_list.this, ""+local.getName(), Toast.LENGTH_SHORT).show();
                                Intent foodDetails=new Intent(getApplicationContext(),FoodDetails.class);
                                foodDetails.putExtra("FoodID",food_adepter.getRef(position).getKey());//send food id to new activity
                                startActivity(foodDetails);

                            }
                        });
                    }
                    @NonNull
                    @Override
                    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.food_items, parent, false);
                        return new FoodViewHolder(view);
                    }
                } ;

        Log.d("TAG", String.valueOf(food_adepter.getItemCount()));
        food_adepter.startListening();
        recyclerView_food.setAdapter(food_adepter);

    }
}
