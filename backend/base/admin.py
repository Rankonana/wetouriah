from django.contrib import admin
from .models import *

admin.site.register(UserProfile)
admin.site.register(Car)
admin.site.register(WareHouse)
admin.site.register(RequestPickupImages)
admin.site.register(RequestPickup)
admin.site.register(Pickup)
admin.site.register(PickupMessage)
