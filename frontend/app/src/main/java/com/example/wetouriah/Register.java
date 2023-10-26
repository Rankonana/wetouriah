package com.example.wetouriah;

import static com.example.wetouriah.Constants.generateTrackingNumber;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class Register extends AppCompatActivity {

    private RelativeLayout lyUsernameAndPassword, lyProfileDetails,lyRole;
    private Button btnRolesNext,btnNext, btnBack,btnRegister;
    private FloatingActionButton selectImageButton;

    private ImageView imageView;
    private EditText email,  username, password,
            title, firstname, lastname, address, phone_number;

    private RadioGroup rgRoles;


    private String imagepath;
    private String role,selectedOption;

    RadioButton rdAdmin,rdWarehouseOwner, rdCustomer,rdDriver;

    TextInputLayout lyedtPassword1,lypassword;

    //edtPassword1

    String usernameEmailExist = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Registration");
        lyRole = findViewById(R.id.lyRole);
        lyUsernameAndPassword = findViewById(R.id.lyUsernameAndPassword);
        lyProfileDetails = findViewById(R.id.lyProfileDetails);
        btnRolesNext = findViewById(R.id.btnRolesNext);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnRegister = findViewById(R.id.btnRegister);

        email = findViewById(R.id.email);
        rgRoles = findViewById(R.id.rgRoles);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        title = findViewById(R.id.title);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        address = findViewById(R.id.address);
        phone_number = findViewById(R.id.phone_number);


        imageView = findViewById(R.id.imageView);
        selectImageButton = findViewById(R.id.selectImageButton);

        rdAdmin =   findViewById(R.id.rdAdmin);
        rdWarehouseOwner=   findViewById(R.id.rdWarehouseOwner);
        rdCustomer=   findViewById(R.id.rdCustomer);
        rdDriver =   findViewById(R.id.rdDriver);
        lyedtPassword1=   findViewById(R.id.lyedtPassword1);
        lypassword =   findViewById(R.id.lypassword);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String logedrole = sharedPreferences.getString("role", null);

        if (logedrole != null && !logedrole.isEmpty() ) {
            if("1".equals(logedrole)){
                rdAdmin.setVisibility(View.VISIBLE);
                rdDriver.setVisibility(View.GONE);
                rdCustomer.setVisibility(View.GONE);
                rdWarehouseOwner.setVisibility(View.GONE);

                lyedtPassword1.setVisibility(View.GONE);
                lypassword.setVisibility(View.GONE);

                String auto_password = generateTrackingNumber();
                password.setText(auto_password.toString());


            }

        }else {
            rdAdmin.setVisibility(View.GONE);

        }



        rgRoles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                selectedOption = radioButton.getText().toString();

                if(selectedOption.equals("Admin")) {
                    role = "1";
                }
                if(selectedOption.equals("Customer")) {
                    role= "2";
                }
                if(selectedOption.equals("Driver")) {
                    role= "3";
                }
                if(selectedOption.equals("Warehouse Owner")) {
                    role= "4";
                }

            }
        });

        btnRolesNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                lyRole.setVisibility(View.GONE);
                lyUsernameAndPassword.setVisibility(View.VISIBLE);

            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(username.getText().length() >0){
                    mCheckUsernameEmail(username.getText().toString(),"");

                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(usernameEmailExist.length() >0){
                            Toast.makeText(getApplicationContext(), usernameEmailExist, Toast.LENGTH_SHORT).show();
                            usernameEmailExist = "";

                        }else {
                            lyUsernameAndPassword.setVisibility(View.GONE);
                            lyProfileDetails.setVisibility(View.VISIBLE);
                            usernameEmailExist ="";
                        }

                    }
                },2000);





            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyUsernameAndPassword.setVisibility(View.VISIBLE);
                lyProfileDetails.setVisibility(View.GONE);

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                if(email.getText().length() >0){
                    mCheckUsernameEmail("",email.getText().toString());

                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(usernameEmailExist.length() >0){
                            Toast.makeText(getApplicationContext(), usernameEmailExist, Toast.LENGTH_SHORT).show();
                            usernameEmailExist = "";
                        }else {

                            String url = "https://" + Constants.SERVER_IP_ADDRESS+ "/api/";

                            if(imagepath != null ){
                                addUserWithImage(url,imagepath,email.getText().toString(),
                                        role,
                                        username.getText().toString(),
                                        password.getText().toString(),

                                        title.getText().toString(),
                                        firstname.getText().toString(),
                                        lastname.getText().toString(),
                                        address.getText().toString(),
                                        phone_number.getText().toString());
                            }else {
                                addUserNoImageImage(email.getText().toString(),
                                        role,
                                        username.getText().toString(),
                                        password.getText().toString(),

                                        title.getText().toString(),
                                        firstname.getText().toString(),
                                        lastname.getText().toString(),
                                        address.getText().toString(),
                                        phone_number.getText().toString());
                            }


                            if(role.equals("1")){
                                Toast.makeText(getApplicationContext(), "Temp password sent via email", Toast.LENGTH_SHORT).show();


                            }
                        }

                    }
                },2000);

                //


            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Register.this)
                        .crop()
                        .galleryOnly()
                        .maxResultSize(512,512)
                        .provider(ImageProvider.GALLERY)
                        .start();



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


    public void addUserWithImage(String url, String profile_picture,
                            String email,String role, String username,String password,
                            String title,String firstname,String lastname,
                            String address,String phone_number) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        File file = new File(imagepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part imagefield = MultipartBody.Part.createFormData("profile_picture", file.getName(), requestFile);



        RequestBody user_email = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        RequestBody user_role = RequestBody.create(MediaType.parse("multipart/form-data"), role);

        RequestBody user_username= RequestBody.create(MediaType.parse("multipart/form-data"), username);
        RequestBody user_password = RequestBody.create(MediaType.parse("multipart/form-data"), password);

        RequestBody user_title= RequestBody.create(MediaType.parse("multipart/form-data"), title);
        RequestBody user_firstname = RequestBody.create(MediaType.parse("multipart/form-data"), firstname);
        RequestBody user_lastname = RequestBody.create(MediaType.parse("multipart/form-data"), lastname);
        RequestBody user_address= RequestBody.create(MediaType.parse("multipart/form-data"), address);
        RequestBody user_phone_number = RequestBody.create(MediaType.parse("multipart/form-data"),phone_number);

        APIService apiService = retrofit.create(APIService.class);
        Call<AddUserResponse> call = apiService.addUserWithImage(user_email,user_role,
                user_username,user_password,
                user_title,imagefield,user_firstname,user_lastname,user_address,user_phone_number);

        call.enqueue(new Callback<AddUserResponse>() {
            @Override
            public void onResponse(Call<AddUserResponse> call, Response<AddUserResponse> response) {


                               if(response.isSuccessful()){
                                   if(response.body().getStatusCode().toString().equals("201")) {


                                       Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent(Register.this,MainActivity.class);
                                       startActivity(intent);
                                       finish();




                                   }else{
                                       Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();

                                   }
                               }
            }

            @Override
            public void onFailure(Call<AddUserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void addUserNoImageImage(
                                 String email,String role, String username,String password,
                                 String title,String firstname,String lastname,
                                 String address,String phone_number) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();



        RequestBody user_email = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        RequestBody user_role = RequestBody.create(MediaType.parse("multipart/form-data"), role);

        RequestBody user_username= RequestBody.create(MediaType.parse("multipart/form-data"), username);
        RequestBody user_password = RequestBody.create(MediaType.parse("multipart/form-data"), password);

        RequestBody user_title= RequestBody.create(MediaType.parse("multipart/form-data"), title);
        RequestBody user_firstname = RequestBody.create(MediaType.parse("multipart/form-data"), firstname);
        RequestBody user_lastname = RequestBody.create(MediaType.parse("multipart/form-data"), lastname);
        RequestBody user_address= RequestBody.create(MediaType.parse("multipart/form-data"), address);
        RequestBody user_phone_number = RequestBody.create(MediaType.parse("multipart/form-data"),phone_number);

        APIService apiService = retrofit.create(APIService.class);
        Call<AddUserResponse> call = apiService.addUserNoImage(user_email,user_role,
                user_username,user_password,
                user_title,user_firstname,user_lastname,user_address,user_phone_number);

        call.enqueue(new Callback<AddUserResponse>() {
            @Override
            public void onResponse(Call<AddUserResponse> call, Response<AddUserResponse> response) {


                if(response.isSuccessful()){
                    if(response.body().getStatusCode().toString().equals("201")) {


                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this,MainActivity.class);
                        startActivity(intent);
                        finish();




                    }else{
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<AddUserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

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

    public void mCheckUsernameEmail(String Username,String Email) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody g_username= RequestBody.create(MediaType.parse("multipart/form-data"), Username);
        RequestBody g_email= RequestBody.create(MediaType.parse("multipart/form-data"), Email);

        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.apiCheckUsernameEmail(g_username,g_email);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){
                    usernameEmailExist = response.body().getMessage();
                    //Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }





}