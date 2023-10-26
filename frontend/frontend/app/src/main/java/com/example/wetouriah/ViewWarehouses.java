package com.example.wetouriah;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ViewWarehouses extends AppCompatActivity   {

    EditText txtID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_warehouses);
        txtID = findViewById(R.id.txtID);

        setTitle("Nearest Warehouses");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mWarehouses("");


        txtID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mWarehouses(txtID.getText().toString());
            }
        });

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
            else{
                // Navigate to the MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void mWarehouses(String location ) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);


        Call<List<WarehouseResponse>> call = apiService.getWarehousesObjects(location); // Use the new endpoint

        call.enqueue(new Callback<List<WarehouseResponse>>() {
            @Override
            public void onResponse(Call<List<WarehouseResponse>> call, Response<List<WarehouseResponse>> response) {
                if (response.isSuccessful()) {
                    List<WarehouseResponse> objects = response.body();

                    if (objects != null) {
                        RecyclerView recyclerView = findViewById(R.id.idWarehouses);
                        List<WareHouseItem> items = new ArrayList<WareHouseItem>();

                        for (WarehouseResponse object : objects) {
                            items.add(new WareHouseItem(object.getAddress().toString()));
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(ViewWarehouses.this));
                        recyclerView.setAdapter(new AdapterWarehouse(getApplicationContext(),items));
                    }

                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<List<WarehouseResponse>> call, Throwable t) {
                // Handle failure
            }
        });

    }





}