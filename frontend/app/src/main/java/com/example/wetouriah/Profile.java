package com.example.wetouriah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    private Button btnUpdate;
    private String imagepath;
    private CheckBox status;

    private FloatingActionButton selectImageButton;


    String user_id,saved_username,role;
    List<String> roleItems;
    ArrayAdapter<String> roleAdapter;
    Spinner spinner;

    RelativeLayout roleAndStatus;

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

        status = findViewById(R.id.status);

        roleAndStatus = findViewById(R.id.roleAndStatus);



        imageView = findViewById(R.id.imageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        btnUpdate = findViewById(R.id.btnUpdate);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", null);
        role = sharedPreferences.getString("role", null);
        saved_username = sharedPreferences.getString("username", null);



        roleItems = new ArrayList<>();
        roleItems.add("Admin");
        roleItems.add("Customer");
        roleItems.add("Driver");
        roleItems.add("WareHouse");

        roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,roleItems);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(roleAdapter);






        try {

            UserItem user = (UserItem) getIntent().getSerializableExtra("user");
            if(user != null){
                user_id = user.getId().toString();
                loadUser(user_id);

                if (role.equals("1")) {
                    roleAndStatus.setVisibility(View.VISIBLE);
                }
                if (saved_username.equals(user.username.toString())) {
                    roleAndStatus.setVisibility(View.GONE);
                }




            }
            else {
                loadUser(user_id.toString());

            }


        } catch (Exception e) {

        }



        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Profile.this)
                        .crop()
                        .galleryOnly()
                        .maxResultSize(512,512)
                        .provider(ImageProvider.GALLERY)
                        .start();

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://" + Constants.SERVER_IP_ADDRESS+ "/api/";



                String spinnerSelected = spinner.getSelectedItem().toString();

                if (spinnerSelected.equals("Admin")) {
                    spinnerSelected = "1";

                }
                if (spinnerSelected.equals("Customer")) {
                    spinnerSelected = "2";

                }
                if (spinnerSelected.equals("Driver")) {
                    spinnerSelected = "3";

                }
                if (spinnerSelected.equals("WareHouse")) {
                    spinnerSelected = "4";

                }


                if(imagepath != null && !imagepath.isEmpty()){


                    updateUserWithImage(user_id,email.getText().toString(),
                            title.getText().toString(),
                            firstname.getText().toString(),
                            lastname.getText().toString(),
                            address.getText().toString(),
                            phone_number.getText().toString(),spinnerSelected,String.valueOf(status.isChecked()));

                }else{
                    updateUserNoImage(user_id,email.getText().toString(),
                            title.getText().toString(),
                            firstname.getText().toString(),
                            lastname.getText().toString(),
                            address.getText().toString(),
                            phone_number.getText().toString(),spinnerSelected,String.valueOf(status.isChecked()));


                }







            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
            Uri uri = data.getData();
            if(uri != null){
                imagepath = RealPathUtil.getRealPath(this,uri);
                imageView.setImageURI(uri);
            }

        }


    }

    public void updateUserWithImage(String id,
                                    String email,
                                    String title,String firstname,String lastname,
                                    String address,String phone_number,String role,String is_active) {

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

        RequestBody user_role = RequestBody.create(MediaType.parse("multipart/form-data"),role);
        RequestBody user_status = RequestBody.create(MediaType.parse("multipart/form-data"),is_active);


        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.updateUser(user_id,user_email,
                user_title,imagefield,user_firstname,user_lastname,user_address,user_phone_number,user_role,user_status);

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

    public void updateUserNoImage(String id,
                                  String email,
                                  String title,String firstname,String lastname,
                                  String address,String phone_number,String role,String is_active) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();


        RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"), id);

        RequestBody user_email = RequestBody.create(MediaType.parse("multipart/form-data"), email);

        RequestBody user_title= RequestBody.create(MediaType.parse("multipart/form-data"), title);
        RequestBody user_firstname = RequestBody.create(MediaType.parse("multipart/form-data"), firstname);
        RequestBody user_lastname = RequestBody.create(MediaType.parse("multipart/form-data"), lastname);
        RequestBody user_address= RequestBody.create(MediaType.parse("multipart/form-data"), address);
        RequestBody user_phone_number = RequestBody.create(MediaType.parse("multipart/form-data"),phone_number);

        RequestBody user_role = RequestBody.create(MediaType.parse("multipart/form-data"),role);
        RequestBody user_status = RequestBody.create(MediaType.parse("multipart/form-data"),is_active);




        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.updateUserNoImage(user_id,user_email,
                user_title,user_firstname,user_lastname,user_address,user_phone_number,user_role,user_status);


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
                    lastname.setText(response.body().getLastName());
                    address.setText(response.body().getAddress());
                    phone_number.setText(response.body().getPhoneNumber());
                    status.setChecked(response.body().getIsActive());

                    spinner.setSelection( Integer.parseInt(response.body().getRole().toString())-1 );


                    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    String role = sharedPreferences.getString("role", null);

                    if (role.equals("1")) {
                        status.setEnabled(true);
                        spinner.setEnabled(true);

                    }


                }
            }

            @Override
            public void onFailure(Call<GetUserReponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}