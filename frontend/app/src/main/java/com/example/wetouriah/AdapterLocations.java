package com.example.wetouriah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterLocations extends RecyclerView.Adapter<LocationsViewHolder> {


    Context context;
    List<LocationsItem> locationsItems;
    private OnClickListener onClickListener;

    public AdapterLocations(Context context, List<LocationsItem> LocationsItems) {
        this.context = context;
        this.locationsItems = LocationsItems;
    }

    @NonNull
    @Override
    public LocationsViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new LocationsViewHolder(LayoutInflater.from(context).inflate(R.layout.tracking_item_view,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull LocationsViewHolder holder, int position) {

        LocationsItem item = locationsItems.get(position);
        holder.status.setText(locationsItems.get(position).getStatus().toString());
        holder.location.setText("Location: "+ locationsItems.get(position).getLocation().toString());
        holder.timestamp.setText(locationsItems.get(position).getTimestamp().toString());



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
        return locationsItems.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, LocationsItem model);
    }


}

