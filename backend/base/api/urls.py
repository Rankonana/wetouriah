from django.urls import path
from . import views


urlpatterns = [
    path('',views.getRoutes),
    path('users',views.getUsers),
    path('users/<str:pk>/', views.getUser),
    path('add-user/', views.add_or_update_user),



]