package com.example.wetouriah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
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

public class AdminPortal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout tab_layout ;
    RelativeLayout lyAllusers,lyAllCars, lyWarehouses;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    TextView headerUsername;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_portal);
        setTitle("Admin Portal");


        //
        
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //

        tab_layout = findViewById(R.id.tab_layout);

        lyAllusers = findViewById(R.id.lyAllusers);
        lyAllCars = findViewById(R.id.lyAllCars);
        lyWarehouses = findViewById(R.id.lyWarehouses);


        lyAllusers.setVisibility(View.VISIBLE);
        lyAllCars.setVisibility(View.GONE);
        lyWarehouses.setVisibility(View.GONE);


        View headerView = navigationView.getHeaderView(0);
        headerUsername =  headerView.findViewById(R.id.username);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);
        String username = sharedPreferences.getString("username", null);
        headerUsername.setText("Hello, " + username);

       mGetAllUsers();
        mGetAllCars();
        mGetAllWarehouses();


        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Show or hide the layout based on the selected tab
                if (tab.getPosition() == 0) {
                    lyAllusers.setVisibility(View.VISIBLE);
                    lyAllCars.setVisibility(View.GONE);
                    lyWarehouses.setVisibility(View.GONE);


                }
                if (tab.getPosition() == 1) {
                    lyAllusers.setVisibility(View.GONE);
                    lyAllCars.setVisibility(View.VISIBLE);
                    lyWarehouses.setVisibility(View.GONE);
                }
                if (tab.getPosition() == 2) {
                    lyAllusers.setVisibility(View.GONE);
                    lyAllCars.setVisibility(View.GONE);
                    lyWarehouses.setVisibility(View.VISIBLE);


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
    }

    public void mGetAllUsers(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<List<AllUsersResponse>> call = apiService.getAllUSerObjects();


        call.enqueue(new Callback<List<AllUsersResponse>>() {
            @Override
            public void onResponse(Call<List<AllUsersResponse>> call, Response<List<AllUsersResponse>> response) {
                if (response.isSuccessful()) {

                    List<AllUsersResponse> objects = response.body();

                    if (objects != null) {
                        RecyclerView recyclerView = findViewById(R.id.idAllusers);

                        List<UserItem> UserItems = new ArrayList<UserItem>();

                        for (AllUsersResponse object : objects) {
                            String role = object.getRole().toString();
                            if (role.equals("1")) {
                                role = "Admin";
                            }
                            if (role.equals("2")) {
                                role = "Customer";
                            }
                            if (role.equals("3")) {
                                role = "Driver";
                            }
                            if (role.equals("4")) {
                                role = "WareHouse";
                            }
                            UserItems.add(new UserItem(object.getId().toString(),"username: "+object.getUsername(),"Address: "+ object.getAddress(),
                                    "Firstname: "+object.getFirstName().toString(),"Lastname: "+object.getLastName(),"Email: "+ object.getEmail(),
                                    object.getIsActive().toString(),object.getProfilePicture().toString(),"Role: "+role,"Phone: "+object.getPhoneNumber()));

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(AdminPortal.this));

                        final AdapterAllUsers adapterAllUsers = new AdapterAllUsers(getApplicationContext(),UserItems);
                        recyclerView.setAdapter(adapterAllUsers);
//                        recyclerView.setAdapter(new AdapterPickUpRequest(getApplicationContext(), UserItems));

                    }

                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<List<AllUsersResponse>> call, Throwable t) {
                // Handle failure
            }
        });

    }



    public void mGetAllCars(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<List<CarResponse>> call = apiService.getAllCarObjects();


        call.enqueue(new Callback<List<CarResponse>>() {
            @Override
            public void onResponse(Call<List<CarResponse>> call, Response<List<CarResponse>> response) {
                if (response.isSuccessful()) {

                    List<CarResponse> objects = response.body();

                    if (objects != null) {
                        RecyclerView recyclerView = findViewById(R.id.idAllCars);

                        List<CarItem> carItem = new ArrayList<CarItem>();

                        for (CarResponse object : objects) {
                            carItem.add(new CarItem(object.getId().toString(),object.getCarOwner().toString(),"Type: "+object.getType().toString(),"Capacity: "+object.getCapacity().toString(),"Color: "+object.getColor().toString(),
                                    "Make: "+object.getMake().toString(),"Model: "+object.getModel().toString(),"Year: "+object.getYear().toString(),"License Plate: "+object.getLicensePlate().toString(),object.getIsApproved().toString()));

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(AdminPortal.this));

                        final AdapterAllCars adapterAllCars = new AdapterAllCars(getApplicationContext(),carItem);
                        recyclerView.setAdapter(adapterAllCars);
//                        recyclerView.setAdapter(new AdapterPickUpRequest(getApplicationContext(), CarItems));

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

    public void mGetAllWarehouses(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<List<WarehouseResponse>> call = apiService.getAllWareHouseObjects();


        call.enqueue(new Callback<List<WarehouseResponse>>() {
            @Override
            public void onResponse(Call<List<WarehouseResponse>> call, Response<List<WarehouseResponse>> response) {
                if (response.isSuccessful()) {

                    List<WarehouseResponse> objects = response.body();

                    if (objects != null) {
                        RecyclerView recyclerView = findViewById(R.id.idAllWarehouses);

                        List<WareHouseItemAdmin> wareHouseItemAdmin = new ArrayList<WareHouseItemAdmin>();

                        for (WarehouseResponse object : objects) {
                            wareHouseItemAdmin.add(new WareHouseItemAdmin(object.getId().toString(),"","Address: "+ object.getAddress(),"Volume: "+object.getVolume(),object.getCctv().toString(),
                                    object.getArmedResponse().toString(),object.getFireSafetyAndManagement().toString(),object.getParkingSpace().toString(),
                                    object.getIsApproved().toString(),"Operating Hours: "+object.getOperatingHours(),object.getWarehouseOwner().toString()) );

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(AdminPortal.this));

                        final AdapterAllWarehouses adapterAllWarehouses = new AdapterAllWarehouses(getApplicationContext(),wareHouseItemAdmin);
                        recyclerView.setAdapter(adapterAllWarehouses);
//                        recyclerView.setAdapter(new AdapterPickUpRequest(getApplicationContext(), WareHouseItemAdmins));

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

        Intent intent = new Intent(AdminPortal.this,MainActivity.class);
        startActivity(intent);
        finish();
    }





}