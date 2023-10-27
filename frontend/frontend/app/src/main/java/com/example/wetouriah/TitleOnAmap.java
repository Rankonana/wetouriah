package com.example.wetouriah;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TitleOnAmap extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    private List<WareHouseItem> items;
    private List<String> warehouseAddresses;
    private List<String> houseAddresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_on_amap);
        setTitle("Available Warehouses");


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        createHouseAddresses();
    }

    // Helper method to convert address to LatLng
    private LatLng getLocationFromAddress(String location) {
        List<Address> addressList = null;


        Geocoder geocoder = new Geocoder(TitleOnAmap.this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        return latLng;

    }
    private void createHouseAddresses() {
        List<String> addresses = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS + ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<List<WarehouseResponse>> call = apiService.getWarehousesObjects(""); // Use the new endpoint

        call.enqueue(new Callback<List<WarehouseResponse>>() {
            @Override
            public void onResponse(Call<List<WarehouseResponse>> call, Response<List<WarehouseResponse>> response) {
                if (response.isSuccessful()) {
                    List<WarehouseResponse> objects = response.body();
                    warehouseAddresses = new ArrayList<>();
                    if (objects != null) {
                        for (WarehouseResponse object : objects) {
                            warehouseAddresses.add(object.getAddress().toString());
                        }
                    }
                    processAddresses(warehouseAddresses);


                } else {
                }
            }

            @Override
            public void onFailure(Call<List<WarehouseResponse>> call, Throwable t) {
            }
        });


    }

    private void processAddresses(List<String> addresses) {
        for (String address : addresses) {
            LatLng latLng = getLocationFromAddress(address);
            if (latLng != null) {
                mMap.addMarker(new MarkerOptions().position(latLng).title(address));
            }
        }
        // Move camera to the first marker
        if (!addresses.isEmpty()) {
            LatLng firstMarkerLatLng = getLocationFromAddress(addresses.get(0));
            if (firstMarkerLatLng != null) {
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstMarkerLatLng, 12));
            }
        }
    }
}