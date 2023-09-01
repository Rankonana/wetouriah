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

#
from rest_framework.permissions import IsAuthenticated,IsAdminUser
from rest_framework.decorators import api_view, permission_classes, authentication_classes

#

#
from rest_framework.views import APIView
from rest_framework.permissions import AllowAny, IsAuthenticated
#

#ref: https://python.plainenglish.io/role-based-authentication-and-authorization-with-djangorestframework-and-simplejwt-d9614d79995c
@api_view(['POST'])
def login(request):
    serializer = UserLoginSerializer(data=request.data)

    if serializer.is_valid():
        status_code = status.HTTP_200_OK
        user_id = User.objects.get(username=serializer.data['username']).id
        response = {
            'success': True,
            'statusCode': status_code,
            'message': 'User logged in successfully',
            'access': serializer.data['access'],
            'refresh': serializer.data['refresh'],
            'authenticatedUser': {
                'id': user_id,
                'username': serializer.data['username'],
                'role': serializer.data['role']
 
            }
        }

        return Response(response, status=status_code)
    else:
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
@api_view(['GET'])
#@permission_classes([IsAuthenticated])
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
    role = request.query_params.get('role')
    username = request.query_params.get('username')
    sort = request.query_params.get('sort')

    users = User.objects.all()

    if role:
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
#@permission_classes([IsAuthenticated])
def create_user(request):
    serializer = CreateUserSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
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
#@permission_classes([IsAuthenticated])
def update_user(request):
    try:
        user_profile = User.objects.get(id=request.data.get('id'))

    except User.DoesNotExist:
        return Response({'error': 'User profile not found.'}, status=status.HTTP_404_NOT_FOUND)

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
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


#car start
@api_view(['GET'])
#@permission_classes([IsAuthenticated])
def getCars(request):
    cars = Car.objects.all()
    serializer = CarSerializer(cars, many=True)
    return Response(serializer.data)


@api_view(['POST'])
#@permission_classes([IsAuthenticated])
def getCar(request):
    car = Car.objects.get(car_owner=request.data.get('id'))
    serializer = CarSerializer(car, many=False)
    return Response(serializer.data)


#
@api_view(['POST', 'PUT'])
#@permission_classes([IsAuthenticated])
def car_detail(request):
    print(request.data)


    if request.method == 'POST':
        serializer = CarSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            status_code = status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'Car added successfully',
            }
            return Response(response, status=status_code)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
    elif request.method == 'PUT':
        try:
            car = Car.objects.get(car_owner=request.data.get('car_owner'))
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
                'message': 'Car updated successfully',
            }
            return Response(response, status=status_code)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


#car end

#DriversLicense start
@api_view(['GET'])
#@permission_classes([IsAuthenticated])
def getDriversLicenses(request):
    driversLicenses = DriversLicense.objects.all()
    serializer = DriversLicenseSerializer(driversLicenses, many=True)
    return Response(serializer.data)

@api_view(['POST'])
#@permission_classes([IsAuthenticated])
def getDriversLicense(request):
    license_owner = DriversLicense.objects.get(license_owner=request.data.get('id'))
    serializer = DriversLicenseSerializer(license_owner, many=False)
    return Response(serializer.data)

@api_view(['POST', 'PUT'])
#@permission_classes([IsAuthenticated])
def driversLicense_detail(request):
    print(request.data)


    if request.method == 'POST':
        serializer = DriversLicenseSerializer(data=request.data)
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
#@permission_classes([IsAuthenticated])
def searchWareHouses(request):
    # address = request.data.get('address', '')
    address = request.GET.get('address', '')
    warehouses = WareHouse.objects.all()

    if address != '':
        warehouses = warehouses.filter(
            Q(address__icontains = address)
        )
    serializer = WareHouseSerializer(warehouses, many=True)
    return Response(serializer.data)

#WareHouse start
@api_view(['GET'])
#@permission_classes([IsAuthenticated])
def getWareHouses(request):
    warehouses = WareHouse.objects.all()
    serializer = WareHouseSerializer(warehouses, many=True)
    return Response(serializer.data)

# @api_view(['GET'])
# #@permission_classes([IsAuthenticated])
# def getWareHouses(request):

#     print(request.data)
#     address = request.data.get('address', '')
#     warehouses = []
#     warehouses = WareHouse.objects.all()

#     if address != '':
#         warehouses = warehouses.filter(
#             Q(address = address)
#             )
    

#     # Iterate over each book and print its title and authors
#     # for requestp in requestpickups:
#     #     images = requestp.images.all()
#     #     for image in images:
#     #         print(image)

#     serializer = WareHouseSerializer(address, many=True)
#     return Response(serializer.data)


@api_view(['POST'])
#@permission_classes([IsAuthenticated])
def getWareHouse(request):
    warehouse = WareHouse.objects.get(warehouse_owner=request.data.get('id'))
    serializer = WareHouseSerializer(warehouse, many=False)
    return Response(serializer.data)

@api_view(['POST', 'PUT'])
#@permission_classes([IsAuthenticated])
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

@api_view(['GET'])
#@permission_classes([IsAuthenticated])
def getImage(request):
    image = RequestPickupImages.objects.get(id =request.data.get('id'))
    serializer = RequestPickupImagesSerializer(image, many=False)
    return Response(serializer.data)

@api_view(['POST'])
def getAllUserRequestPickups(request):
    print(request.data)
    customer = request.data.get('customer', '')

    requestpickups = RequestPickup.objects.all()

    if customer != '':
        requestpickups = requestpickups.filter(Q(customer=customer))

    #requestpickups = requestpickups.order_by('-updated')  #Apply order_by() on the queryset

    serializer = RequestPickupSerializer(requestpickups, many=True)
    return Response(serializer.data)


@api_view(['POST'])
def getUnratedUserRequestPickups(request):
    customer = request.data.get('customer', '')

    # Retrieve all unrated RequestPickup objects

    requestpickups = RequestPickup.objects.filter(pickup__is_delivered=True, pickup__rating=0)

    if customer != '':
        requestpickups = requestpickups.filter(customer=customer)

    requestpickups = requestpickups.order_by('-updated')  # Apply ordering here

    serializer = RequestPickupSerializer(requestpickups, many=True)
    return Response(serializer.data)


@api_view(['POST'])
def getUndeliveredUserRequestPickups(request):
    customer = request.data.get('customer', '')
    
    # Retrieve all undelivered RequestPickup objects
    requestpickups = RequestPickup.objects.filter(pickup__is_delivered=False).order_by('-updated')

    if customer != '':
        requestpickups = requestpickups.filter(is_picked=True, customer=customer)

    serializer = RequestPickupSerializer(requestpickups, many=True)
    return Response(serializer.data)


@api_view(['POST'])
def check_just_delivered_request_pickups(request):
    user_id = request.data.get('user_id', '')  # Assuming you pass the user ID of the car owner
    try:
        car_owner = User.objects.get(id=user_id)
        # Retrieve the unpicked RequestPickup objects for the car
        try:
            unpicked_request_pickups = RequestPickup.objects.get(is_picked=False,pickup__car__car_owner=car_owner)
        except RequestPickup.DoesNotExist:
            return Response("Request Pickup not found", status=status.HTTP_404_NOT_FOUND)
        try:
            serializer = RequestPickupSerializer(unpicked_request_pickups, many=False)
            return Response(serializer.data)
        except RequestPickup.DoesNotExist:
            return Response("Request Pickup not found", status=status.HTTP_404_NOT_FOUND)
    except User.DoesNotExist:
        return Response("Car owner not found", status=status.HTTP_404_NOT_FOUND)





@api_view(['POST'])
def getDriverUndeliveredRequestPickups(request):
    user_id = request.data.get('user_id', '')  # Assuming you pass the user ID of the car owner

    try:
        car_owner = User.objects.get(id=user_id)
    except User.DoesNotExist:
        return Response("Car owner not found", status=status.HTTP_404_NOT_FOUND)

    # Retrieve all undelivered RequestPickup objects for the car owner's cars
    requestpickups = RequestPickup.objects.filter(
        is_picked = True,
        pickup__is_delivered=False,
        pickup__car__car_owner=car_owner
    ).order_by('-updated')

    serializer = RequestPickupSerializer(requestpickups, many=True)
    return Response(serializer.data)

@api_view(['POST'])
def getDriverRatedRequestPickups(request):
    user_id = request.data.get('user_id', '')  # Assuming you pass the user ID of the car owner

    try:
        car_owner = User.objects.get(id=user_id)
    except User.DoesNotExist:
        return Response("Car owner not found", status=status.HTTP_404_NOT_FOUND)

    # Retrieve all undelivered and unrated RequestPickup objects for the car owner's cars
    requestpickups = RequestPickup.objects.filter(
        pickup__is_delivered=True,
        pickup__car__car_owner=car_owner,
        pickup__rating__gt=0
    ).order_by('-updated')

    serializer = RequestPickupSerializer(requestpickups, many=True)
    return Response(serializer.data)

@api_view(['POST'])
def getAllDriverRequestPickups(request):
    user_id = request.data.get('user_id', '')  # Assuming you pass the user ID of the car owner

    try:
        car_owner = User.objects.get(id=user_id)
    except User.DoesNotExist:
        return Response("Car owner not found", status=status.HTTP_404_NOT_FOUND)

    # Retrieve all undelivered and unrated RequestPickup objects for the car owner's cars
    requestpickups = RequestPickup.objects.filter(
        pickup__car__car_owner=car_owner,
        pickup__is_delivered=True

    ).order_by('-updated')

    serializer = RequestPickupSerializer(requestpickups, many=True)
    return Response(serializer.data)

@api_view(['POST'])
def check_unpicked_request_pickups(request):
    user_id = request.data.get('user_id', '')  # Assuming you pass the user ID of the car owner
    try:
        car_owner = User.objects.get(id=user_id)
        # Retrieve the unpicked RequestPickup objects for the car
        try:
            unpicked_request_pickups = RequestPickup.objects.get(is_picked=False,pickup__car__car_owner=car_owner)
        except RequestPickup.DoesNotExist:
            return Response("Request Pickup not found", status=status.HTTP_404_NOT_FOUND)
        try:
            serializer = RequestPickupSerializer(unpicked_request_pickups, many=False)
            return Response(serializer.data)
        except RequestPickup.DoesNotExist:
            return Response("Request Pickup not found", status=status.HTTP_404_NOT_FOUND)
    except User.DoesNotExist:
        return Response("Car owner not found", status=status.HTTP_404_NOT_FOUND)




@api_view(['POST'])
def acceptDeclineJob(request):
    requestPickup_id = request.data.get('requestPickup_id', '')
    accept_decline = request.data.get('accept_decline', '')

    try:
        requestPickup = RequestPickup.objects.get(id=requestPickup_id)
        setattr(requestPickup, "is_picked", accept_decline)
        requestPickup.save()
        status_code = status.HTTP_201_CREATED
        response = ""
        if accept_decline == "True":
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'job accepted',
            }
        else:
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'job declined',
            }

        return Response(response, status=status_code)
            
    except RequestPickup.DoesNotExist:
        status_code = status.HTTP_400_BAD_REQUEST
        response = {
                'success': True,
                'statusCode': status_code,
                'message': serializer.errors,
                }
        return Response(response, status=status_code)


@api_view(['POST'])
def add_proof_of_delivery_image(request):
    serializer = ProofOfDeliverySerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        status_code = status.HTTP_201_CREATED
        response = {
                'success': True,
                'statusCode': status_code,
                'message': 'image added',
            }
        return Response(response, status_code)
    else:
        return Response(serializer.errors, status=400)

@api_view(['POST'])
def mark_request_pickup_delivered(request):
    request_pickup_id = request.data.get('request_pickup_id', '')
    try:
        request_pickup = RequestPickup.objects.get(id=request_pickup_id)
        pickup = Pickup.objects.get(request_pickup=request_pickup)
        setattr(pickup, "is_delivered", "True")
        pickup.save()
        status_code = status.HTTP_201_CREATED
        response = {
                'success': True,
                'statusCode': status_code,
                'message': 'delivered',
            }
        return Response(response, status_code)

    except RequestPickup.DoesNotExist:
        status_code = status.HTTP_400_BAD_REQUEST
        response = {
                'success': True,
                'statusCode': status_code,
                'message': serializer.errors,
                }
        return Response(response, status=status_code)


@api_view(['POST'])
def getDeliveryImagesAndRating(request):
    request_pickup_id = request.data.get('pickup', '')
    try:
        pickup = RequestPickup.objects.get(id=request_pickup_id)
    except RequestPickup.DoesNotExist:
        return Response("RequestPickup not found", status=status.HTTP_404_NOT_FOUND)

    images = ProofOfDelivery.objects.filter(pickup=pickup)
    image_urls = [request.build_absolute_uri(image.image.url) for image in images]
    rating = Pickup.objects.get(request_pickup=request_pickup_id).rating

    response_data = {
        'images': image_urls,
        'rating': rating
    }
    # response_data = {
    #     'images': "test",
    #     'rating': "test"
    # }
    
    return Response(response_data)


@api_view(['GET'])
#@permission_classes([IsAuthenticated])
def getRequestPickup(request,pk):
    requestpickup = RequestPickup.objects.get(id=pk)
    serializer = RequestPickupSerializer(requestpickup, many=False)
    return Response(serializer.data)

@api_view(['POST', 'PUT'])
#@permission_classes([IsAuthenticated])
def requestpickup_detail(request):
    try:
        requestpickup = RequestPickup.objects.get(id=request.data.get('id'))
    except RequestPickup.DoesNotExist:
        requestpickup = None

    if request.method == 'POST':
        serializer = RequestPickupSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
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
        if requestpickup is None:
            return Response(status=status.HTTP_404_NOT_FOUND)
        serializer = RequestPickupSerializer(requestpickup, request.data)
        if serializer.is_valid():
            serializer.save()
            status_code =status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'pick request updated successfully',
            }
            return Response(response, status_code)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

#RequestPickup end


#Pickup start
@api_view(['GET'])
#@permission_classes([IsAuthenticated])
def getPickups(request):
    pickup = Pickup.objects.all()
    serializer = PickupSerializer(pickup, many=True)
    return Response(serializer.data)

@api_view(['POST'])
#@permission_classes([IsAuthenticated])
def getPickupIDfromRequestPickupID(request):
    pickup = Pickup.objects.get(request_pickup=request.data.get('request_pickup'))
    serializer = PickupSerializer(pickup, many=False)
    return Response(serializer.data)

@api_view(['GET'])
#@permission_classes([IsAuthenticated])
def getPickup(request,pk):
    pickup = Pickup.objects.get(id=pk)
    serializer = PickupSerializer(pickup, many=False)
    return Response(serializer.data)

@api_view(['POST', 'PUT'])
#@permission_classes([IsAuthenticated])
def pickup_detail(request):

    try:
        pickup = Pickup.objects.get(request_pickup=request.data.get('request_pickup'))
    except Pickup.DoesNotExist:
        pickup = None

    if request.method == 'POST':
        serializer = PickupSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            status_code =status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'pick  added successfully',
            }
            return Response(response, status_code)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
    elif request.method == 'PUT':
        if pickup is None:
            return Response(status=status.HTTP_404_NOT_FOUND)
        serializer = PickupSerializer(pickup, request.data)
        if serializer.is_valid():
            serializer.save()
            status_code =status.HTTP_201_CREATED
            response = {
                'success': True,
                'statusCode': status_code,
                'message': 'pickup  updated successfully',
            }
            return Response(response, status_code)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

#Pickup end




@api_view(['POST'])
#@permission_classes([IsAuthenticated])
def trackParcel(request):
    pickup = Pickup.objects.get(id=request.data.get('id'))
    serializer = PickupSerializer(pickup, many=False)
    return Response(serializer.data)












