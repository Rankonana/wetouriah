package com.example.wetouriah;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

public class TrackParcel extends AppCompatActivity {


    EditText txtID;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_parcel);

        //
        setTitle("Track Parcel");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //



        txtID = findViewById(R.id.txtID);
        btnSearch = findViewById(R.id.btnSearch);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadLocation(txtID.getText().toString());
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
            else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadLocation(String id) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody pick_id= RequestBody.create(MediaType.parse("multipart/form-data"), id);

        APIService apiService = retrofit.create(APIService.class);
        Call<PickupResponse> call = apiService.getPickUp(pick_id);

        call.enqueue(new Callback<PickupResponse>() {
            @Override
            public void onResponse(Call<PickupResponse> call, Response<PickupResponse> response) {

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Your parcel is in: \n  "+response.body().getLastKnownLocation(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PickupResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}