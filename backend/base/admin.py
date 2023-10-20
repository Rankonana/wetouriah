from django.contrib import admin
from .models import *

admin.site.register(User)
admin.site.register(Car)
admin.site.register(WareHouse)
admin.site.register(RequestPickup)
admin.site.register(DriversLicense) 
admin.site.register(RequestPickupStatus) 


admin.site.register(TrackingLog) 
admin.site.register(ImageStatus) 
admin.site.register(Images) 
admin.site.register(CourierAvailability) 
admin.site.register(DeclineList)


