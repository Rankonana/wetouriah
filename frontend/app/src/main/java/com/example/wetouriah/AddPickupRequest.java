package com.example.wetouriah;

import static com.example.wetouriah.Constants.generateTrackingNumber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPickupRequest extends AppCompatActivity {



    private ScrollView lyLocationsWightVolume,lyPrice,lyImages,lyRecipentDetails;


    EditText recipient_name,recipient_phone,pickup_location,dropoff_location,
            volume,weight, parcel_description;

    Button btnShowPrice,btnShowImages, btnShowRecipientDetails, btnAddPickRequest;

    TextView txtPrice;
    String price_to_pay;

    PickUpRequestItem pickUpRequestItem;
    String RequestPickup_id;


    private static final int PICK_IMAGE_REQUEST = 1;
    private LinearLayout layoutImages;
    private ArrayList<Uri> imageUris;
    SliderView sliderView;
    private ImageView imageView;
    Button btn_add_more;


    private ArrayList<ImageItem> LocalimageItems;
    boolean containsTargetImage = false;

    CardView qaMapOrAddress,enterAnAddress,enterAWarehouse;
    Button btnQaClose,btnChooseWarehouse, btnEnterAnAddress,btnABack,btnAOk;
    Button BtnWarehousesList;


    List<WareHouseItemAdmin> wareHouseItemAdmin;
    RecyclerView recyclerView ;
    AdapterPickWarehouse adapterAllWarehouses;
    EditText aWarehouse, aDestination;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pickup_request);


        setTitle("Add Pickup Request");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        lyLocationsWightVolume = findViewById(R.id.lyLocationsWightVolume);
        pickup_location = findViewById(R.id.pickup_location);
        dropoff_location = findViewById(R.id.dropoff_location);
        volume = findViewById(R.id.volume);
        weight = findViewById(R.id.weight);
        parcel_description = findViewById(R.id.parcel_description);
        btnShowPrice = findViewById(R.id.btnShowPrice);


        lyPrice = findViewById(R.id.lyPrice);
        txtPrice = findViewById(R.id.txtPrice);
        btnShowImages = findViewById(R.id.btnShowImages);


        lyImages =  findViewById(R.id.lyImages);
//        layoutImages = findViewById(R.id.layout_images);
//        imageView = findViewById(R.id.image_view);
        btn_add_more = findViewById(R.id.btn_add_more);

        imageUris = new ArrayList<>();
        LocalimageItems = new ArrayList<ImageItem>();
        sliderView = findViewById(R.id.slider);


        btnShowRecipientDetails = findViewById(R.id.btnShowRecipientDetails);


        lyRecipentDetails = findViewById(R.id.lyRecipentDetails);
        recipient_name = findViewById(R.id.recipient_name);
        recipient_phone = findViewById(R.id.recipient_phone);
        btnAddPickRequest = findViewById(R.id.btnAddPickRequest);

        qaMapOrAddress = findViewById(R.id.qaMapOrAddress);
        btnChooseWarehouse= findViewById(R.id.btnChooseWarehouse);
        btnEnterAnAddress= findViewById(R.id.btnEnterAnAddress);

        aWarehouse = findViewById(R.id.aWarehouse);
        aDestination = findViewById(R.id.aDestination);
        enterAnAddress = findViewById(R.id.enterAnAddress);
        enterAWarehouse = findViewById(R.id.enterAWarehouse);
        recyclerView = findViewById(R.id.rvWarehouses);
        btnABack  = findViewById(R.id.btnABack);
        btnAOk  = findViewById(R.id.btnAOk);
        btnQaClose = findViewById(R.id.btnQaClose);
        BtnWarehousesList = findViewById(R.id.BtnWarehousesList);


        Intent intent = getIntent();
        pickUpRequestItem = (PickUpRequestItem) intent.getSerializableExtra("model");

        LoadPickUpRequest();
        mGetAllWarehouses();
        mGetUserAddress();

        aWarehouse.addTextChangedListener(new TextWatcher() {
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
                    filterdataWarehouses(editable.toString());

                }


            }
        });
        dropoff_location.setFocusable(false);
        dropoff_location.setFocusableInTouchMode(false);

        BtnWarehousesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterAWarehouse.setVisibility(View.GONE);
            }
        });
        dropoff_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qaMapOrAddress.setVisibility(View.VISIBLE);
            }
        });

        btnQaClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qaMapOrAddress.setVisibility(View.GONE);
            }
        });

        btnABack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterAnAddress.setVisibility(View.GONE);
            }
        });
        btnAOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(aDestination.getText().length() >0){
                        dropoff_location.setText(aDestination.getText().toString());
                        enterAnAddress.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(AddPickupRequest.this, "Enter an address", Toast.LENGTH_SHORT).show();


                    }
                }
            });


        btnChooseWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qaMapOrAddress.setVisibility(View.GONE);
                enterAWarehouse.setVisibility(View.VISIBLE);
            }
        });
        btnEnterAnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qaMapOrAddress.setVisibility(View.GONE);
                enterAnAddress.setVisibility(View.VISIBLE);
            }
        });


        btnShowPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideAllLayers();
                lyPrice.setVisibility(View.VISIBLE);


                Geocoder geocoder = new Geocoder(AddPickupRequest.this);
                Location currentLocation = new Location("locationA");
                Location destination = new Location("locationB");


                try {

                    List<Address> originAddresses = geocoder.getFromLocationName(pickup_location.getText().toString(), 1);
                    List<Address> destinationAddresses = geocoder.getFromLocationName(dropoff_location.getText().toString(), 1);



                    if (originAddresses != null && originAddresses.size() > 0 && destinationAddresses != null && destinationAddresses.size() > 0) {
                        Address origin = originAddresses.get(0);
                        Address dest = destinationAddresses.get(0);

                        currentLocation.setLatitude(origin.getLatitude());
                        currentLocation.setLongitude(origin.getLongitude());

                        destination.setLatitude(dest.getLatitude());
                        destination.setLongitude(dest.getLongitude());

                        float distance = currentLocation.distanceTo(destination)/1000;
                        //Toast.makeText(AddPickupRequest.this, "Distance between the two addresses: " + distance + " meters", Toast.LENGTH_SHORT).show();

                        double dvolume =  Double.parseDouble(volume.getText().toString());; // Volume in cubic meters
                        double dweight = Double.parseDouble(weight.getText().toString());; // Weight in kilograms

                        double xprice = mCalulatePrice(distance,dvolume, dweight);
                        price_to_pay = String.format("%.2f", xprice) ;
                        txtPrice.setText("R" + String.format("%.2f", xprice) );


                    } else {
                        Log.e("AddPick", "Failed to geocode the addresses");

                        Toast.makeText(AddPickupRequest.this, "Failed to geocode the addresses", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("AddPick", "Exception" + e.toString());

                    Toast.makeText(AddPickupRequest.this, "Failed to calculate the distance", Toast.LENGTH_SHORT).show();
                }





            }
        });

        btnShowImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideAllLayers();
                lyImages.setVisibility(View.VISIBLE);

            }
        });

        btn_add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(AddPickupRequest.this)
                        .crop()
                        .galleryOnly()
                        .maxResultSize(512,512)
                        .provider(ImageProvider.GALLERY)
                        .start();
            }
        });

        btnShowRecipientDetails.setOnClickListener(new View.OnClickListener() {
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

                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                String user_id = sharedPreferences.getString("user_id", null);

                if(RequestPickup_id != null && !RequestPickup_id.isEmpty()){


                    if (!imageUris.isEmpty()) {
                        for (Uri imageUri : imageUris) {

                            mUpdateWithImagesPickRequest(
                                    pickUpRequestItem.getId(),
                                    pickUpRequestItem.getCustomer().toString(),
                                    pickUpRequestItem.getTracking_number().toString(),
                                    recipient_name.getText().toString(),
                                    recipient_phone.getText().toString(),
                                    pickup_location.getText().toString(),
                                    dropoff_location.getText().toString(),
                                    volume.getText().toString(),
                                    weight.getText().toString(),
                                    parcel_description.getText().toString(),
                                    price_to_pay
                            );

                        }
                    }else{

                        mUpdateNoImagesPickRequest(
                                pickUpRequestItem.getId(),
                                pickUpRequestItem.getCustomer().toString(),
                                pickUpRequestItem.getTracking_number().toString(),
                                recipient_name.getText().toString(),
                                recipient_phone.getText().toString(),
                                pickup_location.getText().toString(),
                                dropoff_location.getText().toString(),
                                volume.getText().toString(),
                                weight.getText().toString(),
                                parcel_description.getText().toString(),
                                price_to_pay
                        );

                    }



                }else {

                    if (!imageUris.isEmpty()) {
                        for (Uri imageUri : imageUris) {

                            mAddWithImagespickRequest(
                                    user_id,
                                    generateTrackingNumber(),
                                    recipient_name.getText().toString(),
                                    recipient_phone.getText().toString(),
                                    pickup_location.getText().toString(),
                                    dropoff_location.getText().toString(),
                                    volume.getText().toString(),
                                    weight.getText().toString(),
                                    parcel_description.getText().toString(),
                                    price_to_pay,
                                    "1");

                        }
                    }else {
                        mAddNoImagespickRequest(
                                user_id,
                                generateTrackingNumber(),
                                recipient_name.getText().toString(),
                                recipient_phone.getText().toString(),
                                pickup_location.getText().toString(),
                                dropoff_location.getText().toString(),
                                volume.getText().toString(),
                                weight.getText().toString(),
                                parcel_description.getText().toString(),
                                price_to_pay,
                                "1");

                    }



                }




                Intent intent = new Intent(AddPickupRequest.this, MainActivity.class);
                startActivity(intent);
                finish();




            }
        });



    }
    private void hideAllLayers(){
        lyLocationsWightVolume.setVisibility(View.GONE);
        lyPrice.setVisibility(View.GONE);
        lyImages.setVisibility(View.GONE);
        lyRecipentDetails.setVisibility(View.GONE);
    }

    public void mAddNoImagespickRequest(String customer,
                                String  tracking_number,
                                String recipient_name,
                                String recipient_phone,
                                String  pickup_location,
                                String dropoff_location,
                                String volume,
                                String weight,
                                String parcel_description,
                                String price_to_pay,
                                String status) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody pick_customer = RequestBody.create(MediaType.parse("multipart/form-data"), customer);
        RequestBody pick_tracking_number = RequestBody.create(MediaType.parse("multipart/form-data"), tracking_number);
        RequestBody pick_recipient_name = RequestBody.create(MediaType.parse("multipart/form-data"), recipient_name);
        RequestBody pick_recipient_phone = RequestBody.create(MediaType.parse("multipart/form-data"), recipient_phone);

        RequestBody pick_pickup_location = RequestBody.create(MediaType.parse("multipart/form-data"), pickup_location);
        RequestBody pick_dropoff_location = RequestBody.create(MediaType.parse("multipart/form-data"), dropoff_location);
        RequestBody pick_volume = RequestBody.create(MediaType.parse("multipart/form-data"), volume);
        RequestBody pick_weight = RequestBody.create(MediaType.parse("multipart/form-data"), weight);
        RequestBody pick_parcel_description = RequestBody.create(MediaType.parse("multipart/form-data"), parcel_description);
        RequestBody pick_price_to_pay = RequestBody.create(MediaType.parse("multipart/form-data"), price_to_pay);
        RequestBody pick_status = RequestBody.create(MediaType.parse("multipart/form-data"), status);





        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.addNoImagesPickRequest(
                pick_customer  ,
                pick_tracking_number,
                pick_recipient_name,
                pick_recipient_phone,
                pick_pickup_location,
                pick_dropoff_location,
                pick_volume,
                pick_weight,
                pick_parcel_description,
                pick_price_to_pay,
                pick_status
        );

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

    public void mAddWithImagespickRequest(String customer,
                                        String  tracking_number,
                                        String recipient_name,
                                        String recipient_phone,
                                        String  pickup_location,
                                        String dropoff_location,
                                        String volume,
                                        String weight,
                                        String parcel_description,
                                        String price_to_pay,
                                        String status) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody pick_customer = RequestBody.create(MediaType.parse("multipart/form-data"), customer);
        RequestBody pick_tracking_number = RequestBody.create(MediaType.parse("multipart/form-data"), tracking_number);
        RequestBody pick_recipient_name = RequestBody.create(MediaType.parse("multipart/form-data"), recipient_name);
        RequestBody pick_recipient_phone = RequestBody.create(MediaType.parse("multipart/form-data"), recipient_phone);

        RequestBody pick_pickup_location = RequestBody.create(MediaType.parse("multipart/form-data"), pickup_location);
        RequestBody pick_dropoff_location = RequestBody.create(MediaType.parse("multipart/form-data"), dropoff_location);
        RequestBody pick_volume = RequestBody.create(MediaType.parse("multipart/form-data"), volume);
        RequestBody pick_weight = RequestBody.create(MediaType.parse("multipart/form-data"), weight);
        RequestBody pick_parcel_description = RequestBody.create(MediaType.parse("multipart/form-data"), parcel_description);
        RequestBody pick_price_to_pay = RequestBody.create(MediaType.parse("multipart/form-data"), price_to_pay);
        RequestBody pick_status = RequestBody.create(MediaType.parse("multipart/form-data"), status);


        List<MultipartBody.Part> imageParts = new ArrayList<>();
        if (!imageUris.isEmpty()) {
            for (Uri imageUri : imageUris) {

                String   imagepath = RealPathUtil.getRealPath(this,imageUri);
                File file = new File(imagepath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagefield = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                imageParts.add(imagefield);

            }
        }



        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.addWithImagesPickRequest(
                pick_customer  ,
                pick_tracking_number,
                pick_recipient_name,
                pick_recipient_phone,
                pick_pickup_location,
                pick_dropoff_location,
                pick_volume,
                pick_weight,
                pick_parcel_description,
                pick_price_to_pay,
                pick_status,
                imageParts
        );

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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    public void LoadPickUpRequest(){


        if (pickUpRequestItem != null) {
            setTitle("Update Pickup Request");
            btnAddPickRequest.setText("Update Request");

            pickup_location.setText(pickUpRequestItem.getPickup_location());
            dropoff_location.setText(pickUpRequestItem.getDropoff_location());
            volume .setText(pickUpRequestItem.getVolume());
            weight.setText(pickUpRequestItem.getWeight());
            parcel_description.setText(pickUpRequestItem.getParcel_description());

            recipient_name.setText(pickUpRequestItem.getRecipient_name());
            recipient_phone.setText(pickUpRequestItem.getRecipient_phone());
            RequestPickup_id = pickUpRequestItem.getId();

            mGetAllImages(pickUpRequestItem.getTracking_number());

        }else{



            LocalimageItems.add(new ImageItem("1","/media/NoImage.png","4","4"));
            SliderAdapter adapter = new SliderAdapter(this, LocalimageItems);
            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            sliderView.setSliderAdapter(adapter);
            sliderView.setScrollTimeInSec(3);
            sliderView.setAutoCycle(true);
            sliderView.startAutoCycle();

            containsTargetImage = true;



        }

    }

    public void mGetAllImages(String tracking_number){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", null);

        RequestBody get_tracking_number= RequestBody.create(MediaType.parse("multipart/form-data"), tracking_number);

        Call<List<ImagesResponse>> call = apiService.getAllImages(get_tracking_number);



        call.enqueue(new Callback<List<ImagesResponse>>() {
            @Override
            public void onResponse(Call<List<ImagesResponse>> call, Response<List<ImagesResponse>> response) {
                if (response.isSuccessful()) {

                    List<ImagesResponse> objects = response.body();

                   // parcelImageitems = new ArrayList<ImageItem>();;


                    if ( objects != null && !objects.isEmpty()) {

                       // Toast.makeText(AddPickupRequest.this, "Images found", Toast.LENGTH_SHORT).show();

                        RecyclerView recyclerView = findViewById(R.id.rvLocations);



                        for (ImagesResponse object : objects) {

                            String imagestatus = object.getImageStatus().toString();

                            //During creation
                            if(imagestatus.equals("4")){
                                LocalimageItems.add(new ImageItem(object.getId().toString(),object.getImage(),object.getPickupRequest().toString(),object.getImageStatus().toString())
                                );
                            }



                        }

                        loadParcelImages(LocalimageItems);



                    }else{
                       // Toast.makeText(AddPickupRequest.this, "No images found", Toast.LENGTH_SHORT).show();
                        loadParcelImages(LocalimageItems);


                    }

                } else {
                    Toast.makeText(AddPickupRequest.this, "response not successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ImagesResponse>> call, Throwable t) {
                // Handle failure
                Toast.makeText(AddPickupRequest.this, "failure", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void mUpdateNoImagesPickRequest(
                                String RequestPickup_id,
                                String customer,
                                String tracking_number,
                                String recipient_name,
                                String recipient_phone,
                                String  pickup_location,
                                String dropoff_location,
                                String volume,
                                String weight,
                                String parcel_description,
                                String price_to_pay
                                ) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody pick_id = RequestBody.create(MediaType.parse("multipart/form-data"), RequestPickup_id);
        RequestBody pick_customer = RequestBody.create(MediaType.parse("multipart/form-data"), customer);
        RequestBody pick_tracking_number = RequestBody.create(MediaType.parse("multipart/form-data"), tracking_number);

        RequestBody pick_recipient_name = RequestBody.create(MediaType.parse("multipart/form-data"), recipient_name);
        RequestBody pick_recipient_phone = RequestBody.create(MediaType.parse("multipart/form-data"), recipient_phone);

        RequestBody pick_pickup_location = RequestBody.create(MediaType.parse("multipart/form-data"), pickup_location);
        RequestBody pick_dropoff_location = RequestBody.create(MediaType.parse("multipart/form-data"), dropoff_location);
        RequestBody pick_volume = RequestBody.create(MediaType.parse("multipart/form-data"), volume);
        RequestBody pick_weight = RequestBody.create(MediaType.parse("multipart/form-data"), weight);
        RequestBody pick_parcel_description = RequestBody.create(MediaType.parse("multipart/form-data"), parcel_description);
        RequestBody pick_price_to_pay = RequestBody.create(MediaType.parse("multipart/form-data"), price_to_pay);






        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.updateNoImagesPickRequest(
                pick_id  ,
                pick_customer,
                pick_tracking_number,
                pick_recipient_name,
                pick_recipient_phone,
                pick_pickup_location,
                pick_dropoff_location,
                pick_volume,
                pick_weight,
                pick_parcel_description,
                pick_price_to_pay
        );

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
    public void mUpdateWithImagesPickRequest(
            String RequestPickup_id,
            String customer,
            String tracking_number,
            String recipient_name,
            String recipient_phone,
            String  pickup_location,
            String dropoff_location,
            String volume,
            String weight,
            String parcel_description,
            String price_to_pay
    ) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody pick_id = RequestBody.create(MediaType.parse("multipart/form-data"), RequestPickup_id);
        RequestBody pick_customer = RequestBody.create(MediaType.parse("multipart/form-data"), customer);
        RequestBody pick_tracking_number = RequestBody.create(MediaType.parse("multipart/form-data"), tracking_number);

        RequestBody pick_recipient_name = RequestBody.create(MediaType.parse("multipart/form-data"), recipient_name);
        RequestBody pick_recipient_phone = RequestBody.create(MediaType.parse("multipart/form-data"), recipient_phone);

        RequestBody pick_pickup_location = RequestBody.create(MediaType.parse("multipart/form-data"), pickup_location);
        RequestBody pick_dropoff_location = RequestBody.create(MediaType.parse("multipart/form-data"), dropoff_location);
        RequestBody pick_volume = RequestBody.create(MediaType.parse("multipart/form-data"), volume);
        RequestBody pick_weight = RequestBody.create(MediaType.parse("multipart/form-data"), weight);
        RequestBody pick_parcel_description = RequestBody.create(MediaType.parse("multipart/form-data"), parcel_description);
        RequestBody pick_price_to_pay = RequestBody.create(MediaType.parse("multipart/form-data"), price_to_pay);



//        List<MultipartBody.Part> imageParts = new ArrayList<>();
//        if (!imageUris.isEmpty()) {
//            for (Uri imageUri : imageUris) {
//
//                String   imagepath = RealPathUtil.getRealPath(this,imageUri);
//                File file = new File(imagepath);
//                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                MultipartBody.Part imagefield = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//                imageParts.add(imagefield);
//
//            }
//        }
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        if (!imageUris.isEmpty()) {
            for (Uri imageUri : imageUris) {

                String   imagepath = RealPathUtil.getRealPath(this,imageUri);
                File file = new File(imagepath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imagefield = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                imageParts.add(imagefield);

            }
        }


        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.updateWithImagesPickRequest(
                pick_id  ,
                pick_customer,
                pick_tracking_number,
                pick_recipient_name,
                pick_recipient_phone,
                pick_pickup_location,
                pick_dropoff_location,
                pick_volume,
                pick_weight,
                pick_parcel_description,
                pick_price_to_pay,
                imageParts
        );

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

    public void loadParcelImages(ArrayList<ImageItem> imageItems){


        if (imageItems == null || imageItems.isEmpty()) {

            imageItems.add(new ImageItem("1","/media/NoImage.png","1","1")
            );
            containsTargetImage = true;
            SliderAdapter adapter = new SliderAdapter(this, imageItems);
            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            sliderView.setSliderAdapter(adapter);
            sliderView.setScrollTimeInSec(3);
            sliderView.setAutoCycle(true);
            sliderView.startAutoCycle();

        } else {

            SliderAdapter adapter = new SliderAdapter(this, imageItems);
            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            sliderView.setSliderAdapter(adapter);
            sliderView.setScrollTimeInSec(3);
            sliderView.setAutoCycle(true);
            sliderView.startAutoCycle();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
            Uri uri = data.getData();
            if(uri != null){
                imageUris.add(uri);
                if (containsTargetImage) {
                    LocalimageItems.remove(0);
                    containsTargetImage = false;
                }

                LocalimageItems.add(new ImageItem("1",uri.toString(),"4","4"));
                SliderAdapter adapter = new SliderAdapter(this, LocalimageItems);
                sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                sliderView.setSliderAdapter(adapter);
                sliderView.setScrollTimeInSec(3);
                sliderView.setAutoCycle(true);
                sliderView.startAutoCycle();
            }

        }




    }

    public void mGetAllWarehouses(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
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

                        wareHouseItemAdmin = new ArrayList<WareHouseItemAdmin>();

                        for (WarehouseResponse object : objects) {
                            wareHouseItemAdmin.add(new WareHouseItemAdmin(object.getId().toString(),"",object.getAddress(),object.getVolume(),object.getCctv().toString(),
                                    object.getArmedResponse().toString(),object.getFireSafetyAndManagement().toString(),object.getParkingSpace().toString(),
                                    object.getIsApproved().toString(),object.getOperatingHours(),object.getWarehouseOwner().toString()) );

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(AddPickupRequest.this));

                        adapterAllWarehouses = new AdapterPickWarehouse(getApplicationContext(),wareHouseItemAdmin);
                        recyclerView.setAdapter(adapterAllWarehouses);

                        adapterAllWarehouses.setOnClickListener(new AdapterPickWarehouse.OnClickListener() {
                            @Override
                            public void onClick(int position, WareHouseItemAdmin model) {

                                dropoff_location.setText(model.getAddress().toString());
                                enterAWarehouse.setVisibility(View.GONE);

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


    private void filterdataWarehouses(String text) {


        List<WareHouseItemAdmin> filteredList = new ArrayList<WareHouseItemAdmin>();

        for (WareHouseItemAdmin item : wareHouseItemAdmin) {
            try {
                if (item.getId().toLowerCase().contains(text.toLowerCase()) ||
                        item.getAddress().toLowerCase().contains(text.toLowerCase())

                ) {
                    filteredList.add(item);
                }
            }catch (Exception e) {
                Log.e("AddPickuprequest", "Exception" + e.toString());

            }

        }

        adapterAllWarehouses.SetFilteredList(filteredList);
    }

    public void mGetUserAddress() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        RequestBody g_username= RequestBody.create(MediaType.parse("multipart/form-data"), username);

        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.apiGetUserAddress(g_username);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){
                    if(response.body().getMessage().length() >0){
                        pickup_location.setText(response.body().getMessage());

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