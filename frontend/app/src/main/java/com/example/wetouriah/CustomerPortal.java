package com.example.wetouriah;

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

    private RelativeLayout lyPickRequestList;

    private RelativeLayout lyLocationsWightVolume,lyPrice,lyDatesAndTime,lyRecipentDetails;


    //pick up re start
    EditText pickdateEditText,picktimeEditText;

    EditText recipient_name,recipient_phone,pickup_location,droff_location,
            volume,weight;

    Button btnAddPickRequest,btnShowPrice, btnShowDateTime,btnShowRecipentDetails;



    private int year, month, day, hour, minute;
    FloatingActionButton mAddFab;



    ScrollView lyAddPickRequest;

    //pick up re end

    TextView headerUsername;
    TextView txtPrice;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_portal);
        setTitle("Customer Portal");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        txtPrice = findViewById(R.id.txtPrice);
        lyPickRequestList = findViewById(R.id.lyPickRequestList);
        lyLocationsWightVolume = findViewById(R.id.lyLocationsWightVolume);
        lyPrice = findViewById(R.id.lyPrice);
        lyDatesAndTime = findViewById(R.id.lyDatesAndTime);
        lyRecipentDetails = findViewById(R.id.lyRecipentDetails);

        btnShowPrice = findViewById(R.id.btnShowPrice);
        btnShowDateTime = findViewById(R.id.btnShowDateTime);
        btnShowRecipentDetails = findViewById(R.id.btnShowRecipentDetails);
        btnAddPickRequest = findViewById(R.id.btnAddPickRequest);




        View headerView = navigationView.getHeaderView(0);
        headerUsername =  headerView.findViewById(R.id.username);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);
        String username = sharedPreferences.getString("username", null);
        headerUsername.setText("hello, " + username);




        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        startPeriodicApiCall();

        pickup_location = findViewById(R.id.pickup_location);
        droff_location = findViewById(R.id.droff_location);
        volume = findViewById(R.id.volume);
        weight = findViewById(R.id.weight);

        pickdateEditText = findViewById(R.id.pickdateEditText);
        picktimeEditText = findViewById(R.id.picktimeEditText);


        recipient_name = findViewById(R.id.recipient_name);
        recipient_phone = findViewById(R.id.recipient_phone);


        mAddFab = findViewById(R.id.add_fab);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);


        btnShowPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideAllLayers();
                lyPrice.setVisibility(View.VISIBLE);


                Geocoder geocoder = new Geocoder(CustomerPortal.this);
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
                        Toast.makeText(CustomerPortal.this, "Distance between the two addresses: " + distance + " meters", Toast.LENGTH_SHORT).show();

                        double dvolume =  Double.parseDouble(volume.getText().toString());; // Volume in cubic meters
                        double dweight = Double.parseDouble(weight.getText().toString());; // Weight in kilograms

                        double xprice = mCalulatePrice(distance,dvolume, dweight);
                        txtPrice.setText(Double.toString(xprice));


                    } else {
                        Toast.makeText(CustomerPortal.this, "Failed to geocode the addresses", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CustomerPortal.this, "Failed to calculate the distance", Toast.LENGTH_SHORT).show();
                }





            }
        });
        btnShowDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllLayers();
                lyDatesAndTime.setVisibility(View.VISIBLE);
            }
        });
        btnShowRecipentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllLayers();
                lyRecipentDetails.setVisibility(View.VISIBLE);

            }
        });


        btnAddPickRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllLayers();
                lyPickRequestList.setVisibility(View.VISIBLE);

                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                String user_id = sharedPreferences.getString("user_id", null);

                String date_and_time_pickup = pickdateEditText.getText().toString() +" " +picktimeEditText.getText().toString();

                mAddpickRequest(date_and_time_pickup,
                        recipient_name.getText().toString(),
                        recipient_phone.getText().toString(),

                        pickup_location.getText().toString(),
                        droff_location.getText().toString(),

                        volume.getText().toString(),

                        weight.getText().toString(),

                        txtPrice.getText().toString(),
                        user_id);

                setTitle("Customer Portal");





            }
        });


        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("Create PickUp Request");
                hideAllLayers();
                lyLocationsWightVolume.setVisibility(View.VISIBLE);
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



        //on create end


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

    private void hideAllLayers(){
        lyPickRequestList.setVisibility(View.GONE);
        lyLocationsWightVolume.setVisibility(View.GONE);
        lyPrice.setVisibility(View.GONE);
        lyDatesAndTime.setVisibility(View.GONE);
        lyRecipentDetails.setVisibility(View.GONE);
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

        Intent intent = new Intent(CustomerPortal.this,MainActivity.class);
        startActivity(intent);
        finish();
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

    public void mAddpickRequest(String date_and_time_pickup,String  recipient_name, String  recipient_phone,String pickup_location,String dropoff_location, String volume,String weight,String calulatedPrice,String customer) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody pick_date_and_time_pickup = RequestBody.create(MediaType.parse("multipart/form-data"), date_and_time_pickup);
        RequestBody pick_recipient_name = RequestBody.create(MediaType.parse("multipart/form-data"), recipient_name);
        RequestBody pick_recipient_phone = RequestBody.create(MediaType.parse("multipart/form-data"), recipient_phone);
        RequestBody pick_pickup_location = RequestBody.create(MediaType.parse("multipart/form-data"), pickup_location);
        RequestBody pick_dropoff_location = RequestBody.create(MediaType.parse("multipart/form-data"), dropoff_location);
        RequestBody pick_volume = RequestBody.create(MediaType.parse("multipart/form-data"), volume);
        RequestBody pick_weight = RequestBody.create(MediaType.parse("multipart/form-data"), weight);
        RequestBody pick_price = RequestBody.create(MediaType.parse("multipart/form-data"), calulatedPrice);

        RequestBody pick_customer = RequestBody.create(MediaType.parse("multipart/form-data"), customer);





        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.addPickRequest(pick_date_and_time_pickup,
                pick_recipient_name,
                pick_recipient_phone,

                pick_pickup_location,
                pick_dropoff_location,

                pick_volume,
                pick_weight,

                pick_price,
                pick_customer);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){
                    if(response.body().getStatusCode().toString().equals("201")) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }



    public void mGetAllPickRequest(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/") // Replace with your actual base URL
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
                            String status= object.getStatus().toString();
                            if (status.equals("1")){
                                status = "Delivered";
                            }
                            if (status.equals("2")){
                                status = "In Transit";
                            }

                            pickUpRequestItems.add(new PickUpRequestItem(object.getId().toString(),
                                    object.getTrackingNumber(),object.getCustomer().toString(),
                                    object.getRequestTime().toString(),
                                    object.getDateAndTimePickup().toString(),
                                    object.getRecipientName().toString(),
                                    object.getRecipientPhone().toString(),
                                    object.getPickupLocation().toString(),
                                    object.getDropoffLocation().toString(),
                                    object.getVolume().toString(),
                                    object.getWeight().toString(),
                                    object.getPriceToPay().toString(),
                                    object.getImages().toString(),
                                    status)
                            );
                            pickUpRequestItems.add(new PickUpRequestItem(object.getId().toString(),
                                    object.getTrackingNumber(),object.getCustomer().toString(),
                                    object.getRequestTime().toString(),
                                    object.getDateAndTimePickup().toString(),
                                    object.getRecipientName().toString(),
                                    object.getRecipientPhone().toString(),
                                    object.getPickupLocation().toString(),
                                    object.getDropoffLocation().toString(),
                                    object.getVolume().toString(),
                                    object.getWeight().toString(),
                                    object.getPriceToPay().toString(),
                                    object.getImages().toString(),
                                    status)
                            );
                            pickUpRequestItems.add(new PickUpRequestItem(object.getId().toString(),
                                    object.getTrackingNumber(),object.getCustomer().toString(),
                                    object.getRequestTime().toString(),
                                    object.getDateAndTimePickup().toString(),
                                    object.getRecipientName().toString(),
                                    object.getRecipientPhone().toString(),
                                    object.getPickupLocation().toString(),
                                    object.getDropoffLocation().toString(),
                                    object.getVolume().toString(),
                                    object.getWeight().toString(),
                                    object.getPriceToPay().toString(),
                                    object.getImages().toString(),
                                    status)
                            );
                            pickUpRequestItems.add(new PickUpRequestItem(object.getId().toString(),
                                    object.getTrackingNumber(),object.getCustomer().toString(),
                                    object.getRequestTime().toString(),
                                    object.getDateAndTimePickup().toString(),
                                    object.getRecipientName().toString(),
                                    object.getRecipientPhone().toString(),
                                    object.getPickupLocation().toString(),
                                    object.getDropoffLocation().toString(),
                                    object.getVolume().toString(),
                                    object.getWeight().toString(),
                                    object.getPriceToPay().toString(),
                                    object.getImages().toString(),
                                    status)
                            );

                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerPortal.this));

                        final AdapterPickUpRequest adapterPickUpRequest = new AdapterPickUpRequest(getApplicationContext(),pickUpRequestItems);
                        recyclerView.setAdapter(adapterPickUpRequest);


                        adapterPickUpRequest.setOnClickListener(new AdapterPickUpRequest.OnClickListener() {
                            @Override
                            public void onClick(int position, PickUpRequestItem model) {
                                Intent intent = new Intent(CustomerPortal.this, PickUpRequestDetailed.class);
                                intent.putExtra(NEXT_SCREEN, model);
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
                mGetAllPickRequest();

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
        Intent intent = new Intent(this, CustomerPortal.class);
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

    public void changePassword()
    {
        Intent intent = new Intent(getApplicationContext(),ResetPassword.class);
        intent.putExtra("password", "password");
        startActivity(intent);
        finish();
    }





}