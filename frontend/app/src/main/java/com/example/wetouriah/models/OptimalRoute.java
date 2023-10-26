package com.example.wetouriah.models;

import android.content.Context;
import android.content.SharedPreferences;
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

    private static class DistanceCallbackImpl implements DistanceCallback {
        private double distance;

        @Override
        public void onDistanceReceived(double distance) {
            Log.e(TAG,  "DistanceCallbackImpl onDistanceReceived" + String.valueOf(distance) );

            this.distance = distance;
        }

        @Override
        public void onDistanceError(Throwable t) {
            // Handle the error here, e.g., log the error or take appropriate action.
            Log.e(TAG,  "DistanceCallbackImpl onDistanceError" + t.toString() );

        }

        public double getDistance() {
            Log.e(TAG,  "DistanceCallbackImpl getDistance" + String.valueOf(distance) );

            return distance;
        }
    }


    public static List<PickUpRequestItem> findOptimalRoute(List<PickUpRequestItem> parcels, String startingLocation) {
        List<PickUpRequestItem> optimalRoute = new ArrayList<>();

        PickUpRequestItem currentParcel = null;
        String currentLocation = startingLocation;

        while (!parcels.isEmpty()) {
            PickUpRequestItem nearestParcel = null;
            double shortestDistance = Double.MAX_VALUE;

            for (PickUpRequestItem parcel : parcels) {


                if(parcel.getStatus().equals("5") ||  parcel.getStatus().equals("8")  ){

                    double distance = calculateDistance(currentLocation, parcel.getPickup_location());
                    if (distance < shortestDistance) {
                        nearestParcel = parcel;
                        shortestDistance = distance;
                    }

                }else{
                    double distance = calculateDistance(currentLocation, parcel.getDropoff_location());
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

    private static double calculateDistance(String location1, String location2) {

        DistanceCallbackImpl callback = new DistanceCallbackImpl();
        getDistance(location1,location2, callback);
        double distance = callback.getDistance();

        double randomValue = Math.random() * 100;
//        Log.e(TAG,  " from callback : " + String.valueOf(distance) );
//        Log.e(TAG,  " randomValue : " + String.valueOf(randomValue) );

        return randomValue;
    }

    private static void getDistance(String startLocation, String endLocation, DistanceCallback callback) {
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
                    Log.e(TAG,  " inside api: " + String.valueOf(distance) );

                    callback.onDistanceReceived(distance); // Pass the distance to the callback
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                callback.onDistanceError(t); // Notify the callback about the error
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


    //callback interface
    interface DistanceCallback {
        void onDistanceReceived(double distance);
        void onDistanceError(Throwable t);
    }


}
