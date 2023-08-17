package com.example.wetouriah;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddDriversLicense extends AppCompatActivity {

    EditText license_owner, fullname  , identity_number , date_of_birth , license_number , expiry_date , country_of_issue , code , restrictions , gender ,
            date_of_issue,
            uploadLicense;
     CheckBox is_approved ;

    private APIService apiService;

     Button btnAddDriversLicense;

    String user_id,driversLicense_id;

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
        identity_number  = findViewById(R.id.identity_number);
        date_of_birth  = findViewById(R.id.date_of_birth);
        license_number  = findViewById(R.id.license_number);
        expiry_date  = findViewById(R.id.expiry_date);
        country_of_issue  = findViewById(R.id.country_of_issue);
        code  = findViewById(R.id.code);
        restrictions  = findViewById(R.id.restrictions);
        gender  = findViewById(R.id.gender);
        date_of_issue = findViewById(R.id.date_of_issue);
        uploadLicense = findViewById(R.id.uploadLicense);
        is_approved = findViewById(R.id.is_approved);

        btnAddDriversLicense = findViewById(R.id.btnAddDriversLicense);



        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", null);
        driversLicense_id = sharedPreferences.getString("driversLicense_id", null);

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
                .baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/") // Replace with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);


        btnAddDriversLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!driversLicense_id.equals("-1")) {


//                    DriversLicense updateDriversLicense = new DriversLicense(driversLicense_id,user_id,type.getText().toString(),capacity.getText().toString(),color.getText().toString(),
//                            make.getText().toString(),model.getText().toString(),year.getText().toString(),license_plate.getText().toString(),is_approved.getText().toString());
//                    mupdateCar(updateDriversLicense);
//                    DriversLicense updateDriversLicense = new DriversLicense(id,license_owner, fullname  , identity_number , date_of_birth , license_number , expiry_date , country_of_issue , code , restrictions , gender ,
//                            date_of_issue,
//                            uploadLicense ,
//                            is_approved );
//                    mupdateCar(updateDriversLicense);

                } else {

                    //DriversLicense newDriversLicense = new DriversLicense(driversLicense_id,user_id,type.getText().toString(),capacity.getText().toString(),color.getText().toString(),
//                            make.getText().toString(),model.getText().toString(),year.getText().toString(),license_plate.getText().toString(),is_approved.getText().toString());
//                    addDriversLicense(newDriversLicense);

//                    DriversLicense newDriversLicense = new DriversLicense(id,license_owner, fullname  , identity_number , date_of_birth , license_number , expiry_date , country_of_issue , code , restrictions , gender ,
//                            date_of_issue,
//                            uploadLicense ,
//                            is_approved );
//                    addDriversLicense(newDriversLicense);

                }



            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDriversLicense(DriversLicense driversLicenseItem) {

        RequestBody get_icense_owner = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getLicense_owner()));
        RequestBody get_fullname   = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getFullname()));
        RequestBody get_identity_number  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getIdentity_number()));
        RequestBody get_date_of_birth  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getDate_of_birth()));
        RequestBody get_license_number  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getLicense_number()));
        RequestBody get_expiry_date  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getExpiry_date()));
        RequestBody get_country_of_issue  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getCountry_of_issue()));
        RequestBody get_code  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getCode()));
        RequestBody get_restrictions  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getRestrictions()));
        RequestBody get_gender  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getGender()));
        RequestBody get_date_of_issue = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getDate_of_issue()));
        RequestBody get_uploadLicense  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getUploadLicense()));


        Call<DriversLicenseResponse> call = apiService.addDriversLicense(get_icense_owner,
                get_fullname,get_identity_number,get_date_of_birth,get_license_number,get_expiry_date,
                get_country_of_issue,get_code,get_restrictions,get_gender,get_date_of_issue,get_uploadLicense);

        call.enqueue(new Callback<DriversLicenseResponse>() {
            @Override
            public void onResponse(Call<DriversLicenseResponse> call, Response<DriversLicenseResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "License added succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "there was error adding your driversLicense", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DriversLicenseResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mupdateCar(DriversLicense driversLicenseItem) {

        driversLicenseItem.setId(driversLicense_id);

        RequestBody get_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getId()));
        RequestBody get_icense_owner = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getLicense_owner()));
        RequestBody get_fullname   = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getFullname()));
        RequestBody get_identity_number  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getIdentity_number()));
        RequestBody get_date_of_birth  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getDate_of_birth()));
        RequestBody get_license_number  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getLicense_number()));
        RequestBody get_expiry_date  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getExpiry_date()));
        RequestBody get_country_of_issue  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getCountry_of_issue()));
        RequestBody get_code  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getCode()));
        RequestBody get_restrictions  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getRestrictions()));
        RequestBody get_gender  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getGender()));
        RequestBody get_date_of_issue = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getDate_of_issue()));
        RequestBody get_uploadLicense  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getUploadLicense()));
        RequestBody get_is_approved = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(driversLicenseItem.getIs_approved()));


        Call<DriversLicenseResponse> call = apiService.updateDriversLicense(get_id,get_icense_owner,
                get_fullname,get_identity_number,get_date_of_birth,get_license_number,get_expiry_date,
                get_country_of_issue,get_code,get_restrictions,get_gender,get_date_of_issue,get_uploadLicense,get_is_approved);
        call.enqueue(new Callback<DriversLicenseResponse>() {
            @Override
            public void onResponse(Call<DriversLicenseResponse> call, Response<DriversLicenseResponse> response) {
                if (response.isSuccessful()) {

                    add_updateDriversLicense( user_id.toString(), String.valueOf(is_approved.isChecked()));


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

    public void loadDriversLicense(String id) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody user_id= RequestBody.create(MediaType.parse("multipart/form-data"), id);

        APIService apiService = retrofit.create(APIService.class);
        Call<DriversLicenseResponse> call = apiService.loadDriversLicense(user_id);

        call.enqueue(new Callback<DriversLicenseResponse>() {
            @Override
            public void onResponse(Call<DriversLicenseResponse> call, Response<DriversLicenseResponse> response) {

                if(response.isSuccessful()){
                    if(Integer.parseInt(response.body().getId().toString()) > 0) {



                        license_owner.setText(response.body().getLicenseOwner().toString());
                        fullname.setText(response.body().getFullname().toString());
                        identity_number.setText(response.body().getIdentityNumber().toString());
                        date_of_birth.setText(response.body().getDateOfBirth().toString());
                        license_number.setText(response.body().getUploadLicense().toString());
                        expiry_date.setText(response.body().getExpiryDate().toString());
                        country_of_issue.setText(response.body().getCountryOfIssue().toString());
                        code.setText(response.body().getCode().toString());
                        restrictions.setText(response.body().getRestrictions().toString());
                        gender.setText(response.body().getGender().toString());
                        date_of_issue.setText(response.body().getDateOfIssue().toString());
                        uploadLicense.setText(response.body().getUploadLicense().toString());
                        is_approved.setChecked(response.body().getIsApproved());

                        btnAddDriversLicense.setText("update");

                        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("driversLicense_id", response.body().getId().toString());
                        editor.apply();

                    }else {


                        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("driversLicense_id", "-1");
                        editor.apply();
                    }



                }
                else {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("driversLicense_id", "-1");
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<DriversLicenseResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void add_updateDriversLicense(String driversLicense_owner,String is_approved ) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody c_wdriversLicense_owner = RequestBody.create(MediaType.parse("multipart/form-data"), driversLicense_owner );
        RequestBody c_is_approved = RequestBody.create(MediaType.parse("multipart/form-data"), is_approved );

        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call=  apiService.updateLicenseApproval(c_wdriversLicense_owner,c_is_approved);

        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){

                    if(response.body().getStatusCode().toString().equals("201")) {
//                        Toast.makeText(getApplicationContext(), "approval updated", Toast.LENGTH_SHORT).show();
                    }else{
//                        Toast.makeText(getApplicationContext(), "Error updating approval", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }



}