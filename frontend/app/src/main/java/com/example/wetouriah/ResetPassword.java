package com.example.wetouriah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetPassword extends AppCompatActivity {

    TextView btnlogin, btnRegister;

    Button btnResetButton, btnVerify, btnCLickHereForLogin,btnConfirm;

    LinearLayout lyEnterUsername, lyenterVerificationCode,lySuccessMessage,lyConfirmPassword;

    TextInputEditText edtUsername;
    TextInputEditText edtVerificationCode,edtPassword1 ,edtPassword2;

    TextView loginText;
    String receivedData;

    String emailCode;

    TextView msgMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        btnlogin = findViewById(R.id.btnlogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnResetButton = findViewById(R.id.btnResetButton);
        msgMessage = findViewById(R.id.msgMessage);
        btnVerify = findViewById(R.id.btnVerify);
        btnCLickHereForLogin = findViewById(R.id.btnCLickHereForLogin);
        btnConfirm = findViewById(R.id.btnConfirm);



        lyEnterUsername = findViewById(R.id.lyEnterUsername);
        lyenterVerificationCode = findViewById(R.id.lyenterVerificationCode);
        lySuccessMessage = findViewById(R.id.lySuccessMessage);
        lyConfirmPassword = findViewById(R.id.lyConfirmPassword);



        Intent intent = getIntent();
        edtUsername = findViewById(R.id.edtUsername);
        edtVerificationCode = findViewById(R.id.edtVerificationCode);

        loginText = findViewById(R.id.loginText);

        edtPassword1 = findViewById(R.id.edtPassword1);
        edtPassword2 = findViewById(R.id.edtPassword2);

        // Retrieve the string data from the intent
        if (intent != null) {
            receivedData = intent.getStringExtra("password");
            if (receivedData != null) {

                loginText.setText("Enter Password");
                btnResetButton.setText("Change Password");
                msgMessage.setText("Your password\n has been changed");

                btnCLickHereForLogin.setText("Go back");
                edtUsername.setHint("Enter your current password");
                btnlogin.setVisibility(View.GONE);
                btnRegister.setVisibility(View.GONE);
                edtUsername.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            }
            else {
                edtUsername.setHint("Enter your username");

            }
        }




        btnResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (receivedData != null) {

                    try {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                        String username = sharedPreferences.getString("username", null);
                        if(username.toString().length() > 0 && edtUsername.getText().length() >0){
                            CheckPassword(username.toString().toLowerCase(),edtUsername.getText().toString());

                        }else {
                            Toast.makeText(getApplicationContext(), "Fill in the text box", Toast.LENGTH_SHORT).show();

                        }


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    try {
                        if(edtUsername.getText().length() > 0){
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                            String username = sharedPreferences.getString("username", null);
                            CheckUsername(edtUsername.getText().toString(),"reset");
                            lyenterVerificationCode.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(getApplicationContext(), "Enter your username", Toast.LENGTH_SHORT).show();

                        }




                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Invalid username", Toast.LENGTH_SHORT).show();

                    }




                }


            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if( edtVerificationCode.getText().toString().equals(emailCode.toString())  ){
                    lyenterVerificationCode.setVisibility(View.GONE);
                    lyConfirmPassword.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(getApplicationContext(),  "Invalid code", Toast.LENGTH_SHORT).show();

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

                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", null);


                if( edtPassword1.getText().length() > 0 && edtPassword2.getText().length() > 0 ){
                    if(edtPassword1.getText().toString().equals(edtPassword2.getText().toString()) )
                    {

                        //imagepath != null && !imagepath.isEmpty()

                        if(username == null || username.isEmpty() ){

                            ChangePassword(edtUsername.getText().toString().toLowerCase(),edtPassword2.getText().toString());

//                        ChangePassword("rankonana",edtPassword2.getText().toString());

                        }
                        else {
                            ChangePassword(username.toString().toLowerCase(),edtPassword2.getText().toString());

                        }


                    }
                    else {
                        Toast.makeText(getApplicationContext(), "password do not match", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Enter your new password", Toast.LENGTH_SHORT).show();

                }






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

    public void CheckPassword(String username,String password) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody user_username= RequestBody.create(MediaType.parse("multipart/form-data"), username);
        RequestBody user_password = RequestBody.create(MediaType.parse("multipart/form-data"), password);



        APIService apiService = retrofit.create(APIService.class);
        Call<LoginResponse> call = apiService.loginUser(user_username,user_password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.isSuccessful()){
                    if(response.body().getAccess().length() > 0) {

                        lyEnterUsername.setVisibility(View.GONE);
                        lyConfirmPassword.setVisibility(View.VISIBLE);

                    }else {
                        Toast.makeText(getApplicationContext(), "invalid password", Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void CheckUsername(String username,String password) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody user_username= RequestBody.create(MediaType.parse("multipart/form-data"), username);
        RequestBody user_password = RequestBody.create(MediaType.parse("multipart/form-data"), password);



        APIService apiService = retrofit.create(APIService.class);
        Call<LoginResponse> call = apiService.loginUser(user_username,user_password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.isSuccessful()){

                    if(response.body().getAccess().length() > 0){

                        lyEnterUsername.setVisibility(View.GONE);
                        lyenterVerificationCode.setVisibility(View.VISIBLE);
                        SendCode(edtUsername.getText().toString().toLowerCase());


                    }else {
                        Toast.makeText(getApplicationContext(), "invalid username or email", Toast.LENGTH_SHORT).show();

                    }


                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void ChangePassword(String username,String password) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody user_username= RequestBody.create(MediaType.parse("multipart/form-data"), username);
        RequestBody user_password = RequestBody.create(MediaType.parse("multipart/form-data"), password);



        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.changePassword(user_username,user_password);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){
                    lyConfirmPassword.setVisibility(View.GONE);
                    lySuccessMessage.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }



    public void SendCode(String username) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody user_username= RequestBody.create(MediaType.parse("multipart/form-data"), username);

        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.sendCode(user_username);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){

                    setCode(response.body().getMessage());

                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void setCode(String code){
        emailCode = code;

    }

}