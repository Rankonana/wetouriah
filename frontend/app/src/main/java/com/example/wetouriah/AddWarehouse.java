package com.example.wetouriah;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Part;

public class AddWarehouse extends AppCompatActivity {

    String warehouse_owner;
    private String imagepath;
    ImageView image;
    EditText address,volume,operating_hours;

    CheckBox cctv,armed_response,fire_safety_and_management,parking_space,is_approved;

    Button btnAddWarehouse;

    private static final String TAG = "AddWarehouse";

    String user_id,warehouse_id,role;
    private FloatingActionButton selectImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_warehouse);

        //
        setTitle("Your Warehouse");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //


        image = findViewById(R.id.image);
        address = findViewById(R.id.address);
        volume = findViewById(R.id.volume);
        cctv = findViewById(R.id.cctv);
        armed_response = findViewById(R.id.armed_response);
        fire_safety_and_management = findViewById(R.id.fire_safety_and_management);
        parking_space = findViewById(R.id.parking_space);
        operating_hours = findViewById(R.id.operating_hours);
        is_approved = findViewById(R.id.is_approved);

        btnAddWarehouse = findViewById(R.id.btnAddWarehouse);

        selectImageButton = findViewById(R.id.selectImageButton);

        //
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", null);
        warehouse_id = sharedPreferences.getString("warehouse_id", null);
        role = sharedPreferences.getString("role", null);



        try {


            WareHouseItemAdmin warehouse = (WareHouseItemAdmin) getIntent().getSerializableExtra("warehouse");
            if(warehouse != null){
                user_id = warehouse.getWarehouse_owner().toString();
                warehouse_id = warehouse.getId().toString();
                loadwarehouse(user_id);


                if (role.equals("1")) {
                    is_approved.setEnabled(true);
                }
            }
            else {
                loadwarehouse(user_id);

            }

        } catch (Exception e) {

        }

        //

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(AddWarehouse.this)
                        .crop()
                        .cropOval()
                        .maxResultSize(512,512)
                        .provider(ImageProvider.BOTH)
                        .start();


            }
        });

        btnAddWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if there warehouse id update
                    //if there is imagepath
                    //else do with image path
                //else add
                    //if there is imagepath
                    //else do with image path

//                Toast.makeText(getApplicationContext(), "imagepath :"+ imagepath, Toast.LENGTH_SHORT).show();


                if (imagepath != null && !imagepath.isEmpty()) {

                         if(warehouse_id != null && !warehouse_id.isEmpty()){
//                             Toast.makeText(getApplicationContext(), "warehouse_id :"+ warehouse_id, Toast.LENGTH_SHORT).show();
//                             Toast.makeText(getApplicationContext(), "user :"+ user_id, Toast.LENGTH_SHORT).show();
//                             Toast.makeText(getApplicationContext(), "imagepath :"+ imagepath, Toast.LENGTH_SHORT).show();

                             updateWarehouseImage(Integer.parseInt(warehouse_id),user_id,address.getText().toString(),volume.getText().toString(),
                                     String.valueOf(cctv.isChecked()),String.valueOf(armed_response.isChecked()),
                                     String.valueOf(fire_safety_and_management.isChecked()),String.valueOf(parking_space.isChecked()),
                                     operating_hours.getText().toString(),String.valueOf(is_approved.isChecked()));

                         }else{
                             addWarehouseImage(user_id,address.getText().toString(),volume.getText().toString(),
                                     String.valueOf(cctv.isChecked()),String.valueOf(armed_response.isChecked()),
                                     String.valueOf(fire_safety_and_management.isChecked()),String.valueOf(parking_space.isChecked()),
                                     operating_hours.getText().toString(),String.valueOf(is_approved.isChecked()));
                         }



                     } else {
                         if(warehouse_id != null && !warehouse_id.isEmpty()){
//                             Toast.makeText(getApplicationContext(), "warehouse_id :"+ warehouse_id, Toast.LENGTH_SHORT).show();
//                             Toast.makeText(getApplicationContext(), "user :"+ user_id, Toast.LENGTH_SHORT).show();

                             updateWarehouseNoImage(Integer.parseInt(warehouse_id),user_id,address.getText().toString(),volume.getText().toString(),
                                     String.valueOf(cctv.isChecked()),String.valueOf(armed_response.isChecked()),
                                     String.valueOf(fire_safety_and_management.isChecked()),String.valueOf(parking_space.isChecked()),
                                     operating_hours.getText().toString(),String.valueOf(is_approved.isChecked()));


                         }else{
                             addWarehouseNoImage(user_id,address.getText().toString(),volume.getText().toString(),
                                     String.valueOf(cctv.isChecked()),String.valueOf(armed_response.isChecked()),
                                     String.valueOf(fire_safety_and_management.isChecked()),String.valueOf(parking_space.isChecked()),
                                     operating_hours.getText().toString(),String.valueOf(is_approved.isChecked()));

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




    public void updateWarehouseImage(int warehouse_id,String warehouse_owner,String address,
                                    String volume,String cctv, String armed_response,String fire_safety_and_management,
                                    String parking_space, String operating_hours,String is_Approved ) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        File file = new File(imagepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imagefield = MultipartBody.Part.createFormData("image", file.getName(), requestFile);


        RequestBody user_warehouse_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(warehouse_id) );
        RequestBody user_warehouse_owner = RequestBody.create(MediaType.parse("multipart/form-data"), warehouse_owner);

        RequestBody user_address = RequestBody.create(MediaType.parse("multipart/form-data"), address);
        RequestBody user_volume = RequestBody.create(MediaType.parse("multipart/form-data"), volume);

        RequestBody user_cctv = RequestBody.create(MediaType.parse("multipart/form-data"), cctv);
        RequestBody user_armed_response = RequestBody.create(MediaType.parse("multipart/form-data"), armed_response);
        RequestBody user_fire_safety_and_management = RequestBody.create(MediaType.parse("multipart/form-data"), fire_safety_and_management);
        RequestBody user_parking_space= RequestBody.create(MediaType.parse("multipart/form-data"), parking_space);
        RequestBody user_operating_hours = RequestBody.create(MediaType.parse("multipart/form-data"),operating_hours);
        RequestBody user_isApproved = RequestBody.create(MediaType.parse("multipart/form-data"),is_Approved);





        APIService apiService = retrofit.create(APIService.class);


        Call<APIResponse> call =  apiService.updateWarehouseWithImage(user_warehouse_id,user_warehouse_owner,imagefield,user_address,user_volume,user_cctv,user_armed_response,user_fire_safety_and_management,user_parking_space,user_operating_hours,user_isApproved);


        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){

                    if(response.body().getStatusCode().toString().equals("201")) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();


                    }else{
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "test");

                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void addWarehouseImage(String warehouse_owner,String address,
                                           String volume,String cctv, String armed_response,String fire_safety_and_management,
                                           String parking_space, String operating_hours,String is_Approved ) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        File file = new File(imagepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imagefield = MultipartBody.Part.createFormData("image", file.getName(), requestFile);



//        RequestBody user_warehouse_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(warehouse_id) );
        RequestBody user_warehouse_owner = RequestBody.create(MediaType.parse("multipart/form-data"), warehouse_owner);

        RequestBody user_address = RequestBody.create(MediaType.parse("multipart/form-data"), address);
        RequestBody user_volume = RequestBody.create(MediaType.parse("multipart/form-data"), volume);

        RequestBody user_cctv = RequestBody.create(MediaType.parse("multipart/form-data"), cctv);
        RequestBody user_armed_response = RequestBody.create(MediaType.parse("multipart/form-data"), armed_response);
        RequestBody user_fire_safety_and_management = RequestBody.create(MediaType.parse("multipart/form-data"), fire_safety_and_management);
        RequestBody user_parking_space= RequestBody.create(MediaType.parse("multipart/form-data"), parking_space);
        RequestBody user_operating_hours = RequestBody.create(MediaType.parse("multipart/form-data"),operating_hours);
        RequestBody user_isApproved = RequestBody.create(MediaType.parse("multipart/form-data"),is_Approved);





        APIService apiService = retrofit.create(APIService.class);


        Call<APIResponse> call =  apiService.addWarehouseWithImage(user_warehouse_owner,imagefield,user_address,user_volume,user_cctv,user_armed_response,user_fire_safety_and_management,user_parking_space,user_operating_hours,user_isApproved);


        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){

                    if(response.body().getStatusCode().toString().equals("201")) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();


                    }else{
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "test");

                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }
    public void updateWarehouseNoImage(int warehouse_id,String warehouse_owner,String address,
                                           String volume,String cctv, String armed_response,String fire_safety_and_management,
                                           String parking_space, String operating_hours,String is_Approved ) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();




        RequestBody user_warehouse_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(warehouse_id) );
        RequestBody user_warehouse_owner = RequestBody.create(MediaType.parse("multipart/form-data"), warehouse_owner);

        RequestBody user_address = RequestBody.create(MediaType.parse("multipart/form-data"), address);
        RequestBody user_volume = RequestBody.create(MediaType.parse("multipart/form-data"), volume);

        RequestBody user_cctv = RequestBody.create(MediaType.parse("multipart/form-data"), cctv);
        RequestBody user_armed_response = RequestBody.create(MediaType.parse("multipart/form-data"), armed_response);
        RequestBody user_fire_safety_and_management = RequestBody.create(MediaType.parse("multipart/form-data"), fire_safety_and_management);
        RequestBody user_parking_space= RequestBody.create(MediaType.parse("multipart/form-data"), parking_space);
        RequestBody user_operating_hours = RequestBody.create(MediaType.parse("multipart/form-data"),operating_hours);
        RequestBody user_isApproved = RequestBody.create(MediaType.parse("multipart/form-data"),is_Approved);





        APIService apiService = retrofit.create(APIService.class);


        Call<APIResponse> call =  apiService.updateWarehouseNoImage(user_warehouse_id,user_warehouse_owner,user_address,user_volume,user_cctv,user_armed_response,user_fire_safety_and_management,user_parking_space,user_operating_hours,user_isApproved);


        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){

                    if(response.body().getStatusCode().toString().equals("201")) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();


                    }else{
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "test");

                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }
    public void addWarehouseNoImage(String warehouse_owner,String address,
                                           String volume,String cctv, String armed_response,String fire_safety_and_management,
                                           String parking_space, String operating_hours,String is_Approved ) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();




//        RequestBody user_warehouse_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(warehouse_id) );
        RequestBody user_warehouse_owner = RequestBody.create(MediaType.parse("multipart/form-data"), warehouse_owner);

        RequestBody user_address = RequestBody.create(MediaType.parse("multipart/form-data"), address);
        RequestBody user_volume = RequestBody.create(MediaType.parse("multipart/form-data"), volume);

        RequestBody user_cctv = RequestBody.create(MediaType.parse("multipart/form-data"), cctv);
        RequestBody user_armed_response = RequestBody.create(MediaType.parse("multipart/form-data"), armed_response);
        RequestBody user_fire_safety_and_management = RequestBody.create(MediaType.parse("multipart/form-data"), fire_safety_and_management);
        RequestBody user_parking_space= RequestBody.create(MediaType.parse("multipart/form-data"), parking_space);
        RequestBody user_operating_hours = RequestBody.create(MediaType.parse("multipart/form-data"),operating_hours);
        RequestBody user_isApproved = RequestBody.create(MediaType.parse("multipart/form-data"),is_Approved);





        APIService apiService = retrofit.create(APIService.class);


        Call<APIResponse> call =  apiService.addWarehouseNoImage(user_warehouse_owner,user_address,user_volume,user_cctv,user_armed_response,user_fire_safety_and_management,user_parking_space,user_operating_hours,user_isApproved);


        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){

                    if(response.body().getStatusCode().toString().equals("201")) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();


                    }else{
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "test");

                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void loadwarehouse(String id) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody user_id= RequestBody.create(MediaType.parse("multipart/form-data"), id);



        APIService apiService = retrofit.create(APIService.class);
        Call<WarehouseResponse> call = apiService.getWarehouse(user_id);

        call.enqueue(new Callback<WarehouseResponse>() {
            @Override
            public void onResponse(Call<WarehouseResponse> call, Response<WarehouseResponse> response) {

                if(response.isSuccessful()){
                    Picasso.get().load( "http://" + Constants.SERVER_IP_ADDRESS+ ":8000"+"/" +response.body().getImage()).into(image);

                    address.setText(response.body().getAddress());
                    volume.setText(response.body().getVolume());
                    cctv.setChecked(response.body().getCctv());
                    armed_response.setChecked(response.body().getArmedResponse());
                    fire_safety_and_management.setChecked(response.body().getFireSafetyAndManagement());
                    parking_space.setChecked(response.body().getParkingSpace());
                    is_approved.setChecked(response.body().getIsApproved());


                    operating_hours.setText(response.body().getOperatingHours());

                    btnAddWarehouse.setText("update");

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("warehouse_id", response.body().getId().toString());
                    editor.apply();
                    SetWarehouseId(response.body().getId().toString());

                }
            }

            @Override
            public void onFailure(Call<WarehouseResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }
    private void SetWarehouseId(String warehouseId){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("warehouse_id", warehouse_id);
        editor.apply();
        warehouse_id = warehouseId;


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