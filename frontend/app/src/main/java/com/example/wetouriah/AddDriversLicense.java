package com.example.wetouriah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import static com.example.wetouriah.Constants.ChangeDateToISO;
import static com.example.wetouriah.Constants.formatDate;

public class AddDriversLicense extends AppCompatActivity {

    EditText license_owner, fullname  ,  license_number , expiry_date ;
    CheckBox is_approved ;

    private APIService apiService;

    Button btnAddDriversLicense;

    String user_id,driversLicense_id,role;
    private String imagepath;
    ImageView image;
    private FloatingActionButton selectImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drivers_license);

        setTitle("Driver's License");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        license_owner = findViewById(R.id.license_owner);
        fullname   = findViewById(R.id.fullname);
        license_number  = findViewById(R.id.license_number);
        expiry_date  = findViewById(R.id.expiry_date);

        is_approved = findViewById(R.id.is_approved);

        image = findViewById(R.id.image);
        selectImageButton = findViewById(R.id.selectImageButton);

        btnAddDriversLicense = findViewById(R.id.btnAddDriversLicense);



        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("driversLicense_id");
        editor.apply();

        user_id = sharedPreferences.getString("user_id", null);
        driversLicense_id = sharedPreferences.getString("driversLicense_id", null);
        role = sharedPreferences.getString("role", null);

        if (role.equals("1")) {
            is_approved.setEnabled(true);
        }

        try {


            DriversLicense driversLicense = (DriversLicense) getIntent().getSerializableExtra("driversLicense");
            if(driversLicense != null){
                user_id = driversLicense.getLicense_owner().toString();
                driversLicense_id =  driversLicense.getId().toString();
                loadDriversLicense(user_id);
                is_approved.setEnabled(true);
            }
            else {
                loadDriversLicense(user_id.toString());

            }

        } catch (Exception e) {

        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(AddDriversLicense.this)
                        .crop()
                        .galleryOnly()
                        .maxResultSize(512,512)
                        .provider(ImageProvider.GALLERY)
                        .start();


            }
        });

        btnAddDriversLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (imagepath != null && !imagepath.isEmpty()) {

                    if(driversLicense_id != null && !driversLicense_id.isEmpty()){



                        updateDriversLicenseWithImage(driversLicense_id,
                                user_id,
                                fullname.getText().toString(),
                                license_number.getText().toString(),
                                ChangeDateToISO(expiry_date.getText().toString()),
                                String.valueOf(is_approved.isChecked()) );


                    }else{

                        addDriversLicenseWithImage(
                                user_id,
                                fullname.getText().toString(),

                                license_number.getText().toString(),
                                ChangeDateToISO(expiry_date.getText().toString()),

                                String.valueOf(is_approved.isChecked()) );

                    }



                } else {
                    if(driversLicense_id != null && !driversLicense_id.isEmpty()){



                        updateDriversLicenseNoImage(driversLicense_id,
                                user_id,
                                fullname.getText().toString(),

                                license_number.getText().toString(),
                                ChangeDateToISO(expiry_date.getText().toString()),
                                String.valueOf(is_approved.isChecked()) );

//                        updateDriversLicenseNoImage(driversLicense_id,
//                                user_id,
//                                fullname.getText().toString(),
//
//                                license_number.getText().toString(),
//                                expiry_date.getText().toString(),
//                                String.valueOf(is_approved.isChecked()) );



                    }else{


                        addDriversLicenseNoImage(
                                user_id,
                                fullname.getText().toString(),

                                license_number.getText().toString(),
                                ChangeDateToISO(expiry_date.getText().toString()),
                                String.valueOf(is_approved.isChecked()) );

//                        addDriversLicenseNoImage(
//                                user_id,
//                                fullname.getText().toString(),
//
//                                license_number.getText().toString(),
//                                expiry_date.getText().toString(),
//                                String.valueOf(is_approved.isChecked()) );



                    }

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
                image.setImageURI(uri);
            }

        }

    }


    public void loadDriversLicense(String id) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody user_id= RequestBody.create(MediaType.parse("multipart/form-data"), id);

        APIService apiService = retrofit.create(APIService.class);
        Call<DriversLicenseResponse> call = apiService.loadDriversLicense(user_id);

        call.enqueue(new Callback<DriversLicenseResponse>() {
            @Override
            public void onResponse(Call<DriversLicenseResponse> call, Response<DriversLicenseResponse> response) {

                if(response.isSuccessful()){
                    if(Integer.parseInt(response.body().getId().toString()) > 0) {

                        Picasso.get().load( "https://" + Constants.SERVER_IP_ADDRESS+"/" +response.body().getUploadLicense()).into(image);

                        license_owner.setText(response.body().getLicenseOwner().toString());
                        fullname.setText(response.body().getFullname().toString());
                        license_number.setText(response.body().getUploadLicense().toString());
                        String OriginalDate = response.body().getExpiryDate().toString();
                        expiry_date.setText(formatDate(OriginalDate));
                        is_approved.setChecked(response.body().getIsApproved());

                        btnAddDriversLicense.setText("update");

                        setDriversLicense_id(response.body().getId().toString());

                    }else {


//                        setDriversLicense_id("-1");
                    }



                }
                else {

//                    setDriversLicense_id("-1");
                }
            }

            @Override
            public void onFailure(Call<DriversLicenseResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }




    public void setDriversLicense_id(String sdriversLicense_id) {

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("driversLicense_id", driversLicense_id);
        editor.apply();
        driversLicense_id = sdriversLicense_id;

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

    private void updateDriversLicenseNoImage(String id ,
                                             String license_owner,
                                             String fullname ,

                                             String license_number,
                                             String expiry_date  ,

                                             String is_approved ) {

//        driversLicenseItem.setId(driversLicense_id);

        RequestBody get_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id));
        RequestBody get_license_owner = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(license_owner));
        RequestBody get_fullname   = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(fullname));
RequestBody get_license_number  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(license_number));
        RequestBody get_expiry_date  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(expiry_date));
        RequestBody get_is_approved = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(is_approved));


        Call<DriversLicenseResponse> call = apiService.updateDriversLicenseNoImage(get_id,get_license_owner,
                get_fullname,get_license_number,get_expiry_date,get_is_approved);

//        Call<DriversLicenseResponse> call = apiService.updateDriversLicenseNoImage(get_id,get_license_owner,
//                get_fullname,get_identity_number,get_license_number,
//                get_country_of_issue,get_code,get_restrictions,get_gender,get_is_approved);


        call.enqueue(new Callback<DriversLicenseResponse>() {
            @Override
            public void onResponse(Call<DriversLicenseResponse> call, Response<DriversLicenseResponse> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Drivers License updated succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "there was error updating your driversLicense", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DriversLicenseResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDriversLicenseWithImage(String id ,
                                               String license_owner,
                                               String fullname ,
                                               String license_number,
                                               String expiry_date  ,

                                               String is_approved ) {

        File file = new File(imagepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imagefield = MultipartBody.Part.createFormData("uploadLicense", file.getName(), requestFile);



        RequestBody get_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id));
        RequestBody get_license_owner = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(license_owner));
        RequestBody get_fullname   = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(fullname));
        RequestBody get_license_number  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(license_number));
        RequestBody get_expiry_date  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(expiry_date));
        RequestBody get_is_approved = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(is_approved));


        Call<DriversLicenseResponse> call = apiService.updateDriversLicenseWithImage(get_id,get_license_owner,
                get_fullname,get_license_number,get_expiry_date,imagefield,get_is_approved);



        call.enqueue(new Callback<DriversLicenseResponse>() {
            @Override
            public void onResponse(Call<DriversLicenseResponse> call, Response<DriversLicenseResponse> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Drivers License updated succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "there was error updating your driversLicense", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DriversLicenseResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addDriversLicenseNoImage(
                                             String license_owner,
                                             String fullname ,

                                             String license_number,
                                             String expiry_date  ,

                                             String is_approved ) {

//        driversLicenseItem.setId(driversLicense_id);


        RequestBody get_license_owner = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(license_owner));
        RequestBody get_fullname   = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(fullname));
RequestBody get_license_number  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(license_number));
        RequestBody get_expiry_date  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(expiry_date));
        RequestBody get_is_approved = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(is_approved));


        Call<DriversLicenseResponse> call = apiService.addDriversLicenseNoImage(get_license_owner,
                get_fullname,get_license_number,get_expiry_date,get_is_approved);




        call.enqueue(new Callback<DriversLicenseResponse>() {
            @Override
            public void onResponse(Call<DriversLicenseResponse> call, Response<DriversLicenseResponse> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Drivers License add succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "there was error updating your driversLicense", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DriversLicenseResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDriversLicenseWithImage(
                                               String license_owner,
                                               String fullname,

                                               String license_number,
                                               String expiry_date  ,

                                               String is_approved ) {

        File file = new File(imagepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imagefield = MultipartBody.Part.createFormData("uploadLicense", file.getName(), requestFile);




        RequestBody get_license_owner = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(license_owner));
        RequestBody get_fullname   = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(fullname));
        RequestBody get_license_number  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(license_number));
        RequestBody get_expiry_date  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(expiry_date));
        RequestBody get_is_approved = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(is_approved));


        Call<DriversLicenseResponse> call = apiService.addDriversLicenseWithImage(get_license_owner,
                get_fullname,get_license_number,get_expiry_date,imagefield,get_is_approved);



        call.enqueue(new Callback<DriversLicenseResponse>() {
            @Override
            public void onResponse(Call<DriversLicenseResponse> call, Response<DriversLicenseResponse> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Drivers License add succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "there was error updating your driversLicense", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DriversLicenseResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}