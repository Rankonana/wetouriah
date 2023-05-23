from django.urls import path
from . import views


urlpatterns = [
    path('',views.getRoutes),
    path('users',views.getUsers),
    path('users/<str:pk>/', views.getUser),
    path('create-user/', views.create_user),
    path('update-user/<str:user_id>/', views.update_user),

    path('cars',views.getCars),
    path('cars/<str:pk>/', views.getCar),
    path('add-update-car/', views.car_detail),

    path('warehouses',views.getWareHouses),
    path('warehouses/<str:pk>/', views.getWareHouse),
    path('add-update-warehouse/', views.warehouse_detail),







]