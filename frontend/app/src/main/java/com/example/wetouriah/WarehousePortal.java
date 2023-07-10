package com.example.wetouriah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WarehousePortal extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {


    Button btnAddWarehouse, btnRequestpickup, btnViewItems;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_portal);
        setTitle("Warehouse Portal");

        btnAddWarehouse = (Button)findViewById(R.id.btnAddWarehouse);
        btnRequestpickup = (Button)findViewById(R.id.btnRequestpickup);
        btnViewItems = (Button)findViewById(R.id.btnViewItems);


        //
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //



        btnAddWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarehousePortal.this,AddWarehouse.class);
                startActivity(intent);
                finish();

            }
        });
        btnRequestpickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarehousePortal.this,RequestPickUp.class);
                startActivity(intent);
                finish();

            }
        });
        btnViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarehousePortal.this,Login.class);
                startActivity(intent);
                finish();

            }
        });



//        RecyclerView recyclerView = findViewById(R.id.idWarehouses);
//
//        List<Item> items = new ArrayList<Item>();
//
//
//        items.add(new Item("https://" + Constants.SERVER_IP_ADDRESS+ ""+"/media/profile_picture.jpg","12B Comet","15 sq"));
//        items.add(new Item("https://" + Constants.SERVER_IP_ADDRESS+ ""+"/media/6.png","11B Comet","15 sq"));
//        items.add(new Item("https://" + Constants.SERVER_IP_ADDRESS+ ""+"/media/IMG_20230605_140045146.jpeg","11B Comet","15 sq"));
//        items.add(new Item("https://" + Constants.SERVER_IP_ADDRESS+ ""+"/media/profile_picture.jpg","11B Comet","15 sq"));
//        items.add(new Item("https://" + Constants.SERVER_IP_ADDRESS+ ""+"/media/profile_picture.jpg","11B Comet","15 sq"));
//
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),items));


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
                showToast("logout success");
                Logout();
                break;
            case R.id.nav_inner_profile:
                Intent intent = new Intent(WarehousePortal.this,Profile.class);
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
        // Get an instance of SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        // Get an instance of SharedPreferences Editor to modify the preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Remove the value from the SharedPreferences using the key
        editor.remove("access");
        editor.remove("refresh");
        editor.remove("role");
        editor.remove("username");
        editor.remove("username");
        // Commit the changes
        editor.apply();

        Intent intent = new Intent(WarehousePortal.this,MainActivity.class);
        startActivity(intent);
        finish();
    }



}