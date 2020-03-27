package com.mithntcs.eat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mithntcs.eat.AdminHome;
import com.mithntcs.eat.Common.Comman;
import com.mithntcs.eat.Home;
import com.mithntcs.eat.R;
import com.mithntcs.eat.model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;

public class AdminSignIn extends AppCompatActivity {

    private FButton btn_signin;
    private MaterialEditText editText_phone, editText_password;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_in);
        intiView();
    }

    private void intiView() {

        btn_signin = (FButton) findViewById(R.id.btn_signin);
        editText_phone = (MaterialEditText) findViewById(R.id.id_mobile_nbr);
        editText_password = (MaterialEditText) findViewById(R.id.id_password);


        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate_fiels()) {
                    signInUser(editText_phone.getText().toString(), editText_password.getText().toString());

                }
            }
        });

    }

    private void signInUser(String phone, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Waiting...");
        progressDialog.show();

        final String localPhone = phone;
        final String localPassword = password;

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(localPhone).exists()) {

                    progressDialog.dismiss();
                    User user = dataSnapshot.child(localPhone).getValue(User.class);
                    assert user != null;
                    user.setPhone(localPhone);
                    if (Boolean.parseBoolean(user.getIsStaff())) { //if is Staff==true

                        if (user.getPassword().equals(localPassword)) {
                            Comman.currentUser = user;
                            startActivity(new Intent(getApplicationContext(), AdminHome.class));
                            finish();
                        } else
                            Toast.makeText(AdminSignIn.this, "Wrong Password !", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(AdminSignIn.this, "Please Login With Staff Account !", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    progressDialog.dismiss();
                    Toast.makeText(AdminSignIn.this, "User does not exist in Database ", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    boolean validate_fiels() {

        String phone = editText_phone.getText().toString();
        String password = editText_password.getText().toString();

        if (phone.trim().length() > 0 && password.trim().length() > 0) {


            /*if(phone.equals("admin") && password.equals("admin")){


                session.createUserLoginSession("Android Example",
                        "androidexample84@gmail.com");

                // Starting MainActivity
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                finish();
                return true;

            }else{

                // username / password doesn't match&
                Toast.makeText(getApplicationContext(),
                        "Username/Password is incorrect",
                        Toast.LENGTH_LONG).show();
                return false;
            }*/
            return true;
        } else {

            // user didn't entered username or password
            Toast.makeText(getApplicationContext(),
                    "Please enter username and password",
                    Toast.LENGTH_LONG).show();
            return false;
        }


    }
}
