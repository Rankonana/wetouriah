package com.example.wetouriah;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {

    private ImageView imageView;
    private EditText email,  username, password,
            title, firstname, lastname, address, phone_number;

    private Button selectImageButton,btnUpdate;
    private String imagepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile Details");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        title = findViewById(R.id.title);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        address = findViewById(R.id.address);
        phone_number = findViewById(R.id.phone_number);


        imageView = findViewById(R.id.imageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        btnUpdate = findViewById(R.id.btnUpdate);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", null);

        try {

            loadUser(user_id.toString());
        } catch (Exception e) {

        }

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Profile.this)
                        .crop()
                        .cropOval()
                        .maxResultSize(512,512)
                        .provider(ImageProvider.BOTH)
                        .start();

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                String user_id = sharedPreferences.getString("user_id", null);

                String url = "https://" + Constants.SERVER_IP_ADDRESS+ "/api/";

                updateUser(user_id,email.getText().toString(),
                        title.getText().toString(),
                        firstname.getText().toString(),
                        lastname.getText().toString(),
                        address.getText().toString(),
                        phone_number.getText().toString());

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        imagepath = RealPathUtil.getRealPath(this,uri);
        imageView.setImageURI(uri);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            String role = sharedPreferences.getString("role", null);
            // Check the user's role
            if (role.equals("2")) {
                // Navigate to the CustomerPortal
                Intent intent = new Intent(this, CustomerPortal.class);
                startActivity(intent);
                finish();
            }
            if (role.equals("4")) {
                // Navigate to the WarehousePortal
                Intent intent = new Intent(this, WarehousePortal.class);
                startActivity(intent);
                finish();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void updateUser(String id,
                        String email,
                        String title,String firstname,String lastname,
                        String address,String phone_number) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        File file = new File(imagepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part imagefield = MultipartBody.Part.createFormData("profile_picture", file.getName(), requestFile);

        RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"), id);

        RequestBody user_email = RequestBody.create(MediaType.parse("multipart/form-data"), email);

        RequestBody user_title= RequestBody.create(MediaType.parse("multipart/form-data"), title);
        RequestBody user_firstname = RequestBody.create(MediaType.parse("multipart/form-data"), firstname);
        RequestBody user_lastname = RequestBody.create(MediaType.parse("multipart/form-data"), lastname);
        RequestBody user_address= RequestBody.create(MediaType.parse("multipart/form-data"), address);
        RequestBody user_phone_number = RequestBody.create(MediaType.parse("multipart/form-data"),phone_number);

        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.updateUser(user_id,user_email,
                user_title,imagefield,user_firstname,user_lastname,user_address,user_phone_number);

//        Toast.makeText(getApplicationContext(), "reached", Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){
//                    Toast.makeText(getApplicationContext(), "reached ", Toast.LENGTH_SHORT).show();

                    if(response.body().getStatusCode().toString().equals("201")) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }




    public void loadUser(String id) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody user_id= RequestBody.create(MediaType.parse("multipart/form-data"), id);

        APIService apiService = retrofit.create(APIService.class);
        Call<GetUserReponse> call = apiService.loadUser(user_id);

        call.enqueue(new Callback<GetUserReponse>() {
            @Override
            public void onResponse(Call<GetUserReponse> call, Response<GetUserReponse> response) {

                if(response.isSuccessful()){
                    Picasso.get().load( "https://" + Constants.SERVER_IP_ADDRESS+"/" +response.body().getProfilePicture()).into(imageView);
                    email.setText(response.body().getEmail());
//                    username.setText(response.body().getUsername());
                    title.setText(response.body().getTitle());
                    firstname.setText(response.body().getFirstName());
                    lastname.setText(response.body().getLastname());
                    address.setText(response.body().getAddress());
                    phone_number.setText(response.body().getPhoneNumber());

                }
            }

            @Override
            public void onFailure(Call<GetUserReponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }
}