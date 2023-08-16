package com.example.wetouriah;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.example.wetouriah.databinding.ActivityPickUpRequestDetailedBinding;


public class PickUpRequestDetailed extends AppCompatActivity {

//    private ActivityPickUpRequestDetailedBinding binding;



    TextView request_time,updated,date_and_time_pickup,date_and_time_dropoff;

    TextView  recipient_name,recipient_phone,pickup_location,dropoff_location,
            volume,weight,parcel_description,special_notes,price_to_pay,customer,images;

    Button drive,chat, deliver;

    RadioGroup rdgAcceptDecline;
    RadioButton rdAccept,rdDecline;

    RelativeLayout lyMainDetailed,lyAcceptDecline;


    Button btnRate;


    PickUpRequestItem pickUpRequestItem = null;

    String pickup = null;

    SharedPreferences sharedPreferences;
    String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_request_detailed);


        request_time = findViewById(R.id.request_time);

        recipient_name = findViewById(R.id.recipient_name);
        recipient_phone = findViewById(R.id.recipient_phone);
        pickup_location = findViewById(R.id.pickup_location);
        dropoff_location = findViewById(R.id.dropoff_location);
        volume = findViewById(R.id.volume);
        weight = findViewById(R.id.weight);
        price_to_pay = findViewById(R.id.price_to_pay);

        drive = findViewById(R.id.drive);

        rdgAcceptDecline = findViewById(R.id.rdgAcceptDecline);
        rdAccept = findViewById(R.id.rdAccept);
        rdDecline = findViewById(R.id.rdDecline);

        chat = findViewById(R.id.chat);

        deliver = findViewById(R.id.deliver);



        lyMainDetailed = findViewById(R.id.lyMainDetailed);
        lyAcceptDecline = findViewById(R.id.lyAcceptDecline);








        setTitle("Detailed Pick Up ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        role = sharedPreferences.getString("role", null);

//        if (getIntent().hasExtra(RequestPickUp.NEXT_SCREEN)) {
//            pickUpRequestItem = (PickUpRequestItem) getIntent().getSerializableExtra(RequestPickUp.NEXT_SCREEN);
//
//        }

        if (role.equals("1")) {
            // Navigate to the Admin
        }

        if (role.equals("2")) {
            // Navigate to the CustomerPortal
            drive.setVisibility(View.INVISIBLE);
            lyAcceptDecline.setVisibility(View.INVISIBLE);

        }
        if (role.equals("3")) {
           // Navigate to the Driver

        }
        if (role.equals("4")) {
            // Navigate to the WarehousePortal
            drive.setVisibility(View.INVISIBLE);
            lyAcceptDecline.setVisibility(View.INVISIBLE);
        }

        if (pickUpRequestItem != null) {

            request_time.setText("Requested at: "+ pickUpRequestItem.getRequest_time());
            date_and_time_pickup.setText("date and time pickup: "+pickUpRequestItem.getDate_and_time_pickup());
            recipient_name.setText("Recipient name: "+pickUpRequestItem.getRecipient_name());
            recipient_phone.setText("Recipient phone: "+pickUpRequestItem.getRecipient_phone());
            pickup_location.setText("Pickup location: "+pickUpRequestItem.getPickup_location());
            dropoff_location.setText("Dropoff location: "+pickUpRequestItem.getDropoff_location());
            volume.setText("Volume: "+pickUpRequestItem.getVolume());
            weight.setText("Weight: " +pickUpRequestItem.getWeight());
            price_to_pay.setText("R "+pickUpRequestItem.getPrice_to_pay());
            customer.setText("customer: "+pickUpRequestItem.getCustomer());
//            images.setText(pickUpRequestItem.getImages());

//            Toast.makeText(PickUpRequestDetailed.this,"Rated"+ pickUpRequestItem.getId(),Toast.LENGTH_SHORT).show();



        }

        drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGoogleMapsDirections(PickUpRequestDetailed.this,pickUpRequestItem.getPickup_location().toString(),pickUpRequestItem.getDropoff_location().toString());

            }
        });
        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PickUpRequestDetailed.this, "id detailed "+ pickUpRequestItem.getId().toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PickUpRequestDetailed.this, Deliver.class);
                intent.putExtra("item_id", Integer.parseInt(pickUpRequestItem.getId().toString()) );
                startActivity(intent);
                finish();

            }
        });

        rdgAcceptDecline.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdAccept) {
                    mAcceptDecline(pickUpRequestItem.getId().toString(),"True");
                } else if (checkedId == R.id.rdDecline) {
                    mAcceptDecline(pickUpRequestItem.getId().toString(),"False");
                }
            }
        });


        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetAnID(pickUpRequestItem.getId());
            }
        });





    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Check the user's role

            if (role.equals("1")) {
                // Navigate to the CustomerPortal
                Intent intent = new Intent(this, AdminPortal.class);
                startActivity(intent);
                finish();
            }

            if (role.equals("2")) {
                // Navigate to the CustomerPortal
                Intent intent = new Intent(this, CustomerPortal.class);
                startActivity(intent);
                finish();
            }
            if (role.equals("3")) {
                // Navigate to the WarehousePortal
                Intent intent = new Intent(this, DriverPortal.class);
                startActivity(intent);
                finish();
            }
            if (role.equals("4")) {
                // Navigate to the WarehousePortal
                Intent intent = new Intent(this, WarehousePortal.class);
                startActivity(intent);
                finish();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void GetAnID(String request_pickup) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody request_pickup_id= RequestBody.create(MediaType.parse("multipart/form-data"), request_pickup);

        APIService apiService = retrofit.create(APIService.class);
        Call<PickupResponse> call = apiService.getPickupIDfromRequestpickup(request_pickup_id);

        call.enqueue(new Callback<PickupResponse>() {
            @Override
            public void onResponse(Call<PickupResponse> call, Response<PickupResponse> response) {

                if(response.isSuccessful()){
                    pickup = response.body().getId().toString();

                    methodThatUsesPickupValue(pickup);
                }
            }
            @Override
            public void onFailure(Call<PickupResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void methodThatUsesPickupValue(String pickup) {
        Intent intent = new Intent(getApplicationContext(), Chat.class);
        intent.putExtra("id", pickup);
        startActivity(intent);
        finish();

    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//            String role = sharedPreferences.getString("role", null);
//            // Check the user's role
//            if (role.equals("2")) {
//                // Navigate to the CustomerPortal
//                Intent intent = new Intent(this, RequestPickUp.class);
//                startActivity(intent);
//                finish();
//            }
//            if (role.equals("4")) {
//                // Navigate to the WarehousePortal
//                Intent intent = new Intent(this, RequestPickUp.class);
//                startActivity(intent);
//                finish();
//            }
//
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    public static final String NEXT_SCREEN = "drive";

    public static void openGoogleMapsDirections(Context context, String originAddress, String destinationAddress) {
        double originLat, originLng, destLat, destLng;

        // Geocode origin address to coordinates if needed
        if (TextUtils.isEmpty(originAddress)) {
            // Handle case when origin address is not provided
            Toast.makeText(context, "Origin address is missing", Toast.LENGTH_SHORT).show();
            return;
        } else {
            LatLng originLatLng = geocodeAddress(context, originAddress);
            if (originLatLng == null) {
                // Handle case when origin address is not valid or couldn't be geocoded
                Toast.makeText(context, "Invalid origin address", Toast.LENGTH_SHORT).show();
                return;
            }
            originLat = originLatLng.latitude;
            originLng = originLatLng.longitude;
        }

        // Geocode destination address to coordinates if needed
        if (TextUtils.isEmpty(destinationAddress)) {
            // Handle case when destination address is not provided
            Toast.makeText(context, "Destination address is missing", Toast.LENGTH_SHORT).show();
            return;
        } else {
            LatLng destLatLng = geocodeAddress(context, destinationAddress);
            if (destLatLng == null) {
                // Handle case when destination address is not valid or couldn't be geocoded
                Toast.makeText(context, "Invalid destination address", Toast.LENGTH_SHORT).show();
                return;
            }
            destLat = destLatLng.latitude;
            destLng = destLatLng.longitude;
        }

        Uri uri = Uri.parse("http://maps.google.com/maps?saddr=" + originLat + "," + originLng + "&daddr=" + destLat + "," + destLng);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Google Maps app is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private static LatLng geocodeAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (addresses != null && addresses.size() > 0) {
            Address location = addresses.get(0);
            return new LatLng(location.getLatitude(), location.getLongitude());
        }

        return null;
    }

    public void mAcceptDecline(String pickup_id, String accept_decline) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();


        RequestBody get_pickup_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(pickup_id) );
        RequestBody get_pick_accept_decline = RequestBody.create(MediaType.parse("multipart/form-data"), accept_decline);

        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call=  apiService.acceptDeclineJob(get_pickup_id,get_pick_accept_decline);


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
}