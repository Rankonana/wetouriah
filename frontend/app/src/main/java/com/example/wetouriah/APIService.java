package com.example.wetouriah;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIService {

//    https://www.youtube.com/watch?v=DCo1nLUGq8I&ab_channel=WaleedTalha

    @Multipart
    @POST("send-sms/")
    Call<APIResponse> apiSendSMS(@Part("recipient_number") RequestBody recipient_number,
                                                @Part("message") RequestBody message,
                                 @Part("from_warehouse") RequestBody from_warehouse,
                                 @Part("parcel_id") RequestBody parcel_id);


    @Multipart
    @POST("delete-requestpickup/")
    Call<APIResponse> deletePickupRequest(@Part("id") RequestBody id);

    @Multipart
    @POST("courier-availability/")
    Call<APIResponse> apiLocationAndAvailabilty(@Part("courier") RequestBody courier,
                                                @Part("available") RequestBody available,
                                                @Part("location") RequestBody location);
    @Multipart
    @POST("check-user-email/")
    Call<APIResponse> apiCheckUsernameEmail(@Part("username") RequestBody username,
                                                @Part("email") RequestBody email);
    @Multipart
    @POST("get-distance-and-time/")
    Call<APIResponse> apiDistanceAndTime(@Part("startLocation") RequestBody startLocation,
                                                @Part("endLocation") RequestBody endLocation);

    @Multipart
    @POST("accept-decline-parcel/")
    Call<APIResponse> apiAcceptDeclineJob(@Part("courier") RequestBody courier,
                                                @Part("requestPickup") RequestBody requestPickup,
                                                @Part("status") RequestBody status);

    @Multipart
    @POST("check-job-offers/")
    Call<RequestPickupResponse> apiCheckJobOffers(@Part("courier") RequestBody courier);

    @Multipart
    @POST("get-courier-availability/")
    Call<APIResponse> apiGetAvailibility(@Part("courier") RequestBody courier);

    @Multipart
    @POST("tracking-log/")
    Call<List<TrackingLogResponse>> getTrackingLog(@Part("tracking_number") RequestBody tracking_number);

    @Multipart
    @POST("get-all-request-pickup-images/")
    Call<List<ImagesResponse>> getAllImages(@Part("tracking_number") RequestBody tracking_number);

    @Multipart
    @POST("create-user/")
    Call<AddUserResponse> addUserWithImage(@Part("email") RequestBody email,
                                  @Part("role") RequestBody role,
                          @Part("username") RequestBody username,
                          @Part("password") RequestBody password,

                          @Part("title") RequestBody title,

                         @Part MultipartBody.Part profile_picture,

                          @Part("first_name") RequestBody firstname,
                          @Part("last_name") RequestBody lastname,
                          @Part("address") RequestBody address,
                          @Part("phone_number") RequestBody phone_number);

    @Multipart
    @POST("create-user/")
    Call<AddUserResponse> addUserNoImage(@Part("email") RequestBody email,
                                           @Part("role") RequestBody role,
                                           @Part("username") RequestBody username,
                                           @Part("password") RequestBody password,

                                           @Part("title") RequestBody title,

                                           @Part("first_name") RequestBody firstname,
                                           @Part("last_name") RequestBody lastname,
                                           @Part("address") RequestBody address,
                                           @Part("phone_number") RequestBody phone_number);
    @Multipart
    @PUT("update-user/")
    Call<APIResponse> updateUser(@Part("id") RequestBody id,@Part("email") RequestBody email,

                                  @Part("title") RequestBody title,

                                  @Part MultipartBody.Part profile_picture,

                                  @Part("first_name") RequestBody firstname,
                                  @Part("last_name") RequestBody lastname,
                                  @Part("address") RequestBody address,
                                  @Part("phone_number") RequestBody phone_number,
                                 @Part("role") RequestBody role,
                                 @Part("is_active") RequestBody is_active);

    @Multipart
    @PUT("update-user/")
    Call<APIResponse> updateUserNoImage(@Part("id") RequestBody id,@Part("email") RequestBody email,

                                 @Part("title") RequestBody title,
                                 @Part("first_name") RequestBody firstname,
                                 @Part("last_name") RequestBody lastname,
                                 @Part("address") RequestBody address,
                                 @Part("phone_number") RequestBody phone_number,
                                 @Part("role") RequestBody role,
                                 @Part("is_active") RequestBody is_active);


    @Multipart
    @POST("reset-password/")
    Call<APIResponse> changePassword(@Part("username") RequestBody username,
                                 @Part("password") RequestBody password);


    @Multipart
    @POST("send-code/")
    Call<APIResponse> sendCode(@Part("username") RequestBody username);

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
                                     @Part("operating_hours") RequestBody operating_hours,
                                   @Part("is_approved") RequestBody is_approved);

    @Multipart
    @PUT("add-update-warehouse/")
    Call<APIResponse> updateWarehouseWithImage(@Part("id") RequestBody warehouse_id,
                                             @Part("warehouse_owner") RequestBody warehouse_owner,
                                            @Part MultipartBody.Part image,
                                             @Part("address") RequestBody address,
                                             @Part("volume") RequestBody volume,
                                             @Part("cctv") RequestBody cctv,
                                             @Part("armed_response") RequestBody armed_response,
                                             @Part("fire_safety_and_management") RequestBody fire_safety_and_management,
                                             @Part("parking_space") RequestBody parking_space,
                                             @Part("operating_hours") RequestBody operating_hours,
                                             @Part("is_approved") RequestBody is_approved);

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
                                              @Part("operating_hours") RequestBody operating_hours,
                                             @Part("is_approved") RequestBody is_approved);

    @Multipart
    @POST("add-update-warehouse/")
    Call<APIResponse> addWarehouseNoImage(@Part("warehouse_owner") RequestBody warehouse_owner,
                                             @Part("address") RequestBody address,
                                             @Part("volume") RequestBody volume,
                                             @Part("cctv") RequestBody cctv,
                                             @Part("armed_response") RequestBody armed_response,
                                             @Part("fire_safety_and_management") RequestBody fire_safety_and_management,
                                             @Part("parking_space") RequestBody parking_space,
                                             @Part("operating_hours") RequestBody operating_hours,
                                             @Part("is_approved") RequestBody is_approved);

    @Multipart
    @POST("add-update-warehouse/")
    Call<APIResponse> addWarehouseWithImage(@Part("warehouse_owner") RequestBody warehouse_owner,
                                            @Part MultipartBody.Part image,
                                          @Part("address") RequestBody address,
                                          @Part("volume") RequestBody volume,
                                          @Part("cctv") RequestBody cctv,
                                          @Part("armed_response") RequestBody armed_response,
                                          @Part("fire_safety_and_management") RequestBody fire_safety_and_management,
                                          @Part("parking_space") RequestBody parking_space,
                                          @Part("operating_hours") RequestBody operating_hours,
                                          @Part("is_approved") RequestBody is_approved);


    @Multipart
    @POST("add-update-requestpickup/")
    Call<APIResponse> addNoImagesPickRequest(@Part("customer") RequestBody customer,
                                           @Part("tracking_number") RequestBody tracking_number,
                                           @Part("recipient_name") RequestBody recipient_name,
                                     @Part("recipient_phone") RequestBody recipient_phone,


                                     @Part("pickup_location") RequestBody pickup_location,
                                           @Part("dropoff_location") RequestBody dropoff_location,

                                           @Part("volume") RequestBody volume,
                                           @Part("weight") RequestBody weight,

                                           @Part("parcel_description") RequestBody parcel_description,
                                           @Part("price_to_pay") RequestBody price_to_pay,
                                     @Part("status") RequestBody status);

    @Multipart
    @POST("add-update-requestpickup/")
    Call<APIResponse> addWithImagesPickRequest(@Part("customer") RequestBody customer,
                                             @Part("tracking_number") RequestBody tracking_number,
                                             @Part("recipient_name") RequestBody recipient_name,
                                             @Part("recipient_phone") RequestBody recipient_phone,


                                             @Part("pickup_location") RequestBody pickup_location,
                                             @Part("dropoff_location") RequestBody dropoff_location,

                                             @Part("volume") RequestBody volume,
                                             @Part("weight") RequestBody weight,

                                             @Part("parcel_description") RequestBody parcel_description,
                                             @Part("price_to_pay") RequestBody price_to_pay,
                                             @Part("status") RequestBody status,
                                               @Part List<MultipartBody.Part> images);


    @Multipart
    @PUT("add-update-requestpickup/")
    Call<APIResponse> updateNoImagesPickRequest(@Part("id") RequestBody id,
                                        @Part("customer") RequestBody customer,
                                        @Part("tracking_number") RequestBody tracking_number,
                                         @Part("recipient_name") RequestBody recipient_name,
                                         @Part("recipient_phone") RequestBody recipient_phone,


                                         @Part("pickup_location") RequestBody pickup_location,
                                         @Part("dropoff_location") RequestBody dropoff_location,

                                         @Part("volume") RequestBody volume,
                                         @Part("weight") RequestBody weight,

                                         @Part("parcel_description") RequestBody parcel_description,
                                         @Part("price_to_pay") RequestBody price_to_pay);

    @Multipart
    @PUT("add-update-requestpickup/")
    Call<APIResponse> updateWithImagesPickRequest(@Part("id") RequestBody id,
                                                @Part("customer") RequestBody customer,
                                                @Part("tracking_number") RequestBody tracking_number,
                                                @Part("recipient_name") RequestBody recipient_name,
                                                @Part("recipient_phone") RequestBody recipient_phone,


                                                @Part("pickup_location") RequestBody pickup_location,
                                                @Part("dropoff_location") RequestBody dropoff_location,

                                                @Part("volume") RequestBody volume,
                                                @Part("weight") RequestBody weight,

                                                @Part("parcel_description") RequestBody parcel_description,
                                                @Part("price_to_pay") RequestBody price_to_pay,
                                                  @Part List<MultipartBody.Part> images);
    @Multipart
    @PUT("add-update-requestpickup/")
    Call<APIResponse> ratePickRequest(@Part("tracking_number") RequestBody tracking_number,
                                      @Part("rating") RequestBody  rating);

    @Multipart
    @PUT("add-update-requestpickup/")
    Call<APIResponse> setStatusPickRequest(@Part("tracking_number") RequestBody tracking_number,
                                      @Part("status") RequestBody  status);



    @POST("get-all-request-pickup-status/")
    Call<List<RequestPickupStatusResponse>> getPickRequestStatus();


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
                                 @Part("is_approved") RequestBody isApproved,
                                 @Part("is_default") RequestBody is_default);





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
                                          @Part("is_approved") RequestBody isApproved,
                                 @Part("is_default") RequestBody is_default);



    @Multipart
    @POST("add-update-drivers-license/")
    Call<DriversLicenseResponse> addDriversLicenseWithImage(
                                                   @Part("license_owner") RequestBody license_owner,
                                                   @Part("fullname") RequestBody fullname,
                                                   @Part("license_number") RequestBody license_number,
                                                   @Part("expiry_date") RequestBody expiry_date,
                                                   @Part MultipartBody.Part uploadLicense,
                                                   @Part("is_approved") RequestBody is_approved);
    @Multipart
    @POST("add-update-drivers-license/")
    Call<DriversLicenseResponse> addDriversLicenseNoImage(
            @Part("license_owner") RequestBody license_owner,
            @Part("fullname") RequestBody fullname,
            @Part("license_number") RequestBody license_number,
            @Part("expiry_date") RequestBody expiry_date,
            @Part("is_approved") RequestBody is_approved);

    @Multipart
    @PUT("add-update-drivers-license/")
    Call<DriversLicenseResponse> updateDriversLicenseWithImage( @Part("id") RequestBody id,
                                 @Part("license_owner") RequestBody license_owner,
                                 @Part("fullname") RequestBody fullname,

                                 @Part("license_number") RequestBody license_number,
                                 @Part("expiry_date") RequestBody expiry_date,

                                                                @Part MultipartBody.Part uploadLicense ,
                                                       @Part("is_approved") RequestBody is_approved);


    @Multipart
    @PUT("add-update-drivers-license/")
    Call<DriversLicenseResponse> updateDriversLicenseNoImage( @Part("id") RequestBody id,
                                                              @Part("license_owner") RequestBody license_owner,
                                                              @Part("fullname") RequestBody fullname,
                                                              @Part("license_number") RequestBody license_number,
                                                              @Part("expiry_date") RequestBody expiry_date,
                                                              @Part("is_approved") RequestBody is_approved);


    @Multipart
    @POST("get-car/")
    Call<CarResponse> loadCar(@Part("id") RequestBody id);


    @Multipart
    @POST("get-drivers-license/")
    Call<DriversLicenseResponse> loadDriversLicense(@Part("id") RequestBody id);

    @Multipart
    @POST("track-parcel/")
    Call<PickupResponse> getPickUp(@Part("id") RequestBody id);


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

    @POST("users/") // Replace "endpoint" with your actual API endpoint
    Call<List<AllUsersResponse>> getAllUSerObjects();

    @POST("cars/") // Replace "endpoint" with your actual API endpoint
    Call<List<CarResponse>> getAllCarObjects();

    @Multipart
    @POST("cars/") // Replace "endpoint" with your actual API endpoint
    Call<List<CarResponse>> getAllUserCarsObjects(@Part("driver") RequestBody driver);

    @GET("warehouses/") // Replace "endpoint" with your actual API endpoint
    Call<List<WarehouseResponse>> getAllWareHouseObjects();


    @GET("drivers-licenses/") // Replace "endpoint" with your actual API endpoint
    Call<List<DriversLicenseResponse>> getAllDriversLicenseObjects();






    @Multipart
    @PUT("add-update-car/")
    Call<APIResponse> updateCarApproval(@Part("id") RequestBody id,
                                              @Part("is_approved") RequestBody is_approved,
                                        @Part("is_default") RequestBody is_default
    );




    @Multipart
    @PUT("update-user/")
    Call<APIResponse> updateUserActive(@Part("id") RequestBody id,
                                        @Part("is_active") RequestBody is_active
    );



    @Multipart
    @POST("all-requestpickups-by-user/") // Replace "endpoint" with your actual API endpoint
    Call<List<RequestPickupResponse>> getAllUserRequest(@Part("customer") RequestBody customer);

    @Multipart
    @POST("get-single-requestpickup/") // Replace "endpoint" with your actual API endpoint
    Call<RequestPickupResponse> apiGetSingleRequestPickup(@Part("tracking_number") RequestBody tracking_number);
//
    @Multipart
    @POST("all-requestpickups-by-driver/") // Replace "endpoint" with your actual API endpoint
    Call<List<RequestPickupResponse>> getAllDriverRequest(@Part("driver") RequestBody driver);




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



    @Multipart
    @POST("driver-pickup-and-dropff-locations/") // Replace "endpoint" with your actual API endpoint
    Call<List<RoutesResponse>> getAllRoutes(@Part("driver") RequestBody driver);











}
