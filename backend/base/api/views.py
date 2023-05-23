from rest_framework.decorators import api_view
from rest_framework.response import Response
from base.models import *
from .serializers import *
from rest_framework import status
from django.http import Http404
from rest_framework.decorators import api_view, parser_classes
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.response import Response
from rest_framework import status
from .serializers import *
import requests

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

@api_view(['GET'])
def getUsers(request):
    users = UserProfile.objects.all()
    serializer = UserSerializer(users, many=True)
    return Response(serializer.data)

@api_view(['GET'])
def getUser(request,pk):
    user = UserProfile.objects.get(id=pk)
    serializer = UserSerializer(user, many=False)
    return Response(serializer.data)

@api_view(['POST'])
def create_user(request):
    serializer = CreateUserSerializer(data=request.data)

    user = None

    if serializer.is_valid():
        username = serializer.validated_data['username']
        password = serializer.validated_data['password']
        user = User.objects.create_user(username=username, password=password)
    else:
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    data = request.data.copy()
    data['user'] = user.id
    userSerializer = UserSerializer(data=data)

    if userSerializer.is_valid():
        userSerializer.save()
        return Response(userSerializer.data,status=status.HTTP_201_CREATED)
    else:
        return Response(userSerializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['PUT'])
def update_user(request, user_id):
    try:
        user_profile = UserProfile.objects.get(user_id=user_id)
    except UserProfile.DoesNotExist:
        return Response({'error': 'User profile not found.'}, status=status.HTTP_404_NOT_FOUND)

    serializer = UserSerializer(user_profile, data=request.data, partial=True)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data)
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

#car start
@api_view(['GET'])
def getCars(request):
    cars = Car.objects.all()
    serializer = CarSerializer(cars, many=True)
    return Response(serializer.data)

@api_view(['GET'])
def getCar(request,pk):
    car = Car.objects.get(id=pk)
    serializer = Car(car, many=False)
    return Response(serializer.data)

@api_view(['POST', 'PUT'])
def car_detail(request):

    try:
        car = Car.objects.get(id=request.data.get('id'))
    except Car.DoesNotExist:
        car = None

    if request.method == 'POST':
        serializer = CarSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data,status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
    elif request.method == 'PUT':
        if car is None:
            return Response(status=status.HTTP_404_NOT_FOUND)
        serializer = CarSerializer(car, request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

#car end

#WareHouse start
@api_view(['GET'])
def getWareHouses(request):
    warehouse = WareHouse.objects.all()
    serializer = WareHouseSerializer(warehouse, many=True)
    return Response(serializer.data)

@api_view(['GET'])
def getWareHouse(request,pk):
    warehouse = WareHouse.objects.get(id=pk)
    serializer = WareHouseSerializer(warehouse, many=False)
    return Response(serializer.data)

@api_view(['POST', 'PUT'])
def warehouse_detail(request):

    try:
        warehouse = WareHouse.objects.get(id=request.data.get('id'))
    except WareHouse.DoesNotExist:
        warehouse = None

    if request.method == 'POST':
        serializer = WareHouseSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data,status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
    elif request.method == 'PUT':
        if warehouse is None:
            return Response(status=status.HTTP_404_NOT_FOUND)
        serializer = WareHouseSerializer(warehouse, request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

#WareHouse end

#RequestPickup start
@api_view(['GET'])
def getRequestPickups(request):
    requestpickup = RequestPickup.objects.all()
    serializer = RequestPickupSerializer(requestpickup, many=True)
    return Response(serializer.data)

@api_view(['GET'])
def getRequestPickup(request,pk):
    requestpickup = RequestPickup.objects.get(id=pk)
    serializer = RequestPickupSerializer(requestpickup, many=False)
    return Response(serializer.data)

@api_view(['POST', 'PUT'])
def requestpickup_detail(request):

    try:
        requestpickup = RequestPickup.objects.get(id=request.data.get('id'))
    except RequestPickup.DoesNotExist:
        requestpickup = None

    if request.method == 'POST':
        serializer = RequestPickupSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data,status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
    elif request.method == 'PUT':
        if requestpickup is None:
            return Response(status=status.HTTP_404_NOT_FOUND)
        serializer = RequestPickupSerializer(requestpickup, request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

#RequestPickup end


#Pickup start
@api_view(['GET'])
def getPickups(request):
    pickup = Pickup.objects.all()
    serializer = PickupSerializer(pickup, many=True)
    return Response(serializer.data)

@api_view(['GET'])
def getPickup(request,pk):
    pickup = Pickup.objects.get(id=pk)
    serializer = PickupSerializer(pickup, many=False)
    return Response(serializer.data)

@api_view(['POST', 'PUT'])
def pickup_detail(request):

    try:
        pickup = Pickup.objects.get(id=request.data.get('id'))
    except Pickup.DoesNotExist:
        pickup = None

    if request.method == 'POST':
        serializer = PickupSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data,status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
    elif request.method == 'PUT':
        if pickup is None:
            return Response(status=status.HTTP_404_NOT_FOUND)
        serializer = PickupSerializer(pickup, request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

#Pickup end


#PickupMessage start
@api_view(['GET'])
def getPickupMessages(request):
    pickupmessage = PickupMessage.objects.all()
    serializer = PickupMessageSerializer(pickupmessage, many=True)
    return Response(serializer.data)

@api_view(['GET'])
def getPickupMessage(request,pk):
    pickupmessage = PickupMessage.objects.get(id=pk)
    serializer = PickupMessageSerializer(pickupmessage, many=False)
    return Response(serializer.data)

@api_view(['POST'])
def pickup_message_detail(request):
    serializer = PickupMessageSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data,status=status.HTTP_201_CREATED)
    else:
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    

#PickupMessage end