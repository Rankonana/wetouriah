package com.example.wetouriah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class ResetPassword extends AppCompatActivity {

    TextView btnlogin, btnRegister;

    Button btnResetButton, btnVerify, btnCLickHereForLogin,btnConfirm;

    LinearLayout lyEnterUsername, lyenterVerificationCode,lySuccessMessage,lyConfirmPassword;

    TextInputEditText edtUsername;

    String receivedData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        btnlogin = findViewById(R.id.btnlogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnResetButton = findViewById(R.id.btnResetButton);
        btnVerify = findViewById(R.id.btnVerify);
        btnCLickHereForLogin = findViewById(R.id.btnCLickHereForLogin);
        btnConfirm = findViewById(R.id.btnConfirm);



        lyEnterUsername = findViewById(R.id.lyEnterUsername);
        lyenterVerificationCode = findViewById(R.id.lyenterVerificationCode);
        lySuccessMessage = findViewById(R.id.lySuccessMessage);
        lyConfirmPassword = findViewById(R.id.lyConfirmPassword);



        Intent intent = getIntent();
        edtUsername = findViewById(R.id.edtUsername);

        // Retrieve the string data from the intent
        if (intent != null) {
            receivedData = intent.getStringExtra("password");
            if (receivedData != null) {

                btnCLickHereForLogin.setText("Go back");
                edtUsername.setHint("Enter your password");
            }
            else {
                edtUsername.setHint("Enter your username");

            }
        }




        btnResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (receivedData != null) {
                    lyEnterUsername.setVisibility(View.GONE);
                    lyConfirmPassword.setVisibility(View.VISIBLE);

                }else{
                    lyEnterUsername.setVisibility(View.GONE);
                    lyenterVerificationCode.setVisibility(View.VISIBLE);
                }


            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (receivedData != null) {
                    edtUsername.setHint("Enter password");
                }else{
                    lyenterVerificationCode.setVisibility(View.GONE);
                    lySuccessMessage.setVisibility(View.VISIBLE);
                }

            }
        });

        btnCLickHereForLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (receivedData != null) {



                    Intent intent = new Intent(ResetPassword.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(ResetPassword.this, Login.class);
                    startActivity(intent);
                    finish();
                }



            }
        });




        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassword.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassword.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lyConfirmPassword.setVisibility(View.GONE);
                lySuccessMessage.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onBackPressed() {
        // Create an intent for the default activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack
        startActivity(intent);
        finish(); // Finish the current activity
    }
}