package com.mithntcs.eat;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mithntcs.eat.Common.Comman;
import com.mithntcs.eat.InterFace.ItemClickListener;
import com.mithntcs.eat.model.Category;
import com.mithntcs.eat.ui.SignIn;
import com.mithntcs.eat.viewHolder.MenuViewHolder;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseDatabase database;
    DatabaseReference category;
    TextView txtFullName;
    RecyclerView recyclerView_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> menu_adepter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //toolbar.setNavigationIcon(R.mipmap.ic_app_icon);
        //toolbar.setLogo(R.mipmap.ic_app_icon);

        database=FirebaseDatabase.getInstance();
        category=database.getReference("Category");



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(getApplicationContext(),Cart.class));

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_Menu, R.id.nav_cart, R.id.nav_Orders,
                R.id.nav_log_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/





        //set name for user
        View headerView=navigationView.getHeaderView(0);
        txtFullName=(TextView)headerView.findViewById(R.id.txt_fullName);
        txtFullName.setText(Comman.currentUser.getName());

        //Load menu
        recyclerView_menu=(RecyclerView)findViewById(R.id.rec_menu);
        recyclerView_menu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView_menu.setLayoutManager(layoutManager);

        LoadMenu();
    }

    private void LoadMenu() {

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(category, Category.class)
                        .build();
        menu_adepter=
                new FirebaseRecyclerAdapter<Category, MenuViewHolder>
                        (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull final Category model) {


                        holder.textView_menu.setText(model.getName());
                        Picasso.get().load(model.getImage()).into(holder.imageView_menu);
                        final Category clickitem=model;
                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                // Toast.makeText(Home.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();
                                Intent food_list=new Intent(getApplicationContext(),Food_list.class);
                                food_list.putExtra("CategoryId",menu_adepter.getRef(position).getKey());
                                //Toast.makeText(getApplicationContext(), "cat"+menu_adepter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                                startActivity(food_list);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.menu_item, parent, false);
                        return new MenuViewHolder(view);
                    }
                } ;

        menu_adepter.startListening();
        recyclerView_menu.setAdapter(menu_adepter);
    }




    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {

       /* NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(menuItem, navController)
                || super.onOptionsItemSelected(menuItem);*/

        super.onOptionsItemSelected(menuItem);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        if (id == R.id.nav_cart) {

            startActivity(new Intent(getApplicationContext(),Cart.class));

        } else if (id == R.id.nav_Orders) {
            startActivity(new Intent(getApplicationContext(),OrderStatus.class));

        } else if (id == R.id.nav_log_out) {

            Intent intentLogOut=new Intent(getApplicationContext(), SignIn.class);
            intentLogOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentLogOut);
        }

        DrawerLayout drawer =
                (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =
                (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
            super.onBackPressed();
        }
    }
}
