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

    # path('login/', views.login),
    #path('token/', AuthUserLoginView.as_view(), name='token_obtain_pair'),
    path('token/', views.login),

    #path('token/', TokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),


    path('cars/',views.getCars),
    path('get-car/', views.getCar),
    path('add-update-car/', views.car_detail),

    
    path('drivers-licenses/',views.getDriversLicenses),
    path('get-drivers-license/', views.getDriversLicense),
    path('add-update-drivers-license/', views.driversLicense_detail),

    path('warehouses/',views.getWareHouses),
    path('get-warehouse/', views.getWareHouse),
    path('add-update-warehouse/', views.warehouse_detail),


    path('get-image/',views.getImage),


    path('all-requestpickups-by-user/',views.getAllUserRequestPickups),#get all requestpickups created by a user
    path('all-undelivered-requestpickups-by-user/',views.getUndeliveredUserRequestPickups),#get all undelivered requestpickups created by a user
    path('all-unrated-requestpickups-by-user/',views.getUnratedUserRequestPickups),#get all unrated requestpickups created by a user
    path('single-requestpickups-by-user/<str:pk>/',views.getRequestPickup),#view single requestpickups created by user
    path('add-update-requestpickup/', views.requestpickup_detail),


    path('all-requestpickups-by-driver/',views.getAllDriverRequestPickups),#get all requestpickups created by a user
    path('all-undelivered-requestpickups-by-driver/',views.getDriverUndeliveredRequestPickups),#get all undelivered requestpickups created by a driver
    path('all-unrated-requestpickups-by-driver/',views.getDriverRatedRequestPickups),#get all unrated requestpickups created by a driver
    path('driver-notification/',views.check_unpicked_request_pickups),#check job assignments
    path('decline-accept-job/',views.acceptDeclineJob),#check job assignments
    path('add-delivery-proof-images/',views.add_proof_of_delivery_image),#proof of delivery images
    path('set-is-delivered/',views.mark_request_pickup_delivered),#mark as delivred

    path('get-delivery-images-and-rating/',views.getDeliveryImagesAndRating),#getImages and Rating






    path('pickups',views.getPickups),
    path('pickups/<str:pk>/', views.getPickup),
    path('get-pickup-id-from-requestpickup-id/',views.getPickupIDfromRequestPickupID),#get an  pickup id of a certain requestpickup
    path('add-update-pickup/', views.pickup_detail),

    # path('pickup-messages',views.getPickupMessages),
    path('pickup-messages/', views.getPickupMessages),
    path('send-message/', views.pickup_message_detail),


    path('track-parcel/', views.trackParcel),




]

