package com.example.wetouriah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
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

    RelativeLayout lyAllusers,lyAllCars, lyWarehouses,lyDriversLicenses;

    List<UserItem> UserItems;
    List<CarItem> carItems;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    TextView headerUsername;

    FloatingActionButton mAdd_users;


    // search users

    AdapterAllUsers adapterAllUsers;
    AdapterAllCars adapterAllCars;


    EditText searchUsersEditText;
      CardView lyFilterUsers;
      ImageView imgFilterUsers;
      CheckBox activeUserCheckBox;
      CheckBox adminCheckBox,customerCheckBox, driverCheckBox, warehouseCheckBox;
      Button btnApplyUsers, btnDiscardUsers;

    // search cars
    EditText searchCarsEditText;
    CardView lyFilterCars;
    ImageView imgFilterCars;
    RadioGroup radioStatusCars;
    RadioButton approvedCarCheckBox,notapprovedCarCheckBox;
    Button btnDiscardCars, btnApplyCars;


    // search warehouses
    EditText searchwarehousesEditText;
    CardView lyFilterwarehouses;
    ImageView imgFilterwarehouses;
    CheckBox chCctv,chArmedResponse,chFireSafety,chParkingSpace;
    Button btnDiscardWarehouses, btnApplyWarehouses;


    // search licenses
    EditText searchLicensesEditText;
    CardView lyFilterLicenses;
    ImageView imgFilterLicenses;
    CheckBox approvedlicenseCheckBox;
    Button btnDiscardLicenses, btnApplyLicenses;


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

        lyAllusers = findViewById(R.id.lyAllusers);
        lyAllCars = findViewById(R.id.lyAllCars);
        lyWarehouses = findViewById(R.id.lyWarehouses);
        lyDriversLicenses = findViewById(R.id.lyDriversLicenses);

        mAdd_users = findViewById(R.id.add_users);




        View headerView = navigationView.getHeaderView(0);
        headerUsername =  headerView.findViewById(R.id.username);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);
        String username = sharedPreferences.getString("username", null);
        headerUsername.setText("Hello, " + username);

        mGetAllUsers();
        mGetAllCars();
        mGetAllWarehouses();
        mGetAllDriversLicense();

        hideAlllayers();
        lyAllusers.setVisibility(View.VISIBLE);


        //Searching users
        searchUsersEditText = findViewById(R.id.searchUsersEditText);
        lyFilterUsers = findViewById(R.id.lyFilterUsers);
        imgFilterUsers = findViewById(R.id.imgFilterUsers);
        activeUserCheckBox= findViewById(R.id.activeUserCheckBox);
        adminCheckBox= findViewById(R.id.adminCheckBox);
        driverCheckBox= findViewById(R.id.driverCheckBox);
        customerCheckBox = findViewById(R.id.customerCheckBox);
        warehouseCheckBox= findViewById(R.id.warehouseCheckBox);
        btnApplyUsers= findViewById(R.id.btnApplyUsers);
        btnDiscardUsers = findViewById(R.id.btnDiscardUsers);



        // search cars
        searchCarsEditText= findViewById(R.id.searchCarsEditText);
        lyFilterCars = findViewById(R.id.lyFilterCars);
        imgFilterCars = findViewById(R.id.imgFilterCars);

        radioStatusCars = findViewById(R.id.radioStatusCars);
        approvedCarCheckBox = findViewById(R.id.approvedCarCheckBox);
        notapprovedCarCheckBox = findViewById(R.id.notapprovedCarCheckBox);

        
        
        btnDiscardCars = findViewById(R.id.btnDiscardCars);
        btnApplyCars = findViewById(R.id.btnApplyCars);

        // search warehouses
        searchwarehousesEditText = findViewById(R.id.searchwarehousesEditText);
        lyFilterwarehouses = findViewById(R.id.lyFilterwarehouses);
        imgFilterwarehouses = findViewById(R.id.imgFilterwarehouses);
        chCctv = findViewById(R.id.chCctv);
        chArmedResponse= findViewById(R.id.chArmedResponse);
        chFireSafety= findViewById(R.id.chFireSafety);
        chParkingSpace = findViewById(R.id.chParkingSpace);
        btnDiscardWarehouses= findViewById(R.id.btnDiscardWarehouses);
        btnApplyWarehouses= findViewById(R.id.btnApplyWarehouses);

        // search licenses
        searchLicensesEditText = findViewById(R.id.searchLicensesEditText);
        lyFilterLicenses = findViewById(R.id.lyFilterLicenses);
        imgFilterLicenses = findViewById(R.id.imgFilterLicenses);
        approvedlicenseCheckBox = findViewById(R.id.approvedlicenseCheckBox);
        btnDiscardLicenses = findViewById(R.id.btnDiscardLicenses);
        btnApplyLicenses = findViewById(R.id.btnApplyWarehouses);





        searchUsersEditText.addTextChangedListener(new TextWatcher() {
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
                    filterdataUsers(editable.toString());

                }


            }
        });

        searchCarsEditText.addTextChangedListener(new TextWatcher() {
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
                    filterdataCars(editable.toString());

                }


            }
        });

        imgFilterUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = lyFilterUsers.getVisibility();
                if (visibility == View.VISIBLE) {
                    lyFilterUsers.setVisibility(View.GONE);
                } else
                    lyFilterUsers.setVisibility(View.VISIBLE);
                }

        });

        imgFilterCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = lyFilterCars.getVisibility();
                if (visibility == View.VISIBLE) {
                    lyFilterCars.setVisibility(View.GONE);
                } else
                    lyFilterCars.setVisibility(View.VISIBLE);
            }

        });

        imgFilterwarehouses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = lyFilterwarehouses.getVisibility();
                if (visibility == View.VISIBLE) {
                    lyFilterwarehouses.setVisibility(View.GONE);
                } else
                    lyFilterwarehouses.setVisibility(View.VISIBLE);
            }

        });

        btnDiscardWarehouses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyFilterwarehouses.setVisibility(View.GONE);
            }

        });
        btnApplyWarehouses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyFilterwarehouses.setVisibility(View.GONE);

            }

        });


        imgFilterLicenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = lyFilterLicenses.getVisibility();
                if (visibility == View.VISIBLE) {
                    lyFilterLicenses.setVisibility(View.GONE);
                } else
                    lyFilterLicenses.setVisibility(View.VISIBLE);
            }

        });

        btnApplyUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyFilterUsers.setVisibility(View.GONE);

                if(searchUsersEditText.getText().length() >0){
                    filterdataUsers(searchUsersEditText.getText().toString());
                }else {
                    filterdataUsers("");

                }

            }

        });
        btnDiscardUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyFilterUsers.setVisibility(View.GONE);
                activeUserCheckBox.setChecked(true);
                adminCheckBox.setChecked(true);
                customerCheckBox.setChecked(true);
                driverCheckBox.setChecked(true);
                warehouseCheckBox.setChecked(true);

                if(searchUsersEditText.getText().length() >0){
                    filterdataUsers(searchUsersEditText.getText().toString());
                }else {
                    filterdataUsers("");

                }

            }

        });

        btnDiscardCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyFilterCars.setVisibility(View.GONE);
                radioStatusCars.clearCheck();

                if(searchCarsEditText.getText().length() >0){
                    filterdataCars(searchCarsEditText.getText().toString());
                }else {
                    filterdataCars("");
                }
            }

        });
        btnApplyCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyFilterCars.setVisibility(View.GONE);


                if(searchCarsEditText.getText().length() >0){
                    filterdataCars(searchCarsEditText.getText().toString());
                }else {
                    filterdataCars("");

                }

            }

        });



        mAdd_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent reg = new Intent(AdminPortal.this,Register.class);
                startActivity(reg);
                finish();


            }
        });


    }



    private void filterdataUsers(String text) {

       // Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();


        List<UserItem> filteredList = new ArrayList<UserItem>();

        for (UserItem item : UserItems) {
            try {
                if (item.getUsername().toLowerCase().contains(text.toLowerCase()) ||
                        item.getFirst_name().toLowerCase().contains(text.toLowerCase()) ||
                        item.getLast_name().toLowerCase().contains(text.toLowerCase())  ||
                        item.getAddress().toLowerCase().contains(text.toLowerCase())  ||
                        item.getEmail().toLowerCase().contains(text.toLowerCase())

                ) {
                    filteredList.add(item);
                }
            }catch (Exception e) {
                Log.e("AdminPortal", "Exception" + e.toString());

            }

        }

        if (!activeUserCheckBox.isChecked()) {
            // Filter the list to include items with status 1, 2, and 3
            List<UserItem> UserItemItems = new ArrayList<>();
            for (UserItem item : filteredList) {

                String status = String.valueOf(item.getIs_active());
                //Log.e("AdminPortal", "item.getIs_active()" + status);

                if (status.equals("false")) {
                    UserItemItems.add(item);
                }
            }
            filteredList = UserItemItems;
        }

        List<UserItem> UserWithroles = new ArrayList<>();

        for (UserItem user : filteredList) {
            Log.e("AdminPortal", "filteredList" + filteredList.toString());

            if ((adminCheckBox.isChecked() && user.getRole().equals("Admin")) ||
                    (customerCheckBox.isChecked() && user.getRole().equals("Customer")) ||
                    (driverCheckBox.isChecked() && user.getRole().equals("Driver")) ||
                    (warehouseCheckBox.isChecked() && user.getRole().equals("WareHouse"))) {
                UserWithroles.add(user);
                Log.e("AdminPortal", "user" + user.toString());

            }
        }
        Log.e("AdminPortal", "UserWithroles" + UserWithroles.toString());

        filteredList = UserWithroles;


        adapterAllUsers.SetFilteredList(filteredList);
    }
    private void filterdataCars(String text) {

        // Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

        List<CarItem> filteredList =  new ArrayList<>();

        for (CarItem item : carItems) {
            try {
                if (item.getCapacity().toLowerCase().contains(text.toLowerCase()) ||
                        item.getLicense_plate().toLowerCase().contains(text.toLowerCase()) ||
                        item.getMake ().toLowerCase().contains(text.toLowerCase())  ||
                        item.getModel().toLowerCase().contains(text.toLowerCase())  ||
                        item.getType().toLowerCase().contains(text.toLowerCase()) ||
                         item.getYear().toLowerCase().contains(text.toLowerCase())

                ) {
                    filteredList.add(item);
                }
            }catch (Exception e) {
                Log.e("AdminPortal", "Exception" + e.toString());

            }

        }

        if (approvedCarCheckBox.isChecked()) {
            // Filter the list to include items with status 1, 2, and 3
            List<CarItem> CarItemItems = new ArrayList<>();
            for (CarItem item : filteredList) {

                String status = String.valueOf(item.getIs_approved());
                //Log.e("AdminPortal", "item.getIs_active()" + status);

                if (status.equals("true")) {
                    CarItemItems.add(item);
                }
            }
            filteredList = CarItemItems;
        }

        if (notapprovedCarCheckBox.isChecked()) {
            // Filter the list to include items with status 1, 2, and 3
            List<CarItem> CarItemItems = new ArrayList<>();
            for (CarItem item : filteredList) {

                String status = String.valueOf(item.getIs_approved());
                //Log.e("AdminPortal", "item.getIs_active()" + status);

                if (status.equals("false")) {
                    CarItemItems.add(item);
                }
            }
            filteredList = CarItemItems;
        }
        

        adapterAllCars.SetFilteredList(filteredList);
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

                        UserItems = new ArrayList<UserItem>();

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
//                            UserItems.add(new UserItem(object.getId().toString(),"username: "+object.getUsername(),"Address: "+ object.getAddress(),
//                                    "Firstname: "+object.getFirstName().toString(),"Lastname: "+object.getLastName(),"Email: "+ object.getEmail(),
//                                    object.getIsActive().toString(),object.getProfilePicture().toString(),"Role: "+role,"Phone: "+object.getPhoneNumber()));

                            UserItems.add(new UserItem(object.getId().toString(),object.getUsername(), object.getAddress(),
                                    object.getFirstName().toString(),object.getLastName(),object.getEmail(),
                                    object.getIsActive().toString(),object.getProfilePicture().toString(),role,object.getPhoneNumber()));

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(AdminPortal.this));

                        adapterAllUsers = new AdapterAllUsers(getApplicationContext(),UserItems);
                        recyclerView.setAdapter(adapterAllUsers);
//                        recyclerView.setAdapter(new AdapterPickUpRequest(getApplicationContext(), UserItems));

                        adapterAllUsers.setOnClickListener(new AdapterAllUsers.OnClickListener() {
                            @Override
                            public void onClick(int position, UserItem model) {
                                Intent intent = new Intent(AdminPortal.this, Profile.class);
                                intent.putExtra("user",  model);
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

                        carItems = new ArrayList<CarItem>();

                        for (CarResponse object : objects) {
                            carItems.add(new CarItem(
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
                        recyclerView.setLayoutManager(new LinearLayoutManager(AdminPortal.this));

                        adapterAllCars = new AdapterAllCars(getApplicationContext(),carItems);
                        recyclerView.setAdapter(adapterAllCars);
//                        recyclerView.setAdapter(new AdapterPickUpRequest(getApplicationContext(), CarItems));

                        adapterAllCars.setOnClickListener(new AdapterAllCars.OnClickListener() {
                            @Override
                            public void onClick(int position, CarItem model) {
                                Intent intent = new Intent(AdminPortal.this, AddCar.class);
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

    public void mGetAllDriversLicense(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<List<DriversLicenseResponse>> call = apiService.getAllDriversLicenseObjects();


        call.enqueue(new Callback<List<DriversLicenseResponse>>() {
            @Override
            public void onResponse(Call<List<DriversLicenseResponse>> call, Response<List<DriversLicenseResponse>> response) {
                if (response.isSuccessful()) {

                    List<DriversLicenseResponse> objects = response.body();

                    if (objects != null) {
                        RecyclerView recyclerView = findViewById(R.id.idAllDriversLicenses);

                        List<DriversLicense> driversLicense = new ArrayList<DriversLicense>();

                        for (DriversLicenseResponse object : objects) {

                            driversLicense.add(new DriversLicense(
                                    object.getId().toString(),
                                    object.getLicenseOwner().toString(),
                                    object.getFullname().toString(),
                                    object.getLicenseNumber().toString(),
                                    object.getExpiryDate().toString(),
                                    object.getUploadLicense().toString(),
                                    object.getIsApproved().toString()));


                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(AdminPortal.this));

                        final AdapterAllDriversLicense adapterAllDriversLicense = new AdapterAllDriversLicense(getApplicationContext(),driversLicense);
                        recyclerView.setAdapter(adapterAllDriversLicense);
//                        recyclerView.setAdapter(new AdapterPickUpRequest(getApplicationContext(), driversLicense));

                        adapterAllDriversLicense.setOnClickListener(new AdapterAllDriversLicense.OnClickListener() {
                            @Override
                            public void onClick(int position, DriversLicense model) {
                                Intent intent = new Intent(AdminPortal.this, AddDriversLicense.class);
                                intent.putExtra("driversLicense",  model);
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
            public void onFailure(Call<List<DriversLicenseResponse>> call, Throwable t) {
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

                        adapterAllWarehouses.setOnClickListener(new AdapterAllWarehouses.OnClickListener() {
                            @Override
                            public void onClick(int position, WareHouseItemAdmin model) {
                                Intent intent = new Intent(AdminPortal.this, AddWarehouse.class);
                                intent.putExtra("warehouse",  model);
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
            case R.id.users:
                hideAlllayers();
                lyAllusers.setVisibility(View.VISIBLE);
                break;
            case R.id.cars:
                hideAlllayers();
                lyAllCars.setVisibility(View.VISIBLE);
                break;
            case R.id.warehouses:
                hideAlllayers();
                lyWarehouses.setVisibility(View.VISIBLE);
                break;
            case R.id.licenses:
                hideAlllayers();
                lyDriversLicenses.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_loginout:
                Logout();
                break;
            case R.id.password:
                changePassword();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void hideAlllayers(){
        lyAllusers.setVisibility(View.GONE);
        lyAllCars.setVisibility(View.GONE);
        lyWarehouses.setVisibility(View.GONE);
        lyDriversLicenses.setVisibility(View.GONE);
    }
    public void DriversLicense()
    {
        Intent intent = new Intent(getApplicationContext(),AddDriversLicense.class);
        startActivity(intent);
        finish();
    }

    public void changePassword()
    {
        Intent intent = new Intent(getApplicationContext(),ResetPassword.class);
        intent.putExtra("password", "password");
        startActivity(intent);
        finish();
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