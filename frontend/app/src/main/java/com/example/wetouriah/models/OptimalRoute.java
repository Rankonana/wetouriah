package com.example.wetouriah.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.wetouriah.APIResponse;
import com.example.wetouriah.APIService;
import com.example.wetouriah.Constants;
import com.example.wetouriah.PickUpRequestItem;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;





public class OptimalRoute  {

    private static final String TAG = "OptimalRoute";

    static String setInsideRetrofit ="0";

    static Handler handler;
    static Runnable runnable;
    static boolean isHandlerRunning = true;

//    List<PickUpRequestItem,String> parcelAndDistance;





    public static List<PickUpRequestItem> findOptimalRoute(List<PickUpRequestItem> parcels, String startingLocation) {
        List<PickUpRequestItem> optimalRoute = new ArrayList<>();

        PickUpRequestItem currentParcel = null;
        String currentLocation = startingLocation;

        while (!parcels.isEmpty()) {
            PickUpRequestItem nearestParcel = null;
            double shortestDistance = Double.MAX_VALUE;

            for (PickUpRequestItem parcel : parcels) {


                if(parcel.getStatus().equals("5") ||  parcel.getStatus().equals("8")  ){


                    getDistance(currentLocation, parcel.getPickup_location());
                    double distance = Double.parseDouble(setInsideRetrofit);
                    if (distance < shortestDistance) {
                        nearestParcel = parcel;
                        shortestDistance = distance;
                    }


                }else{

                    getDistance(currentLocation, parcel.getDropoff_location());
                    double distance = Double.parseDouble(setInsideRetrofit);
                    if (distance < shortestDistance) {
                        nearestParcel = parcel;
                        shortestDistance = distance;
                    }


                }

            }


            if(nearestParcel.getStatus().equals("5") ||  nearestParcel.getStatus().equals("8")  ){

                if (nearestParcel != null) {
                    optimalRoute.add(nearestParcel);
                    parcels.remove(nearestParcel);
                    currentLocation = nearestParcel.getPickup_location();
                }

            }else{
                if (nearestParcel != null) {
                    optimalRoute.add(nearestParcel);
                    parcels.remove(nearestParcel);
                    currentLocation = nearestParcel.getDropoff_location();
                }

            }


        }

        return optimalRoute;
    }



    private static void getDistance(String startLocation, String endLocation) {
        //Log.e(TAG,  " start getDistance "  );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS + "/api/") // Replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        RequestBody get_startLocation = RequestBody.create(MediaType.parse("multipart/form-data"), startLocation);
        RequestBody get_endLocation = RequestBody.create(MediaType.parse("multipart/form-data"), endLocation);

        Call<APIResponse> call = apiService.apiDistanceAndTime(get_startLocation, get_endLocation);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.isSuccessful()) {
                    double distance = parseDistance(response.body().getMessage());
                    Log.e(TAG,  "1. inside api: " + String.valueOf(distance) );

                    setInsideRetrofit = String.valueOf(distance);

                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.e(TAG,  String.valueOf(t) );
            }
        });
    }

    private static double parseDistance(String message) {
        try {
            String distancePattern = "distance:(\\d+)";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(distancePattern);
            java.util.regex.Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                return Double.parseDouble(matcher.group(1));
            } else {
                return 0.00;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.00;
        }
    }





}
