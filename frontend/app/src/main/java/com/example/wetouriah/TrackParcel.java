package com.example.wetouriah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        txtID = findViewById(R.id.txtID);
        btnSearch = findViewById(R.id.btnSearch);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadLocation(txtID.getText().toString());
            }
        });
    }

    public void loadLocation(String id) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
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