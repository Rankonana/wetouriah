package com.example.wetouriah;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllCarsViewHolder extends RecyclerView.ViewHolder  {



    CheckBox is_approved;

    TextView type,car_owner,capacity,color,make,model,year,license_plate;


    public AllCarsViewHolder(@NonNull View itemView) {
        super(itemView);
        is_approved  = itemView.findViewById(R.id.is_approved);
        type = itemView.findViewById(R.id.type);
        car_owner = itemView.findViewById(R.id.car_owner);
        capacity = itemView.findViewById(R.id.capacity);
        color = itemView.findViewById(R.id.color);
        make = itemView.findViewById(R.id.make);
        model = itemView.findViewById(R.id.model);
        year = itemView.findViewById(R.id.year);
        license_plate = itemView.findViewById(R.id.license_plate);


    }





}