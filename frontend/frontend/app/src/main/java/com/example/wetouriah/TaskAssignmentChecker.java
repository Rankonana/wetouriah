package com.example.wetouriah;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskAssignmentChecker {
//
//    private static final int INTERVAL = 60000; // Check every 1 minute
//    private static final int NOTIFICATION_ID = 1;
//
//    private Context context;
//    private Handler handler;
//    private Runnable runnable;
//
//    public TaskAssignmentChecker(Context context) {
//        this.context = context;
//        this.handler = new Handler() {
//            @Override
//            public void publish(LogRecord logRecord) {
//
//            }
//
//            @Override
//            public void flush() {
//
//            }
//
//            @Override
//            public void close() throws SecurityException {
//
//            }
//        };
//        this.runnable = new Runnable() {
//            @Override
//            public void run() {
//                checkTaskAssignment();
//                handler.postDelayed(this, INTERVAL);
//            }
//        };
//    }
//
//    public void startChecking() {
//        handler.postDelayed(runnable, INTERVAL);
//    }
//
//    public void stopChecking() {
//        handler.removeCallbacks(runnable);
//    }
//
//    private void checkTaskAssignment() {
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/") // Replace with your actual base URL
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        APIService apiService = retrofit.create(APIService.class);
//
////        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
////        String user_id = sharedPreferences.getString("user_id", null);
//
//        RequestBody get_driver= RequestBody.create(MediaType.parse("multipart/form-data"), "15");
//
//        retrofit2.Call<List<RequestPickupResponse>> call = apiService.getUnDeliveredDriverObjects(get_driver);
//        call.enqueue(new Callback<List<RequestPickupResponse>>() {
//            @Override
//            public void onResponse(retrofit2.Call<List<RequestPickupResponse>> call, Response<List<RequestPickupResponse>> response) {
//                if (response.isSuccessful()) {
//
//                    createNotification();
//
//
//                } else {
//                    // Handle error
//                }
//            }
//
//
//
//            @Override
//            public void onFailure(Call<List<RequestPickupResponse>> call, Throwable t) {
//                // Handle failure
//            }
//        });
//    }
//
//
//
//    private void createNotification() {
//        Intent intent = new Intent(context, DriverPortal.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
//                .setSmallIcon(R.drawable.notification)
//                .setContentTitle("Task Assigned")
//                .setContentText("You have been assigned a new task")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }
}
