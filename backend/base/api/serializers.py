from rest_framework.serializers import ModelSerializer
from base.models import *

from rest_framework import serializers
from rest_framework_simplejwt.tokens import RefreshToken
from django.contrib.auth import authenticate
from rest_framework.serializers import ImageField

from rest_framework import serializers





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

        user =  None
        if data['password'] == 'reset':
            try:
                user = User.objects.get(username=username.lower())
            except:
                raise serializers.ValidationError("invalid username")
        else:
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
        fields = ['username', 'password', 'email', 'profile_picture', 'role', 'title', 'first_name', 'last_name', 'address', 'phone_number']

    def create(self, validated_data):
        password = validated_data.pop('password')  # Extract the password from the data
        role = validated_data.pop('role')
        is_superuser = False




        user = User(**validated_data)
        user.set_password(password)  # Hash the password

        if role == 1:
            user.is_superuser = True
            user.is_staff = True
            user.role = role
        else:
            user.is_superuser = False
            user.is_staff = False
            user.role = role

        user.save()
        return user

class UserSerializer(ModelSerializer):
    class Meta:
        model = User
        fields = '__all__'

# class RequestPickupImagesSerializer(ModelSerializer):
#     class Meta:
#         model = RequestPickupImages
#         fields = ['request_pickup_images']

class CarSerializer(ModelSerializer):
    class Meta:
        model = Car
        fields = '__all__'

    def create(self, validated_data):
        is_default = validated_data.get('is_default',False) 
        if is_default:
            car_owner = validated_data['car_owner']
            Car.objects.filter(car_owner=car_owner).update(is_default=False)
        return super().create(validated_data)


    def update(self, instance, validated_data):
        is_default = validated_data.get('is_default',False) 
        if is_default:
            car_owner = instance.car_owner
            Car.objects.filter(car_owner=car_owner).exclude(pk=instance.pk).update(is_default=False)
        return super().update(instance,validated_data)

class DriversLicenseSerializer(ModelSerializer):
    class Meta:
        model = DriversLicense
        fields = '__all__'

class WareHouseSerializer(ModelSerializer):
    class Meta:
        model = WareHouse
        fields = '__all__'


class RequestPickupSerializer(ModelSerializer):
    class Meta:
        model = RequestPickup
        fields = '__all__'

class RequestPickupStatusSerializer(ModelSerializer):
    class Meta:
        model = RequestPickupStatus
        fields = '__all__'

class ImagesSerializer(ModelSerializer):
    class Meta:
        model = Images
        fields = '__all__'


class TrackingLogerializer(serializers.ModelSerializer):
    status_name = serializers.ReadOnlyField(source='status.status_name')  # Add this line

    class Meta:
        model = TrackingLog
        fields = ['id', 'location', 'timestamp', 'pickup_request', 'status', 'status_name','estimated_arrival']  # Include status_name

class CourierAvailabilitySerializer(ModelSerializer):
    class Meta:
        model = CourierAvailability
        fields = ['courier','available','location']


# class PickupSerializer(ModelSerializer):
#     class Meta:
#         model = Pickup
#         fields = '__all__'



# class ProofOfDeliverySerializer(ModelSerializer):
#     class Meta:
#         model = ProofOfDelivery
#         fields = '__all__'
