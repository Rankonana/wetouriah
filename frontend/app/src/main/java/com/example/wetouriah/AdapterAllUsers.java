package com.example.wetouriah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterAllUsers extends RecyclerView.Adapter<AllUsersViewHolder> {


    Context context;
    List<UserItem> UserItems;
    private OnClickListener onClickListener;

    public void SetFilteredList(List<UserItem> filteredUserItems){
        this.UserItems = filteredUserItems;
        notifyDataSetChanged();
    }

    public AdapterAllUsers(Context context, List<UserItem> UserItems) {
        this.context = context;
        this.UserItems = UserItems;

    }

    @NonNull
    @Override
    public AllUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllUsersViewHolder(LayoutInflater.from(context).inflate(R.layout.item_all_users_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllUsersViewHolder holder, int position) {

        UserItem item = UserItems.get(position);

        String imageUrl = "http://" + Constants.SERVER_IP_ADDRESS + ":8000/" +  UserItems.get(position).getProfile_picture();
        Picasso.get().load(imageUrl).into(holder.profile_picture);

        holder.is_active.setChecked(Boolean.parseBoolean(UserItems.get(position).getIs_active()));

        holder.username.setText("Username: " + UserItems.get(position).getUsername());
        holder.firstname.setText("Firstname: " + UserItems.get(position).getFirst_name());
        holder.lastname.setText("Lastname: "+UserItems.get(position).getLast_name());
        holder.address.setText("Address: "+UserItems.get(position).getAddress());
        holder.role.setText("Role: "+UserItems.get(position).getRole());
        holder.email.setText("Email: "+UserItems.get(position).getEmail());

        holder.is_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserStatus( UserItems.get(holder.getAdapterPosition()).getId(), String.valueOf(holder.is_active.isChecked()));

            }
        });

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
        return UserItems.size();
    }

    public void updateUserStatus(String id,String is_active ) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + Constants.SERVER_IP_ADDRESS+ ":8000/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestBody u_id = RequestBody.create(MediaType.parse("multipart/form-data"), id );
        RequestBody u_active = RequestBody.create(MediaType.parse("multipart/form-data"), is_active );

        APIService apiService = retrofit.create(APIService.class);
        Call<APIResponse> call=  apiService.updateUserActive(u_id,u_active);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.isSuccessful()){

                    if(response.body().getStatusCode().toString().equals("201")) {
                        Toast.makeText(context.getApplicationContext(), "active status updated", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context.getApplicationContext(), "Error updating active status", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, UserItem model);
    }






}

