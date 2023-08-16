package com.example.wetouriah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterWarehouse extends RecyclerView.Adapter<ViewHolderWarehouse> {


    Context context;
    List<WareHouseItem> items;

    public AdapterWarehouse(Context context, List<WareHouseItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolderWarehouse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderWarehouse(LayoutInflater.from(context).inflate(R.layout.item_all_warehouse_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderWarehouse holder, int position) {
        holder.address.setText(items.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}

