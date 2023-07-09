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
    print(request)
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
#@permission_classes([IsAuthenticated])
def getUsers(request):
    print("getting users")
    users = User.objects.all()
    serializer = UserSerializer(users, many=True)
    return Response(serializer.data)

@api_view(['POST'])
# @permission_classes([IsAuthenticated])
def getUser(request):
    user = User.objects.get(id=request.data.get('id'))
    serializer = UserSerializer(user, many=False)
    return Response(serializer.data)

@api_view(['POST'])
#@permission_classes([IsAuthenticated])
def create_user(request):
    print(request.data)
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


@api_view(['POST', 'PUT'])
#@permission_classes([IsAuthenticated])
def car_detail(request):
    print(request.data)

    try:
        car = Car.objects.get(car_owner=request.data.get('car_owner'))
    except Car.DoesNotExist:
        car = None

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

#WareHouse start
@api_view(['GET'])
#@permission_classes([IsAuthenticated])
def searchWareHouses(request):
    print(request.data)
    # address = request.data.get('address', '')
    address = request.GET.get('address', '')
    print(address)
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
    print(request.data)
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
        print("Exist")

    except WareHouse.DoesNotExist:
        warehouse = None

    if request.method == 'POST':
        print(request.data)
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

    requestpickups = requestpickups.order_by('-updated')  # Apply order_by() on the queryset

    serializer = RequestPickupSerializer(requestpickups, many=True)
    return Response(serializer.data)


@api_view(['POST'])
def getUnratedUserRequestPickups(request):
    customer = request.data.get('customer', '')

    # Retrieve all unrated RequestPickup objects
    requestpickups = RequestPickup.objects.filter(pickup__rating=None)

    if customer != '':
        requestpickups = requestpickups.filter(customer=customer)

    serializer = RequestPickupSerializer(requestpickups, many=True).order_by('-updated')
    return Response(serializer.data)

@api_view(['POST'])
def getUndeliveredUserRequestPickups(request):
    print("undelivred")
    print(request.data.get('customer'))
    customer = request.data.get('customer', '')
    
    # Retrieve all undelivered RequestPickup objects
    requestpickups = RequestPickup.objects.filter(pickup__is_delivered=False).order_by('-updated')

    if customer != '':
        requestpickups = requestpickups.filter(customer=customer)

    serializer = RequestPickupSerializer(requestpickups, many=True)
    return Response(serializer.data)


@api_view(['GET'])
#@permission_classes([IsAuthenticated])
def getRequestPickup(request,pk):
    requestpickup = RequestPickup.objects.get(id=pk)
    serializer = RequestPickupSerializer(requestpickup, many=False)
    return Response(serializer.data)

@api_view(['POST', 'PUT'])
#@permission_classes([IsAuthenticated])
def requestpickup_detail(request):
    print(request.data)
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


# #PickupMessage start
# @api_view(['GET'])
# #@permission_classes([IsAuthenticated])
# def getPickupMessages(request):
#     pickupmessage = PickupMessage.objects.all()
#     serializer = PickupMessageSerializer(pickupmessage, many=True)
#     return Response(serializer.data)

@api_view(['POST'])
#@permission_classes([IsAuthenticated])
def getPickupMessages(request):
    pickup = request.data.get('pickup')
    pickupmessages = PickupMessage.objects.all()
    pickupmessages = pickupmessages.filter(
        Q(pickup = pickup)
    )
    serializer = PickupMessageSerializer(pickupmessages, many=True)
    return Response(serializer.data)

@api_view(['POST'])
#@permission_classes([IsAuthenticated])
def pickup_message_detail(request):
    serializer = PickupMessageSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        status_code =status.HTTP_201_CREATED
        response = {
                'success': True,
                'statusCode': status_code,
                'message': 'Message Sent',
            }
        return Response(response,status=status.HTTP_201_CREATED)
    else:
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    

#PickupMessage end


@api_view(['POST'])
#@permission_classes([IsAuthenticated])
def trackParcel(request):
    pickup = Pickup.objects.get(id=request.data.get('id'))
    serializer = PickupSerializer(pickup, many=False)
    return Response(serializer.data)












