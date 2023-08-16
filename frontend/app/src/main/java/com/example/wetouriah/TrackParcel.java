package com.example.wetouriah;

import static com.example.wetouriah.CustomerPortal.NEXT_SCREEN;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrackParcel extends AppCompatActivity {


    RelativeLayout  lyLocations;
    CardView cardSearch,cardLocations;
    EditText txtID;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_parcel);

        setTitle("Track Parcel");


        txtID = findViewById(R.id.txtID);
        btnSearch = findViewById(R.id.btnSearch);

        cardSearch = findViewById(R.id.cardSearch);
        cardLocations = findViewById(R.id.cardLocations);



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardSearch.setVisibility(View.GONE);
                cardLocations.setVisibility(View.VISIBLE);
//                loadLocation(txtID.getText().toString());
                LoadLocations();
            }
        });
    }



    private void LoadLocations(){

        RecyclerView recyclerView = findViewById(R.id.rvLocations);

        recyclerView.setLayoutManager(new LinearLayoutManager(TrackParcel.this));

        List<LocationsItem> LocationsItems = new ArrayList<LocationsItem>();

        LocationsItems.add(new LocationsItem("Durban",
                    "27 May\n 14:00","Pickup at proceesing facility")
            );
            LocationsItems.add(new LocationsItem("Bethlehem",
                    "26 May\n 16:00","In Transit from the warehouse")
            );
            LocationsItems.add(new LocationsItem("Paul Ra",
                    "26 May\n 09:00", "Pickuped from the Customer")
            );
        LocationsItems.add(new LocationsItem("Bloemfontein",
                "25 May\n 12:00", "Pickuped from the Customer")
        );
        LocationsItems.add(new LocationsItem("kimberly",
                "24 May\n 15:00", "Pickuped from the Customer")
        );
        LocationsItems.add(new LocationsItem("Matiele",
                "23 May\n 16:00", "Pickuped from the Customer")
        );
        LocationsItems.add(new LocationsItem("KWT",
                "19 May\n 14:00", "Pickuped from the Customer")
        );
        LocationsItems.add(new LocationsItem("East London",
                "18 May\n 08:00", "Pickuped from the Customer")
        );
        LocationsItems.add(new LocationsItem("PE",
                "12 May\n 09:00", "Pickuped from the Customer")
        );
        LocationsItems.add(new LocationsItem("Port St Johnss",
                "09 May\n 09:00", "Pickuped from the Customer")
        );


        final AdapterLocations adapterPickUpRequest = new AdapterLocations(getApplicationContext(),LocationsItems);

        recyclerView.setAdapter(adapterPickUpRequest);


    }

//    public void loadLocation(String id) {
//
//        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
//                .addConverterFactory(GsonConverterFactory.create()).build();
//
//        RequestBody pick_id= RequestBody.create(MediaType.parse("multipart/form-data"), id);
//
//        APIService apiService = retrofit.create(APIService.class);
//        Call<PickupResponse> call = apiService.getPickUp(pick_id);
//
//        call.enqueue(new Callback<PickupResponse>() {
//            @Override
//            public void onResponse(Call<PickupResponse> call, Response<PickupResponse> response) {
//
//                if(response.isSuccessful()){
//                    Toast.makeText(getApplicationContext(), "Your parcel is in: \n  "+response.body().getLastKnownLocation(), Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<PickupResponse> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        // Create an intent for the default activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack
        startActivity(intent);
        finish(); // Finish the current activity
    }
}