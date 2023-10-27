package com.example.wetouriah;

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

public class AddCar extends AppCompatActivity {

    EditText car_owner,type,capacity,color, make, model,year,license_plate;
    CheckBox is_approved;
    Button btnAddCar;

    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        setTitle("Your Car");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        type = findViewById(R.id.type);
        capacity = findViewById(R.id.capacity);
        color = findViewById(R.id.color);
        make = findViewById(R.id.make);
        model = findViewById(R.id.model);
        year = findViewById(R.id.year);
        license_plate = findViewById(R.id.license_plate);
        is_approved = findViewById(R.id.is_approved);
        btnAddCar = findViewById(R.id.btnAddCar);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", null);

        try {
            loadCar(user_id);
        } catch (Exception e) {

        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/") // Replace with your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);


        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                String user_id = sharedPreferences.getString("user_id", null);
                String car_id = sharedPreferences.getString("car_id", null);



                if (!car_id.equals("-1")) {



                    CarItem updateCarItem = new CarItem(car_id,user_id,type.getText().toString(),capacity.getText().toString(),color.getText().toString(),
                            make.getText().toString(),model.getText().toString(),year.getText().toString(),license_plate.getText().toString(),is_approved.getText().toString());
                    mupdateCar(updateCarItem);

                } else {

                    CarItem newCarItem = new CarItem(car_id,user_id,type.getText().toString(),capacity.getText().toString(),color.getText().toString(),
                            make.getText().toString(),model.getText().toString(),year.getText().toString(),license_plate.getText().toString(),is_approved.getText().toString());
                    addCar(newCarItem);

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
            Call<CarResponse> call = apiService.addCar(get_car_owner,get_type,
                    get_capacity,get_color,get_make,get_model,get_year,get_icense_plate,get_is_approved);

        call.enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Car added succesfully", Toast.LENGTH_SHORT).show();
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

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String car_id = sharedPreferences.getString("car_id", null);
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

        Call<CarResponse> call = apiService.updateCar(get_id,get_car_owner,get_type,
                get_capacity,get_color,get_make,get_model,get_year,get_icense_plate,get_is_approved);
        call.enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Car updated succesfully", Toast.LENGTH_SHORT).show();
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

    public void loadCar(String id) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody user_id= RequestBody.create(MediaType.parse("multipart/form-data"), id);

        APIService apiService = retrofit.create(APIService.class);
        Call<CarResponse> call = apiService.loadCar(user_id);

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
                        btnAddCar.setText("update");

                        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("car_id", response.body().getId().toString());
                        editor.apply();

                    }else {


                        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("car_id", "-1");
                        editor.apply();
                    }



                }
                else {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("car_id", "-1");
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}