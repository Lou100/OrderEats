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
import com.mithntcs.eat.R;
import com.mithntcs.eat.model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;

public class SignUp extends AppCompatActivity {

    private FButton btn_signUp;
    private TextView txt_signin;
    private MaterialEditText editText_phone,editText_password,editText_email,editText_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        intiView();
    }
    private void intiView() {

        btn_signUp=(FButton)findViewById(R.id.btn_signup);
        txt_signin=(TextView)findViewById(R.id.id_signIn);
        editText_phone=(MaterialEditText)findViewById(R.id.id_mobile_nbr);
        editText_password=(MaterialEditText)findViewById(R.id.id_password);
        editText_email=(MaterialEditText)findViewById(R.id.id_email);
        editText_username=(MaterialEditText)findViewById(R.id.id_user_name);

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Waiting...");
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //check if user already user phone number
                        progressDialog.dismiss();

                        if(validate_fiels()){
                            progressDialog.dismiss();
                            if(dataSnapshot.child(editText_phone.getText().toString()).exists()){

                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this, "Phone Number already register", Toast.LENGTH_SHORT).show();

                            }else {
                                progressDialog.dismiss();
                                User user=new User(editText_username.getText().toString(),editText_password.getText().toString(), editText_email.getText().toString());
                                table_user.child(editText_phone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Sign Up Successfully !", Toast.LENGTH_SHORT).show();
                                finish();

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });
        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
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
                    "All field required",
                    Toast.LENGTH_LONG).show();

            return false;
        }


    }
}
