from django.contrib.auth.models import User,AbstractUser
from django.db import models
from django.db.models import Q

import requests

from django.db.models import F, Count
from django.db.models import Value as V
from django.db.models.functions import Concat
from django.db.models import CharField
from django.db.models import ExpressionWrapper
from geopy.distance import geodesic

from django.db.models import F, Count
from django.db.models import Value as V
from django.db.models.functions import Concat
from django.db.models import CharField
from django.db.models import ExpressionWrapper


#


class User(AbstractUser):

    # These fields tie to the roles!
    ADMIN = 1
    CUSTOMER = 2
    DRIVER = 3
    WAREHOUSE = 4

    ROLE_CHOICES = (
        (ADMIN, 'Admin'),
        (CUSTOMER, 'Customer'),
        (DRIVER, 'Driver'),
        (WAREHOUSE,'WareHouse')
    )
    profile_picture = models.ImageField(default="NoImage.jpg",null=True,blank=True)
    role = models.PositiveSmallIntegerField(choices=ROLE_CHOICES, blank=True, null=True, default=4)
    title = models.CharField(max_length=200,null=True,blank=True)
    address = models.CharField(max_length=200,null=True,blank=True)
    phone_number  = models.CharField(max_length=200,null=True,blank=True)

    # USERNAME_FIELD = email
    REQUIRED_FIELDS = []
    

    def __str__(self):
        return self.username

class CourierAvailability(models.Model):
    courier = models.ForeignKey(User, on_delete=models.CASCADE)
    available = models.BooleanField(default=False)
    location = models.CharField(max_length=200, null=True, blank=True)
    distance_to_pickup = models.CharField(max_length=200, null=True, blank=True)

    def __str__(self):
        return self.courier.username + " - " + str(self.available)

    def save(self, *args, **kwargs):
        super(CourierAvailability, self).save(*args, **kwargs)

        # Find related RequestPickup objects that match the courier

        matching_pickups = RequestPickup.objects.filter(courier=self.courier).exclude(status__in=[1,3,9,10,11,12,15])

        # Update the TrackingLog location for matching RequestPickups
        for pickup in matching_pickups:
            city = get_city_name(self.location)
            time = None
            if pickup.status in ['5','8']:
                distance, time = get_longest_arrival_time(self.location, pickup.pickup_location, "AIzaSyCfiGeaaLSDtDpepqYFmc-vjFXlFys8lXs")
            else:
                distance ,time = get_longest_arrival_time(self.location, pickup.dropoff_location, "AIzaSyCfiGeaaLSDtDpepqYFmc-vjFXlFys8lXs")

            
            tracking_log = TrackingLog(pickup_request=pickup, location= str(pickup.courier.username) + ", " + str(pickup.courier.get_role_display()) ,estimated_arrival = format_seconds(time))
            tracking_log.save()

class DriversLicense(models.Model):
    license_owner = models.ForeignKey(User, on_delete=models.CASCADE)
    fullname  = models.CharField(max_length=200,null=True,blank=True)
    license_number = models.CharField(max_length=200,null=True,blank=True)
    expiry_date = models.DateTimeField(null=True,blank=True)
    uploadLicense = models.ImageField(default="NoImage.jpg",null=True,blank=True)
    is_approved = models.BooleanField(default=False )

    # identity_number = models.CharField(max_length=200,null=True,blank=True)#
    # date_of_birth = models.DateTimeField(null=True,blank=True)#   
    # country_of_issue = models.CharField(max_length=200,null=True,blank=True)#
    # code = models.CharField(max_length=200,null=True,blank=True)#
    # restrictions = models.CharField(max_length=200,null=True,blank=True)#
    # gender = models.CharField(max_length=200,null=True,blank=True)#
    # date_of_issue= models.DateTimeField(null=True,blank=True)#
 



    def __str__(self):
        return str(self.license_owner)

class Car(models.Model):
    car_owner = models.ForeignKey(User, on_delete=models.CASCADE)
    type = models.CharField(max_length=200,null=True,blank=True) #change type to cartype
    capacity = models.CharField(max_length=200,null=True,blank=True)
    color = models.CharField(max_length=200,null=True,blank=True)
    make = models.CharField(max_length=200,null=True,blank=True)
    model = models.CharField(max_length=200,null=True,blank=True)
    year = models.CharField(max_length=200,null=True,blank=True)
    license_plate = models.CharField(max_length=200,null=True,blank=True)
    is_approved = models.BooleanField(default=False ) 
    is_default = models.BooleanField(default=False ) 




    def __str__(self):
        return str(self.car_owner)

class WareHouse(models.Model):
    warehouse_owner = models.ForeignKey(User, on_delete=models.CASCADE)
    image = models.ImageField(default="NoImage.jpg",null=True,blank=True)
    address = models.CharField(max_length=200,null=True,blank=True)
    volume = models.CharField(max_length=200,null=True,blank=True)
    cctv = models.BooleanField(default=False)
    armed_response = models.BooleanField(default=False)
    fire_safety_and_management = models.BooleanField(default=False)
    parking_space = models.BooleanField(default=False)
    operating_hours = models.CharField(max_length=200,null=True,blank=True)
    is_approved = models.BooleanField(default=False ) 


    def __str__(self):
        return str(self.warehouse_owner) + " " + self.address

# DELIVERY_CHOICES = (
#     (pending, 'pending'),
#     (driver_assigned, 'driver_assigned'),
#     (picked_up, 'picked_up'),
#     (in_transit , 'in_transit'),
#     (out_for_delivery ,'out_for_delivery'),
#     (delivered , 'delivered'),
#     (returned ,'returned')
# )
class RequestPickupStatus(models.Model):
    status_name = models.CharField(max_length=200,null=True,blank=True)
    def __str__(self):
        return str(self.status_name)

class RequestPickup(models.Model):

    customer = models.ForeignKey(User, on_delete=models.CASCADE, null=True,blank=True,related_name='customer')
    tracking_number = models.CharField(max_length=20, null=True,blank=True)
    request_time = models.DateTimeField(auto_now_add=True)
    recipient_name = models.CharField(max_length=100, null=True,blank=True)
    recipient_phone = models.CharField(max_length=100, null=True,blank=True)
    pickup_location = models.CharField(max_length=200,null=True,blank=True)
    dropoff_location = models.CharField(max_length=200,null=True,blank=True)

    volume = models.CharField(max_length=200,null=True,blank=True)
    weight = models.DecimalField(max_digits=1000, decimal_places=2, null=True,blank=True)
    parcel_description = models.CharField(max_length=200,null=True,blank=True)
    price_to_pay = models.CharField(max_length=200,null=True,blank=True)
    status = models.ForeignKey(RequestPickupStatus, on_delete=models.CASCADE,blank=True, null=True)
    courier  = models.ForeignKey(User, on_delete=models.CASCADE,blank=True, null=True,related_name= 'courier') 
    duration = models.CharField(max_length=200,null=True,blank=True) 
    rating = models.PositiveIntegerField(blank=True, null=True)
    start_datetime = models.DateTimeField(blank=True, null=True)
    end_datetime = models.DateTimeField(blank=True, null=True)
    

    def __str__(self):
        return  "Driver: "+ str(self.courier) + ", From: " + str(self.customer) + ", To: " + str(self.recipient_name) + ", Trackingnumber: "+ str(self.tracking_number) +", Status: "+ str(self.status) 
    
    def save(self, *args, **kwargs):
        if self.courier == None:
            self.courier = AssignACourier(self)
        super(RequestPickup, self).save(*args, **kwargs)

        if self.courier:
            courier_availability = CourierAvailability.objects.filter(courier=self.courier).first()
            if courier_availability:
                driver_location = courier_availability.location
                city = get_city_name(driver_location)
                # print(driver_location)
                # print(city)

                if self.status_id in [5, 8]:
                    location = self.pickup_location
                else:
                    location = self.dropoff_location
                distance,time = get_longest_arrival_time(courier_availability.location, location, "AIzaSyCfiGeaaLSDtDpepqYFmc-vjFXlFys8lXs")
                status = RequestPickupStatus.objects.get(status_name = self.status)

                tracking_log = TrackingLog(
                    pickup_request=self,
                    location=str(self.courier.username) + ", " + str(self.courier.get_role_display()),
                    status = status,
                    estimated_arrival=format_seconds(time)
                )
                #print(status)
                tracking_log.save()
    
class TrackingLog(models.Model):
    pickup_request = models.ForeignKey(RequestPickup, on_delete=models.CASCADE)
    location = models.CharField(max_length=100)
    timestamp = models.DateTimeField(auto_now_add=True)
    status = models.ForeignKey(RequestPickupStatus, on_delete=models.CASCADE, blank=True, null=True)
    estimated_arrival = models.CharField(max_length=100,blank=True, null=True)


    def save(self, *args, **kwargs):
        # Check if the associated RequestPickup has a status set
        if self.pickup_request.status:
            self.status = self.pickup_request.status

        super(TrackingLog, self).save(*args, **kwargs)

    def __str__(self):
        return f"{self.pickup_request} - {self.location} - {self.timestamp}"

class ImageStatus(models.Model):
    status_name = models.CharField(max_length=100)

    def __str__(self):
        return self.status_name

class Images(models.Model):
    image = models.ImageField(default="NoImage.jpg",null=True,blank=True)
    pickup_request = models.ForeignKey(RequestPickup, on_delete=models.CASCADE)
    image_status = models.ForeignKey(ImageStatus, on_delete=models.CASCADE)

    def __str__(self):
        return f"{self.pickup_request} - {self.image_status}"

def get_city_name(location):
    #print(courier_availability.location)
    if location:
        #print(courier_availability.location)
        #print(courier_availability.location)

        # Make a request to the Google Maps Geocoding API
        google_maps_api_key = 'AIzaSyCfiGeaaLSDtDpepqYFmc-vjFXlFys8lXs'
        base_url = 'https://maps.googleapis.com/maps/api/geocode/json?'

        latlng = location  # Coordinates in the format "lat,lng"
        params = {
            'latlng': latlng,
            'key': google_maps_api_key
        }

        response = requests.get(base_url, params=params)
        data = response.json()
       # print(data)

        if data['status'] == 'OK' and 'results' in data:
            results = data['results']

            # Extract the city name from the geocoding results
            city_name = None
            for result in results:
                for component in result.get('address_components', []):
                    if 'locality' in component.get('types', []):
                        city_name = component.get('long_name')
                        break
            #distance,estimated_arrival = get_longest_arrival_time()

            return city_name

            #print(data)
            #tracking_log.save()


def get_longest_arrival_time(origin, destination, api_key):
    base_url = "https://maps.googleapis.com/maps/api/distancematrix/json?"
    params = {
        "origins": origin,
        "destinations": destination,
        "key": api_key,
    }

    response = requests.get(base_url, params=params)

    if response.status_code == 200:
        data = response.json()
        #print(data)
        distance = data.get('rows', [{}])[0].get('elements', [{}])[0].get('distance', {}).get('value', 'Unknown')
        duration = data.get('rows', [{}])[0].get('elements', [{}])[0].get('duration', {}).get('value', 'Unknown')
        return distance,duration

    return "Unable to retrieve data or calculate arrival time."


def format_seconds(seconds):
    try:
        if seconds >= 3600:
            hours = seconds // 3600
            seconds %= 3600
            minutes = seconds // 60
            seconds %= 60
            return f"{hours} hrs, {minutes} mins"
        elif seconds >= 60:
            minutes = seconds // 60
            seconds %= 60
            return f"{minutes} mins"
        else:
            return f"{seconds} seconds"
    except:
        print("can't parse" + str(seconds))
        return f" can't parse {seconds}"


class DeclineList(models.Model):
    courier = models.ForeignKey(User, on_delete=models.CASCADE)  
    requestPickup = models.ForeignKey(RequestPickup, on_delete=models.CASCADE)  

    def __str__(self):
        return self.courier.username
    
    def save(self, *args, **kwargs):
        requestPickup = RequestPickup.objects.get(id=self.requestPickup.pk)
        requestPickup.courier = AssignACourier(requestPickup)
        requestPickup.save()
        super(DeclineList, self).save(*args, **kwargs)


def AssignACourier(request_pickup):
    # Retrieve all available couriers
    available_couriers = CourierAvailability.objects.filter(available=True)
    couriers_in_decline_list = DeclineList.objects.filter(requestPickup=request_pickup).values_list('courier', flat=True)
    available_couriers = available_couriers.exclude(courier__in=couriers_in_decline_list)



    for courier in available_couriers:
        courier_location = courier.location
        pickup_location = request_pickup.pickup_location
        distance,time = get_longest_arrival_time(courier_location, pickup_location,"AIzaSyCfiGeaaLSDtDpepqYFmc-vjFXlFys8lXs")
        courier.distance_to_pickup = distance  # Add a temporary attribute for distance
    # Prioritize by distance to pickup location
    available_couriers = available_couriers.order_by('distance_to_pickup')

    # Prioritize by the number of pending pickups
    # Get a list of CourierAvailability objects
    courier_availability_list = available_couriers
    # Create a dictionary to store the count of RequestPickup for each user
    user_request_count = {}
    # Iterate over the CourierAvailability objects
    for courier_availability in courier_availability_list:
        user = courier_availability.courier
        # Filter RequestPickup objects related to the current CourierAvailability object
        request_pickups = RequestPickup.objects.filter(courier=user)
        # Count the RequestPickup objects for the current user
        request_count = request_pickups.count()
        user_request_count[user.username] = request_count

    courier_availability_list_sorted = sorted(
    courier_availability_list,
    key=lambda ca: user_request_count.get(ca.courier.username, 0),
    reverse=False
    )   
    sorted_ids = [ca.id for ca in courier_availability_list_sorted]
    q_objects = Q(id__in=sorted_ids)
    available_couriers = CourierAvailability.objects.filter(q_objects)


    # Prioritize by available space in the courier's car
    # available_couriers = available_couriers.annotate(
    #     car_space=F('courier__courier__car__capacity') - 
    #               ExpressionWrapper(F('courier__courier__requestpickup__volume'), 
    #                                 output_field=CharField())
    # ).filter(car_space__gte=V(str(request_pickup.volume)))

    # # Prioritize by courier's rating
    #courier_availability_list_sorted = courier_availability_list_sorted.order_by('-courier__courier__rating')

    if courier_availability_list_sorted:
        print("selected is: "+ str(courier_availability_list_sorted[0].courier))
        return courier_availability_list_sorted[0].courier  # Return the top-priority courier
    else:
        return None  # No available courier that meets the criteria
    






