package com.example.wetouriah;

import static com.example.wetouriah.Constants.formatDate;
import static com.example.wetouriah.Constants.formatDateString;
import static com.example.wetouriah.Constants.formatTrackingLogDateString;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.api.internal.StatusCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class PickUpRequestDetailed extends AppCompatActivity {

    TextView updated;

    TextView  recipient_name,recipient_phone,pickup_location,dropoff_location,trackingId,status,request_time,
            volume,weight,parcel_description,special_notes,price_to_pay,images;

    RatingBar ratingDetailed;


    RadioGroup rdgAcceptDecline;
    RadioButton rdAccept,rdDecline;

    RelativeLayout lyMainDetailed;


    Button btnRate;


    PickUpRequestItem pickUpRequestItem = null;

    String pickup = null;

    SharedPreferences sharedPreferences;
    String role;


    Button  drive,btnCall,btnWhatsApp;


    LinearLayout lyMain;
    CardView deleteCardView;
    Button  buttonYes,buttonCancel;

    RelativeLayout lyRate;
    RatingBar ratingBar;
    Button btnSubmitRating;

    CardView lyAcceptDecline;
    TextView txtAcceptDeclinePickup,txtAcceptDeclineDropoff;
    Button acceptOfferButton, declineOfferButton;

    CardView lyChangeStatus;
    Spinner statusSpinner;
    Button BtnChangeStatus;


    private ArrayList<ImageItem> parcelImageitems;
    private ArrayList<ImageItem> parcelProofOfDeliveryimageItems;
    private ArrayList<ImageItem> parcelSignatureIimageItems;

    SliderView sliderView,SliderViewProffDelivery,SliderViewSignature ;

    String status_Intent;


    CardView lySignIn;
    RelativeLayout lySendCode ;
    Button btnSendCode ;
    LinearLayout lyVerifyCode ;
    EditText edtCode ;
    Button btnResend ;
    Button btnConfirmCode ;
    TextView txtSendCode;
    String smsMessage ;
    String sDeliveryCode = "734539";
    List<RequestPickupStatusItem> statusList;
    StatusSpinnerAdapter adapterStatus;

    String from_warehouse_pick_drop = "No";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_request_detailed);
        request_time = findViewById(R.id.request_time);

        recipient_name = findViewById(R.id.recipient_name);
//        recipient_phone = findViewById(R.id.recipient_phone);
        trackingId = findViewById(R.id.trackingId);
        status= findViewById(R.id.status);
        request_time = findViewById(R.id.request_time);
        pickup_location = findViewById(R.id.pickup_location);
        dropoff_location = findViewById(R.id.dropoff_location);
        volume = findViewById(R.id.volume);
        weight = findViewById(R.id.weight);
        parcel_description = findViewById(R.id.parcel_description);
        ratingDetailed = findViewById(R.id.ratingDetailed);
        price_to_pay = findViewById(R.id.price_to_pay);



        drive = findViewById(R.id.drive);
        btnCall = findViewById(R.id.btnCall);
        btnWhatsApp = findViewById(R.id.btnWhatsApp);



        lyMain = findViewById(R.id.lyMain);
        deleteCardView = findViewById(R.id.deleteCardView);
        buttonYes = findViewById(R.id.buttonYes);
        buttonCancel= findViewById(R.id.buttonCancel);

        lyAcceptDecline = findViewById(R.id.lyAcceptDecline);



        lyRate = findViewById(R.id.lyRate);
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmitRating = findViewById(R.id.btnSubmitRating);

        lyAcceptDecline = findViewById(R.id.lyAcceptDecline);
        txtAcceptDeclinePickup = findViewById(R.id.txtAcceptDeclinePickup);
        txtAcceptDeclineDropoff = findViewById(R.id.txtAcceptDeclineDropoff);
        acceptOfferButton = findViewById(R.id.acceptOfferButton);
        declineOfferButton = findViewById(R.id.declineOfferButton);


        lyChangeStatus = findViewById(R.id.lyChangeStatus);
        statusSpinner = findViewById(R.id.statusSpinner);
        BtnChangeStatus = findViewById(R.id.BtnChangeStatus);


        sliderView = findViewById(R.id.slider);
        SliderViewProffDelivery = findViewById(R.id.SliderViewProffDelivery);
        SliderViewSignature = findViewById(R.id.SliderViewSignature);


        txtSendCode = findViewById(R.id.txtSendCode);
        lySignIn = findViewById(R.id.lySignIn);
        lySendCode = findViewById(R.id.lySendCode);
        btnSendCode = findViewById(R.id.btnSendCode);
        lyVerifyCode = findViewById(R.id.lyVerifyCode);
        edtCode = findViewById(R.id.edtCode);
        btnResend = findViewById(R.id.btnResend);
        btnConfirmCode = findViewById(R.id.btnConfirmCode);



        setTitle("Detailed Pick Up ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        role = sharedPreferences.getString("role", null);

        Intent intent = getIntent();
        pickUpRequestItem = (PickUpRequestItem) intent.getSerializableExtra("model");


        if (pickUpRequestItem != null) {


            mGetAllImages(pickUpRequestItem.getTracking_number());

            recipient_name.setText("Recipient name: "+pickUpRequestItem.getRecipient_name());
            trackingId.setText("Tracking ID: "+ pickUpRequestItem.getTracking_number());

            request_time.setText("Requested at: "+ formatDateString(pickUpRequestItem.getRequest_time()));
            pickup_location.setText("Pickup location: "+pickUpRequestItem.getPickup_location());
            dropoff_location.setText("Dropoff location: "+pickUpRequestItem.getDropoff_location());
            volume.setText("Volume: "+pickUpRequestItem.getVolume() + "m3 | ");
            weight.setText("Weight: " +pickUpRequestItem.getWeight()  + "kg");
            parcel_description.setText(pickUpRequestItem.getParcel_description());
            price_to_pay.setText("| R "+pickUpRequestItem.getPrice_to_pay());

            try {
                ratingDetailed.setRating( Float.parseFloat(pickUpRequestItem.getRating()));
            } catch (NumberFormatException e) {
                ratingDetailed.setRating( Float.parseFloat("0"));
            }

            status_Intent = pickUpRequestItem.getStatus();

            if(status_Intent.equals("1")){
                status_Intent =  "Pending";
            }
            if(status_Intent.equals("2")){
                status_Intent =  "Picked-up";
            }
            if(status_Intent.equals("3")){
                status_Intent =  "Delivered to the Recipient";
            }
            if(status_Intent.equals("4")){
                status_Intent =  "In-transit";
            }
            if(status_Intent.equals("5")){
                status_Intent =  "Driver-assigned";
            }
            if(status_Intent.equals("6")){
                status_Intent =  "Out for delivery";
            }
            if(status_Intent.equals("7")){
                status_Intent =  "Return to Sender";
            }
            if(status_Intent.equals("8")){
                status_Intent =  "Out for pick-up";
            }
            if(status_Intent.equals("9")){
                status_Intent =  "Delivered at Warehouse";
            }
            if(status_Intent.equals("10")){
                status_Intent =  "Cancelled";
            }
            if(status_Intent.equals("11")){
                status_Intent =  "Collected by the Recipient";
            }
            if(status_Intent.equals("12")){
                status_Intent =  "Returned to Sender";
            }
            if(status_Intent.equals("13")){
                status_Intent =  "Delivery Attempt";
            }
            if(status_Intent.equals("14")){
                status_Intent =  "Failed Delivery";
            }
            if(status_Intent.equals("15")){
                status_Intent =  "Lost / Stolen";
            }
            if(status_Intent.equals("16")){
                status_Intent =  "Out for Delivery";
            }


            status.setText("Status: "+status_Intent);
//            images.setText(pickUpRequestItem.getImages());

//            Toast.makeText(PickUpRequestDetailed.this,"Rated"+ pickUpRequestItem.getId(),Toast.LENGTH_SHORT).show();



        }




        //PickUpRequestItem temp =  (PickUpRequestItem) intent.getSerializableExtra("model");


        //mSinglePickRequest(String.valueOf(temp.getTracking_number()));





        if (role.equals("1")) {
            // Navigate to the Admin
        }

        if (role.equals("2")) {
            // Navigate to the CustomerPortal
//            drive.setVisibility(View.INVISIBLE);
            lyAcceptDecline.setVisibility(View.INVISIBLE);

        }
        if (role.equals("3")) {
           // Navigate to the Driver

        }
        if (role.equals("4")) {
            // Navigate to the WarehousePortal
//            drive.setVisibility(View.INVISIBLE);
            lyAcceptDecline.setVisibility(View.INVISIBLE);
        }

        //Toast.makeText(PickUpRequestDetailed.this,"error here 3",Toast.LENGTH_SHORT).show();


        drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGoogleMapsDirections(PickUpRequestDetailed.this,pickUpRequestItem.getPickup_location().toString(),pickUpRequestItem.getDropoff_location().toString());

            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ pickUpRequestItem.getRecipient_phone()));
                startActivity(intent);

            }
        });

        btnWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //Check on physical device
                    Uri uri = Uri.parse("https://wa.me/" + pickUpRequestItem.getRecipient_phone());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(PickUpRequestDetailed.this,"it looks like you don't have whatsapp",Toast.LENGTH_LONG).show();

                }

            }
        });

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PickUpRequestDetailed.this, "Code sent", Toast.LENGTH_SHORT).show();

                mSendSMS(pickUpRequestItem.getRecipient_phone().toString(), smsMessage,from_warehouse_pick_drop,pickUpRequestItem.getId().toString());
                lySendCode.setVisibility(View.GONE);
                lyVerifyCode.setVisibility(View.VISIBLE);

            }
        });
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PickUpRequestDetailed.this, "Code resent again", Toast.LENGTH_SHORT).show();
                mSendSMS(pickUpRequestItem.getRecipient_phone().toString(), smsMessage,from_warehouse_pick_drop,pickUpRequestItem.getId().toString());

            }
        });
        btnConfirmCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PickUpRequestDetailed.this, "Code verified", Toast.LENGTH_SHORT).show();

                RequestPickupStatusItem selectedStatus = (RequestPickupStatusItem) statusSpinner.getSelectedItem();
                String statusName = selectedStatus.getStatus_name();
                String statusID = selectedStatus.getId();



                if( edtCode.getText().length() >0) {

                    if(sDeliveryCode.equals(edtCode.getText().toString())){
                        lyVerifyCode.setVisibility(View.GONE);



                        mSetStatus(pickUpRequestItem.getTracking_number(),statusID);
//                            lyMain.setVisibility(View.VISIBLE);
//                            lyChangeStatus.setVisibility(View.GONE);
//                            setTitle("Detailed Pick Up ");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();



                    }else {
                        Toast.makeText(PickUpRequestDetailed.this, "Invalid code", Toast.LENGTH_SHORT).show();

                    }

                }else {
                    Toast.makeText(PickUpRequestDetailed.this, "Please enter delivery code", Toast.LENGTH_SHORT).show();

                }




            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Navigate to the CustomerPortal
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        if (item.getItemId() == R.id.edit) {
//            Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PickUpRequestDetailed.this, AddPickupRequest.class);
            intent.putExtra("model", pickUpRequestItem);
            startActivity(intent);
            finish();

            return true;
        }

        if (item.getItemId() == R.id.delete) {

            lyMain.setVisibility(View.GONE);
            deleteCardView.setVisibility(View.VISIBLE);

            setTitle("Delete");

            buttonYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mDelete(pickUpRequestItem.getId());
                    Intent delete = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(delete);
                    finish();

                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lyMain.setVisibility(View.VISIBLE);
                    deleteCardView.setVisibility(View.GONE);
                    setTitle("Detailed Pick Up ");

                }
            });

            return true;
        }

        if (item.getItemId() == R.id.signIn) {
            setTitle("Sign in ");

            //mgetAllStatus();


            //
            statusList = new ArrayList<>();

            RequestPickupStatusItem s2 = new RequestPickupStatusItem("3","Delivered to the Recipient");
            RequestPickupStatusItem s7 = new RequestPickupStatusItem("9","Delivered at Warehouse");

            statusList.add(s2);
            statusList.add(s7);


            // Create a custom adapter for the Spinner
            adapterStatus = new StatusSpinnerAdapter(getApplicationContext(), statusList);
            statusSpinner.setAdapter(adapterStatus);

            //



            lyMain.setVisibility(View.GONE);

//            Random random = new Random();
//            int randomNumber = random.nextInt(80–65) + 65;

            Random rand = new Random();
            //System.out.printf("%04d%n", rand.nextInt(10000));

            sDeliveryCode  =  String.valueOf(rand.nextInt(10000)) ;

            smsMessage = "Hello, " + "\nHere is your "+ "delivery code"  + " code :" + sDeliveryCode;


            lyChangeStatus.setVisibility(View.VISIBLE);
            //lySignIn.setVisibility(View.VISIBLE);

            BtnChangeStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lyChangeStatus.setVisibility(View.GONE);
                    lySignIn.setVisibility(View.VISIBLE);
//                    Toast.makeText(PickUpRequestDetailed.this, "Code resent again", Toast.LENGTH_SHORT).show();
                    mSendSMS(pickUpRequestItem.getRecipient_phone().toString(), smsMessage,from_warehouse_pick_drop,pickUpRequestItem.getId().toString());

                }
            });

            return true;


        }


        if (item.getItemId() == R.id.rate) {
            lyMain.setVisibility(View.GONE);
            lyRate.setVisibility(View.VISIBLE);


            btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();

                mRate(pickUpRequestItem.getTracking_number(), String.valueOf(rating));
                lyMain.setVisibility(View.VISIBLE);
                lyRate.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
            return true;
        }

        if (item.getItemId() == R.id.AcceptDecline) {
            lyMain.setVisibility(View.GONE);
            lyAcceptDecline.setVisibility(View.VISIBLE);

            txtAcceptDeclinePickup.setText("Pickup location: " + pickUpRequestItem.getPickup_location());
            txtAcceptDeclineDropoff.setText("Drop-off location: "+ pickUpRequestItem.dropoff_location);


            acceptOfferButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    float rating = ratingBar.getRating();
                    //mSetStatus(pickUpRequestItem.getTracking_number(),"5");
                    mAcceptDecline(pickUpRequestItem.getId(),"accept");

                    lyMain.setVisibility(View.VISIBLE);
                    lyAcceptDecline.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            declineOfferButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    float rating = ratingBar.getRating();

                 //   mDecline(pickUpRequestItem.getTracking_number(),"");

                    mAcceptDecline(pickUpRequestItem.getId(),"decline");
                    //mSetStatus(pickUpRequestItem.getTracking_number(),"1");
                    lyMain.setVisibility(View.VISIBLE);
                    lyAcceptDecline.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            return true;
        }

        if (item.getItemId() == R.id.track) {
            Intent track = new Intent(getApplicationContext(),TrackParcel.class);
            track.putExtra("model", pickUpRequestItem);
            startActivity(track);
            finish();
            return true;
        }

        if (item.getItemId() == R.id.ChangeStatus) {

            setTitle("Change Status");
            statusList = new ArrayList<>();
            RequestPickupStatusItem s1 = new RequestPickupStatusItem("2","Picked Up");
            RequestPickupStatusItem s2 = new RequestPickupStatusItem("3","Delivered to the Recipient");
            RequestPickupStatusItem s3 = new RequestPickupStatusItem("4","In-transit");
            RequestPickupStatusItem s5 = new RequestPickupStatusItem("6","Out for delivery");
            RequestPickupStatusItem s6 = new RequestPickupStatusItem("8","Out for pick-up");
            RequestPickupStatusItem s7 = new RequestPickupStatusItem("9","Delivered at Warehouse");
            RequestPickupStatusItem s8 = new RequestPickupStatusItem("11","Collected by the Recipient");
            RequestPickupStatusItem s9 = new RequestPickupStatusItem("13","Delivery Attempt");
            RequestPickupStatusItem s11 = new RequestPickupStatusItem("16","Out for Delivery");


            if(role.equals("3")){
                statusList.add(s1);
                statusList.add(s2);
                statusList.add(s3);
                statusList.add(s5);
                statusList.add(s6);
                statusList.add(s7);
                statusList.add(s9);
                statusList.add(s11);
            }
            if(role.equals("4")){
                statusList.add(s8);

            }

            // Create a custom adapter for the Spinner
            adapterStatus = new StatusSpinnerAdapter(getApplicationContext(), statusList);
            statusSpinner.setAdapter(adapterStatus);


            //

            lyMain.setVisibility(View.GONE);
            lyChangeStatus.setVisibility(View.VISIBLE);
            BtnChangeStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RequestPickupStatusItem selectedStatus = (RequestPickupStatusItem) statusSpinner.getSelectedItem();
                    String statusName = selectedStatus.getStatus_name();
                    String statusID = selectedStatus.getId();


                    if(statusID.equals("2")||  statusID.equals("3") || statusID.equals("9") || statusID.equals("11")){




                        lyChangeStatus.setVisibility(View.GONE);
                        lySignIn.setVisibility(View.VISIBLE);
                        txtSendCode.setText("Send " + statusName + " Code ");

                        Random rand = new Random();
                        //System.out.printf("%04d%n", rand.nextInt(10000));

                        sDeliveryCode  =  String.valueOf(rand.nextInt(10000)) ;

                        smsMessage = "Hello, " + "\nHere is your "+ statusName  + " code :" + sDeliveryCode;


                    }else {
                        mSetStatus(pickUpRequestItem.getTracking_number(),statusID);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }


                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void mDelete(String id) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody request_pickup_id= RequestBody.create(MediaType.parse("multipart/form-data"), id);

        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.deletePickupRequest(request_pickup_id);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void mRate(String tracking_number, String rating) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody request_tracking_number= RequestBody.create(MediaType.parse("multipart/form-data"), tracking_number);
        RequestBody request_rating= RequestBody.create(MediaType.parse("multipart/form-data"), rating);


        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.ratePickRequest(request_tracking_number,request_rating);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mSetStatus(String tracking_number, String status) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody request_tracking_number= RequestBody.create(MediaType.parse("multipart/form-data"), tracking_number);
        RequestBody request_status= RequestBody.create(MediaType.parse("multipart/form-data"), status);


        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call = apiService.setStatusPickRequest(request_tracking_number,request_status);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
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

                    parcelImageitems = new ArrayList<ImageItem>();;
                    parcelProofOfDeliveryimageItems = new ArrayList<ImageItem>();;
                    parcelSignatureIimageItems = new ArrayList<ImageItem>();;

                    if ( objects != null && !objects.isEmpty()) {


                        RecyclerView recyclerView = findViewById(R.id.rvLocations);



                        for (ImagesResponse object : objects) {

                            String imagestatus = object.getImageStatus().toString();

//                            parcelImageitems.add(new ImageItem(object.getId().toString(),object.getImage(),object.getPickupRequest().toString(),object.getImageStatus().toString())
//                            );

                            //During creation
                            if(imagestatus.equals("4")){
                                parcelImageitems.add(new ImageItem(object.getId().toString(),object.getImage(),object.getPickupRequest().toString(),object.getImageStatus().toString())
                                );
                            }
                            //During signature
                            if(imagestatus.equals("3")){
                                parcelSignatureIimageItems.add(new ImageItem(object.getId().toString(),object.getImage(),object.getPickupRequest().toString(),object.getImageStatus().toString())
                                );
                            }
                            //During delivery
                            if(imagestatus.equals("2")){
                                parcelProofOfDeliveryimageItems.add(new ImageItem(object.getId().toString(),object.getImage(),object.getPickupRequest().toString(),object.getImageStatus().toString())
                                );
                            }


                        }

                        loadParcelImages(parcelImageitems);
                        loadParcelSignatureIimageItems(parcelSignatureIimageItems);
                        loadParcelProofOfDeliveryimageItem(parcelProofOfDeliveryimageItems);



                    }else{
                        loadParcelImages(parcelImageitems);
                        loadParcelSignatureIimageItems(parcelSignatureIimageItems);
                        loadParcelProofOfDeliveryimageItem(parcelProofOfDeliveryimageItems);

                    }

                } else {
                    Toast.makeText(PickUpRequestDetailed.this, "response not successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ImagesResponse>> call, Throwable t) {
                // Handle failure
                Toast.makeText(PickUpRequestDetailed.this, "failure", Toast.LENGTH_SHORT).show();

            }
        });

    }



    public void mgetAllStatus(){




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);


        Call<List<RequestPickupStatusResponse>> call = apiService.getPickRequestStatus();

        call.enqueue(new Callback<List<RequestPickupStatusResponse>>() {
            @Override
            public void onResponse(Call<List<RequestPickupStatusResponse>> call, Response<List<RequestPickupStatusResponse>> response) {
                if (response.isSuccessful()) {
                    List<RequestPickupStatusResponse> objects = response.body();



                    if (objects != null) {

                         statusList = new ArrayList<>();



                        for (RequestPickupStatusResponse object : objects) {
                            statusList.add(new RequestPickupStatusItem(object.getId().toString() ,object.getStatusName()));
                        }
                        // Create a custom adapter for the Spinner
                        adapterStatus = new StatusSpinnerAdapter(getApplicationContext(), statusList);
                        statusSpinner.setAdapter(adapterStatus);




                    }else{
                        Toast.makeText(PickUpRequestDetailed.this, "objects null", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(PickUpRequestDetailed.this, "response not successfully", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<List<RequestPickupStatusResponse>> call, Throwable t) {
                // Handle failure
                Toast.makeText(PickUpRequestDetailed.this, "failure", Toast.LENGTH_SHORT).show();

            }
        });




    }

    public void loadParcelImages(ArrayList<ImageItem> imageItems){


        if (imageItems == null || imageItems.isEmpty()) {

            imageItems.add(new ImageItem("1","/media/NoImage.png","1","1")
            );
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

    public void loadParcelSignatureIimageItems(ArrayList<ImageItem> parcelSignatureIimageItems){

        if (parcelSignatureIimageItems == null || parcelSignatureIimageItems.isEmpty()) {
            parcelSignatureIimageItems.add(new ImageItem("1","/media/NoImage.png","1","1")
            );
            SliderAdapter adapter = new SliderAdapter(this, parcelSignatureIimageItems);
            SliderViewSignature.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            SliderViewSignature.setSliderAdapter(adapter);
            SliderViewSignature.setScrollTimeInSec(3);
            SliderViewSignature.setAutoCycle(true);
            SliderViewSignature.startAutoCycle();

        } else {
            SliderAdapter adapter = new SliderAdapter(this, parcelSignatureIimageItems);
            SliderViewSignature.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            SliderViewSignature.setSliderAdapter(adapter);
            SliderViewSignature.setScrollTimeInSec(3);
            SliderViewSignature.setAutoCycle(true);
            SliderViewSignature.startAutoCycle();
        }



    }
    public void loadParcelProofOfDeliveryimageItem(ArrayList<ImageItem> parcelProofOfDeliveryimageItems){

        if (parcelProofOfDeliveryimageItems == null || parcelProofOfDeliveryimageItems.isEmpty()) {
            parcelProofOfDeliveryimageItems.add(new ImageItem("1","/media/NoImage.png","1","1"));

            SliderAdapter adapter = new SliderAdapter(this, parcelProofOfDeliveryimageItems);
            SliderViewProffDelivery.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            SliderViewProffDelivery.setSliderAdapter(adapter);
            SliderViewProffDelivery.setScrollTimeInSec(3);
            SliderViewProffDelivery.setAutoCycle(true);
            SliderViewProffDelivery.startAutoCycle();

        } else {
            SliderAdapter adapter = new SliderAdapter(this, parcelProofOfDeliveryimageItems);
            SliderViewProffDelivery.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            SliderViewProffDelivery.setSliderAdapter(adapter);
            SliderViewProffDelivery.setScrollTimeInSec(3);
            SliderViewProffDelivery.setAutoCycle(true);
            SliderViewProffDelivery.startAutoCycle();
        }


    }





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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        MenuItem editMenuItem = menu.findItem(R.id.edit);
        MenuItem deleteMenuItem = menu.findItem(R.id.delete);
        MenuItem rateMenuItem = menu.findItem(R.id.rate);
        MenuItem signInMenuItem = menu.findItem(R.id.signIn);
        MenuItem AcceptDeclineMenuItem = menu.findItem(R.id.AcceptDecline);
        MenuItem ChangeStatusMenuItem = menu.findItem(R.id.ChangeStatus);
        MenuItem trackMenuItem = menu.findItem(R.id.track);


        try{

            String Newstatus = pickUpRequestItem.getStatus();




            //edit and delete
            //Pending Status and Customer and Warehouse Owner
            if( (role.equals("2") || role.equals("4")) && Newstatus.equals("1") ){
                editMenuItem.setVisible(true);
                deleteMenuItem.setVisible(true);

            }
            //Sign , Accept , Change Status
            //Pending Status and Driver
            if( role.equals("3")  && Newstatus.equals("1") ){
                AcceptDeclineMenuItem.setVisible(true);
            }

            //Change Status menu item
            //list of status that can be changed by driver
            //You can only changed status if the current status if not any of these
            //Role required Driver
            //1,3,9,10,11,12
            if ((role.equals("3") && (!Newstatus.equals("1") &&
                    !Newstatus.equals("3") &&
                    !Newstatus.equals("9") &&
                    !Newstatus.equals("10") &&
                    !Newstatus.equals("11") &&
                    !Newstatus.equals("12") ) ) ||

                    (role.equals("4") && (Newstatus.equals("9")  ) )

            ){
                ChangeStatusMenuItem.setVisible(true);

            }

            //signInMenuItem
            if (role.equals("3") && (!Newstatus.equals("1") &&
                    !Newstatus.equals("2") &&
                    !Newstatus.equals("3") &&
                    !Newstatus.equals("5") &&
                    !Newstatus.equals("7") &&
                    !Newstatus.equals("8") &&
                    !Newstatus.equals("9")&&
                    !Newstatus.equals("10")&&
                    !Newstatus.equals("11")&&
                    !Newstatus.equals("12")&&
                    !Newstatus.equals("15")) ){
                signInMenuItem.setVisible(true);


            }

            //trackMenuItem
            if ( !role.equals("3") && (!Newstatus.equals("1") &&
                    !Newstatus.equals("3") &&
                    !Newstatus.equals("5") &&
                    !Newstatus.equals("8") &&
                    !Newstatus.equals("10") &&
                    !Newstatus.equals("9") &&
                    !Newstatus.equals("11") &&
                    !Newstatus.equals("15")) ){
                trackMenuItem.setVisible(true);


            }
            //rateMenuItem
            if ( !role.equals("3") && ( Newstatus.equals("3") ||
                    Newstatus.equals("11") ) ){

                if(String.valueOf(pickUpRequestItem.getRating()).equals("")){
                    rateMenuItem.setVisible(true);
                }else {
                    rateMenuItem.setVisible(false);

                }

            }


            //Toast.makeText(getApplicationContext(), " last role is: " + role.toString() + " is visible: "+ String.valueOf(signInMenuItem.isVisible()) , Toast.LENGTH_SHORT).show();

        }catch(Exception e) {
            //Toast.makeText(getApplicationContext(), " error: " + e.toString() + " is visible: "+ String.valueOf(signInMenuItem.isVisible()) , Toast.LENGTH_SHORT).show();

        }

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailed_itemmenu, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void mAcceptDecline(String requestPickup,String status){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", null);

        RequestBody get_courier= RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
        RequestBody get_requestPickup= RequestBody.create(MediaType.parse("multipart/form-data"), requestPickup);
        RequestBody get_status= RequestBody.create(MediaType.parse("multipart/form-data"), status);

        Call<APIResponse> call = apiService.apiAcceptDeclineJob(get_courier,get_requestPickup,get_status);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void mSinglePickRequest(String tracking_number){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);


        RequestBody get_tracking_number= RequestBody.create(MediaType.parse("multipart/form-data"), tracking_number);


        Call<RequestPickupResponse> call = apiService.apiGetSingleRequestPickup(get_tracking_number);

        call.enqueue(new Callback<RequestPickupResponse>() {
            @Override
            public void onResponse(Call<RequestPickupResponse> call, Response<RequestPickupResponse> response) {
                if (response.isSuccessful()) {
                   // Toast.makeText(PickUpRequestDetailed.this, "Successful", Toast.LENGTH_SHORT).show();


                    //Toast.makeText(PickUpRequestDetailed.this, "mSinglePickRequest 1", Toast.LENGTH_SHORT).show();

                    RequestPickupResponse object = response.body();
                    //Toast.makeText(PickUpRequestDetailed.this, "mSinglePickRequest 2", Toast.LENGTH_SHORT).show();

                    if (object != null) {

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

                        pickUpRequestItem = new PickUpRequestItem(object.getId().toString(),
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

                            asssignPickUpRequestItem(pickUpRequestItem);


                        //Toast.makeText(PickUpRequestDetailed.this, "Tracking_number:  "+ object.getTrackingNumber().toString(), Toast.LENGTH_SHORT).show();

                        //Toast.makeText(PickUpRequestDetailed.this, "pickUpRequestItem test:  "+ String.valueOf(pickUpRequestItem) , Toast.LENGTH_SHORT).show();
                        //Toast.makeText(PickUpRequestDetailed.this, "Tracking_number:  "+ String.valueOf(pickUpRequestItem.getTracking_number()) , Toast.LENGTH_SHORT).show();



                    }else{
                        Toast.makeText(PickUpRequestDetailed.this, "objects null", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toast.makeText(PickUpRequestDetailed.this, "response not successfully", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<RequestPickupResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(PickUpRequestDetailed.this, "failure", Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void asssignPickUpRequestItem(PickUpRequestItem xpickUpRequestItem){
        pickUpRequestItem = xpickUpRequestItem;

        if (pickUpRequestItem != null) {


            mGetAllImages(pickUpRequestItem.getTracking_number());

            recipient_name.setText("Recipient name: "+pickUpRequestItem.getRecipient_name());
            trackingId.setText("Tracking ID: "+ pickUpRequestItem.getTracking_number());

            request_time.setText("Requested at: "+ formatDateString(pickUpRequestItem.getRequest_time()));
            pickup_location.setText("Pickup location: "+pickUpRequestItem.getPickup_location());
            dropoff_location.setText("Dropoff location: "+pickUpRequestItem.getDropoff_location());
            volume.setText("Volume: "+pickUpRequestItem.getVolume() + "m3 | ");
            weight.setText("Weight: " +pickUpRequestItem.getWeight()  + "kg");
            parcel_description.setText(pickUpRequestItem.getParcel_description());
            price_to_pay.setText("| R "+pickUpRequestItem.getPrice_to_pay());

            try {
                ratingDetailed.setRating( Float.parseFloat(pickUpRequestItem.getRating()));
            } catch (NumberFormatException e) {
                ratingDetailed.setRating( Float.parseFloat("0"));
            }

            status_Intent = pickUpRequestItem.getStatus();

            if(status_Intent.equals("1")){
                status_Intent =  "Pending";
            }
            if(status_Intent.equals("2")){
                status_Intent =  "Picked-up";
            }
            if(status_Intent.equals("3")){
                status_Intent =  "Delivered to the Recipient";
            }
            if(status_Intent.equals("4")){
                status_Intent =  "In-transit";
            }
            if(status_Intent.equals("5")){
                status_Intent =  "Driver-assigned";
            }
            if(status_Intent.equals("6")){
                status_Intent =  "Out for delivery";
            }
            if(status_Intent.equals("7")){
                status_Intent =  "Return to Sender";
            }
            if(status_Intent.equals("8")){
                status_Intent =  "Out for pick-up";
            }
            if(status_Intent.equals("9")){
                status_Intent =  "Delivered at Warehouse";
            }
            if(status_Intent.equals("10")){
                status_Intent =  "Cancelled";
            }
            if(status_Intent.equals("11")){
                status_Intent =  "Collected by the Recipient";
            }
            if(status_Intent.equals("12")){
                status_Intent =  "Returned to Sender";
            }
            if(status_Intent.equals("13")){
                status_Intent =  "Delivery Attempt";
            }
            if(status_Intent.equals("14")){
                status_Intent =  "Failed Delivery";
            }
            if(status_Intent.equals("15")){
                status_Intent =  "Lost / Stolen";
            }
            if(status_Intent.equals("16")){
                status_Intent =  "Out for Delivery";
            }


            status.setText("Status: "+status_Intent);
//            images.setText(pickUpRequestItem.getImages());

//            Toast.makeText(PickUpRequestDetailed.this,"Rated"+ pickUpRequestItem.getId(),Toast.LENGTH_SHORT).show();



        }


    }


    public void mSendSMS(String recipient_number, String message, String from_warehouse, String parcel_id){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Toast.makeText(getApplicationContext(), "sms r1", Toast.LENGTH_SHORT).show();

        APIService apiService = retrofit.create(APIService.class);

        RequestBody get_recipient_number= RequestBody.create(MediaType.parse("multipart/form-data"), recipient_number);
        RequestBody get_message= RequestBody.create(MediaType.parse("multipart/form-data"), message);
        RequestBody get_from_warehouse= RequestBody.create(MediaType.parse("multipart/form-data"), from_warehouse);
        RequestBody get_parcel_id= RequestBody.create(MediaType.parse("multipart/form-data"), parcel_id);



        Call<APIResponse> call = apiService.apiSendSMS(get_recipient_number,get_message,get_from_warehouse,get_parcel_id);

        //Toast.makeText(getApplicationContext(), "sms r2", Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                //Toast.makeText(getApplicationContext(), "sms r3", Toast.LENGTH_SHORT).show();

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                   // Toast.makeText(getApplicationContext(), "sms r4", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }










}