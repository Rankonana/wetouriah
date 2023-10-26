package com.example.wetouriah;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

public class Deliver extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    int itemId;
    String role;

    private static final int PICK_IMAGE_REQUEST = 1;
    private LinearLayout layoutImages;
    private ArrayList<Uri> imageUris;
    private ImageView imageView;
    Button btn_add_more,btn_submit;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver);
        setTitle("Delivery");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        role = sharedPreferences.getString("role", null);


        layoutImages = findViewById(R.id.layout_images);
        imageView = findViewById(R.id.image_view);
        btn_add_more = findViewById(R.id.btn_add_more);
        btn_submit = findViewById(R.id.btn_submit);
        ratingBar = findViewById(R.id.ratingBar);



        // Retrieve the ID from the intent's extra
        itemId = getIntent().getIntExtra("item_id", -1);

        // Use the ID to show a toast message
        String message = "Item ID: " + itemId;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        imageUris = new ArrayList<>();

        setImagesAndARating( String.valueOf(itemId) );

        if (role.equals("1")) {
            // Navigate to the AdminPortal

        }

        if (role.equals("2")) {
            // Navigate to the CustomerPortal
            btn_add_more.setVisibility(View.INVISIBLE);
            btn_submit.setVisibility(View.INVISIBLE);


        }
        if (role.equals("3")) {
            // Navigate to the DriverPortal
            ratingBar.setEnabled(false);
        }
        if (role.equals("4")) {
            // Navigate to the WarehousePortal
            btn_add_more.setVisibility(View.INVISIBLE);
            btn_submit.setVisibility(View.INVISIBLE);

        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDelivered(String.valueOf(itemId));
            }
        });

        btn_add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(Deliver.this)
                        .crop()
                        .galleryOnly()
                        .maxResultSize(512,512)
                        .provider(ImageProvider.GALLERY)
                        .start();
            }
        });


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                addRatig( String.valueOf(itemId) , String.valueOf(ratingBar.getRating()));
            }
        });


    }

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


    public void submitForm(String pickupId) {
        if (!imageUris.isEmpty()) {
            for (Uri imageUri : imageUris) {

                String   imagepath = RealPathUtil.getRealPath(this,imageUri);


                //
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                File file = new File(imagepath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                MultipartBody.Part imagefield = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                RequestBody get_pickupId = RequestBody.create(MediaType.parse("multipart/form-data"), pickupId);


                APIService apiService = retrofit.create(APIService.class);
                Call<APIResponse> call = apiService.uploadProofImage(get_pickupId,imagefield);


                call.enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        // Handle success response
//                        Toast.makeText(getApplicationContext(), "reached", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        // Handle failure
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
            Uri uri = data.getData();
            if(uri != null){
                imageUris.add(uri);
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 8, 0, 0);
                imageView.setLayoutParams(layoutParams);
                imageView.setImageURI(uri);
                layoutImages.addView(imageView);
            }

        }





    }




    public void mDelivered(String request_pickup_id) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();


        RequestBody get_request_pickup_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(request_pickup_id) );

        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call=  apiService.setIsDelivered(get_request_pickup_id);


        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){

                    if(response.body().getStatusCode().toString().equals("201")) {
                        submitForm(String.valueOf(request_pickup_id));

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

    public void addRatig(String request_pickup,String rating ) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();


        RequestBody pick_request_pickup = RequestBody.create(MediaType.parse("multipart/form-data"), request_pickup);

        RequestBody pick_rating = RequestBody.create(MediaType.parse("multipart/form-data"), rating);

        APIService apiService = retrofit.create(APIService.class);

        Call<APIResponse> call= apiService.rate(pick_request_pickup,pick_rating);

        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){

                    if(response.body().getStatusCode().toString().equals("201")) {
                        Toast.makeText(getApplicationContext(), "rating was a success", Toast.LENGTH_SHORT).show();

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

    public void setImagesAndARating(String request_pickup) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody pick_request_pickup = RequestBody.create(MediaType.parse("multipart/form-data"), request_pickup);
        APIService apiService = retrofit.create(APIService.class);

        Call<DeliveryImagesAndRatingResponse> call= apiService.getImagesAndRating(pick_request_pickup);

        Toast.makeText(getApplicationContext(), "reached 1 ", Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<DeliveryImagesAndRatingResponse>() {
            @Override
            public void onResponse(Call<DeliveryImagesAndRatingResponse> call, Response<DeliveryImagesAndRatingResponse> response) {
                if(response.isSuccessful()){

                    Toast.makeText(getApplicationContext(), "reached 2", Toast.LENGTH_SHORT).show();

                    List<String> images = response.body().getImages();
                    int rating = response.body().getRating();
                    displayImagesAndrating(images,rating);
                }
            }
            @Override
            public void onFailure(Call<DeliveryImagesAndRatingResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void displayImagesAndrating(List<String> images,int rating){

        if (!images.isEmpty()) {
            for (String image : images) {
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 8, 0, 0);
                imageView.setLayoutParams(layoutParams);
                Picasso.get().load( image.toString()).into(imageView);

                layoutImages.addView(imageView);

            }
            ratingBar.setRating(rating);

            //
            btn_add_more.setVisibility(View.INVISIBLE);
            btn_submit.setVisibility(View.INVISIBLE);


            if (rating >0){
                ratingBar.setEnabled(false);
            }




            //

        }




    }

}