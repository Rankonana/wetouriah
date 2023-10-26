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

        holder.tracking_number.setText(pickUpRequestItems.get(position).getTracking_number().toString());

        if(pickUpRequestItems.get(position).getPickup_location().length() > 29){
            holder.pickup_location.setText(pickUpRequestItems.get(position).getPickup_location().toString().substring(0, 30));

        }else {
            holder.pickup_location.setText(pickUpRequestItems.get(position).getPickup_location().toString());

        }

        if(pickUpRequestItems.get(position).getDropoff_location().length() > 29){
            holder.dropoff_location.setText(pickUpRequestItems.get(position).getDropoff_location().toString().substring(0, 30));

        }else {
            holder.dropoff_location.setText(pickUpRequestItems.get(position).getDropoff_location().toString());

        }




        String status = pickUpRequestItems.get(position).getStatus().toString();

        if(status.equals("1")){
            status =  "Pending";
        }
        if(status.equals("2")){
            status =  "Picked-up";
        }
        if(status.equals("3")){
            status =  "Delivered to the Recipient";
        }
        if(status.equals("4")){
            status =  "In-transit";
        }
        if(status.equals("5")){
            status =  "Driver-assigned";
        }
        if(status.equals("6")){
            status =  "Out for delivery";
        }
        if(status.equals("7")){
            status =  "Return to Sender";
        }
        if(status.equals("8")){
            status =  "Out for pick-up";
        }
        if(status.equals("9")){
            status =  "Delivered at Warehouse";
        }
        if(status.equals("10")){
            status =  "Cancelled";
        }
        if(status.equals("11")){
            status =  "Collected by the Recipient";
        }
        if(status.equals("12")){
            status =  "Returned to Sender";
        }
        if(status.equals("13")){
            status =  "Delivery Attempt";
        }
        if(status.equals("14")){
            status =  "Failed Delivery";
        }
        if(status.equals("15")){
            status =  "Lost / Stolen";
        }
        if(status.equals("16")){
            status =  "Out for Delivery";
        }

        holder.status.setText(status);


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

