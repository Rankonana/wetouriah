from django.urls import path, include
from . import views

urlpatterns = [
    path('',views.hello,name="home"),
    path('',views.hello,name="home"),

]