package com.example.wetouriah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
//            Toast.makeText(getApplicationContext(), "Hello, " + username, Toast.LENGTH_SHORT).show();
            if("1".equals(role)){

                Intent intent = new Intent(MainActivity.this,AdminPortal.class);
                startActivity(intent);


            }
            if("2".equals(role) ){
                Intent intent = new Intent(MainActivity.this, CustomerPortal.class);
                startActivity(intent);

            }
            if("3".equals(role)){
                Intent intent = new Intent(MainActivity.this,DriverPortal.class);
                startActivity(intent);

            }
            if("4".equals(role) ){
                Intent intent = new Intent(MainActivity.this,WarehousePortal.class);
                startActivity(intent);

            }

        }





        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Let's login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "lets register", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });

        btnViewWarehouses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(MainActivity.this,ViewWarehouses.class);
                Intent intent = new Intent(MainActivity.this,TitleOnAmap.class);
                Toast.makeText(MainActivity.this, "Reached", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        btnTrackparcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "lets track your parcel", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,TrackParcel.class);
                startActivity(intent);
            }
        });


//            if(role.equals("2") ){
//                Intent intent = new Intent(MainActivity.this,CustomerPortal.class);
//                startActivity(intent);
//            }
//            if(role.equals("3") ){
//                Intent intent = new Intent(MainActivity.this,DriverPortal.class);
//                startActivity(intent);
//            }
//            if(role.equals("4") ){
//                Intent intent = new Intent(MainActivity.this,WarehousePortal.class);
//                startActivity(intent);
//            }

//        if(!role.isEmpty()){
//            if(role.equals("1")  ){
//                Intent intent = new Intent(MainActivity.this,AdminPortal.class);
//                startActivity(intent);
//
//            }
//            if(role.equals("2") ){
//                Intent intent = new Intent(MainActivity.this,CustomerPortal.class);
//                startActivity(intent);
//            }
//            if(role.equals("3") ){
//                Intent intent = new Intent(MainActivity.this,DriverPortal.class);
//                startActivity(intent);
//            }
//            if(role.equals("4") ){
//                Intent intent = new Intent(MainActivity.this,WarehousePortal.class);
//                startActivity(intent);
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