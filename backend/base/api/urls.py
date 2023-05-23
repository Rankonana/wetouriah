from django.urls import path
from . import views


urlpatterns = [
    path('',views.getRoutes),
    path('users',views.getUsers),
    path('users/<str:pk>/', views.getUser),
    path('create-user/', views.create_user),
    path('update-user/<str:user_id>/', views.update_user),

    path('login/', views.login),


    path('cars',views.getCars),
    path('cars/<str:pk>/', views.getCar),
    path('add-update-car/', views.car_detail),

    path('warehouses',views.getWareHouses),
    path('warehouses/<str:pk>/', views.getWareHouse),
    path('add-update-warehouse/', views.warehouse_detail),

    path('requestpickups',views.getRequestPickups),
    path('requestpickups/<str:pk>/', views.getRequestPickup),
    path('add-update-requestpickup/', views.requestpickup_detail),

    path('pickups',views.getPickups),
    path('pickups/<str:pk>/', views.getPickup),
    path('add-update-pickup/', views.pickup_detail),

    path('pickup-messages',views.getPickupMessages),
    path('pickup-messages/<str:pk>/', views.getPickupMessage),
    path('add-update-pickup-messages/', views.pickup_message_detail),






]