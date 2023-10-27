package com.example.wetouriah;

//import static com.example.wetouriah.CustomerPortal.NEXT_SCREEN;

import static com.example.wetouriah.Constants.formatTrackingLogDateString;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

    TextView txtStatus,estTime;
    PickUpRequestItem pickUpRequestItem;
    String status_Intent;

    String parcelExist="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_parcel);

        setTitle("Track Parcel");


        txtID = findViewById(R.id.txtID);
        btnSearch = findViewById(R.id.btnSearch);

        cardSearch = findViewById(R.id.cardSearch);
        cardLocations = findViewById(R.id.cardLocations);
        txtStatus = findViewById(R.id.txtStatus);
        estTime = findViewById(R.id.estTime);
        Intent intent = getIntent();
        pickUpRequestItem = (PickUpRequestItem) intent.getSerializableExtra("model");

        if (pickUpRequestItem != null) {

            status_Intent = pickUpRequestItem.getStatus();

            if(status_Intent.equals("1")){
                status_Intent = "Pending";
            }
            if(status_Intent.equals("2")){
                status_Intent = "Pick-up";
            }
            if(status_Intent.equals("3")){
                status_Intent = "Delivered";
            }
            if(status_Intent.equals("4")){
                status_Intent = "In-Transit";
            }
            if(status_Intent.equals("5")){
                status_Intent = "Driver-assigned";
            }
            if(status_Intent.equals("6")){
                status_Intent = "Out for Delivery";
            }
            if(status_Intent.equals("7")){
                status_Intent = "Returned";
            }

            txtStatus.setText("Status: "+status_Intent);

            txtID.setText(pickUpRequestItem.getTracking_number());



        }



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( txtID.getText().length() >0){
                    mCheckParcelExistence(txtID.getText().toString());


                    ////
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(parcelExist.length() >0){
                                Toast.makeText(TrackParcel.this, parcelExist, Toast.LENGTH_SHORT).show();

                                parcelExist = "";

                            }else{
                                cardSearch.setVisibility(View.GONE);
                                cardLocations.setVisibility(View.VISIBLE);
                                loadTracking(txtID.getText().toString());

                            }

                        }
                    },3000);

                    ////



                }else {
                    Toast.makeText(TrackParcel.this, "Enter tracking number", Toast.LENGTH_SHORT).show();

                }





            }
        });
    }



    public void loadTracking(String tracking_number){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", null);

        RequestBody get_tracking_number= RequestBody.create(MediaType.parse("multipart/form-data"), tracking_number);


        Call<List<TrackingLogResponse>> call = apiService.getTrackingLog(get_tracking_number);

        call.enqueue(new Callback<List<TrackingLogResponse>>() {
            @Override
            public void onResponse(Call<List<TrackingLogResponse>> call, Response<List<TrackingLogResponse>> response) {
                if (response.isSuccessful()) {
                    List<TrackingLogResponse> objects = response.body();



                    if (objects != null) {

                        RecyclerView recyclerView = findViewById(R.id.rvLocations);

                        List<LocationsItem> locationsItems = new ArrayList<LocationsItem>();


                        for (TrackingLogResponse object : objects) {

//                            locationsItems.add(new LocationsItem(object.getLocation(),
//                                    object.getTimestamp(),object.getStatusName())
//                            );

                            locationsItems.add(new LocationsItem(object.getLocation(),
                                    formatTrackingLogDateString(object.getTimestamp()),object.getStatusName(),object.getEstimatedArrival())
                            );

                        }
                        if (!locationsItems.isEmpty()) {
                            // Get the last item from the list
                            LocationsItem lastItem = locationsItems.get(0);;

                            // Get the status from the last item
                            String status = lastItem.getStatus();

                            // Display the status in a Toast message
                            txtStatus.setText("Status: " + status);
                            if(lastItem.getEstimatedArrival().contains("hrs")){
                                estTime.setText("Estimated arrival time: "  + lastItem.getEstimatedArrival());

                            }else {
                                estTime.setVisibility(View.GONE);
                            }
                        }


                        recyclerView.setLayoutManager(new LinearLayoutManager(TrackParcel.this));

                        final AdapterLocations adapterPickUpRequest = new AdapterLocations(getApplicationContext(),locationsItems);
                        recyclerView.setAdapter(adapterPickUpRequest);


                    }else{
                        Toast.makeText(TrackParcel.this, "objects null", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(TrackParcel.this, "response not successfully", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<List<TrackingLogResponse>> call, Throwable t) {
                // Handle failure
                Toast.makeText(TrackParcel.this, "failure", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void mCheckParcelExistence(String tracking_number) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody g_tracking_number= RequestBody.create(MediaType.parse("multipart/form-data"), tracking_number);

        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.apiCheckParcelExistence(g_tracking_number);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){
                    parcelExist = response.body().getMessage();
                    //Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        // Create an intent for the default activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack
        startActivity(intent);
        finish(); // Finish the current activity
    }
}