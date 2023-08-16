package com.example.wetouriah;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterAllWarehouses extends RecyclerView.Adapter<AllWarehousesViewHolder> {


    Context context;
    List<WareHouseItemAdmin> wareHouseItemAdmins;

    public AdapterAllWarehouses(Context context, List<WareHouseItemAdmin> wareHouseItemAdmins) {
        this.context = context;
        this.wareHouseItemAdmins = wareHouseItemAdmins;

    }

    @NonNull
    @Override
    public AllWarehousesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllWarehousesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_all_warehouse_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllWarehousesViewHolder holder, int position) {
//        holder.image.setText(wareHouseItemAdmins.get(position).getImage());
        holder.volume.setText(wareHouseItemAdmins.get(position).getVolume());
        holder.cctv.setChecked(Boolean.parseBoolean(wareHouseItemAdmins.get(position).getCctv()));
        holder.armed_response.setChecked(Boolean.parseBoolean(wareHouseItemAdmins.get(position).getArmed_response()));
        holder.address.setText(wareHouseItemAdmins.get(position).getAddress());
        holder.fire_safety_and_management.setChecked(Boolean.parseBoolean(wareHouseItemAdmins.get(position).getFire_safety_and_management()));
        holder.parking_space.setChecked(Boolean.parseBoolean(wareHouseItemAdmins.get(position).getParking_space()));
        holder.operating_hours.setText(wareHouseItemAdmins.get(position).getOperating_hours());
        holder.is_approved.setChecked(Boolean.parseBoolean(wareHouseItemAdmins.get(position).getIs_approved()));

        holder.is_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_updateWarehouse( wareHouseItemAdmins.get(holder.getAdapterPosition()).getId(),wareHouseItemAdmins.get(holder.getAdapterPosition()).getWarehouse_owner(), String.valueOf(holder.is_approved.isChecked()));

            }
        });


    }
    @Override
    public int getItemCount() {
        return wareHouseItemAdmins.size();
    }

    public void add_updateWarehouse(String warehouse_id, String warehouse_owner,String is_approved ) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody w_warehouse_id = RequestBody.create(MediaType.parse("multipart/form-data"), warehouse_id );
        RequestBody w_warehouse_owner = RequestBody.create(MediaType.parse("multipart/form-data"), warehouse_owner );


        RequestBody w_is_approved = RequestBody.create(MediaType.parse("multipart/form-data"), is_approved);
        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call=  apiService.updateWarehouseApproval(w_warehouse_id,w_warehouse_owner,w_is_approved);

        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if(response.isSuccessful()){

                    if(response.body().getStatusCode().toString().equals("201")) {
                        Toast.makeText(context.getApplicationContext(), "approval updated", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context.getApplicationContext(), "Error updating approval", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }






}

