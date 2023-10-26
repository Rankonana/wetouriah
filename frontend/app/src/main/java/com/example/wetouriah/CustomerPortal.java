package com.example.wetouriah;

import static com.example.wetouriah.Constants.generateTrackingNumber;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerPortal extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    FloatingActionButton mAddFab;




    TextView headerUsername;

    private Handler handler;
    private Runnable runnable;
    private boolean isHandlerRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_portal);
        setTitle("Customer Portal");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        mAddFab = findViewById(R.id.add_fab);



        View headerView = navigationView.getHeaderView(0);
        headerUsername =  headerView.findViewById(R.id.username);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);
        String username = sharedPreferences.getString("username", null);
        headerUsername.setText("Hello, " + username);




        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        startPeriodicApiCall();

        //mGetAllPickRequest();



        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHandlerRunning = false;
                Intent intent = new Intent(CustomerPortal.this, AddPickupRequest.class);
                startActivity(intent);
                finish();
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item clicks here
        switch (item.getItemId()) {
//            case R.id.nav_warehouses:
////              Intent intent = new Intent(CustomerPortal.this,ViewWarehouses.class);
//                Intent wa = new Intent(CustomerPortal.this,TitleOnAmap.class);
//                startActivity(wa);
//                finish();
//                break;
            case R.id.nav_track:
                Intent track = new Intent(CustomerPortal.this,TrackParcel.class);
                startActivity(track);
                finish();
                break;
            case R.id.password:
                changePassword();
                break;
            case R.id.nav_loginout:
                Logout();
                break;
            case R.id.nav_inner_profile:
                Intent intent = new Intent(CustomerPortal.this,Profile.class);
                startActivity(intent);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



//
    private void Logout(){
        isHandlerRunning = false;
        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Clear all SharedPreferences data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(CustomerPortal.this,MainActivity.class);
        startActivity(intent);
        finish();
    }





    public void mGetAllPickRequest(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", null);

        RequestBody get_customer= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);


        Call<List<RequestPickupResponse>> call = apiService.getAllUserRequest(get_customer);

        call.enqueue(new Callback<List<RequestPickupResponse>>() {
            @Override
            public void onResponse(Call<List<RequestPickupResponse>> call, Response<List<RequestPickupResponse>> response) {
                if (response.isSuccessful()) {
                    List<RequestPickupResponse> objects = response.body();



                    if (objects != null) {

                        RecyclerView recyclerView = findViewById(R.id.rvAllDeliveries);

                        List<PickUpRequestItem> pickUpRequestItems = new ArrayList<PickUpRequestItem>();




                        for (RequestPickupResponse object : objects) {

                            String duration, rating,start_datetime,end_datetime,car;

                            if(object.getDuration() == null){
                                duration="";
                            }else{
                                duration = object.getDuration().toString();
                            }
                            if(object.getRating()  == null){
                                rating="";
                            }else{
                                rating = object.getRating().toString();
                            }
                            if(object.getStartDatetime()  == null){
                                start_datetime="";
                            }else{
                                start_datetime= object.getStartDatetime().toString();
                            }
                            if(object.getEndDatetime()  == null){
                                end_datetime="";
                            }else{
                                end_datetime=object.getEndDatetime().toString();
                            }
                            if(object.getCar()  == null){
                                car="";
                            }else{
                                car= object.getCar().toString();
                            }

                            pickUpRequestItems.add(new PickUpRequestItem(object.getId().toString(),
                                    object.getCustomer().toString() ,
                                    object.getTrackingNumber(),
                                    object.getRequestTime() ,
                                    object.getRecipientName() ,
                                    object.getRecipientPhone(),
                                    object. getPickupLocation() ,
                                    object.getDropoffLocation(),
                                    object.getVolume() ,
                                    object.getWeight() ,
                                    object.getParcelDescription(),
                                    object.getPriceToPay() ,
                                    object.getStatus().toString() ,
                                    car,
                                    duration,
                                    rating ,
                                    start_datetime ,
                                    end_datetime)
                            );



                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerPortal.this));

                        final AdapterPickUpRequest adapterPickUpRequest = new AdapterPickUpRequest(getApplicationContext(),pickUpRequestItems);
                        recyclerView.setAdapter(adapterPickUpRequest);


                        adapterPickUpRequest.setOnClickListener(new AdapterPickUpRequest.OnClickListener() {
                            @Override
                            public void onClick(int position, PickUpRequestItem model) {
                                Intent intent = new Intent(CustomerPortal.this, PickUpRequestDetailed.class);
                                intent.putExtra("model", model);
                                startActivity(intent);
                                finish();

                            }
                        });

                    }else{
                        Toast.makeText(CustomerPortal.this, "objects null", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(CustomerPortal.this, "response not successfully", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<List<RequestPickupResponse>> call, Throwable t) {
                // Handle failure
                Toast.makeText(CustomerPortal.this, "failure", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void startPeriodicApiCall() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                if(isHandlerRunning){
                    // Call your API method here
                    mGetAllPickRequest();

                    // Schedule the next execution after a delay
                    handler.postDelayed(this, 3000); // Execute every 5 seconds

                }
 }
        };

        // Start the initial execution
        handler.post(runnable);
    }

    public void changePassword()
    {
        isHandlerRunning = false;
        Intent intent = new Intent(getApplicationContext(),ResetPassword.class);
        intent.putExtra("password", "password");
        startActivity(intent);
        finish();
    }





}