from rest_framework.serializers import ModelSerializer
from base.models import *

from rest_framework import serializers
from rest_framework_simplejwt.tokens import RefreshToken
from django.contrib.auth import authenticate

from django.contrib.auth.models import User



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
                # 'role': user.role,
            }

            return validation
        except User.DoesNotExist:
            raise serializers.ValidationError("Invalid login credentials")


class CreateUserSerializer(ModelSerializer):
    class Meta:
        model = User
        fields = ['username','password']
        
class UserSerializer(ModelSerializer):
    class Meta:
        model = UserProfile
        fields = '__all__'

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
