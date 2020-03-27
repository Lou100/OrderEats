package com.mithntcs.eat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mithntcs.eat.ui.AdminSignIn;
import com.mithntcs.eat.ui.SignIn;
import com.mithntcs.eat.ui.SignUp;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {

    private FButton btn_signin,btn_signup;
    private Button btn_admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiView();
    }

    private void intiView() {
        btn_admin=(Button)findViewById(R.id.btn_admin_signIN);
        btn_signin=(FButton)findViewById(R.id.btn_signin);
        btn_signup=(FButton)findViewById(R.id.btn_signup);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminSignIn.class));
            }
        });
    }
}
