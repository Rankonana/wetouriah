package com.example.wetouriah;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

//    https://www.youtube.com/watch?v=DCo1nLUGq8I&ab_channel=WaleedTalha
    @Multipart
    @POST("create-user/")
    Call<AddUser> addUser(@Part("email") RequestBody email,

                          @Part("username") RequestBody username,
                          @Part("password") RequestBody password,

                          @Part("title") RequestBody title,

                         @Part MultipartBody.Part profile_picture,

                          @Part("firstname") RequestBody firstname,
                          @Part("lastname") RequestBody lastname,
                          @Part("address") RequestBody address,
                          @Part("phone_number") RequestBody phone_number);
}
