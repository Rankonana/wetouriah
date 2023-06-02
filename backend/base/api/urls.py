from django.urls import path
from . import views

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
    path('users',views.getUsers),
    path('users/<str:pk>/', views.getUser),
    path('create-user/', views.create_user),
    path('update-user/<str:pk>/', views.update_user),

    # path('login/', views.login),
    #path('token/', AuthUserLoginView.as_view(), name='token_obtain_pair'),
    path('token/', views.login),

    #path('token/', TokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),


    path('cars',views.getCars),
    path('cars/<str:pk>/', views.getCar),
    path('add-update-car/', views.car_detail),

    path('warehouses',views.getWareHouses),
    path('warehouses/<str:pk>/', views.getWareHouse),
    path('add-update-warehouse/', views.warehouse_detail),

    path('all-requestpickups-by-user/',views.getUserRequestPickups),#view all requestpickups created by warehouse owner
    path('single-requestpickups-by-user/<str:pk>/',views.getRequestPickup),#view single requestpickups created by warehouse owner

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