package com.example.wetouriah;

import static com.example.wetouriah.models.OptimalRoute.findOptimalRoute;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.LocationRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wetouriah.adapters.listIteneraryAdapter;
import com.example.wetouriah.util.MyItemTouchHelperCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DriverPortal extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, NavigationView.OnNavigationItemSelectedListener {


    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private static final String TAG = "DriverPortal";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    TextView headerUsername;

    boolean setInitaavilableForJobs = true;
    SwitchMaterial avilableForJobs;


    FloatingActionButton mAdd_car;


    //private Handler handler;
    // private Runnable runnable;

    String user_id,role;

    RelativeLayout lyAllCars,lyPickRequestList;
    RelativeLayout lyNoJobs,rlList, rlMap;

    GoogleMap mMap;

    List<PickUpRequestItem> routePickUpRequestItems;
    private boolean isRoutePickUpItemsReady = false;


    Button zoomInButton, zoomOutButton ;

    Button btnNavigateAll;



    // private RecyclerView recyclerView;
    RecyclerView recyclerView ;

    CardView map_CardViewitem;
    Button button1, button2;

    private static final long FUNCTION_INTERVAL_MS = 30000; // 10 seconds
    //private Handler handler = new Handler();
    private boolean isFunctionRunning = false;


    private static final long REORDER_STOPS_TIMEOUT_MS = 2000;
    private Handler handler = new Handler();


    listIteneraryAdapter.PickUpRequestItemReorderListener pickUpRequestItemReorderListener;
    listIteneraryAdapter  adapter ;
    ItemTouchHelper.Callback callback;
    ItemTouchHelper itemTouchHelper;

    private ImageView loadingGif;

    // initializing
    // FusedLocationProviderClient
    // object
    FusedLocationProviderClient mFusedLocationClient;

    // Initializing other items
    // from layout file

    String lalog;
    int PERMISSION_ID = 44;

    private boolean StopMap = false;


    //Filtering
    EditText searchJobsEditText;
    ImageView  imgFilterJobs;
    CardView lyFilterJobs;
    RadioGroup radioSortGroup,radioBestRouteGroup,rgPickDropby;
    RadioButton radioSortAscending,radioSortDescending,radioFindBestRoute, rdpickup,rdDropoff;
    Button btnDiscardJobs, btnApplyJobs;
    TextView info_text ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_portal);
        setTitle("Driver Portal");

        mAdd_car = findViewById(R.id.mAdd_car);
        lyAllCars = findViewById(R.id.lyAllCars);
        lyPickRequestList= findViewById(R.id.lyPickRequestList);


        rlList= findViewById(R.id.rlList);
        lyNoJobs = findViewById(R.id.lyNoJobs);
        rlMap= findViewById(R.id.rlMap);


        zoomInButton = findViewById(R.id.zoomInButton);
        zoomOutButton = findViewById(R.id.zoomOutButton);

        // recyclerView = findViewById(R.id.recyclerView);
        recyclerView = findViewById(R.id.recyclerView);

        loadingGif = findViewById(R.id.loadingGif);

        map_CardViewitem = findViewById(R.id.map_CardViewitem);

        info_text = findViewById(R.id.info_text2);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnNavigateAll = findViewById(R.id.btnNavigateAll);

        // method to get the location
        getLastLocation();

        //notification start
        //startPeriodicApiCall();
        mGetAllPickRequest();
        mGetAvailabilty();
        //notification end
        Log.e(TAG, "check");

        //
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //


        //Filtering
        searchJobsEditText = findViewById(R.id.searchJobsEditText);
        imgFilterJobs = findViewById(R.id.imgFilterJobs);
        lyFilterJobs = findViewById(R.id.lyFilterJobs);
        rgPickDropby = findViewById(R.id.rgPickDropby);
        btnDiscardJobs = findViewById(R.id.btnDiscardJobs);
        btnApplyJobs = findViewById(R.id.btnApplyJobs);

        radioSortGroup = findViewById(R.id.radioSortGroup);
        radioBestRouteGroup= findViewById(R.id.radioBestRouteGroup);

        radioSortAscending = findViewById(R.id.radioSortAscending);
        radioSortDescending= findViewById(R.id.radioSortDescending);
        radioFindBestRoute= findViewById(R.id.radioFindBestRoute);


        rdpickup= findViewById(R.id.rdpickup);
        rdDropoff= findViewById(R.id.rdDropoff);


        radioSortAscending.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Option 1 is selected, so clear the other radio button in a different group.
                radioBestRouteGroup.clearCheck();
            }
        });

        radioSortDescending.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Option 2 is selected, so clear the other radio button in a different group.
                radioBestRouteGroup.clearCheck();
            }
        });
        radioFindBestRoute.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Option 2 is selected, so clear the other radio button in a different group.
                radioSortGroup.clearCheck();
            }
        });


        searchJobsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This method is called before the text is changed.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This method is called as the text is changing.

                //filterbyUsername(String.valueOf(charSequence) );
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This method is called after the text has changed.
//                String enteredText = editable.toString();
//                FilterTextbox(enteredText);

                if(editable != null ){
                    Filterbytext(editable.toString());

                }


            }
        });

        imgFilterJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = lyFilterJobs.getVisibility();
                if (visibility == View.VISIBLE) {
                    lyFilterJobs.setVisibility(View.GONE);
                } else
                    lyFilterJobs.setVisibility(View.VISIBLE);
            }

        });

        btnDiscardJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lyFilterJobs.setVisibility(View.GONE);
                radioSortGroup.clearCheck(); // Clear the selection
                radioBestRouteGroup.clearCheck(); // Clear the selection
                rgPickDropby.clearCheck(); // Clear the selection


                rgPickDropby.clearCheck(); // Clear the selection

                if(searchJobsEditText.getText().length() >0){
                    Filterbytext(searchJobsEditText.getText().toString());
                }else {
                    Filterbytext("");
                }

//                adapter.setItems(routePickUpRequestItems);
//
////                if(searchJobsEditText.getText().length() >0){
////                    Filterbytext(searchJobsEditText.getText().toString());
////                }else {
////                    //Filterbytext("");
////
////
////                    adapter.setItems(routePickUpRequestItems);
////                }

            }
        });
        btnApplyJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyFilterJobs.setVisibility(View.GONE);
                if(searchJobsEditText.getText().length() >0){
                    Filterbytext(searchJobsEditText.getText().toString());
                }else {
                    Filterbytext("");
                }


            }
        });


        

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itinerary_list_fragment:
                                rlList.setVisibility(View.VISIBLE);
                                rlMap.setVisibility(View.GONE);

                                if(routePickUpRequestItems.size() >0){
                                    loadingGif.setVisibility(View.GONE);
                                    lyNoJobs.setVisibility(View.GONE);
                                    rlList.setVisibility(View.VISIBLE);
                                }else {
                                    loadingGif.setVisibility(View.GONE);
                                    lyNoJobs.setVisibility(View.VISIBLE);
                                    rlList.setVisibility(View.GONE);
                                }

                                return true;
                            case R.id.itinerary_map_fragment:
                                rlList.setVisibility(View.GONE);
                                rlMap.setVisibility(View.VISIBLE);
                                if(routePickUpRequestItems.size() >0){
                                    loadingGif.setVisibility(View.GONE);
                                    lyNoJobs.setVisibility(View.GONE);
                                    rlList.setVisibility(View.VISIBLE);
                                }else {
                                    loadingGif.setVisibility(View.GONE);
                                    lyNoJobs.setVisibility(View.VISIBLE);
                                    rlList.setVisibility(View.GONE);
                                }
                                return true;
                            // Add more cases for other menu items if needed
                        }
                        return false;
                    }
                }
        );


        View headerView = navigationView.getHeaderView(0);
        headerUsername =  headerView.findViewById(R.id.username);
        avilableForJobs  =  headerView.findViewById(R.id.avilableForJobs);;
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        role = sharedPreferences.getString("role", null);
        String username = sharedPreferences.getString("username", null);
        user_id = sharedPreferences.getString("user_id", null);

        if(role.equals("3")){
            avilableForJobs.setVisibility(View.VISIBLE);
        }

        headerUsername.setText("Hello, " + username);

        mAdd_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopFunctionExecution();
                Intent reg = new Intent(DriverPortal.this,AddCar.class);
                startActivity(reg);
                finish();



            }
        });

        btnNavigateAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Filter the list to separate pickup and dropoff locations based on status.
                List<String> stops = new ArrayList<>();

                for (PickUpRequestItem item : adapter.getItems()) {
                    String status = item.getStatus();

                    // Check the status and add the corresponding location to the stops list.
                    if (status.equals("5") || status.equals("8") || status.equals("1")) {
                        LatLng pickupLatLng = getLatLngFromLocationString(item.getPickup_location());
                        if (pickupLatLng != null) {

                            stops.add( String.valueOf(pickupLatLng.latitude ) +","+  String.valueOf(pickupLatLng.longitude ));
                        }
                    } else {
                        LatLng dropoffLatLng = getLatLngFromLocationString(item.getDropoff_location());
                        if (dropoffLatLng != null) {
                            //stops.add(String.valueOf(dropoffLatLng));
                            stops.add( String.valueOf(dropoffLatLng.latitude ) +","+  String.valueOf(dropoffLatLng.longitude ));

                        }
                    }
                }
                requestNewLocationData();

                // Add the user's current location as the starting point.
                stops.add(0,lalog);
               // Log.e(TAG, "my location: " + lalog.toString());

                // Create a URI for Google Maps with the stops.
                String uri = getMapsDirectionsUri(stops);

               // Log.e(TAG, "stops: " + stops.toString());

                // Open Google Maps with the directions.
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps"); // Ensure Google Maps is used.
                startActivity(intent);





            }
        });


        avilableForJobs.setOnCheckedChangeListener(new SwitchMaterial.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!setInitaavilableForJobs) {
                    if (isChecked) {
                        //Toast.makeText(getApplicationContext(), "Availability On", Toast.LENGTH_SHORT).show();
                        getLastLocation();

                        if(lalog !=null){
                            SendLocationAndStatus("1",lalog);

                        }


                    } else {
                        // Toast.makeText(getApplicationContext(), "Availability Off", Toast.LENGTH_SHORT).show();
                        getLastLocation();
                        if(lalog !=null){
                            SendLocationAndStatus("0",lalog);

                        }

                    }
                }
                setInitaavilableForJobs = false; // Reset the flag



            }
        });





        zoomInButton.setOnClickListener(view -> {
            // Increase the camera zoom level by 1
            CameraUpdate zoomIn = CameraUpdateFactory.zoomIn();
            mMap.animateCamera(zoomIn);
        });

        zoomOutButton.setOnClickListener(view -> {
            // Decrease the camera zoom level by 1
            CameraUpdate zoomOut = CameraUpdateFactory.zoomOut();
            mMap.animateCamera(zoomOut);
        });
        //Make sure this function does not break when going to another activity
        startFunctionExecution();
    }
    private void Filterbytext(String text) {
        List<PickUpRequestItem> filteredList = new ArrayList<PickUpRequestItem>();

        for (PickUpRequestItem item : routePickUpRequestItems) {
            if (item.recipient_name.toLowerCase().contains(text.toLowerCase()) ||
                    item.pickup_location.toLowerCase().contains(text.toLowerCase()) ||
                    item.dropoff_location.toLowerCase().contains(text.toLowerCase()) ||
                    item.parcel_description.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (radioSortAscending.isChecked()) {
            // Sort the filteredList by request_time in ascending order
            Collections.sort(filteredList, new Comparator<PickUpRequestItem>() {
                @Override
                public int compare(PickUpRequestItem item1, PickUpRequestItem item2) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX", Locale.US);
                    try {
                        Date date1 = dateFormat.parse(item1.request_time);
                        Date date2 = dateFormat.parse(item2.request_time);
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
        }
        if (radioSortDescending.isChecked()) {
            // Sort the filteredList by request_time in ascending order
            Collections.sort(filteredList, new Comparator<PickUpRequestItem>() {
                @Override
                public int compare(PickUpRequestItem item1, PickUpRequestItem item2) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX", Locale.US);
                    try {
                        Date date1 = dateFormat.parse(item1.request_time);
                        Date date2 = dateFormat.parse(item2.request_time);
                        return date2.compareTo(date1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
        }

        if (rdpickup.isChecked()) {
            // Filter the list to include items with status 1, 2, and 3
            List<PickUpRequestItem> pickupItems = new ArrayList<>();
            for (PickUpRequestItem item : filteredList) {
                int status = Integer.parseInt(item.status);
                if (status == 1
                        || status == 3
                        || status == 5 || status == 8) {
                    pickupItems.add(item);
                }
            }
            filteredList = pickupItems;
        }

        if (rdDropoff.isChecked()) {
            // Filter the list to include items with status 1, 2, and 3
            List<PickUpRequestItem> pickupItems = new ArrayList<>();
            for (PickUpRequestItem item : filteredList) {
                int status = Integer.parseInt(item.status);
                if (status == 2 || status == 4 || status == 6 || status == 7 || status == 16) {
                    pickupItems.add(item);
                }
            }
            filteredList = pickupItems;
        }

        if (radioFindBestRoute.isChecked()) {
            Log.e(TAG, "O list: " + routePickUpRequestItems.toString());
            filteredList = findOptimalRoute( routePickUpRequestItems, lalog);
            Log.e(TAG, "N list: " + filteredList.toString());

        }


        adapter.setItems(filteredList);
    }

    private String getMapsDirectionsUri(List<String> stops) {
        StringBuilder uriBuilder = new StringBuilder("https://www.google.com/maps/dir/");

        for (String stop : stops) {
            uriBuilder.append(stop).append("/");
        }
       // Log.e(TAG, "uriBuilder: " + uriBuilder.toString());
        return uriBuilder.toString();
    }


    public void SendLocationAndStatus(String available,String location){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", null);

        RequestBody get_courier= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
        RequestBody get_available= RequestBody.create(MediaType.parse("multipart/form-data"), available);
        RequestBody get_location= RequestBody.create(MediaType.parse("multipart/form-data"), location);

        Call<APIResponse> call = apiService.apiLocationAndAvailabilty(get_courier,get_available,get_location);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){
                    //Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {
                Log.e("Driver", "isLocationEnabled : " + String.valueOf(isLocationEnabled()));


                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            lalog  = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude())  ;

                            // Toast.makeText(getApplicationContext(), lalog, Toast.LENGTH_LONG).show();

                        }
                    }
                });
            } else {
                Log.e("Driver", "isLocationEnabled : " + String.valueOf(isLocationEnabled()));

                Toast.makeText(getApplicationContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lalog  = String.valueOf(mLastLocation.getLatitude()) + "," + String.valueOf(mLastLocation.getLongitude())  ;
            // Toast.makeText(getApplicationContext(), lalog, Toast.LENGTH_LONG).show();


        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        if (isRoutePickUpItemsReady) {
            // Initialize the map and add markers
            for (PickUpRequestItem item : routePickUpRequestItems) {
                LatLng locationLatLng = getLatLngFromLocationString(item.getDropoff_location());
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(locationLatLng)
                        .title(item.getTracking_number())
                        .snippet(item.getStatus()));

                // Set the custom info window adapter for each marker
                marker.setTag(item); // You can set the item as a tag to access it in the custom info window adapter
                marker.setInfoWindowAnchor(0.5f, 0.5f); // Center the info window on the marker
                LatLng firstLocation = getLatLngFromLocationString(routePickUpRequestItems.get(0).getDropoff_location());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 12));
            }

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    // Return null to use the default info window
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    map_CardViewitem.setVisibility(View.VISIBLE);
//                    // Inflate and return the custom info window layout
                    View infoView = getLayoutInflater().inflate(R.layout.map_item, null);
                    infoView.setVisibility(View.GONE);

                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            PickUpRequestItem x = getPickUpRequestByTrackingNumber(routePickUpRequestItems, String.valueOf(marker.getTitle()) );


                            info_text.setText("Tracking ID: " + String.valueOf(marker.getTitle()));
                            map_CardViewitem.setVisibility(View.GONE);
                            Intent intent = new Intent(DriverPortal.this, PickUpRequestDetailed.class);
                            intent.putExtra("model", x);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag
                            startActivity(intent);


                            }
                    });
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle button2 click (e.g., show a toast)
                            //Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                            PickUpRequestItem x = getPickUpRequestByTrackingNumber(routePickUpRequestItems, String.valueOf(marker.getTitle()) );

                            map_CardViewitem.setVisibility(View.GONE);

                        }
                    });

                    return infoView;
                }
            });
        } else {
           // Log.e(TAG, "routePickUpRequestItems is not ready yet");
        }
    }
    public  PickUpRequestItem getPickUpRequestByTrackingNumber(List<PickUpRequestItem> requests, String trackingNumber) {
        for (PickUpRequestItem request : requests) {
            if (request.getTracking_number().equals(trackingNumber)) {
                return request; // Found the request with the specified tracking number
            }
        }
        return null; // Tracking number not found in the list
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // This method is called when the map is clicked
        map_CardViewitem.setVisibility(View.GONE);
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
            case R.id.deliveries:
                mGetAllPickRequest();
                break;
            case R.id.nav_loginout:
                Logout();
                break;
            case R.id.cars:
                mGetAllCars(user_id);
                break;
            case R.id.password:
                changePassword();
                break;
            case R.id.licenses:
                stopFunctionExecution();
                Intent license = new Intent(DriverPortal.this,AddDriversLicense.class);
                startActivity(license);
                finish();
                break;
            case R.id.nav_inner_profile:
                stopFunctionExecution();
                Intent intent = new Intent(DriverPortal.this,Profile.class);
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


    private void Logout(){

        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Clear all SharedPreferences data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();


        stopFunctionExecution();
        Intent intent = new Intent(DriverPortal.this,MainActivity.class);
        startActivity(intent);
        finish();

        stopFunctionExecution();
    }

    public void mGetAllCars(String user_id){


        lyAllCars.setVisibility(View.VISIBLE);
        lyPickRequestList.setVisibility(View.GONE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        RequestBody post_driver= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);


        Call<List<CarResponse>> call = apiService.getAllUserCarsObjects(post_driver);


        call.enqueue(new Callback<List<CarResponse>>() {
            @Override
            public void onResponse(Call<List<CarResponse>> call, Response<List<CarResponse>> response) {
                if (response.isSuccessful()) {

                    List<CarResponse> objects = response.body();

                    if (objects != null) {
                        RecyclerView recyclerView = findViewById(R.id.idAllCars);

                        List<CarItem> carItem = new ArrayList<CarItem>();

                        for (CarResponse object : objects) {
                            carItem.add(new CarItem(
                                    object.getId().toString(),
                                    object.getCarOwner().toString(),
                                    "Type: "+object.getType().toString(),
                                    "Capacity: "+object.getCapacity().toString(),
                                    "Color: "+object.getColor().toString(),
                                    "Make: "+object.getMake().toString(),
                                    "Model: "+object.getModel().toString(),
                                    "Year: "+object.getYear().toString(),
                                    "License Plate: "+object.getLicensePlate().toString(),
                                    object.getIsApproved().toString(),object.getIsDefault().toString()));

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(DriverPortal.this));

                        final AdapterAllCars adapterAllCars = new AdapterAllCars(getApplicationContext(),carItem);
                        recyclerView.setAdapter(adapterAllCars);
//                        recyclerView.setAdapter(new AdapterPickUpRequest(getApplicationContext(), CarItems));

                        adapterAllCars.setOnClickListener(new AdapterAllCars.OnClickListener() {
                            @Override
                            public void onClick(int position, CarItem model) {
                                stopFunctionExecution();
                                Intent intent = new Intent(DriverPortal.this, AddCar.class);
                                intent.putExtra("car",  model);
                                startActivity(intent);
                                finish();

                            }
                        });

                    }

                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<List<CarResponse>> call, Throwable t) {
                // Handle failure
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        // Pause Google Maps updates here

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume Google Maps updates here
        if (checkPermissions()) {
            getLastLocation();
        }

    }



    public static final String NEXT_SCREEN = "details_screen";


    private void createNotification(PickUpRequestItem pickUpRequestItem) {

        //stopFunctionExecution();
        // Create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create an explicit intent for the target activity
        Intent intent = new Intent(this, PickUpRequestDetailed.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("model", pickUpRequestItem);
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



    private LatLng getLatLngFromLocationString(String location) {
        List<Address> addressList = null;


        Geocoder geocoder = new Geocoder(DriverPortal.this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        return latLng;

    }


    public void mGetAllPickRequest(){
        HideLayers();
        lyPickRequestList.setVisibility(View.VISIBLE);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", null);

        RequestBody get_driver= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);


        Call<List<RequestPickupResponse>> call = apiService.getAllDriverRequest(get_driver);

        call.enqueue(new Callback<List<RequestPickupResponse>>() {
            @Override
            public void onResponse(Call<List<RequestPickupResponse>> call, Response<List<RequestPickupResponse>> response) {
                if (response.isSuccessful()) {
                    List<RequestPickupResponse> objects = response.body();

                    if (objects != null) {



                        List<PickUpRequestItem> newItems = new ArrayList<PickUpRequestItem>();
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

                            newItems.add(new PickUpRequestItem(object.getId().toString(),
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

                            Log.e(TAG, "object.getRequestTime() : " + object.getRequestTime());



                        }


                        assignTheList( newItems);


                    }else{
                        Toast.makeText(DriverPortal.this, "objects null", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(DriverPortal.this, "response not successfully", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<List<RequestPickupResponse>> call, Throwable t) {
                // Handle failure
                Toast.makeText(DriverPortal.this, "failure", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private List<PickUpRequestItem> loadRouteItemsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        // Load saved routeItems from SharedPreferences
        String json = sharedPreferences.getString("pickUpRequestItem", "");
        return PickUpRequestItem.fromJson(json);
    }

    private void saveRouteItemsToSharedPreferences(List<PickUpRequestItem> pickUpRequestItem) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Serialize and save routeItems to SharedPreferences
        String json = PickUpRequestItem.toJson(pickUpRequestItem);
        editor.putString("pickUpRequestItem", json);
        editor.apply();
    }

    private List<PickUpRequestItem> mergeRouteItems(List<PickUpRequestItem> existingItems, List<PickUpRequestItem> newItems) {
        if (existingItems == null) {
            return newItems;
        }

        List<PickUpRequestItem> mergedItems = new ArrayList<>(existingItems);

        for (PickUpRequestItem newItem : newItems) {
            boolean itemExists = false;
            for (int i = 0; i < mergedItems.size(); i++) {
                PickUpRequestItem existingItem = mergedItems.get(i);
                if (existingItem.getTracking_number().equals(newItem.getTracking_number())) {
                    // Replace the existing item with the new item
                    mergedItems.set(i, newItem);
                    Log.e(TAG, "mergeRouteItems : " + existingItem.getRequest_time());
                    //Toast.makeText(getApplicationContext(), existingItem.getRequest_time(), Toast.LENGTH_SHORT).show();

                    itemExists = true;
                    break;
                }
            }
            if (!itemExists) {
                mergedItems.add(newItem);
            }
        }

        // Remove items from existingItems that do not appear in newItems
        for (int i = mergedItems.size() - 1; i >= 0; i--) {
            if (!newItems.contains(mergedItems.get(i))) {
                mergedItems.remove(i);
            }
        }

        return mergedItems;
    }




    public void HideLayers(){
        lyPickRequestList.setVisibility(View.GONE);
        lyAllCars.setVisibility(View.GONE);
    }

    public void assignTheList(List<PickUpRequestItem> newPickUpRequestItems){


        pickUpRequestItemReorderListener = this::notifyProviderOfVehicleStopsNewOrder;


        if( adapter ==null){
            adapter = new listIteneraryAdapter(getApplicationContext(),pickUpRequestItemReorderListener);

        }


        if(pickUpRequestItemReorderListener ==null){
            pickUpRequestItemReorderListener = this::notifyProviderOfVehicleStopsNewOrder;

        }else {
            pickUpRequestItemReorderListener = this::notifyProviderOfVehicleStopsNewOrder;

        }

        if(adapter  ==null){
            adapter = new listIteneraryAdapter(getApplicationContext(),pickUpRequestItemReorderListener);

        }





//        if(adapter.getItems() ==null){
//            Log.e(TAG, "adapter.getItems() is null");
//
//        }else {
//            Log.e(TAG, "adapter.getItems() is not null");
//
//        }


        List<PickUpRequestItem> routeItemsFromPrefs = loadRouteItemsFromSharedPreferences();
        if (routeItemsFromPrefs !=null && !routeItemsFromPrefs.isEmpty()) {
            // routePickUpRequestItems = routeItemsFromPrefs;
            routePickUpRequestItems = mergeRouteItems(routeItemsFromPrefs, newPickUpRequestItems);
            ;

            //routePickUpRequestItems = updatedPickUpRequestItems;

           // Log.e(TAG, "routeItemsFromPrefs is not null");


        }else{
            //List<PickUpRequestItem>  updatedPickUpRequestItems = mergeRouteItems(routeItemsFromPrefs, newPickUpRequestItems);
            routePickUpRequestItems = newPickUpRequestItems;

           // Log.e(TAG, "routeItemsFromPrefs is  null");

        }






        saveRouteItemsToSharedPreferences(routePickUpRequestItems);
        //adapter.setItems(updatedPickUpRequestItems);







        if(routePickUpRequestItems.size() >0){
            loadingGif.setVisibility(View.GONE);
            lyNoJobs.setVisibility(View.GONE);
            rlList.setVisibility(View.VISIBLE);
        }else {
            loadingGif.setVisibility(View.GONE);
            lyNoJobs.setVisibility(View.VISIBLE);
            rlList.setVisibility(View.GONE);
        }

       // Toast.makeText(DriverPortal.this, "size() " + String.valueOf(routePickUpRequestItems.size()), Toast.LENGTH_SHORT).show();




       // Log.e(TAG, "assignTheList");



        //routePickUpRequestItems = updatedPickUpRequestItems;
        isRoutePickUpItemsReady = true;

        if(StopMap == true){
            return;
        }else{
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        recyclerView.setAdapter(adapter);
        adapter.setItems(routePickUpRequestItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // Enable drag-and-drop reordering using ItemTouchHelper
        // Replace the existing ItemTouchHelper.Callback reference with the new one
        callback = new MyItemTouchHelperCallback(recyclerView,adapter,routePickUpRequestItems,pickUpRequestItemReorderListener);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void notifyProviderOfVehicleStopsNewOrder(List<PickUpRequestItem> pickUpRequestItem) {
        // Notify the provider of vehicle stops immediately
        // ...

        // Delayed notification to refresh the adapter after 2000 milliseconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                saveRouteItemsToSharedPreferences(pickUpRequestItem);
            }
        }, REORDER_STOPS_TIMEOUT_MS);

        // adapter.notifyDataSetChanged();
    }



    private Runnable functionRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isFunctionRunning) {
                // Execute your function
                // //     yourSampleFunction();
                ////    mCheckAPI();
                ////    mGetPickRequestUnDelivered();
                ////    mGetPickRequestUnRated();
                ////    mGetPickRequestAll();
                //mGetAllPickRequest();
                mCheckJobOffers();
            }
            // Post the same Runnable again after the defined interval
            handler.postDelayed(this, FUNCTION_INTERVAL_MS);
        }
    };

    // Call this method to start the function execution every 10 seconds.
    public void startFunctionExecution() {
        StopMap  = false;
        isFunctionRunning = false; // Reset the flag
        handler.post(functionRunnable);
    }

    // Call this method to stop the function execution.
    public void stopFunctionExecution() {
        StopMap = true;
        isFunctionRunning = true; // Set the flag to prevent further execution
        handler.removeCallbacks(functionRunnable);
    }

    public void mCheckJobOffers(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", null);

        RequestBody get_courier= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);

        Call<RequestPickupResponse> call = apiService.apiCheckJobOffers(get_courier);

        call.enqueue(new Callback<RequestPickupResponse>() {
            @Override
            public void onResponse(Call<RequestPickupResponse> call, Response<RequestPickupResponse> response) {

                if(response.isSuccessful()){

                    RequestPickupResponse object= response.body();

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


                    PickUpRequestItem   pickUpRequestItem = new PickUpRequestItem(object.getId().toString(),
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
                            end_datetime);


                    createNotification(pickUpRequestItem);


                }
            }
            @Override
            public void onFailure(Call<RequestPickupResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void mGetAvailabilty(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", null);

        RequestBody get_courier= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);

        Call<APIResponse> call = apiService.apiGetAvailibility(get_courier);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){

                    // avilableForJobs.setChecked(response.body().getMessage());



                    SetAvailbility(response.body().getMessage());
                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void SetAvailbility(String ava){

        //avilableForJobs.setChecked(Boolean.parseBoolean(response.body().getMessage()));

        //avilableForJobs.setChecked(Boolean.parseBoolean(ava));

        // mGetAvailabilty

        // Toast.makeText(getApplicationContext(), ava, Toast.LENGTH_SHORT).show();

        setInitaavilableForJobs = true;



        if(ava.equals("true")){
            avilableForJobs.setChecked(true);

        }else{
            avilableForJobs.setChecked(false);

        }


    }
    public void changePassword()
    {
        Intent intent = new Intent(getApplicationContext(),ResetPassword.class);
        intent.putExtra("password", "password");
        startActivity(intent);
        finish();
    }





}