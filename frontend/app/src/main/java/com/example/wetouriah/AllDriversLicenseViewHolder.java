package com.example.wetouriah;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllDriversLicenseViewHolder extends RecyclerView.ViewHolder  {





    TextView id,license_owner, fullname  , identity_number , date_of_birth , license_number , expiry_date , country_of_issue , code , restrictions , gender ,
            drivers_license_number ,
            date_of_issue,
            uploadLicense;

    CheckBox is_approved;



    public AllDriversLicenseViewHolder(@NonNull View itemView) {
        super(itemView);
        license_owner = itemView.findViewById(R.id.license_owner);
        fullname   = itemView.findViewById(R.id.fullname);
        identity_number  = itemView.findViewById(R.id.identity_number);
        date_of_birth  = itemView.findViewById(R.id.date_of_birth);
        license_number  = itemView.findViewById(R.id.license_number);
        expiry_date  = itemView.findViewById(R.id.expiry_date);
        country_of_issue  = itemView.findViewById(R.id.country_of_issue);
        code  = itemView.findViewById(R.id.code);
        restrictions  = itemView.findViewById(R.id.restrictions);
        gender  = itemView.findViewById(R.id.gender);
        date_of_issue = itemView.findViewById(R.id.date_of_issue);
        uploadLicense = itemView.findViewById(R.id.uploadLicense);
        is_approved = itemView.findViewById(R.id.is_approved);


    }





}