from rest_framework.decorators import api_view
from rest_framework.response import Response
from base.models import *
from .serializers import *
from rest_framework import status
from django.http import Http404
from rest_framework.decorators import api_view, parser_classes
from rest_framework.response import Response
from rest_framework import status
from .serializers import *
from django.db.models import Q
import mailersend
from mailersend import emails
import random
from django.http import JsonResponse
import requests
from requests.auth import HTTPBasicAuth


import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

#
from rest_framework.permissions import IsAuthenticated,IsAdminUser
from rest_framework.decorators import api_view, permission_classes, authentication_classes

#

#
from rest_framework.views import APIView
from rest_framework.permissions import AllowAny, IsAuthenticated
#
import os
import requests


from dotenv import load_dotenv

load_dotenv()


###############

#from __future__ import print_function

import os.path

from google.auth.transport.requests import Request
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError

import base64
from email.message import EmailMessage

import google.auth
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError
############

#ref: https://python.plainenglish.io/role-based-authentication-and-authorization-with-djangorestframework-and-simplejwt-d9614d79995c


@api_view(['POST'])

def getTrackingLog(request):
  request_pickup = RequestPickup.objects.get(tracking_number=request.data.get('tracking_number'))
  pickup_request = TrackingLog.objects.filter(pickup_request=request_pickup.id).order_by('-timestamp')
  serializer = TrackingLogerializer(pickup_request, many=True)
  return Response(serializer.data)

@api_view(['POST'])

def getImages(request):
  request_pickup = RequestPickup.objects.get(tracking_number=request.data.get('tracking_number'))
  pickup_request = Images.objects.filter(pickup_request=request_pickup.id)
  serializer = ImagesSerializer(pickup_request, many=True)
  return Response(serializer.data)




@api_view(['POST'])
def login(request):
    serializer = UserLoginSerializer(data=request.data)

    if serializer.is_valid():
        status_code = status.HTTP_200_OK
        try:
            user_id = User.objects.get(username=serializer.data['username']).id
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'login  success',
                'access': serializer.data['access'],
                'refresh': serializer.data['refresh'],
                'authenticatedUser': {
                    'id': user_id,
                    'username': serializer.data['username'],
                    'role': serializer.data['role']
    
                }
            }
            return Response(response, status=status_code)

        except:
            status_code = status.HTTP_200_OK
            response = {
                'success': False,
                'statusCode': status_code,
                'message': 'Invalid username or password ',
                'access': "",
                'refresh': "",
                'authenticatedUser': {
                    'id': "",
                    'username': "",
                    'role': ""
    
                }
            }
            return Response(response, status=status_code)


    else:
        status_code = status.HTTP_200_OK
        response = {
            'success': False,
            'statusCode': status_code,
            'message': 'Invalid usernmae or password ',
            'access': "",
            'refresh': "",
            'authenticatedUser': {
                'id': "",
                'username': "",
                'role': ""
 
            }
        }
        return Response(response, status=status_code)
    
@api_view(['GET'])

def getRoutes(request):
    routes = [
        'GET /api',
        'GET /api/users',
        'GET /api/users/:id>',
        'POST /api/create-user/',
        'PUT /api/update-user/:id>/'
    ]

    return Response(routes)


@api_view(['POST'])
def getUsers(request):
    role = request.data.get('role')
    username = request.data.get('username')
    sort = request.data.get('sort')

    users = User.objects.all()

    #print("role" + role)
    if role:
        print(role)
        users = users.filter(role=role)

    if username:
        users = users.filter(username__icontains=username)

    if sort == 'new':
        users = users.order_by('-date_joined')

    serializer = UserSerializer(users, many=True)
    return Response(serializer.data, status=status.HTTP_200_OK)    

@api_view(['POST'])
# @permission_classes([IsAuthenticated])
def getUser(request):
    user = User.objects.get(id=request.data.get('id'))
    serializer = UserSerializer(user, many=False)
    return Response(serializer.data)

@api_view(['POST'])
def create_user(request):
    serializer = CreateUserSerializer(data=request.data)
    
    print(request.data)
    print(serializer.is_valid())
    #print(serializer)
    if serializer.is_valid():
        serializer.save()
        role = request.data.get('role')
        email = request.data.get('email')
        username = request.data.get('username')
        password = request.data.get('password') 
        title = request.data.get('title')
        first_name = request.data.get('first_name')
        print(role)
        if(role == "1" ):
            print("reached")
            subject = "New Account Created on Wetouriah"
            body = "Hello, "+ title + " " + first_name + "An account has been created on your behalf" +"\nYour credentials are:\n\nUsername: " + username + "\nPassword:  " +  password + "\n\n Login and change the password "
            emailresponse = sendemail(email,subject,body)
            print(emailresponse)

        status_code = status.HTTP_201_CREATED
        response = {
            'success': True,
            'statusCode': status_code,
            'message': 'User registered successfully',
        }

        return Response(response, status=status_code)
    else:
        status_code = status.HTTP_400_BAD_REQUEST
        response = {
            'success': True,
            'statusCode': status_code,
            'message': serializer.errors,
        }
        return Response(response, status=status_code)

@api_view(['PUT'])
def update_user(request):
    try:
        user_profile = User.objects.get(id=request.data.get('id'))

    except User.DoesNotExist:
        return Response({'error': 'User profile not found.'}, status=status.HTTP_404_NOT_FOUND)

    # Check if the new role is ADMIN (1) and update accordingly
    new_role = request.data.get('role')
    print(request.data)
    print(new_role)
    if new_role == '1':
        # Set the role to ADMIN (1) and also make the user a superuser and staff member
        user_profile.role = 1
        user_profile.is_superuser = True
        user_profile.is_staff = True
        print("test")
    else:
        # If the new role is not ADMIN, update the role field only
        user_profile.role = new_role
        user_profile.is_superuser = False
        user_profile.is_staff = False
        print("test2")

    serializer = UserSerializer(user_profile, data=request.data, partial=True)
    if serializer.is_valid():
        serializer.save()
        status_code = status.HTTP_201_CREATED
        response = {
            'success': True,
            'statusCode': status_code,
            'message': 'User updated successfully',
        }

        return Response(response, status=status_code)
    else:
        status_code = status.HTTP_400_BAD_REQUEST
        response = {
            'success': True,
            'statusCode': status_code,
            'message': serializer.errors,
        }
        return Response(response, status=status_code)                          

@api_view(['POST'])
def reset_password(request):
    try:
        user = User.objects.get(username=request.data.get('username').lower())
        new_password = request.data.get('password')
        print(new_password)
        user.set_password(new_password)
        user.save()
        status_code = status.HTTP_201_CREATED
        response = {
            'success': True,
            'statusCode': status_code,
            'message': 'password changed successfully',
        }
        return Response(response, status=status_code)
    except User.DoesNotExist:
        status_code = status.HTTP_404_NOT_FOUND
        response = {
            'success': True,
            'statusCode': status_code,
            'message': 'invalid username',
        }
        return Response(response, status=status_code)


@api_view(['POST'])
def sendPasswordResetCode(request):

    random_number = ''.join(random.choices('0123456789', k=5))
    user =  User.objects.get(username=request.data.get('username'))


    emailresponse = sendemail(str(user.email),"Reset Password","Hello " + user.username + " your reset code is : " + str(random_number))

    if emailresponse == "success":

        print(random_number)
        status_code = status.HTTP_201_CREATED
        response = {
                'success': True,
                'statusCode': status_code,
                'message': random_number,
            }
        return Response(response, status=status_code)
    else:
        status_code = status.HTTP_500_INTERNAL_SERVER_ERROR
        response = {
                'success': True,
                'statusCode': status_code,
                'message': 'there was an error sending email' + str(emailresponse),
            }
        return Response(response, status=status_code)







#car start
@api_view(['POST'])
def getCars(request):
    car_owner = request.data.get('driver','')
    cars = Car.objects.all()
    if car_owner != '':
        cars = cars.filter(Q(car_owner=car_owner))

    serializer = CarSerializer(cars, many=True)
    return Response(serializer.data)


@api_view(['POST'])
def getCar(request):
    car = Car.objects.get(id=request.data.get('id'))
    serializer = CarSerializer(car, many=False)
    return Response(serializer.data)


#
@api_view(['POST', 'PUT'])
def car_detail(request):
    


    if request.method == 'POST':
        print("POST")
        print(request.data)
        serializer = CarSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            status_code = status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'Vehicle added successfully',
            }
            return Response(response, status=status_code)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
    elif request.method == 'PUT':

        try:
            print("PUT1")
            print(request.data)
            car = Car.objects.get(id=request.data.get('id'))
            print("id")
            print(request.data.get('id'))
            print("car obj")
            print(car)
        except Car.DoesNotExist:
            car = None

        if car is None:
            return Response(status=status.HTTP_404_NOT_FOUND)
        serializer = CarSerializer(car, request.data)
        if serializer.is_valid():
            serializer.save()
            status_code = status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'Vehicle updated successfully',
            }
            return Response(response, status=status_code)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


#car end

#DriversLicense start
@api_view(['GET'])
def getDriversLicenses(request):
    driversLicenses = DriversLicense.objects.all()
    serializer = DriversLicenseSerializer(driversLicenses, many=True)
    return Response(serializer.data)

@api_view(['POST'])
def getDriversLicense(request):
    license_owner = DriversLicense.objects.get(license_owner=request.data.get('id'))
    serializer = DriversLicenseSerializer(license_owner, many=False)
    return Response(serializer.data)

@api_view(['POST', 'PUT'])
def driversLicense_detail(request):
    #print(request.data)


    if request.method == 'POST':
        print("On post")
        serializer = DriversLicenseSerializer(data=request.data)
        #print(serializer.is_valid())
        #print(serializer.errors)
        #print(serializer)

        if serializer.is_valid():
            serializer.save()
            status_code = status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'license added successfully',
            }
            return Response(response, status=status_code)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
    elif request.method == 'PUT':
        print("On put")
        try:
            license = DriversLicense.objects.get(license_owner=request.data.get('license_owner'))
        except DriversLicense.DoesNotExist:
            license = None

        if license is None:
            return Response(status=status.HTTP_404_NOT_FOUND)
        serializer = DriversLicenseSerializer(license, request.data)
        if serializer.is_valid():
            serializer.save()
            status_code = status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'License updated successfully',
            }
            return Response(response, status=status_code)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


#DriversLicense end


#WareHouse start
@api_view(['GET'])
def getWareHouses(request):
    warehouses = WareHouse.objects.all()
    serializer = WareHouseSerializer(warehouses, many=True)
    return Response(serializer.data)



@api_view(['POST'])
def getWareHouse(request):
    warehouse = WareHouse.objects.get(warehouse_owner=request.data.get('id'))
    serializer = WareHouseSerializer(warehouse, many=False)
    return Response(serializer.data)

@api_view(['POST', 'PUT'])
def warehouse_detail(request):
    print(request.data)
    try:
        warehouse = WareHouse.objects.get(id=request.data.get('id'))

    except WareHouse.DoesNotExist:
        warehouse = None

    if request.method == 'POST':
        serializer = WareHouseSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            status_code = status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'warehouse added successfully',
            }
            return Response(response, status=status_code)
        else:
            status_code = status.HTTP_400_BAD_REQUEST
            response = {
                'success': True,
                'statusCode': status_code,
                'message': serializer.errors,
            }
            return Response(response, status=status_code)
        
    elif request.method == 'PUT':
        if warehouse is None:
            return Response(status=status.HTTP_404_NOT_FOUND)
        serializer = WareHouseSerializer(warehouse, request.data)
        if serializer.is_valid():
            serializer.save()
            status_code = status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'warehouse updated successfully',
            }
            return Response(response, status=status_code)
        else:
            status_code = status.HTTP_400_BAD_REQUEST
            response = {
                'success': True,
                'statusCode': status_code,
                'message': serializer.errors,
            }
            return Response(response, status=status_code)

#WareHouse end

#RequestPickup start



@api_view(['POST'])
def getAllUserRequestPickups(request):
    print(request.data)
    customer = request.data.get('customer', '')

    requestpickups = RequestPickup.objects.all()

    if customer != '':
        print('not empty')
        requestpickups = requestpickups.filter(Q(customer=customer))

    requestpickups = requestpickups.order_by('-request_time')

    serializer = RequestPickupSerializer(requestpickups, many=True)
    return Response(serializer.data)

@api_view(['POST'])
def get_single_pickup(request):
    print(request.data)
    tracking_number = request.data.get('tracking_number', '')

    requestpickup = RequestPickup.objects.get(tracking_number = tracking_number)


    serializer = RequestPickupSerializer(requestpickup, many=False)
    return Response(serializer.data)

@api_view(['POST'])
def getAllDriverRequestPickups(request):
    if request.method == 'POST':
        user_id = request.data.get('driver')

        try:
            user = User.objects.get(id=user_id)

            # Filter request_pickups where the courier is the specified user
            # and the status is one of [1, 2, 3, 4, 5]
            request_pickups = RequestPickup.objects.filter(
                courier=user,
                status__in=[1,2,4,5,6,7,8,13,14,16]
            )
            
            serializer = RequestPickupSerializer(request_pickups, many=True)

            return Response(serializer.data)

        except User.DoesNotExist:
            return Response({'message': 'User not found'}, status=404)

    return Response({'message': 'Invalid request'}, status=400)

@api_view(['POST', 'PUT'])
def requestpickup_detail(request):
    #print(request.data)
    try:
        requestpickup = RequestPickup.objects.get(tracking_number=request.data.get('tracking_number'))
    except RequestPickup.DoesNotExist:
        requestpickup = None

    if request.method == 'POST':
        print("post")
        print(request.data)
        serializer = RequestPickupSerializer(data=request.data)
        if serializer.is_valid():
            requestpickup  = serializer.save()
            # Handle car images if provided
            images_data = request.data.getlist('image')
            image_status = ImageStatus.objects.get(id=4)
            if images_data:
                for image_data in images_data:
                    Images.objects.create(pickup_request=requestpickup, image=image_data, image_status=image_status)

            status_code =status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'pick request added successfully',
            }


            return Response(response, status_code)
        else:
            status_code =status.HTTP_400_BAD_REQUEST
            response = {
                'success': True,
                'statusCode': status_code,
                'message': serializer.errors,
            }
            return Response(response, status_code)
        
    elif request.method == 'PUT':
        print("put")
        print(request.data)
        if requestpickup is None:
            return Response(status=status.HTTP_404_NOT_FOUND)
        serializer = RequestPickupSerializer(requestpickup, request.data)
        if serializer.is_valid():
            requestpickup = serializer.save()
            # Handle car images if provided
            images_data = request.data.getlist('image')
            print('check img1')
            print(images_data)
            if images_data:
                print('check img2')
                print(images_data)
                image_status = ImageStatus.objects.get(id=4)
                for image_data in images_data:
                    Images.objects.create(pickup_request=requestpickup, image=image_data, image_status=image_status)


            status_code =status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'pick request updated successfully',
            }
            return Response(response, status_code)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

#Pickup start
@api_view(['POST'])
def getAllRequestPickupStatus(request):
    request_pickup_status = RequestPickupStatus.objects.all()
    serializer = RequestPickupStatusSerializer(request_pickup_status, many=True)
    return Response(serializer.data)


@api_view(['POST'])
def requestpickup_delete(request):
    #print(request.data)
    try:
        requestpickup = RequestPickup.objects.get(id=request.data.get('id'))
        requestpickup.delete()
        status_code =status.HTTP_200_OK
        response = {
                'success': True,
                'statusCode': status_code,
                'message': 'pick request deleted successfully',
            }
        return Response(response, status_code)
    except RequestPickup.DoesNotExist:
        requestpickup = None
        status_code =status.HTTP_400_BAD_REQUEST
        response = {
                'success': True,
                'statusCode': status_code,
                'message': 'pick request could not be deleted',
            }
        return Response(response, status_code)

#RequestPickup end






#Pickup end


@api_view(['POST'])
def pickupLocations(request):
    if request.method == 'POST':
        # Assuming you have a user_id or username for the specific user
        user_id = request.data.get('driver')  # Replace with your user identification method

        try:
            # Get the user based on user_id (you can use username or any other identifier)
            user = User.objects.get(id=user_id)

            # Get all the cars owned by the user
            cars_owned_by_user = Car.objects.filter(car_owner=user)

            # Get all RequestPickup objects where the car is owned by the user
            request_pickups = RequestPickup.objects.filter(car__in=cars_owned_by_user)

            # Get a list of unique pickup locations
            pickup_locations = request_pickups.values_list('pickup_location', flat=True).distinct()

            return Response(pickup_locations)

        except User.DoesNotExist:
            return Response({'message': 'User not found'}, status=404)

    return Response({'message': 'Invalid request'}, status=400)

@api_view(['POST'])
def dropoffLocations(request):
    if request.method == 'POST':
        # Assuming you have a user_id or username for the specific user
        user_id = request.data.get('driver')  # Replace with your user identification method

        try:
            # Get the user based on user_id (you can use username or any other identifier)
            user = User.objects.get(id=user_id)

            # Get all the cars owned by the user
            cars_owned_by_user = Car.objects.filter(car_owner=user)

            # Get all RequestPickup objects where the car is owned by the user
            request_pickups = RequestPickup.objects.filter(car__in=cars_owned_by_user)

            # Get a list of unique pickup locations
            pickup_locations = request_pickups.values_list('dropoff_location', flat=True).distinct()

            return Response(pickup_locations)

        except User.DoesNotExist:
            return Response({'message': 'User not found'}, status=404)

    return Response({'message': 'Invalid request'}, status=400)


@api_view(['POST'])
def pickup_and_droff_Locations(request):
    if request.method == 'POST':
        # Assuming you have a user_id or username for the specific user
        user_id = request.data.get('driver')  # Replace with your user identification method

        try:
            # Get the user based on user_id (you can use username or any other identifier)
            user = User.objects.get(id=user_id)

            # Get all the cars owned by the user
            cars_owned_by_user = Car.objects.filter(car_owner=user)

            # Get all RequestPickup objects where the car is owned by the user
            request_pickups = RequestPickup.objects.filter(car__in=cars_owned_by_user)

            # Create a list of dictionaries with tracking number, status, and location
            locations = []
            for pickup in request_pickups:
                locations.append({
                    "tracking_number": pickup.tracking_number,
                    "status": pickup.status.status_name,  # Assuming 'status' is a related field
                    "location": pickup.pickup_location
                })

            return Response(locations)

        except User.DoesNotExist:
            return Response({'message': 'User not found'}, status=404)

    return Response({'message': 'Invalid request'}, status=400)


     
@api_view(['POST'])
def courier_availability(request):
    courier = User.objects.get(id=request.data.get('courier'))
    print(request.data)
    print(request.data.get('location'))
    try:
        courier_availability = CourierAvailability.objects.get(courier=courier)
        # If the DriverAvailability object exists, update it
        courier_availability.available = request.data.get('available', courier_availability.available)
        courier_availability.location = request.data.get('location', courier_availability.location)
        courier_availability.save()
        status_code =status.HTTP_200_OK
        response = {
                'success': True,
                'statusCode': status_code,
                'message': 'availability set',
            }
        return Response(response, status_code)
    except CourierAvailability.DoesNotExist:
        # If the DriverAvailability object does not exist, create a new one
        data = {
            'courier': courier,
            'available': request.data.get('available', False),
            'location': request.data.get('location', None)
        }
        courier_availability = CourierAvailability(**data)
        courier_availability.save()
        status_code =status.HTTP_200_OK
        response = {
                'success': True,
                'statusCode': status_code,
                'message': 'availability set',
            }
        return Response(response, status_code)


@api_view(['POST'])
def accept_decline_parcel(request):
    print(request.data)
    courier = User.objects.get(id=request.data.get('courier'))
    requestPickup = RequestPickup.objects.get(id=request.data.get('requestPickup'))
    status = request.data.get('status')

    if(status =='decline'):
        declineList = DeclineList.objects.create(courier=courier,requestPickup=requestPickup)
        declineList.save()
        #requestPickup.courier = None
        #requestPickup.save()
        print('decline')
        status_code =status.HTTP_200_OK
        response = {
                'success': True,
                'statusCode': status_code,
                'message': 'job declined',
            }
        return Response(response, status_code)

    else:
        job_status = RequestPickupStatus.objects.get(status_name='Driver-assigned')
        requestPickup.status = job_status
        requestPickup.save()
        print('accepted')
        status_code =status.HTTP_200_OK
        response = {
                'success': True,
                'statusCode': status_code,
                'message': 'job accepted',
            }
        return Response(response, status_code)


@api_view(['POST'])
def check_job_offers(request):
    courier_id = request.data.get('courier')  # Get courier_id from request data

    # Find a RequestPickup with the specified courier_id, status_id=1, and not in DeclineList
    try:
        request_pickup = RequestPickup.objects.filter(
            courier_id=courier_id,
            status_id=1
        ).exclude(
            id__in=DeclineList.objects.filter(courier_id=courier_id).values('requestPickup_id')
        ).first()

        if request_pickup is not None:
            serializer = RequestPickupSerializer(request_pickup)
            return Response(serializer.data)
        else:
            return Response({'message': 'No eligible RequestPickup found for the courier.'}, status=404)
    except RequestPickup.DoesNotExist:
        return Response({'message': 'No eligible RequestPickup found for the courier.'}, status=404)

@api_view(['POST'])
def get_availabilty_status(request):
    courier = request.data.get('courier')

    try:
        courier_availability = CourierAvailability.objects.get(courier=courier)
        serializer = CourierAvailabilitySerializer(courier_availability)
        
        availability_status = serializer.data.get('available')  # Get the availability status
        
        response = {
            'success': True,
            'statusCode': 200,  # You can set the status code as needed
            'message': availability_status,
        }
        return Response(response, 200)  # You can set the status code as needed
    except CourierAvailability.DoesNotExist:
        response = {
            'success': False,
            'statusCode': 404,
            'message': False,
        }
        return Response(response, 404)

@api_view(['POST'])
def get_distance_andTime(request):
    startLocation  = request.data.get('startLocation')
    endLocation = request.data.get('endLocation')


    try:
        distance,time = get_longest_arrival_time(startLocation, endLocation,"AIzaSyCfiGeaaLSDtDpepqYFmc-vjFXlFys8lXs")
        
        message = "distance:"+ str(distance) + ",time:" + str(time)
        response = {
            'success': True,
            'statusCode': 200,  # You can set the status code as needed
            'message': message,
        }
        print()
        print("startLocation: "+ startLocation + " , "+ "endLocation: "+  endLocation + " " + message)
        print()
        return Response(response, 200)  # You can set the status code as needed
    except :
        response = {
            'success': False,
            'statusCode': 404,
            'message': "distance: "+"0" + ",time: " + "0",
        }
        return Response(response, 404)

@api_view(['POST'])
def sendSMS(request):

    recipient_number= request.data.get('recipient_number')
    message= request.data.get('message')
    from_warehouse= request.data.get('from_warehouse')
    parcel_id= request.data.get('parcel_id')


    if from_warehouse == "yes":
        requestPickup = RequestPickup.objets.get(id=parcel_id)
        recipient_number = requestPickup.customer.phone_number



    print(request.data)
    print(request.data.get('warehouse'))


    smsresponse = fsendSMS(recipient_number,message) 
    if smsresponse == "success":

        print(message)
        status_code = status.HTTP_201_CREATED
        response = {
                'success': True,
                'statusCode': status_code,
                'message': "Message sent",
            }
        return Response(response, status=status_code)
    else:
        status_code = status.HTTP_500_INTERNAL_SERVER_ERROR
        print(smsresponse)
        response = {
                'success': True,
                'statusCode': status_code,
                'message': 'there was an error sending sms',
            }
        return Response(response, status=status_code)


@api_view(['POST'])
def check_username_email(request):
    username= request.data.get('username').lower()
    email= request.data.get('email').lower()


    if username != "":
            
            try:
                user = User.objects.get(username=username)
                status_code = status.HTTP_201_CREATED
                response = {
                    'success': True,
                    'statusCode': status_code,
                    'message': 'username is taken',
                }
                return Response(response, status=status_code)
            except User.DoesNotExist:
                status_code = status.HTTP_404_NOT_FOUND
                response = {
                    'success': True,
                    'statusCode': status_code,
                    'message': '',
                }
                return Response(response, status=status_code)   
    if email != "":
        try:
            user = User.objects.get(email=email)
            status_code = status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'email already exist',
            }
            return Response(response, status=status_code)
        except User.DoesNotExist:
            status_code = status.HTTP_404_NOT_FOUND
            response = {
                'success': True,
                'statusCode': status_code,
                'message': '',
            }
            return Response(response, status=status_code)  
    
    




def sendemail(recipient,subject,body):
    
    SCOPES = ['https://www.googleapis.com/auth/gmail.readonly','https://www.googleapis.com/auth/gmail.send']

    #
    """Shows basic usage of the Gmail API.
    Lists the user's Gmail labels.
    """
    creds = None
    # The file token.json stores the user's access and refresh tokens, and is
    # created automatically when the authorization flow completes for the first
    # time.
    
    if os.path.exists('token.json'):
        print("no creds")
        creds = Credentials.from_authorized_user_file('token.json', SCOPES)
    # If there are no (valid) credentials available, let the user log in.
    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
        else:
            flow = InstalledAppFlow.from_client_secrets_file(
                'credentials.json', SCOPES)
            creds = flow.run_local_server(port=0)
        # Save the credentials for the next run
        with open('token.json', 'w') as token:
            token.write(creds.to_json())

    try:
        service = build('gmail', 'v1', credentials=creds)
        message = EmailMessage()

        message.set_content(body)
    

        message['To'] = recipient
        message['From'] = 'rankonanamokoena@gmail.com'
        message['Subject'] = subject

        # encoded message
        encoded_message = base64.urlsafe_b64encode(message.as_bytes()) \
            .decode()

        create_message = {
            'raw': encoded_message
        }
        # pylint: disable=E1101
        send_message = (service.users().messages().send
                        (userId="me", body=create_message).execute())
        #print(F'Message Id: {send_message["id"]}')

        return "success"
    except HttpError as error:
        return str(error)

def fsendSMS(recipient_number,message):

    apiKey = '7a53cdf3-85aa-4348-8a78-865ab8d295a0'
    apiSecret = 'bf7354fb-9b2b-434a-b67b-ee6dd7b2dec5'

    basic = HTTPBasicAuth(apiKey, apiSecret)

    sendRequest = {
        "messages": [{"content": message, "destination": recipient_number}]
    }

    try:
        sendResponse = requests.post("https://rest.smsportal.com/bulkmessages",
                                    auth=basic,
                                    json=sendRequest)

        if sendResponse.status_code == 200:
            print("success:")
            print(sendResponse.json())
            return str("success")
        else:
            print("Failure:")
            print(sendResponse.json())
            return str("failure to send sms")
    except Exception as e:
        print(e)
        return str(e)