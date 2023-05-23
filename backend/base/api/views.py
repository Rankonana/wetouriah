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
        'GET /api/users/:id>'
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

