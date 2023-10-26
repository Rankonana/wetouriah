package com.example.wetouriah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class AddCar extends AppCompatActivity {

    private static final String TAG = "AddCar";

    EditText car_owner,type,capacity,color, make, model,year,license_plate;
    CheckBox is_approved,is_default;
    Button btnAddCar;

    private APIService apiService;

    String user_id,role,car_id;

    CarItem car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        setTitle("Your Vehicle");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        type = findViewById(R.id.type);
        capacity = findViewById(R.id.capacity);
        color = findViewById(R.id.color);
        make = findViewById(R.id.make);
        model = findViewById(R.id.model);
        year = findViewById(R.id.year);
        license_plate = findViewById(R.id.license_plate);
        is_approved = findViewById(R.id.is_approved);
        is_default = findViewById(R.id.is_default);
        btnAddCar = findViewById(R.id.btnAddCar);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", null);
        car_id = sharedPreferences.getString("car_id", null);
        role = sharedPreferences.getString("role", null);


        try {


            car = (CarItem) getIntent().getSerializableExtra("car");
            if(car != null){
                Log.e(TAG, "car not null");

                Log.e(TAG, car.getCar_owner().toString());
                Log.e(TAG, car.getId().toString());
                Log.e(TAG, String.valueOf(car));

                user_id = car.getCar_owner().toString();
                car_id =  car.getId().toString();
                loadCar(car_id);

                if (role.equals("1")) {
                    is_approved.setEnabled(true);
                }
            }
            else {
                Log.e(TAG, "car null");
               // loadCar(car_id.toString());

            }

        } catch (Exception e) {

        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/") // Replace with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);


        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "car_id : " + car_id);

                if(car != null) {
                    Log.e(TAG, "car_id : " + car_id);


                    CarItem updateCarItem = new CarItem(car_id,user_id,type.getText().toString(),capacity.getText().toString(),color.getText().toString(),
                            make.getText().toString(),model.getText().toString(),year.getText().toString(),license_plate.getText().toString(),String.valueOf(is_approved.isChecked()),String.valueOf(is_default.isChecked()));
                    mupdateCar(updateCarItem);

                } else {

                    Log.e(TAG, "car_id : " + car_id);
                    CarItem newCarItem = new CarItem(car_id,user_id,type.getText().toString(),capacity.getText().toString(),color.getText().toString(),
                            make.getText().toString(),model.getText().toString(),year.getText().toString(),license_plate.getText().toString(),String.valueOf(is_approved.isChecked()),String.valueOf(is_default.isChecked()));
                    addCar(newCarItem);

                }

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


    private void addCar(CarItem carItem) {

//            RequestBody get_id = RequestBody.create(MediaType.parse("multipart/form-data"), "-1");
        RequestBody get_car_owner =  RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getCar_owner()));
        RequestBody get_type = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getType()));
        RequestBody  get_capacity = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getCapacity()));
        RequestBody get_color = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getColor()));
        RequestBody get_make = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getMake()));
        RequestBody get_model = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getModel()));
        RequestBody get_year = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getYear()));
        RequestBody get_icense_plate = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getLicense_plate()));
        RequestBody get_is_approved = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getIs_approved()));
        RequestBody get_is_default = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getIs_Default()));

        Call<CarResponse> call = apiService.addCar(get_car_owner,get_type,
                get_capacity,get_color,get_make,get_model,get_year,get_icense_plate,get_is_approved,get_is_default);

        call.enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "vehicle added succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "there was error adding your car", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mupdateCar(CarItem carItem) {

        carItem.setId(car_id);

        RequestBody get_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getId()));
        RequestBody get_car_owner =  RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getCar_owner()));
        RequestBody get_type = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getType()));
        RequestBody  get_capacity = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getCapacity()));
        RequestBody get_color = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getColor()));
        RequestBody get_make = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getMake()));
        RequestBody get_model = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getModel()));
        RequestBody get_year = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getYear()));
        RequestBody get_icense_plate = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getLicense_plate()));
        RequestBody get_is_approved = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getIs_approved()));
        RequestBody get_is_default = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(carItem.getIs_Default()));

        Call<CarResponse> call = apiService.updateCar(get_id,get_car_owner,get_type,
                get_capacity,get_color,get_make,get_model,get_year,get_icense_plate,get_is_approved,get_is_default);
        call.enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                if (response.isSuccessful()) {

//                    add_updateCar( car_id.toString(), String.valueOf(is_approved.isChecked()),String.valueOf(is_default.isChecked()));


                    Toast.makeText(getApplicationContext(), "vehicle updated succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "there was error updating your car", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadCar(String p_car_id) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody rb_car_id= RequestBody.create(MediaType.parse("multipart/form-data"), p_car_id);

        APIService apiService = retrofit.create(APIService.class);
        Call<CarResponse> call = apiService.loadCar(rb_car_id);

        call.enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {

                if(response.isSuccessful()){
                    if(Integer.parseInt(response.body().getId().toString()) > 0) {
                        type.setText(response.body().getType());
                        capacity.setText(response.body().getCapacity());
                        color.setText(response.body().getColor());
                        make.setText(response.body().getMake());
                        model.setText(response.body().getModel());
                        year.setText(response.body().getYear());
                        license_plate.setText(response.body().getLicensePlate());
                        is_approved.setChecked(response.body().getIsApproved());
                        is_default.setChecked(response.body().getIsDefault());

                        btnAddCar.setText("update");

//                        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("car_id", response.body().getId().toString());
//                        editor.apply();

                    }else {


//                        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("car_id", "-1");
//                        editor.apply();
                    }



                }
                else {
//                    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("car_id", "-1");
//                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void add_updateCar(String car_id,String is_approved,String is_default ) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody c_car_id = RequestBody.create(MediaType.parse("multipart/form-data"), car_id );
        RequestBody c_is_approved = RequestBody.create(MediaType.parse("multipart/form-data"), is_approved );
        RequestBody c_is_default = RequestBody.create(MediaType.parse("multipart/form-data"), is_default );

        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call=  apiService.updateCarApproval(c_car_id,c_is_approved,c_is_default);

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