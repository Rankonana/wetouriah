package com.example.wetouriah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPickUpRequest extends RecyclerView.Adapter<PickUpRequestViewHolder> {


    Context context;
    List<PickUpRequestItem> pickUpRequestItems;
    private OnClickListener onClickListener;

    public AdapterPickUpRequest(Context context, List<PickUpRequestItem> pickUpRequestItems) {
        this.context = context;
        this.pickUpRequestItems = pickUpRequestItems;

    }

    @NonNull
    @Override
    public PickUpRequestViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new PickUpRequestViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull PickUpRequestViewHolder holder, int position) {

        PickUpRequestItem item = pickUpRequestItems.get(position);

        holder.tracking_number.setText("Tracking_number: "+ pickUpRequestItems.get(position).getTracking_number().toString());
        holder.pickup_location.setText("From: " + pickUpRequestItems.get(position).getPickup_location().toString());
        holder.dropoff_location.setText("To: "+ pickUpRequestItems.get(position).getDropoff_location().toString());
        holder.status.setText("Status: "+ pickUpRequestItems.get(position).getStatus().toString());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.getAdapterPosition(), item);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return pickUpRequestItems.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, PickUpRequestItem model);
    }


}

