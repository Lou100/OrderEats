package com.mithntcs.eat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mithntcs.eat.Common.Comman;
import com.mithntcs.eat.Home;
import com.mithntcs.eat.R;
import com.mithntcs.eat.model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;

public class SignIn extends AppCompatActivity {

    private FButton btn_signin;
    private TextView txt_signup;
    private MaterialEditText editText_phone,editText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        intiView();
    }

    private void intiView() {

        btn_signin=(FButton)findViewById(R.id.btn_signin);
        txt_signup=(TextView)findViewById(R.id.id_signUp);
        editText_phone=(MaterialEditText)findViewById(R.id.id_mobile_nbr);
        editText_password=(MaterialEditText)findViewById(R.id.id_password);

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waiting...");
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate_fiels()){
                    progressDialog.show();
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //check user exit or not
                            if(dataSnapshot.child(editText_phone.getText().toString()).exists()){

                                //get user info
                                User user=dataSnapshot.child(editText_phone.getText().toString()).getValue(User.class);
                                user.setPhone(editText_phone.getText().toString());//set phone
                                assert user != null;
                                if (user.getPassword().equals(editText_password.getText().toString())){
                                    progressDialog.dismiss();
                                    // Toast.makeText(SignIn.this, "Sign in Successfully !", Toast.LENGTH_SHORT).show();

                                    Comman.currentUser=user;
                                    startActivity(new Intent(getApplicationContext(), Home.class));
                                    finish();
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignIn.this, "Sign in Failed !!!", Toast.LENGTH_SHORT).show();

                                }
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(SignIn.this, "User not exits !!!", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
    }
    boolean validate_fiels(){

        String phone = editText_phone.getText().toString();
        String password = editText_password.getText().toString();

        if(phone.trim().length() > 0 && password.trim().length() > 0){


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
        }else{

            // user didn't entered username or password
            Toast.makeText(getApplicationContext(),
                    "Please enter username and password",
                    Toast.LENGTH_LONG).show();
            return false;
        }


    }
}
