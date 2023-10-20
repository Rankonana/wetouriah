from django.urls import path
from . import views

from django.conf import settings
from django.conf.urls.static import static

#ref https://django-rest-framework-simplejwt.readthedocs.io/en/latest/getting_started.html
#https://code.tutsplus.com/tutorials/how-to-authenticate-with-jwt-in-django--cms-30460
#https://www.django-rest-framework.org/api-guide/permissions/
#http://www.tomchristie.com/rest-framework-2-docs/api-guide/permissions
from rest_framework_simplejwt.views import (
    TokenObtainPairView,
    TokenRefreshView,
)

from rest_framework_simplejwt import views as jwt_views


# from .views import (
#     AuthUserLoginView
# )
urlpatterns = [
    path('',views.getRoutes),
    path('users/',views.getUsers),
    path('get-user/', views.getUser),
    path('create-user/', views.create_user),
    path('update-user/', views.update_user),
    path('reset-password/', views.reset_password),

    path('send-code/', views.send_code),


    # path('login/', views.login),
    #path('token/', AuthUserLoginView.as_view(), name='token_obtain_pair'),
    path('token/', views.login),

    #path('token/', TokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),




    path('warehouses/',views.getWareHouses),
    path('get-warehouse/', views.getWareHouse),
    path('add-update-warehouse/', views.warehouse_detail),



    path('all-requestpickups-by-user/',views.getAllUserRequestPickups),#get all requestpickups created by a user
    path('get-single-requestpickup/', views.get_single_pickup),

    path('add-update-requestpickup/', views.requestpickup_detail),
    path('delete-requestpickup/', views.requestpickup_delete),
    path('tracking-log/', views.getTrackingLog),
    path('get-all-request-pickup-status/', views.getAllRequestPickupStatus),
    path('get-all-request-pickup-images/', views.getImages),



    #Driver
    # modify routes so that you get items that the driver has accepted to pickup and in transit or other status
    path('all-requestpickups-by-driver/',views.getAllDriverRequestPickups),#get all requestpickups created by a driver
    path('driver-pickup-locations/',views.pickupLocations),#get all pick up adress
    path('driver-dropff-locations/',views.dropoffLocations),#get all drop off address
    path('driver-pickup-and-dropff-locations/',views.pickup_and_droff_Locations),#get all drop off address
    path('courier-availability/',views.courier_availability),
    path('accept-decline-parcel/',views.accept_decline_parcel),
    path('check-job-offers/',views.check_job_offers),
    path('get-courier-availability/',views.get_availabilty_status),
    path('get-distance-and-time/',views.get_distance_andTime),






    #During the assignment of a driver, you look for #rating, space inside their prefered  car, #how many trips they have already made,
    #is there avilabilty status active


    path('cars/',views.getCars),
    path('get-car/', views.getCar),
    path('add-update-car/', views.car_detail),

    
    path('drivers-licenses/',views.getDriversLicenses),
    path('get-drivers-license/', views.getDriversLicense),
    path('add-update-drivers-license/', views.driversLicense_detail),





]

