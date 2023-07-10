package com.example.wetouriah;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    private Button btnRolesNext,btnNext, btnBack,btnRegister, selectImageButton;
    private ImageView imageView;
    private EditText email,  username, password,
            title, firstname, lastname, address, phone_number;

    private RadioGroup rgRoles;


    private String imagepath;
    private String role,selectedOption;


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
                Toast.makeText(Register.this, role.toString(), Toast.LENGTH_LONG).show();

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lyUsernameAndPassword.setVisibility(View.GONE);
                lyProfileDetails.setVisibility(View.VISIBLE);


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

                String url = "https://" + Constants.SERVER_IP_ADDRESS + "/api/";

                addUser(url,imagepath,email.getText().toString(),
                        role,
                        username.getText().toString(),
                        password.getText().toString(),

                        title.getText().toString(),
                        firstname.getText().toString(),
                        lastname.getText().toString(),
                        address.getText().toString(),
                        phone_number.getText().toString());

            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Register.this)
                        .crop()
                        .cropOval()
                        .maxResultSize(512,512)
                        .provider(ImageProvider.BOTH)
                        .start();


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

    public void SimpleAddUser(String apiurl,
                                String email, String username,String password,
                                String title,String firstname,String lastname,
                                String address,String phone_number){

        String url = apiurl;
        OkHttpClient client = new OkHttpClient();

        // Image file
        File imageFile = new File(imagepath);

        // Determine the image format based on the file extension
        String imageExtension = imageFile.getName().substring(imageFile.getName().lastIndexOf(".") + 1);
        String mediaTypeString = "image/" + imageExtension.toLowerCase();

        // Build the request body
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("title", title)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .addFormDataPart("firstname", firstname)
                .addFormDataPart("lastname", lastname)
                .addFormDataPart("address", address)
                .addFormDataPart("phone_number", phone_number)
                .addFormDataPart("profile_picture", imageFile.getName(),
                                        RequestBody.create(MediaType.parse(mediaTypeString), imageFile))
                .build();


        // Create the request
        Request request = new Request.Builder()
                .url(url) // Replace with your server URL
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {

                    Register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_SHORT).show();


                        }
                    });


                }
            }
        });

    }



    public void addUser(String url, String profile_picture,
                            String email,String role, String username,String password,
                            String title,String firstname,String lastname,
                            String address,String phone_number) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS + "/api/")
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
        Call<AddUserResponse> call = apiService.addUser(user_email,user_role,
                user_username,user_password,
                user_title,imagefield,user_firstname,user_lastname,user_address,user_phone_number);

        call.enqueue(new Callback<AddUserResponse>() {
            @Override
            public void onResponse(Call<AddUserResponse> call, Response<AddUserResponse> response) {


                               if(response.isSuccessful()){
                                   if(response.body().getStatusCode().toString().equals("201")) {

                                       if(role.equals("1")) {
                                           Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                           Intent intent = new Intent(Register.this,AdminPortal.class);
                                           startActivity(intent);
                                       }
                                       if(role.equals("2")) {
                                           Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                           Intent intent = new Intent(Register.this, CustomerPortal.class);
                                           startActivity(intent);
                                       }
                                       if(role.equals("3")) {
                                           Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                           Intent intent = new Intent(Register.this,DriverPortal.class);
                                           startActivity(intent);
                                       }
                                       if(role.equals("4")) {
                                           Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                           Intent intent = new Intent(Register.this,WarehousePortal.class);
                                           startActivity(intent);
                                       }


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

}