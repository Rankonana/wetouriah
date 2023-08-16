package com.example.wetouriah;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PickUpRequestViewHolder extends RecyclerView.ViewHolder  {

    TextView tracking_number, status,pickup_location,dropoff_location;

    public PickUpRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        tracking_number = itemView.findViewById(R.id.tracking_number);
        pickup_location = itemView.findViewById(R.id.pickup_location);
        dropoff_location = itemView.findViewById(R.id.dropoff_location);
        status = itemView.findViewById(R.id.status);

    }

}