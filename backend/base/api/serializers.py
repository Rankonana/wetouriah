from rest_framework.serializers import ModelSerializer
from base.models import *



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
