from django.contrib.auth.models import User
from django.db import models


class UserProfile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    profile_picture = models.ImageField(default="NoImage.jpg",null=True,blank=True)
    title = models.CharField(max_length=200,null=True,blank=True)
    firstname = models.CharField(max_length=200,null=True,blank=True)
    lastname = models.CharField(max_length=200,null=True,blank=True)
    address = models.CharField(max_length=200,null=True,blank=True)
    phone_number  = models.CharField(max_length=200,null=True,blank=True)
    email = models.EmailField(unique=True,null=True,blank=True)

    def __str__(self):
        return self.user.username

class Car(models.Model):
    car_owner = models.ForeignKey(UserProfile, on_delete=models.CASCADE)
    type = models.CharField(max_length=200,null=True,blank=True)
    capacity = models.CharField(max_length=200,null=True,blank=True)
    color = models.CharField(max_length=200,null=True,blank=True)
    make = models.CharField(max_length=200,null=True,blank=True)
    model = models.CharField(max_length=200,null=True,blank=True)
    year = models.CharField(max_length=200,null=True,blank=True)
    license_plate = models.CharField(max_length=200,null=True,blank=True)
    #is_approved = models.BooleanField(default=False)


    def __str__(self):
        return self.car_owner + " " + self.type

class WareHouse(models.Model):
    warehouse_owner = models.ForeignKey(UserProfile, on_delete=models.CASCADE)
    image = models.ImageField(default="NoImage.jpg",null=True,blank=True)
    address = models.CharField(max_length=200,null=True,blank=True)
    volume = models.CharField(max_length=200,null=True,blank=True)
    cctv = models.BooleanField(default=False)
    armed_response = models.BooleanField(default=False)
    fire_safety_and_management = models.BooleanField(default=False)
    parking_space = models.BooleanField(default=False)
    operating_hours = models.CharField(max_length=200,null=True,blank=True)
    #is_approved = models.BooleanField(default=False)


    def __str__(self):
        return self.warehouse_owner + " " + self.address

class RequestPickupImages(models.Model):
    request_pickup_images = models.ImageField(null=True,blank=True)


class RequestPickup(models.Model):
    customer = models.ForeignKey(UserProfile, on_delete=models.CASCADE)

    request_time = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)

    date_and_time_pickup = models.DateTimeField(null=True,blank=True)
    date_and_time_dropoff = models.DateTimeField(null=True,blank=True)

    recipient_name = models.CharField(max_length=100)
    recipient_phone = models.CharField(max_length=100)
    pickup_location = models.CharField(max_length=200,null=True,blank=True)
    dropoff_location = models.CharField(max_length=200,null=True,blank=True)

    volume = models.CharField(max_length=200,null=True,blank=True)
    weight = models.DecimalField(max_digits=5, decimal_places=2)
    parcel_description = models.CharField(max_length=200,null=True,blank=True)
    special_notes = models.CharField(max_length=200,null=True,blank=True)
    price_to_pay = models.CharField(max_length=200,null=True,blank=True)
    images = models.ManyToManyField('RequestPickupImages')

    is_picked = models.BooleanField(default=False)

    
    def __str__(self):
        return str(self.request_time)

class RequestPickupMessage(models.Model):
    requestpickup = models.ForeignKey(RequestPickup, on_delete=models.CASCADE)
    sender = models.ForeignKey(UserProfile, on_delete=models.CASCADE)
    #sender = models.ForeignKey(UserProfile, related_name='sent_messages', on_delete=models.CASCADE)
    #recipient = models.ForeignKey(UserProfile, related_name='received_messages', on_delete=models.CASCADE)
    message = models.TextField()
    timestamp = models.DateTimeField(auto_now_add=True)
    #is_read = models.BooleanField(default=False)

    def __str__(self):
        return f"Message from {self.sender} - {self.timestamp}"

class Pickup(models.Model):
    request_pickup = models.ForeignKey(RequestPickup, on_delete=models.CASCADE)
    car = models.ForeignKey(Car, on_delete=models.CASCADE)
    start_datetime = models.DateTimeField(auto_now=True)
    end_datetime = models.DateTimeField(auto_now=True)
    duration = models.CharField(max_length=200,null=True,blank=True)
    tip = models.CharField(max_length=200,null=True,blank=True)
    rating = models.PositiveIntegerField(blank=True, null=True)
    last_known_location = models.TextField()
    is_delivered = models.BooleanField(default=False)

    def __str__(self):
        return  str(self.start_datetime)

class PickupMessage(models.Model):
    pickup = models.ForeignKey(Pickup, on_delete=models.CASCADE)
    sender = models.ForeignKey(UserProfile, on_delete=models.CASCADE)
    #sender = models.ForeignKey(UserProfile, related_name='sent_messages', on_delete=models.CASCADE)
    #recipient = models.ForeignKey(UserProfile, related_name='received_messages', on_delete=models.CASCADE)
    message = models.TextField()
    timestamp = models.DateTimeField(auto_now_add=True)
    #is_read = models.BooleanField(default=False)

    def __str__(self):
        return f"Message from {self.sender} - {self.timestamp}"






