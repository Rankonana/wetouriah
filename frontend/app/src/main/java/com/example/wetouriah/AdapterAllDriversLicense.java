package com.example.wetouriah;

import static com.example.wetouriah.Constants.formatDate;

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

        holder.fullname.setText("Name: " + DriversLicenses.get(position).getFullname());
        holder.license_number.setText("License number: "+DriversLicenses.get(position).getLicense_number());
        holder.expiry_date.setText("Expiry: "+  Constants.formatDate(DriversLicenses.get(position).getExpiry_date()) );

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

