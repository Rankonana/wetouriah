package com.example.wetouriah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DriverPortal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btnProfile, btnAddCar, btnRequestpickup, btnViewItems,btnLogout;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    TextView headerUsername;
    private TabLayout tab_layout ;
    private RelativeLayout lyAllUndelivered,lyUnRated, lyAllPickupRequests;

    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_portal);
        setTitle("Driver Portal");

        tab_layout = findViewById(R.id.tab_layout);
        lyAllUndelivered = findViewById(R.id.lyAllUndelivered);
        lyUnRated = findViewById(R.id.lyUnRated);
        lyAllPickupRequests = findViewById(R.id.lyAllPickupRequests);

        lyAllUndelivered.setVisibility(View.VISIBLE);
        lyUnRated.setVisibility(View.GONE);
        lyAllPickupRequests.setVisibility(View.GONE);

        //notification start
        startPeriodicApiCall();
        //notification end

        //
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //




        View headerView = navigationView.getHeaderView(0);
        headerUsername =  headerView.findViewById(R.id.username);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);
        String username = sharedPreferences.getString("username", null);
        headerUsername.setText("hello, " + username);




        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Show or hide the layout based on the selected tab
                if (tab.getPosition() == 0) {
                    lyAllUndelivered.setVisibility(View.VISIBLE);
                    lyUnRated.setVisibility(View.GONE);
                    lyAllPickupRequests.setVisibility(View.GONE);


                }
                if (tab.getPosition() == 1) {
                    lyAllUndelivered.setVisibility(View.GONE);
                    lyUnRated.setVisibility(View.VISIBLE);
                    lyAllPickupRequests.setVisibility(View.GONE);
                }
                if (tab.getPosition() == 2) {
                    lyAllUndelivered.setVisibility(View.GONE);
                    lyUnRated.setVisibility(View.GONE);
                    lyAllPickupRequests.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Optional: Handle any actions when a tab is unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Optional: Handle any actions when a tab is reselected
            }
        });









//        btnAddCar = (Button)findViewById(R.id.btnAddCar);
//        btnRequestpickup = (Button)findViewById(R.id.btnRequestpickup);
//        btnViewItems = (Button)findViewById(R.id.btnViewItems);

//        btnAddCar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DriverPortal.this,AddCar.class);
//                startActivity(intent);
//            }
//        });



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
            case R.id.nav_loginout:
                Logout();
                break;
            case R.id.nav_car:
                Intent intentCar = new Intent(DriverPortal.this,AddCar.class);
                startActivity(intentCar);
                break;
            case R.id.nav_inner_profile:
                Intent intent = new Intent(DriverPortal.this,Profile.class);
                startActivity(intent);
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void Logout(){

        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Clear all SharedPreferences data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();



        Intent intent = new Intent(DriverPortal.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

//    public void mGetPickRequestUnDelivered(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/") // Replace with your actual base URL
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        APIService apiService = retrofit.create(APIService.class);
//
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//        String user_id = sharedPreferences.getString("user_id", null);
//
//        RequestBody get_driver= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
//
//        Call<List<RequestPickupResponse>> call = apiService.getUnDeliveredDriverObjects(get_driver);
//        call.enqueue(new Callback<List<RequestPickupResponse>>() {
//            @Override
//            public void onResponse(Call<List<RequestPickupResponse>> call, Response<List<RequestPickupResponse>> response) {
//                if (response.isSuccessful()) {
//                    List<RequestPickupResponse> objects = response.body();
//
//                    if (objects != null) {
//                        RecyclerView recyclerView = findViewById(R.id.idUndelivered);
//                        List<PickUpRequestItem> pickUpRequestItems = new ArrayList<PickUpRequestItem>();
//
//                        for (RequestPickupResponse object : objects) {
//
//                            pickUpRequestItems.add(new PickUpRequestItem(object.getId().toString(), object.getCustomer().toString(),object.getRequestTime(),object.getUpdated(),
//                                    object.getDateAndTimePickup(),object.getDateAndTimeDropoff(),
//                                    object.getRecipientName().toString(),object.getRecipientPhone(),object.getPickupLocation(),
//                                    object.getDropoffLocation(),object.getVolume(),object.getWeight().toString(),object.getParcelDescription(),
//                                    object.getSpecialNotes(),object.getPriceToPay(),"http://" + Constants.SERVER_IP_ADDRESS+ ":8000"+"/media/profile_picture.jpg"));
//
//                        }
//                        recyclerView.setLayoutManager(new LinearLayoutManager(DriverPortal.this));
//
//                        final AdapterPickUpRequest adapterPickUpRequest = new AdapterPickUpRequest(getApplicationContext(),pickUpRequestItems);
//                        recyclerView.setAdapter(adapterPickUpRequest);
//                        adapterPickUpRequest.setOnClickListener(new AdapterPickUpRequest.OnClickListener() {
//                            @Override
//                            public void onClick(int position, PickUpRequestItem model) {
//                                Intent intent = new Intent(DriverPortal.this, PickUpRequestDetailed.class);
//                                intent.putExtra(NEXT_SCREEN, model);
//                                startActivity(intent);
//
//                            }
//                        });
////                        recyclerView.setAdapter(new AdapterPickUpRequest(getApplicationContext(), pickUpRequestItems));
//
//                    }
//
//                } else {
//                    // Handle error
//                }
//            }
//
//
//
//            @Override
//            public void onFailure(Call<List<RequestPickupResponse>> call, Throwable t) {
//                // Handle failure
//            }
//        });
//
//    }

//    public void mGetPickRequestUnRated(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/") // Replace with your actual base URL
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        APIService apiService = retrofit.create(APIService.class);
//
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//        String user_id = sharedPreferences.getString("user_id", null);
//
//        RequestBody get_driver= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
//
//
//        Call<List<RequestPickupResponse>> call = apiService.getUnRatedDriverObjects(get_driver);
//        call.enqueue(new Callback<List<RequestPickupResponse>>() {
//            @Override
//            public void onResponse(Call<List<RequestPickupResponse>> call, Response<List<RequestPickupResponse>> response) {
//                if (response.isSuccessful()) {
//                    List<RequestPickupResponse> objects = response.body();
//
//                    if (objects != null) {
//                        RecyclerView recyclerView = findViewById(R.id.idUnRated);
//                        List<PickUpRequestItem> pickUpRequestItems = new ArrayList<PickUpRequestItem>();
//
//                        for (RequestPickupResponse object : objects) {
//
//                            pickUpRequestItems.add(new PickUpRequestItem(object.getId().toString(), object.getCustomer().toString(),object.getRequestTime(),object.getUpdated(),
//                                    object.getDateAndTimePickup(),object.getDateAndTimeDropoff(),
//                                    object.getRecipientName().toString(),object.getRecipientPhone(),object.getPickupLocation(),
//                                    object.getDropoffLocation(),object.getVolume(),object.getWeight().toString(),object.getParcelDescription(),
//                                    object.getSpecialNotes(),object.getPriceToPay(),"http://" + Constants.SERVER_IP_ADDRESS+ ":8000"+"/media/profile_picture.jpg"));
//
//                        }
//                        recyclerView.setLayoutManager(new LinearLayoutManager(DriverPortal.this));
//
//                        final AdapterPickUpRequest adapterPickUpRequest = new AdapterPickUpRequest(getApplicationContext(),pickUpRequestItems);
//                        recyclerView.setAdapter(adapterPickUpRequest);
//                        adapterPickUpRequest.setOnClickListener(new AdapterPickUpRequest.OnClickListener() {
//                            @Override
//                            public void onClick(int position, PickUpRequestItem model) {
//                                Intent intent = new Intent(DriverPortal.this, PickUpRequestDetailed.class);
//                                intent.putExtra(NEXT_SCREEN, model);
//                                startActivity(intent);
//
//                            }
//                        });
////                        recyclerView.setAdapter(new AdapterPickUpRequest(getApplicationContext(), pickUpRequestItems));
//
//                    }
//
//                } else {
//                    // Handle error
//                }
//            }
//
//
//
//            @Override
//            public void onFailure(Call<List<RequestPickupResponse>> call, Throwable t) {
//                // Handle failure
//            }
//        });
//
//    }

//    public void mGetPickRequestAll(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/") // Replace with your actual base URL
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        APIService apiService = retrofit.create(APIService.class);
//
//
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//        String user_id = sharedPreferences.getString("user_id", null);
//
//        RequestBody get_driver= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
//
//
//        Call<List<RequestPickupResponse>> call = apiService.getAllDriverObjects(get_driver);
//        call.enqueue(new Callback<List<RequestPickupResponse>>() {
//            @Override
//            public void onResponse(Call<List<RequestPickupResponse>> call, Response<List<RequestPickupResponse>> response) {
//                if (response.isSuccessful()) {
//                    List<RequestPickupResponse> objects = response.body();
//
//                    if (objects != null) {
//                        RecyclerView recyclerView = findViewById(R.id.idAllPickupRequests);
//                        List<PickUpRequestItem> pickUpRequestItems = new ArrayList<PickUpRequestItem>();
//
//                        for (RequestPickupResponse object : objects) {
//
//                            pickUpRequestItems.add(new PickUpRequestItem(object.getId().toString(), object.getCustomer().toString(),object.getRequestTime(),object.getUpdated(),
//                                    object.getDateAndTimePickup(),object.getDateAndTimeDropoff(),
//                                    object.getRecipientName().toString(),object.getRecipientPhone(),object.getPickupLocation(),
//                                    object.getDropoffLocation(),object.getVolume(),object.getWeight().toString(),object.getParcelDescription(),
//                                    object.getSpecialNotes(),object.getPriceToPay(),"http://" + Constants.SERVER_IP_ADDRESS+ ":8000"+"/media/profile_picture.jpg"));
//
//                        }
//                        recyclerView.setLayoutManager(new LinearLayoutManager(DriverPortal.this));
//
//                        final AdapterPickUpRequest adapterPickUpRequest = new AdapterPickUpRequest(getApplicationContext(),pickUpRequestItems);
//                        recyclerView.setAdapter(adapterPickUpRequest);
//                        adapterPickUpRequest.setOnClickListener(new AdapterPickUpRequest.OnClickListener() {
//                            @Override
//                            public void onClick(int position, PickUpRequestItem model) {
//                                Intent intent = new Intent(DriverPortal.this, PickUpRequestDetailed.class);
//                                intent.putExtra(NEXT_SCREEN, model);
//                                startActivity(intent);
//
//                            }
//                        });
////                        recyclerView.setAdapter(new AdapterPickUpRequest(getApplicationContext(), pickUpRequestItems));
//
//                    }
//
//                } else {
//                    // Handle error
//                }
//            }
//
//
//
//            @Override
//            public void onFailure(Call<List<RequestPickupResponse>> call, Throwable t) {
//                // Handle failure
//            }
//        });
//
//    }

    public static final String NEXT_SCREEN = "details_screen";


    private void createNotification(PickUpRequestItem pickUpRequestItem) {
        // Create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create an explicit intent for the target activity
        Intent intent = new Intent(this, PickUpRequestDetailed.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NEXT_SCREEN, pickUpRequestItem);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        // Get the custom sound URI from the resource
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.success);


        // Create a notification
        Notification notification = new NotificationCompat.Builder(this, "my_channel")
                .setContentTitle("My Notification")
                .setContentText("You got an assignment")
                .setSmallIcon(R.drawable.notification)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Notify the user
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notification);
    }




//    public void mCheckAPI() {
//
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//        String user_id = sharedPreferences.getString("user_id", null);
//
//        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
//                .addConverterFactory(GsonConverterFactory.create()).build();
//
//        RequestBody get_driver= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
//        APIService apiService = retrofit.create(APIService.class);
//        Call<RequestPickupResponse> call = apiService.checkTaskAssignment(get_driver);
//
//        call.enqueue(new Callback<RequestPickupResponse>() {
//            @Override
//            public void onResponse(Call<RequestPickupResponse> call, Response<RequestPickupResponse> response) {
//
//                if(response.isSuccessful()){
//
//                    PickUpRequestItem pickUpRequestItem = new PickUpRequestItem(
//                            response.body().getId().toString(),
//                            response.body().getCustomer().toString(),
//                            response.body().getRequestTime().toString(),
//                            response.body().getUpdated().toString(),
//                            response.body().getDateAndTimePickup().toString(),
//                            response.body().getDateAndTimeDropoff().toString(),
//                            response.body().getRecipientName().toString(),
//                            response.body().getRecipientPhone().toString(),
//                            response.body().getPickupLocation().toString(),
//                            response.body().getDropoffLocation().toString(),
//                            response.body().getVolume().toString(),
//                            response.body().getWeight().toString(),
//                            response.body().getParcelDescription().toString(),
//                            response.body().getSpecialNotes().toString(),
//                            response.body().getPriceToPay().toString(),
//                            response.body().getImages().toString()
//                            );
//
//                    createNotification(pickUpRequestItem);
//
//
//
//
//
//                }
//            }
//            @Override
//            public void onFailure(Call<RequestPickupResponse> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }

    private void startPeriodicApiCall() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Call your API method here
//                mCheckAPI();
//                mGetPickRequestUnDelivered();
//                mGetPickRequestUnRated();
//                mGetPickRequestAll();

                // Schedule the next execution after a delay
                handler.postDelayed(this, 15000); // Execute every 5 seconds
            }
        };

        // Start the initial execution
        handler.post(runnable);
    }






}