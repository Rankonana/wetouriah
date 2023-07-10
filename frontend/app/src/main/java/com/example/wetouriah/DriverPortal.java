package com.example.wetouriah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class DriverPortal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btnProfile, btnAddCar, btnRequestpickup, btnViewItems,btnLogout;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_portal);
        setTitle("Driver Portal");

        //
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //

        btnAddCar = (Button)findViewById(R.id.btnAddCar);
//        btnRequestpickup = (Button)findViewById(R.id.btnRequestpickup);
//        btnViewItems = (Button)findViewById(R.id.btnViewItems);

        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverPortal.this,AddCar.class);
                startActivity(intent);
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
                showToast("logout success");
                Logout();
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

        Intent intent = new Intent(DriverPortal.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}