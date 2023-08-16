package com.example.wetouriah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button btnLogin, btnRegister, btnViewWarehouses,btnTrackparcel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnViewWarehouses = (Button)findViewById(R.id.btnViewWarehouses);
        btnTrackparcel = (Button)findViewById(R.id.btnTrackparcel);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);
        String username = sharedPreferences.getString("username", null);
        String user_id = sharedPreferences.getString("user_id", null);



        if (role != null && !role.isEmpty() && username != null && !username.isEmpty()) {
            if("1".equals(role)){

                Intent intent = new Intent(MainActivity.this,AdminPortal.class);
                startActivity(intent);
                finish();


            }
            if("2".equals(role) ){
                Intent intent = new Intent(MainActivity.this, CustomerPortal.class);
                startActivity(intent);
                finish();

            }
            if("3".equals(role)){
                Intent intent = new Intent(MainActivity.this,DriverPortal.class);
                startActivity(intent);
                finish();

            }
            if("4".equals(role) ){
                Intent intent = new Intent(MainActivity.this,WarehousePortal.class);
                startActivity(intent);
                finish();

            }

        }





        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
                finish();
            }
        });

        btnViewWarehouses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(MainActivity.this,ViewWarehouses.class);
                Intent intent = new Intent(MainActivity.this,TitleOnAmap.class);
                startActivity(intent);
                finish();
            }
        });
        btnTrackparcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TrackParcel.class);
                startActivity(intent);
                finish();
            }
        });


//            if(role.equals("2") ){
//                Intent intent = new Intent(MainActivity.this,CustomerPortal.class);
//                startActivity(intent);
        //finish();
//            }
//            if(role.equals("3") ){
//                Intent intent = new Intent(MainActivity.this,DriverPortal.class);
//                startActivity(intent);
        //finish();
//            }
//            if(role.equals("4") ){
//                Intent intent = new Intent(MainActivity.this,WarehousePortal.class);
//                startActivity(intent);
        //finish();
//            }

//        if(!role.isEmpty()){
//            if(role.equals("1")  ){
//                Intent intent = new Intent(MainActivity.this,AdminPortal.class);
//                startActivity(intent);
        //finish();
//
//            }
//            if(role.equals("2") ){
//                Intent intent = new Intent(MainActivity.this,CustomerPortal.class);
//                startActivity(intent);
        //finish();
//            }
//            if(role.equals("3") ){
//                Intent intent = new Intent(MainActivity.this,DriverPortal.class);
//                startActivity(intent);
        //finish();
//            }
//            if(role.equals("4") ){
//                Intent intent = new Intent(MainActivity.this,WarehousePortal.class);
//                startActivity(intent);
        //finish();
//            }
//        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // This method is called when the activity is brought to the foreground
        // Perform any necessary operations here, such as refreshing data or updating UI
    }

    @Override
    protected void onPause() {
        super.onPause();
        // This method is called when the activity is going into the background
        // Release any resources or unregister any listeners here to avoid memory leaks
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // This method is called before the activity is destroyed
        // Perform any cleanup operations here, such as releasing resources
    }




}