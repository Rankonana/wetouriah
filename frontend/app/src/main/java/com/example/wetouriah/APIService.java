package com.example.wetouriah;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIService {

//    https://www.youtube.com/watch?v=DCo1nLUGq8I&ab_channel=WaleedTalha
    @Multipart
    @POST("create-user/")
    Call<AddUserResponse> addUser(@Part("email") RequestBody email,
                                  @Part("role") RequestBody role,
                          @Part("username") RequestBody username,
                          @Part("password") RequestBody password,

                          @Part("title") RequestBody title,

                         @Part MultipartBody.Part profile_picture,

                          @Part("firstname") RequestBody firstname,
                          @Part("lastname") RequestBody lastname,
                          @Part("address") RequestBody address,
                          @Part("phone_number") RequestBody phone_number);
    @Multipart
    @PUT("update-user/")
    Call<APIResponse> updateUser(@Part("id") RequestBody id,@Part("email") RequestBody email,

                                  @Part("title") RequestBody title,

                                  @Part MultipartBody.Part profile_picture,

                                  @Part("firstname") RequestBody firstname,
                                  @Part("lastname") RequestBody lastname,
                                  @Part("address") RequestBody address,
                                  @Part("phone_number") RequestBody phone_number);

    @Multipart
    @POST("token/")
    Call<LoginResponse> loginUser(@Part("username") RequestBody username,
                                  @Part("password") RequestBody password);

    @Multipart
    @POST("get-warehouse/")
    Call<WarehouseResponse> getWarehouse(@Part("id") RequestBody id);


    @Multipart
    @POST("add-update-warehouse/")
    Call<APIResponse> addWarehouse(@Part("warehouse_owner") RequestBody warehouse_owner,
                                     @Part MultipartBody.Part image,
                                     @Part("address") RequestBody address,
                                     @Part("volume") RequestBody volume,
                                     @Part("cctv") RequestBody cctv,
                                     @Part("armed_response") RequestBody armed_response,
                                     @Part("fire_safety_and_management") RequestBody fire_safety_and_management,
                                     @Part("parking_space") RequestBody parking_space,
                                     @Part("operating_hours") RequestBody operating_hours);

    @Multipart
    @PUT("add-update-warehouse/")
    Call<APIResponse> updateWarehouse(@Part("id") RequestBody warehouse_id,
                                   @Part("warehouse_owner") RequestBody warehouse_owner,
                                   @Part MultipartBody.Part image,
                                   @Part("address") RequestBody address,
                                   @Part("volume") RequestBody volume,
                                   @Part("cctv") RequestBody cctv,
                                   @Part("armed_response") RequestBody armed_response,
                                   @Part("fire_safety_and_management") RequestBody fire_safety_and_management,
                                   @Part("parking_space") RequestBody parking_space,
                                   @Part("operating_hours") RequestBody operating_hours);

    @Multipart
    @PUT("add-update-warehouse/")
    Call<APIResponse> updateWarehouseNoImage(@Part("id") RequestBody warehouse_id,
                                      @Part("warehouse_owner") RequestBody warehouse_owner,
                                      @Part("address") RequestBody address,
                                      @Part("volume") RequestBody volume,
                                      @Part("cctv") RequestBody cctv,
                                      @Part("armed_response") RequestBody armed_response,
                                      @Part("fire_safety_and_management") RequestBody fire_safety_and_management,
                                      @Part("parking_space") RequestBody parking_space,
                                      @Part("operating_hours") RequestBody operating_hours);

//    @Multipart
//    @GET("get-warehouse/")
//    Call<WarehouseResponse> getWarehouse(@Part("id") RequestBody id);

    @Multipart
    @POST("add-update-requestpickup/")
    Call<APIResponse> addPickRequest(@Part("date_and_time_pickup") RequestBody date_and_time_pickup,
                                           @Part("recipient_name") RequestBody recipient_name,
                                           @Part("recipient_phone") RequestBody recipient_phone,

                                           @Part("pickup_location") RequestBody pickup_location,
                                           @Part("dropoff_location") RequestBody dropoff_location,

                                           @Part("volume") RequestBody volume,
                                           @Part("weight") RequestBody weight,

                                           @Part("price_to_pay") RequestBody price_to_pay,
                                           @Part("customer") RequestBody customer);



    @Multipart
    @GET("get-image/")
    Call<ImageResponse> getImage(@Part("id") RequestBody id);



    @Multipart
    @POST("add-update-car/")
    Call<CarResponse> addCar(
                                 @Part("car_owner") RequestBody carOwner,
                                 @Part("type") RequestBody type,
                                 @Part("capacity") RequestBody capacity,
                                 @Part("color") RequestBody color,
                                 @Part("make") RequestBody make,
                                 @Part("model") RequestBody model,
                                 @Part("year") RequestBody year,
                                 @Part("license_plate") RequestBody licensePlate,
                                 @Part("is_approved") RequestBody isApproved);



    @Multipart
    @PUT("add-update-car/")
    Call<CarResponse> updateCar( @Part("id") RequestBody id,
                                          @Part("car_owner") RequestBody carOwner,
                                          @Part("type") RequestBody type,
                                          @Part("capacity") RequestBody capacity,
                                          @Part("color") RequestBody color,
                                          @Part("make") RequestBody make,
                                          @Part("model") RequestBody model,
                                          @Part("year") RequestBody year,
                                          @Part("license_plate") RequestBody licensePlate,
                                          @Part("is_approved") RequestBody isApproved);

    @Multipart
    @POST("get-car/")
    Call<CarResponse> loadCar(@Part("id") RequestBody id);

    @Multipart
    @POST("track-parcel/")
    Call<PickupResponse> getPickUp(@Part("id") RequestBody id);

//    @GET("warehouses/") // Replace "endpoint" with your actual API endpoint
//    Call<List<WarehouseResponse>> getWarehousesObjects();

    @GET("warehouses/") // Replace "endpoint" with your actual API endpoint
    Call<List<WarehouseResponse>> getWarehousesObjects(@Query("address") String address);


    @Multipart
    @POST("get-user/")
    Call<GetUserReponse> loadUser(@Part("id") RequestBody id);


    @Multipart
    @PUT("add-update-pickup/")
    Call<APIResponse> rate(@Part("request_pickup") RequestBody request_pickup,
                                      @Part("rating") RequestBody rating);

    @Multipart
    @POST("send-message/")
    Call<APIResponse> sendMessage(@Part("pickup") RequestBody pickup,
                                  @Part("sender") RequestBody sender,
                                  @Part("message") RequestBody message );


    @Multipart
    @POST("pickup-messages/")
    Call<List<MessagesResponse>> getMessageObjects(@Part("pickup") RequestBody pickup);


//    @GET("all-requestpickups-by-user/") // Replace "endpoint" with your actual API endpoint
//    Call<List<RequestPickupResponse>> getObjects();

    @GET("users/") // Replace "endpoint" with your actual API endpoint
    Call<List<AllUsersResponse>> getAllUSerObjects();

    @GET("cars/") // Replace "endpoint" with your actual API endpoint
    Call<List<CarResponse>> getAllCarObjects();

    @GET("warehouses/") // Replace "endpoint" with your actual API endpoint
    Call<List<WarehouseResponse>> getAllWareHouseObjects();


    @GET("drivers-licenses/") // Replace "endpoint" with your actual API endpoint
    Call<List<DriversLicenseResponse>> getAllDriversLicenseObjects();




    @Multipart
    @PUT("add-update-warehouse/")
    Call<APIResponse> updateWarehouseApproval(@Part("id") RequestBody id,
                                        @Part("warehouse_owner") RequestBody warehouse_owner,
                                              @Part("is_approved") RequestBody is_approved
    );

    @Multipart
    @PUT("add-update-car/")
    Call<APIResponse> updateCarApproval(@Part("car_owner") RequestBody car_owner,
                                              @Part("is_approved") RequestBody is_approved
    );

    @Multipart
    @PUT("update-user/")
    Call<APIResponse> updateUserActive(@Part("id") RequestBody id,
                                        @Part("is_active") RequestBody is_active
    );

    @Multipart
    @GET("all-requestpickups-by-user/")
    Call<List<RequestPickupResponse>> getPickupRequest(@Part("customer") RequestBody customer,
                                                       @Part("keyword") RequestBody keyword,
                                                       @Part("is_picked") RequestBody is_picked);

    @Multipart
    @POST("all-requestpickups-by-user/") // Replace "endpoint" with your actual API endpoint
    Call<List<RequestPickupResponse>> getAllUserRequest(@Part("customer") RequestBody customer);

    @Multipart
    @POST("all-undelivered-requestpickups-by-user/") // Replace "endpoint" with your actual API endpoint
    Call<List<RequestPickupResponse>> getUnDeliveredObjects(@Part("customer") RequestBody customer);

    @Multipart
    @POST("all-unrated-requestpickups-by-user/") // Replace "endpoint" with your actual API endpoint
    Call<List<RequestPickupResponse>> getUnRatedObjects(@Part("customer") RequestBody customer);

    @Multipart
    @POST("get-pickup-id-from-requestpickup-id/")
    Call<PickupResponse> getPickupIDfromRequestpickup(@Part("request_pickup") RequestBody request_pickup);


    @Multipart
    @POST("all-undelivered-requestpickups-by-driver/") // Replace "endpoint" with your actual API endpoint
    Call<List<RequestPickupResponse>> getUnDeliveredDriverObjects(@Part("user_id") RequestBody user_id);

    @Multipart
    @POST("all-unrated-requestpickups-by-driver/") // Replace "endpoint" with your actual API endpoint
    Call<List<RequestPickupResponse>> getUnRatedDriverObjects(@Part("user_id") RequestBody user_id);

    @Multipart
    @POST("all-requestpickups-by-driver/") // Replace "endpoint" with your actual API endpoint
    Call<List<RequestPickupResponse>> getAllDriverObjects(@Part("user_id") RequestBody user_id);

    @Multipart
    @POST("driver-notification/")
    Call<RequestPickupResponse> checkTaskAssignment(@Part("user_id") RequestBody user_id);

    @Multipart
    @POST("decline-accept-job/")
    Call<APIResponse> acceptDeclineJob(@Part("requestPickup_id") RequestBody requestPickup_id,
                                                 @Part("accept_decline") RequestBody accept_decline);

    @Multipart
    @POST("add-delivery-proof-images/")
    Call<APIResponse> uploadProofImage(@Part("pickup") RequestBody pickup,
                                       @Part MultipartBody.Part image);

    @Multipart
    @POST("set-is-delivered/")
    Call<APIResponse> setIsDelivered(@Part("request_pickup_id") RequestBody request_pickup_id);

    @Multipart
    @POST("get-delivery-images-and-rating/")
    Call<DeliveryImagesAndRatingResponse> getImagesAndRating(@Part("pickup") RequestBody request_pickup_id);











}
