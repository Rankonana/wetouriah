package com.example.wetouriah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditText)findViewById(R.id.edtUsername);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        btnButton = (Button)findViewById(R.id.btnButton);

        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogin(edtUsername.getText().toString(),edtPassword.getText().toString());
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);
        String username = sharedPreferences.getString("username", null);
        String user_id = sharedPreferences.getString("user_id", null);



        if (role != null && !role.isEmpty() && username != null && !username.isEmpty()) {
            if("1".equals(role)){

                Intent intent = new Intent(Login.this,AdminPortal.class);
                startActivity(intent);
                finish();
            }
            if("2".equals(role) ){
                Intent intent = new Intent(Login.this, CustomerPortal.class);
                startActivity(intent);
                finish();


            }
            if("3".equals(role)){
                Intent intent = new Intent(Login.this,DriverPortal.class);
                startActivity(intent);
                finish();


            }
            if("4".equals(role) ){
                Intent intent = new Intent(Login.this,WarehousePortal.class);
                startActivity(intent);
                finish();


            }

        }

    }

    public void mLogin(String username,String password) {

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
                    if(response.body().getStatusCode().toString().equals("200")) {

                        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("access", response.body().getAccess());
                        editor.putString("refresh", response.body().getRefresh());
                        editor.putString("role", response.body().getAuthenticatedUser().getRole());
                        editor.putString("username", response.body().getAuthenticatedUser().getUsername());
                        editor.putString("user_id", response.body().getAuthenticatedUser().getID());


                        editor.apply();

                        if(response.body().getAuthenticatedUser().getRole().toString().equals("1")  ){
                            Intent intent = new Intent(Login.this,AdminPortal.class);
                            startActivity(intent);
                            finish();



                        }
                        if(response.body().getAuthenticatedUser().getRole().toString().equals("2") ){
                            Intent intent = new Intent(Login.this, CustomerPortal.class);
                            startActivity(intent);
                            finish();


                        }
                        if(response.body().getAuthenticatedUser().getRole().toString().equals("3") ){
                            Intent intent = new Intent(Login.this,DriverPortal.class);
                            startActivity(intent);
                            finish();


                        }
                        if(response.body().getAuthenticatedUser().getRole().toString().equals("4") ){
                            Intent intent = new Intent(Login.this,WarehousePortal.class);
                            startActivity(intent);
                            finish();


                        }

                    }else{
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }
}