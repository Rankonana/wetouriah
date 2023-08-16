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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

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

public class WarehousePortal extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private TabLayout tab_layout ;
    private RelativeLayout lyAllUndelivered,lyUnRated, lyAllPickupRequests;

    //pick up re start
    EditText pickdateEditText,picktimeEditText,dropoffdateEditText,dropofftimeEditText;

    EditText recipient_name,recipient_phone,pickup_location,droff_location,
            volume,weight,parcel_description,special_notes,price_to_pay,customer,images;

    Button btnAddPickRequest,btnPrice, btnNotification;

    private int year, month, day, hour, minute;
    FloatingActionButton mAddFab;


    RelativeLayout lyPickRequestList,lyPrice;

    ScrollView lyAddPickRequest;

    //pick up re end

    TextView headerUsername;
    TextView qPrice;

    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_portal);
        setTitle("Warehouse Portal");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        tab_layout = findViewById(R.id.tab_layout);
        lyAllUndelivered = findViewById(R.id.lyAllUndelivered);
        lyUnRated = findViewById(R.id.lyUnRated);
        lyAllPickupRequests = findViewById(R.id.lyAllPickupRequests);

        lyPrice = findViewById(R.id.lyPrice);
        qPrice = findViewById(R.id.qPrice);


        lyAllUndelivered.setVisibility(View.VISIBLE);
        lyUnRated.setVisibility(View.GONE);
        lyAllPickupRequests.setVisibility(View.GONE);
        lyPrice.setVisibility(View.GONE);



        View headerView = navigationView.getHeaderView(0);
        headerUsername =  headerView.findViewById(R.id.username);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);
        String username = sharedPreferences.getString("username", null);
        headerUsername.setText("Hello, " + username);




        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Show or hide the layout based on the selected tab
                if (tab.getPosition() == 0) {
                    lyAllUndelivered.setVisibility(View.VISIBLE);
                    lyUnRated.setVisibility(View.GONE);
                    lyAllPickupRequests.setVisibility(View.GONE);
                    lyPrice.setVisibility(View.GONE);


                }
                if (tab.getPosition() == 1) {
                    lyAllUndelivered.setVisibility(View.GONE);
                    lyUnRated.setVisibility(View.VISIBLE);
                    lyAllPickupRequests.setVisibility(View.GONE);
                    lyPrice.setVisibility(View.GONE);

                }
                if (tab.getPosition() == 2) {
                    lyAllUndelivered.setVisibility(View.GONE);
                    lyUnRated.setVisibility(View.GONE);
                    lyAllPickupRequests.setVisibility(View.VISIBLE);
                    lyPrice.setVisibility(View.GONE);



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



        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        startPeriodicApiCall();


        pickdateEditText = findViewById(R.id.pickdateEditText);
        picktimeEditText = findViewById(R.id.picktimeEditText);

        dropoffdateEditText = findViewById(R.id.dropoffdateEditText);
        dropofftimeEditText = findViewById(R.id.dropofftimeEditText);


        recipient_name = findViewById(R.id.recipient_name);
        recipient_phone = findViewById(R.id.recipient_phone);
        pickup_location = findViewById(R.id.pickup_location);
        droff_location = findViewById(R.id.droff_location);
        volume = findViewById(R.id.volume);
        weight = findViewById(R.id.weight);
        parcel_description = findViewById(R.id.parcel_description);
        special_notes = findViewById(R.id.special_notes);
        price_to_pay = findViewById(R.id.price_to_pay);
        customer = findViewById(R.id.customer);
        images = findViewById(R.id.images);
        btnAddPickRequest = findViewById(R.id.btnAddPickRequest);
        btnPrice = findViewById(R.id.btnPrice);



        lyPickRequestList = findViewById(R.id.lyPickRequestList);
        lyAddPickRequest = findViewById(R.id.lyAddPickRequest);


        mAddFab = findViewById(R.id.add_fab);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lyPickRequestList.setVisibility(View.GONE);
                lyAddPickRequest.setVisibility(View.GONE);
                lyPrice.setVisibility(View.VISIBLE);


                Geocoder geocoder = new Geocoder(WarehousePortal.this);
                Location currentLocation = new Location("locationA");
                Location destination = new Location("locationB");

                try {
                    List<Address> originAddresses = geocoder.getFromLocationName(pickup_location.getText().toString(), 1);
                    List<Address> destinationAddresses = geocoder.getFromLocationName(droff_location.getText().toString(), 1);

                    if (originAddresses != null && originAddresses.size() > 0 && destinationAddresses != null && destinationAddresses.size() > 0) {
                        Address origin = originAddresses.get(0);
                        Address dest = destinationAddresses.get(0);

                        currentLocation.setLatitude(origin.getLatitude());
                        currentLocation.setLongitude(origin.getLongitude());

                        destination.setLatitude(dest.getLatitude());
                        destination.setLongitude(dest.getLongitude());

                        float distance = currentLocation.distanceTo(destination)/1000;
                        Toast.makeText(WarehousePortal.this, "Distance between the two addresses: " + distance + " meters", Toast.LENGTH_SHORT).show();

                        double dvolume =  Double.parseDouble(volume.getText().toString());; // Volume in cubic meters
                        double dweight = Double.parseDouble(weight.getText().toString());; // Weight in kilograms

                        double xprice = mCalulatePrice(distance,dvolume, dweight);
                        price_to_pay.setText(Double.toString(xprice));
                        qPrice.setText(Double.toString(xprice));

                        Toast.makeText(WarehousePortal.this, "Price to pay is : " + xprice, Toast.LENGTH_SHORT).show();

                        lyPickRequestList.setVisibility(View.GONE);
                        lyAddPickRequest.setVisibility(View.GONE);
                        lyPrice.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(WarehousePortal.this, "Failed to geocode the addresses", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(WarehousePortal.this, "Failed to calculate the distance", Toast.LENGTH_SHORT).show();
                }




            }
        });

        btnAddPickRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                String user_id = sharedPreferences.getString("user_id", null);

                String date_and_time_pickup = pickdateEditText.getText().toString() +" " +picktimeEditText.getText().toString();
                String date_and_time_dropoff = dropoffdateEditText.getText().toString()+ " "+ dropofftimeEditText.getText().toString();

//                mAddpickRequest(date_and_time_pickup,
//                        date_and_time_dropoff,
//
//                        recipient_name.getText().toString(),
//                        recipient_phone.getText().toString(),
//
//                        pickup_location.getText().toString(),
//                        droff_location.getText().toString(),
//
//                        volume.getText().toString(),
//
//                        weight.getText().toString(),
//
//                        parcel_description.getText().toString(),
//                        special_notes.getText().toString(),
//                        price_to_pay.getText().toString(),
//                        user_id,
//                        images.getText().toString());

                setTitle("Customer Portal");





            }
        });


        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyPickRequestList.setVisibility(View.GONE);
                lyAddPickRequest.setVisibility(View.VISIBLE);
                setTitle("Pick Up Request");
            }
        });

        pickdateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(pickdateEditText);
            }
        });

        picktimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(picktimeEditText);
            }
        });

        dropoffdateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(dropoffdateEditText);

            }
        });
        dropofftimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(dropofftimeEditText);
            }
        });

        //on create end


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

        Intent intent = new Intent(WarehousePortal.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item clicks here
        switch (item.getItemId()) {
            case R.id.nav_own_warehouse:
//              Intent intent = new Intent(WarehousePortal.this,ViewWarehouses.class);
                Intent intWarehouse = new Intent(WarehousePortal.this,AddWarehouse.class);
                startActivity(intWarehouse);
                finish();
                break;
            case R.id.nav_track:
                Intent track = new Intent(WarehousePortal.this,TrackParcel.class);
                startActivity(track);
                finish();
                break;
            case R.id.nav_loginout:
                Logout();
                break;
            case R.id.nav_inner_profile:
                Intent intent = new Intent(WarehousePortal.this,Profile.class);
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

    public static final String NEXT_SCREEN = "details_screen";

    private void showDatePicker(EditText date) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                date.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker(EditText time) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                time.setText(selectedTime);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

//    public void mAddpickRequest(String date_and_time_pickup,String date_and_time_dropoff,String  recipient_name, String  recipient_phone,String pickup_location,String dropoff_location, String volume,String weight, String parcel_description,String special_notes,String calulatedPrice,String customer,String images) {
//
//        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
//                .addConverterFactory(GsonConverterFactory.create()).build();
//
//        RequestBody pick_date_and_time_pickup = RequestBody.create(MediaType.parse("multipart/form-data"), date_and_time_pickup);
//        RequestBody pick_date_and_time_dropoff = RequestBody.create(MediaType.parse("multipart/form-data"), date_and_time_dropoff);
//        RequestBody pick_recipient_name = RequestBody.create(MediaType.parse("multipart/form-data"), recipient_name);
//        RequestBody pick_recipient_phone = RequestBody.create(MediaType.parse("multipart/form-data"), recipient_phone);
//        RequestBody pick_pickup_location = RequestBody.create(MediaType.parse("multipart/form-data"), pickup_location);
//        RequestBody pick_dropoff_location = RequestBody.create(MediaType.parse("multipart/form-data"), dropoff_location);
//        RequestBody pick_volume = RequestBody.create(MediaType.parse("multipart/form-data"), volume);
//        RequestBody pick_weight = RequestBody.create(MediaType.parse("multipart/form-data"), weight);
//        RequestBody pick_parcel_description = RequestBody.create(MediaType.parse("multipart/form-data"), parcel_description);
//        RequestBody pick_special_notes = RequestBody.create(MediaType.parse("multipart/form-data"), special_notes);
//        RequestBody pick_price = RequestBody.create(MediaType.parse("multipart/form-data"), calulatedPrice);
//
//        RequestBody pick_customer = RequestBody.create(MediaType.parse("multipart/form-data"), customer);
//        RequestBody pick_images = RequestBody.create(MediaType.parse("multipart/form-data"), images);
//
//
//
//
//
//        APIService apiService = retrofit.create(APIService.class);
//        Call<APIResponse> call = apiService.addPickRequest(pick_date_and_time_pickup,
//                pick_date_and_time_dropoff,
//
//                pick_recipient_name,
//                pick_recipient_phone,
//
//                pick_pickup_location,
//                pick_dropoff_location,
//
//                pick_volume,
//                pick_weight,
//
//                pick_parcel_description,
//                pick_special_notes,
//                pick_price,
//                pick_customer,
//                pick_images );
//
//        call.enqueue(new Callback<APIResponse>() {
//            @Override
//            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//
//                if(response.isSuccessful()){
//                    if(response.body().getStatusCode().toString().equals("201")) {
//                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
//                        lyPickRequestList.setVisibility(View.VISIBLE);
//                        lyAddPickRequest.setVisibility(View.GONE);
//                        lyPrice.setVisibility(View.GONE);
//                    }else{
//                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<APIResponse> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//
//
//    }


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
//        RequestBody get_customer= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
//
//        Call<List<RequestPickupResponse>> call = apiService.getUnDeliveredObjects(get_customer);
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
//                        recyclerView.setLayoutManager(new LinearLayoutManager(WarehousePortal.this));
//
//                        final AdapterPickUpRequest adapterPickUpRequest = new AdapterPickUpRequest(getApplicationContext(),pickUpRequestItems);
//                        recyclerView.setAdapter(adapterPickUpRequest);
//                        adapterPickUpRequest.setOnClickListener(new AdapterPickUpRequest.OnClickListener() {
//                            @Override
//                            public void onClick(int position, PickUpRequestItem model) {
//                                Intent intent = new Intent(WarehousePortal.this, PickUpRequestDetailed.class);
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
//        RequestBody get_customer= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
//
//
//        Call<List<RequestPickupResponse>> call = apiService.getUnRatedObjects(get_customer);
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
//                            createNotification();
//                        }
//                        recyclerView.setLayoutManager(new LinearLayoutManager(WarehousePortal.this));
//
//                        final AdapterPickUpRequest adapterPickUpRequest = new AdapterPickUpRequest(getApplicationContext(),pickUpRequestItems);
//                        recyclerView.setAdapter(adapterPickUpRequest);
//                        adapterPickUpRequest.setOnClickListener(new AdapterPickUpRequest.OnClickListener() {
//                            @Override
//                            public void onClick(int position, PickUpRequestItem model) {
//                                Intent intent = new Intent(WarehousePortal.this, PickUpRequestDetailed.class);
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
//        RequestBody get_customer= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
//
//
//        Call<List<RequestPickupResponse>> call = apiService.getAllUserObjects(get_customer);
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
//                        recyclerView.setLayoutManager(new LinearLayoutManager(WarehousePortal.this));
//
//                        final AdapterPickUpRequest adapterPickUpRequest = new AdapterPickUpRequest(getApplicationContext(),pickUpRequestItems);
//                        recyclerView.setAdapter(adapterPickUpRequest);
//                        adapterPickUpRequest.setOnClickListener(new AdapterPickUpRequest.OnClickListener() {
//                            @Override
//                            public void onClick(int position, PickUpRequestItem model) {
//                                Intent intent = new Intent(WarehousePortal.this, PickUpRequestDetailed.class);
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

    public double mCalulatePrice(double distance, double volume, double weight){


        double baseRate = 10.0; // Base rate per kilometer
        double volumeRate = 2.0; // Rate per cubic meter
        double weightRate = 0.5; // Rate per kilogram

        double distanceCost = distance * baseRate;
        double volumeCost = volume * volumeRate;
        double weightCost = weight * weightRate;

        double totalPrice = distanceCost + volumeCost + weightCost;

        return totalPrice;


    }


    private void startPeriodicApiCall() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Call your API method here
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

    private void createNotification() {
        // Create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create an explicit intent for the target activity
        Intent intent = new Intent(this, WarehousePortal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.putExtra(NEXT_SCREEN, pickUpRequestItem);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        // Get the custom sound URI from the resource
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.success);


        // Create a notification
        Notification notification = new NotificationCompat.Builder(this, "my_channel")
                .setContentTitle("My Notification")
                .setContentText("You have unrated delivery")
                .setSmallIcon(R.drawable.notification)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Notify the user
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notification);
    }



}