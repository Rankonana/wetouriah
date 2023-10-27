package com.example.wetouriah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterPickWarehouse extends RecyclerView.Adapter<pickWarehouseViewHolder> {


    Context context;
    List<WareHouseItemAdmin> wareHouseItemAdmins;

    private OnClickListener onClickListener;

    public void SetFilteredList(List<WareHouseItemAdmin> filteredWarehouseItems){
        this.wareHouseItemAdmins = filteredWarehouseItems;
        notifyDataSetChanged();
    }

    public AdapterPickWarehouse(Context context, List<WareHouseItemAdmin> wareHouseItemAdmins) {
        this.context = context;
        this.wareHouseItemAdmins = wareHouseItemAdmins;

    }

    @NonNull
    @Override
    public pickWarehouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new pickWarehouseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pick_warehouse_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull pickWarehouseViewHolder holder, int position) {

        WareHouseItemAdmin item = wareHouseItemAdmins.get(position);

//        holder.image.setText(wareHouseItemAdmins.get(position).getImage());

        holder.warehouseID.setText("Warehouse ID: " + wareHouseItemAdmins.get(position).getId());

        holder.volume.setText(wareHouseItemAdmins.get(position).getVolume());
        holder.cctv.setChecked(Boolean.parseBoolean(wareHouseItemAdmins.get(position).getCctv()));
        holder.armed_response.setChecked(Boolean.parseBoolean(wareHouseItemAdmins.get(position).getArmed_response()));
        holder.address.setText("Address: " + wareHouseItemAdmins.get(position).getAddress());
        holder.fire_safety_and_management.setChecked(Boolean.parseBoolean(wareHouseItemAdmins.get(position).getFire_safety_and_management()));
        holder.parking_space.setChecked(Boolean.parseBoolean(wareHouseItemAdmins.get(position).getParking_space()));
        holder.operating_hours.setText("Operating hours: " +  wareHouseItemAdmins.get(position).getOperating_hours());




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
        return wareHouseItemAdmins.size();
    }



    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public interface OnClickListener {
        void onClick(int position, WareHouseItemAdmin model);
    }


}

