package com.example.wetouriah;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllUsersViewHolder extends RecyclerView.ViewHolder  {


    ImageView profile_picture;


    CheckBox is_active;

    TextView username,firstname,lastname,address,role,email;


    public AllUsersViewHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.username);
        profile_picture  = itemView.findViewById(R.id.profile_picture);
        is_active = itemView.findViewById(R.id.is_active);
        firstname = itemView.findViewById(R.id.firstname);
        lastname = itemView.findViewById(R.id.lastname);
        address = itemView.findViewById(R.id.address);
        role = itemView.findViewById(R.id.role);
        email = itemView.findViewById(R.id.email);

    }





}