package com.example.wetouriah;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderWarehouse extends RecyclerView.ViewHolder {

    TextView address;


    public ViewHolderWarehouse(@NonNull View itemView) {
        super(itemView);
        address  = itemView.findViewById(R.id.address);
    }
}