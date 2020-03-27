package com.mithntcs.eat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mithntcs.eat.Common.Comman;
import com.mithntcs.eat.InterFace.ItemClickListener;
import com.mithntcs.eat.model.Category;
import com.mithntcs.eat.ui.AdminSignIn;
import com.mithntcs.eat.viewHolder.MenuViewHolderAdmin;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import info.hoang8f.widget.FButton;

public class AdminHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseDatabase database;
    DatabaseReference category;
    FirebaseStorage storage;
    StorageReference storageReference;

    TextView txtFullName;
    RecyclerView recyclerView_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category, MenuViewHolderAdmin> menu_adepter;

    MaterialEditText txt_menu_name;
    Button btn_select;
    FButton btn_upload;

    Category newCatagory;
    Uri saveUri;
    private final int PICK_IMAGE_REQUEST=71;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Management");
        setSupportActionBar(toolbar);

        //toolbar.setNavigationIcon(R.mipmap.ic_app_icon);
        //toolbar.setLogo(R.mipmap.ic_app_icon);

        database=FirebaseDatabase.getInstance();
        category=database.getReference("Category");

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                showAddMenuDialog();

            }
        });

        drawer = findViewById(R.id.drawer_layout);
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

    private void showAddMenuDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(AdminHome.this);
        builder.setTitle("Add new Category");
        builder.setMessage("please full fill information");

        LayoutInflater layoutInflater=this.getLayoutInflater();
        final View add_menu_view=layoutInflater.inflate(R.layout.add_new_menu,null);

        txt_menu_name=add_menu_view.findViewById(R.id.id_menu__name);
        btn_select=add_menu_view.findViewById(R.id.btn_select_img);
        btn_upload=add_menu_view.findViewById(R.id.btn_Upload);

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
            }
        });

        builder.setView(add_menu_view);
        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                if (newCatagory!=null){
                    category.push().setValue(newCatagory);
                    Snackbar.make(drawer, "New Category Added", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
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

    private void uploadImage() {

        if (saveUri!=null){

            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            String imgName= UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imgName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            Toast.makeText(AdminHome.this, "Uploaded !!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    newCatagory=new Category(txt_menu_name.getText().toString(),uri.toString());

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(AdminHome.this, " "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded"+progress+"%");


                }
            });
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null
                && data.getData()!=null){

            saveUri=data.getData();
            btn_select.setText("Image Selected !");

        }
    }

    private void selectImage() {

        Intent selectImageInt=new Intent();
        selectImageInt.setType("image/*");
        selectImageInt.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selectImageInt,"Select Picture"),PICK_IMAGE_REQUEST);

    }

    private void LoadMenu() {

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(category, Category.class)
                        .build();
        menu_adepter=
                new FirebaseRecyclerAdapter<Category, MenuViewHolderAdmin>
                        (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MenuViewHolderAdmin holder, int position, @NonNull final Category model) {


                        holder.textView_menu.setText(model.getName());
                        Picasso.get().load(model.getImage()).into(holder.imageView_menu);
                        final Category clickitem=model;
                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {

                                /*Intent food_list=new Intent(getApplicationContext(),Food_list.class);
                                food_list.putExtra("CategoryId",menu_adepter.getRef(position).getKey());
                                startActivity(food_list);*/
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public MenuViewHolderAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.menu_item, parent, false);
                        return new MenuViewHolderAdmin(view);
                    }
                } ;

        menu_adepter.startListening();
        recyclerView_menu.setAdapter(menu_adepter);
    }




    @Override
    public boolean onSupportNavigateUp() {
       /* NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();*/
        return true;
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

            //startActivity(new Intent(getApplicationContext(),Cart.class));

        } else if (id == R.id.nav_Orders) {
            //  startActivity(new Intent(getApplicationContext(),OrderStatus.class));

        }else if (id == R.id.nav_log_out) {

            Intent intentLogOut=new Intent(getApplicationContext(), AdminSignIn.class);
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Comman.UPDATE)){
            showUpdateDialog(menu_adepter.getRef(item.getOrder()).getKey(),menu_adepter.getItem(item.getOrder()));

        }
        if (item.getTitle().equals(Comman.DELETE)){

            deleteCategory(menu_adepter.getRef(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {

        category.child(key).removeValue();
        Toast.makeText(this, "Items deleted !!", Toast.LENGTH_SHORT).show();

    }

    private void showUpdateDialog(final String key, final Category item) {

        AlertDialog.Builder builder=new AlertDialog.Builder(AdminHome.this);
        builder.setTitle("Update Category");
        builder.setMessage("please full fill information");

        LayoutInflater layoutInflater=this.getLayoutInflater();
        final View add_menu_view=layoutInflater.inflate(R.layout.add_new_menu,null);

        txt_menu_name=add_menu_view.findViewById(R.id.id_menu__name);
        btn_select=add_menu_view.findViewById(R.id.btn_select_img);
        btn_upload=add_menu_view.findViewById(R.id.btn_Upload);

        txt_menu_name.setText(item.getName());

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeImage(item);
            }
        });

        builder.setView(add_menu_view);
        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                item.setName(txt_menu_name.getText().toString());
                category.child(key).setValue(item);
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

    private void changeImage(final Category item) {


        if (saveUri!=null){

            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            String imgName= UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imgName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            Toast.makeText(AdminHome.this, "Uploaded !!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Toast.makeText(AdminHome.this, " "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded"+progress+"%");


                }
            });
        }

    }
}