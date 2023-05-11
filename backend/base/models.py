from django.contrib.auth.models import User
from django.contrib.auth.models import AbstractUser, AnonymousUser
from django.db import models
from django.contrib.auth.models import AbstractUser


class User(AbstractUser):
    username = models.CharField(unique=True,max_length=200,null=True,blank=True)
    title = models.CharField(max_length=200,null=True,blank=True)
    image = models.ImageField(default="NoImage.jpg",null=True,blank=True)
    name = models.CharField(max_length=200,null=True,blank=True)
    lastname = models.CharField(max_length=200,null=True,blank=True)
    address = models.CharField(max_length=200,null=True,blank=True)
    phone = models.CharField(max_length=200,null=True,blank=True)
    email = models.EmailField(unique=True,null=True,blank=True)
    #rating = models.EmailField(unique=True,null=True,blank=True)


    USERNAME_FIELD = 'username'
    REQUIRED_FIELDS = ['email']

    def __str__(self):
        return self.username
    
class Driver(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name='driver')
    license = models.CharField(max_length=200,null=True,blank=True)

class Car(models.Model):
    driver = models.ForeignKey(Driver, on_delete=models.CASCADE, related_name='car')
    fuel = models.CharField(max_length=200,null=True,blank=True)
    registration_number = models.CharField(max_length=200,null=True,blank=True)
    type = models.CharField(max_length=200,null=True,blank=True)
    capacity = models.CharField(max_length=200,null=True,blank=True)
    #color = models.CharField(max_length=200,null=True,blank=True)
    #make = models.CharField(max_length=200,null=True,blank=True)
    #model = models.CharField(max_length=200,null=True,blank=True)
    #car_vin_number = models.CharField(max_length=200,null=True,blank=True)
    #plate_number = models.CharField(max_length=200,null=True,blank=True)


class WareHouse(models.Model):
    owner = models.ForeignKey(User, on_delete=models.CASCADE, related_name='warehouse')
    image = models.ImageField(default="NoImage.jpg",null=True,blank=True)
    address = models.CharField(max_length=200,null=True,blank=True)
    volume = models.CharField(max_length=200,null=True,blank=True)
    cctv = models.BooleanField(default=False)
    armed_response = models.BooleanField(default=False)
    fire_safety_and_management = models.BooleanField(default=False)
    parking_space = models.BooleanField(default=False)
    operating_hours = models.CharField(max_length=200,null=True,blank=True)

class RequestPickupDropoff(models.Model):
    owner = models.ForeignKey(User, on_delete=models.CASCADE, related_name='requestpickupdropoff')
    created = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)
    date_and_time_pickup = models.DateTimeField(null=True,blank=True)
    date_and_time_dropoff = models.DateTimeField(null=True,blank=True)
    pickup_location = models.CharField(max_length=200,null=True,blank=True)
    dropoff_location = models.BooleanField(default=False)
    price = models.BooleanField(default=False)
    volume = models.CharField(max_length=200,null=True,blank=True)
    parcel_description = models.CharField(max_length=200,null=True,blank=True)
    special_notes = models.CharField(max_length=200,null=True,blank=True)
    price_to_pay = models.CharField(max_length=200,null=True,blank=True)


class Rating(models.Model):
    customer_rating = models.CharField(max_length=200,null=True,blank=True)
    driver_rating = models.CharField(max_length=200,null=True,blank=True)
    driver_comments = models.CharField(max_length=200,null=True,blank=True)
    customer_comments = models.CharField(max_length=200,null=True,blank=True)


class Delivery(models.Model):
    customer = models.ForeignKey(RequestPickupDropoff, on_delete=models.CASCADE, related_name='delivery_customer')
    driver = models.ForeignKey(Driver, on_delete=models.CASCADE, related_name='delivery_driver')
    rating = models.ForeignKey(Rating, on_delete=models.CASCADE, related_name='delivery_rating')
    start_datetime = models.DateTimeField(auto_now=True)
    end_datetime = models.DateTimeField(auto_now=True)
    duration = models.CharField(max_length=200,null=True,blank=True)
    tip = models.CharField(max_length=200,null=True,blank=True)
    price = models.CharField(max_length=200,null=True,blank=True)






