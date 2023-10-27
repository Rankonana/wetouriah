package com.example.wetouriah;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class pickWarehouseViewHolder extends RecyclerView.ViewHolder  {



    ImageView image;

    TextView warehouseID,address,volume,operating_hours;

    CheckBox cctv,armed_response,parking_space,fire_safety_and_management,is_approved;

    public pickWarehouseViewHolder(@NonNull View itemView) {
        super(itemView);
        warehouseID  = itemView.findViewById(R.id.warehouseID);
        image  = itemView.findViewById(R.id.image);
        address = itemView.findViewById(R.id.address);
        volume = itemView.findViewById(R.id.volume);
        operating_hours = itemView.findViewById(R.id.operating_hours);

        cctv = itemView.findViewById(R.id.cctv);
        armed_response = itemView.findViewById(R.id.armed_response);
        parking_space = itemView.findViewById(R.id.parking_space);
        fire_safety_and_management = itemView.findViewById(R.id.fire_safety_and_management);
        is_approved = itemView.findViewById(R.id.is_approved);




    }





}