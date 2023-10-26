package com.example.wetouriah;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Chat extends AppCompatActivity {

    EditText edtmessage;
    Button  btnSend;
    String pickup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setTitle("Messages");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        edtmessage = findViewById(R.id.edtmessage);
        btnSend = findViewById(R.id.btnSend);


        Intent intent = getIntent();
        pickup = intent.getStringExtra("id");

        mGetMessages(pickup);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                String user_id = sharedPreferences.getString("user_id", null);
                sendMessage(pickup,user_id,edtmessage.getText().toString());
                //mGetMessages(pickup);
            }
        });
    }
    public void sendMessage(String pickup,String sender,String message ) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody pickup_pickup = RequestBody.create(MediaType.parse("multipart/form-data"), pickup);

        RequestBody pickup_sender = RequestBody.create(MediaType.parse("multipart/form-data"), sender);

        RequestBody pickup_message = RequestBody.create(MediaType.parse("multipart/form-data"), message);

        APIService apiService = retrofit.create(APIService.class);

        Call<APIResponse> call= apiService.sendMessage(pickup_pickup,pickup_sender,pickup_message);

        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){

                    if(response.body().getStatusCode().toString().equals("201")) {
                        mGetMessages("1");
                        Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();

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

    public void mGetMessages(String pickup) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody get_pickup= RequestBody.create(MediaType.parse("multipart/form-data"), pickup);
        APIService apiService = retrofit.create(APIService.class);
        Call<List<MessagesResponse>> call = apiService.getMessageObjects(get_pickup);
        call.enqueue(new Callback<List<MessagesResponse>>() {
            @Override
            public void onResponse(Call<List<MessagesResponse>> call, Response<List<MessagesResponse>> response) {
                if(response.isSuccessful()){
                    List<MessagesResponse> objects = response.body();
                    if (objects != null) {
                        RecyclerView recyclerView = findViewById(R.id.idMessages);
                        List<MessageItem> MessageItems = new ArrayList<MessageItem>();
                        for (MessagesResponse object : objects) {
                            MessageItems.add( new MessageItem(object.getId().toString(),"Message: "+ object.getMessage().toString(),"Sent at: " + object.getTimestamp(),object.getPickup().toString(),"From: "+ object.getSender().toString()));
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));
                        final  AdapterMessage adapterMessage = new AdapterMessage(getApplicationContext(),MessageItems);
                        recyclerView.setAdapter(adapterMessage);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<MessagesResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}