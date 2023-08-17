package com.example.wetouriah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterAllDriversLicense extends RecyclerView.Adapter<AllDriversLicenseViewHolder> {


    Context context;
    List<DriversLicense> DriversLicenses;

    private OnClickListener onClickListener;


    public AdapterAllDriversLicense(Context context, List<DriversLicense> DriversLicenses) {
        this.context = context;
        this.DriversLicenses = DriversLicenses;

    }

    @NonNull
    @Override
    public AllDriversLicenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllDriversLicenseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_all_drivers_licenses_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllDriversLicenseViewHolder holder, int position) {

        DriversLicense item = DriversLicenses.get(position);

//        holder.license_owner.setText(DriversLicenses.get(position).getLicense_owner());
        holder.fullname.setText("Name: " + DriversLicenses.get(position).getFullname());
//        holder.identity_number.setText(DriversLicenses.get(position).getIdentity_number());
//        holder.date_of_birth.setText(DriversLicenses.get(position).getDate_of_birth());
        holder.license_number.setText("license number: "+DriversLicenses.get(position).getLicense_number());
        holder.expiry_date.setText("expiry: "+DriversLicenses.get(position).getExpiry_date());
//        holder.country_of_issue.setText(DriversLicenses.get(position).getCountry_of_issue());
//        holder.code.setText(DriversLicenses.get(position).getCode());
//        holder.restrictions.setText(DriversLicenses.get(position).getRestrictions());
//        holder.gender.setText(DriversLicenses.get(position).getGender());
//        holder.date_of_issue.setText(DriversLicenses.get(position).getDate_of_issue());
//        holder.uploadLicense.setText(DriversLicenses.get(position).getUploadLicense());
        holder.is_approved.setChecked(Boolean.parseBoolean(DriversLicenses.get(position).getIs_approved()));


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
        return DriversLicenses.size();
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, DriversLicense model);
    }






}

