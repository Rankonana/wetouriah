from django.urls import path
from . import views


urlpatterns = [
    path('',views.getRoutes),
    path('users',views.getUsers),
    path('users/<str:pk>/', views.getUser),
    path('create-user/', views.create_user),
    path('update-user/<str:user_id>/', views.update_user),




]