from rest_framework.serializers import ModelSerializer
from base.models import *

from rest_framework import serializers
from rest_framework_simplejwt.tokens import RefreshToken
from django.contrib.auth import authenticate



class UserLoginSerializer(serializers.Serializer):
    username = serializers.CharField()
    password = serializers.CharField(max_length=128, write_only=True)
    access = serializers.CharField(read_only=True)
    refresh = serializers.CharField(read_only=True)
    role = serializers.CharField(read_only=True)

    def create(self, validated_date):
        pass

    def update(self, instance, validated_data):
        pass

    def validate(self, data):
        username = data['username']
        password = data['password']
        user = authenticate(username=username, password=password)

        if user is None:
            raise serializers.ValidationError("Invalid login credentials")
        
        

        try:
            refresh = RefreshToken.for_user(user)
            refresh_token = str(refresh)
            access_token = str(refresh.access_token)

            # update_last_login(None, user)
            # user_logged_in(user)

            validation = {
                'access': access_token,
                'refresh': refresh_token,
                'username': user.username,
                'role': user.role,
            }

            return validation
        except User.DoesNotExist:
            raise serializers.ValidationError("Invalid login credentials")


class CreateUserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['username', 'password', 'email', 'profile_picture', 'role', 'title', 'firstname', 'lastname', 'address', 'phone_number']

    def create(self, validated_data):
        password = validated_data.pop('password')  # Extract the password from the data
        user = User(**validated_data)
        user.set_password(password)  # Hash the password
        user.save()
        return user

class CarSerializer(ModelSerializer):
    class Meta:
        model = Car
        fields = '__all__'

class WareHouseSerializer(ModelSerializer):
    class Meta:
        model = WareHouse
        fields = '__all__'

class RequestPickupSerializer(ModelSerializer):
    class Meta:
        model = RequestPickup
        fields = '__all__'

class PickupSerializer(ModelSerializer):
    class Meta:
        model = Pickup
        fields = '__all__'

class PickupMessageSerializer(ModelSerializer):
    class Meta:
        model = PickupMessage
        fields = '__all__'
