package com.example.wetouriah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterAllCars extends RecyclerView.Adapter<AllCarsViewHolder> {


    Context context;
    List<CarItem> CarItems;

    private OnClickListener onClickListener;
    public void SetFilteredList(List<CarItem> filteredCarItemItems){
        this.CarItems = filteredCarItemItems;
        notifyDataSetChanged();
    }


    public AdapterAllCars(Context context, List<CarItem> CarItems) {
        this.context = context;
        this.CarItems = CarItems;

    }

    @NonNull
    @Override
    public AllCarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllCarsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_all_cars_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllCarsViewHolder holder, int position) {

        CarItem item = CarItems.get(position);

//        holder.profile_picture.setText(CarItems.get(position).getProfile_picture());
        holder.type.setText(CarItems.get(position).getType());
        holder.car_owner.setText("Car Owner :" +CarItems.get(position).getCar_owner());
        holder.is_approved.setChecked(Boolean.parseBoolean(CarItems.get(position).getIs_approved()));
        holder.capacity.setText(CarItems.get(position).getCapacity());
        holder.color.setText(CarItems.get(position).getColor());
        holder.make.setText(CarItems.get(position).getMake());
        holder.model.setText(CarItems.get(position).getModel());
        holder.year.setText(CarItems.get(position).getYear());
        holder.license_plate.setText(CarItems.get(position).getLicense_plate());


//        holder.is_approved.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                add_updateCar( CarItems.get(holder.getAdapterPosition()).getCar_owner(), String.valueOf(holder.is_approved.isChecked()));
//
//            }
//        });

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
        return CarItems.size();
    }

//    public void add_updateCar(String car_owner,String is_approved ) {
//
//        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
//                .addConverterFactory(GsonConverterFactory.create()).build();
//
//        RequestBody c_wcar_owner = RequestBody.create(MediaType.parse("multipart/form-data"), car_owner );
//        RequestBody c_is_approved = RequestBody.create(MediaType.parse("multipart/form-data"), is_approved );
//
//        APIService apiService = retrofit.create(APIService.class);
//        Call<APIResponse> call=  apiService.updateCarApproval(c_wcar_owner,c_is_approved);
//
//        call.enqueue(new Callback<APIResponse>() {
//
//            @Override
//            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//
//                if(response.isSuccessful()){
//
//                    if(response.body().getStatusCode().toString().equals("201")) {
//                        Toast.makeText(context.getApplicationContext(), "approval updated", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(context.getApplicationContext(), "Error updating approval", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<APIResponse> call, Throwable t) {
//                Toast.makeText(context.getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//
//
//    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, CarItem model);
    }






}

