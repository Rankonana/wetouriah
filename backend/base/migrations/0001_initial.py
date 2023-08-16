# Generated by Django 4.2.1 on 2023-07-28 07:28

from django.conf import settings
import django.contrib.auth.models
import django.contrib.auth.validators
from django.db import migrations, models
import django.db.models.deletion
import django.utils.timezone


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('auth', '0012_alter_user_first_name_max_length'),
    ]

    operations = [
        migrations.CreateModel(
            name='User',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('password', models.CharField(max_length=128, verbose_name='password')),
                ('last_login', models.DateTimeField(blank=True, null=True, verbose_name='last login')),
                ('is_superuser', models.BooleanField(default=False, help_text='Designates that this user has all permissions without explicitly assigning them.', verbose_name='superuser status')),
                ('username', models.CharField(error_messages={'unique': 'A user with that username already exists.'}, help_text='Required. 150 characters or fewer. Letters, digits and @/./+/-/_ only.', max_length=150, unique=True, validators=[django.contrib.auth.validators.UnicodeUsernameValidator()], verbose_name='username')),
                ('first_name', models.CharField(blank=True, max_length=150, verbose_name='first name')),
                ('last_name', models.CharField(blank=True, max_length=150, verbose_name='last name')),
                ('email', models.EmailField(blank=True, max_length=254, verbose_name='email address')),
                ('is_staff', models.BooleanField(default=False, help_text='Designates whether the user can log into this admin site.', verbose_name='staff status')),
                ('is_active', models.BooleanField(default=True, help_text='Designates whether this user should be treated as active. Unselect this instead of deleting accounts.', verbose_name='active')),
                ('date_joined', models.DateTimeField(default=django.utils.timezone.now, verbose_name='date joined')),
                ('profile_picture', models.ImageField(blank=True, default='NoImage.jpg', null=True, upload_to='')),
                ('role', models.PositiveSmallIntegerField(blank=True, choices=[(1, 'Admin'), (2, 'Customer'), (3, 'Driver'), (4, 'WareHouse')], default=4, null=True)),
                ('title', models.CharField(blank=True, max_length=200, null=True)),
                ('address', models.CharField(blank=True, max_length=200, null=True)),
                ('phone_number', models.CharField(blank=True, max_length=200, null=True)),
                ('driver_license', models.CharField(blank=True, max_length=200, null=True)),
                ('groups', models.ManyToManyField(blank=True, help_text='The groups this user belongs to. A user will get all permissions granted to each of their groups.', related_name='user_set', related_query_name='user', to='auth.group', verbose_name='groups')),
                ('user_permissions', models.ManyToManyField(blank=True, help_text='Specific permissions for this user.', related_name='user_set', related_query_name='user', to='auth.permission', verbose_name='user permissions')),
            ],
            options={
                'verbose_name': 'user',
                'verbose_name_plural': 'users',
                'abstract': False,
            },
            managers=[
                ('objects', django.contrib.auth.models.UserManager()),
            ],
        ),
        migrations.CreateModel(
            name='Car',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.CharField(blank=True, max_length=200, null=True)),
                ('capacity', models.CharField(blank=True, max_length=200, null=True)),
                ('color', models.CharField(blank=True, max_length=200, null=True)),
                ('make', models.CharField(blank=True, max_length=200, null=True)),
                ('model', models.CharField(blank=True, max_length=200, null=True)),
                ('year', models.CharField(blank=True, max_length=200, null=True)),
                ('license_plate', models.CharField(blank=True, max_length=200, null=True)),
                ('is_approved', models.BooleanField(default=False)),
                ('car_owner', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.CreateModel(
            name='Pickup',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('start_datetime', models.DateTimeField(auto_now=True)),
                ('end_datetime', models.DateTimeField(auto_now=True)),
                ('duration', models.CharField(blank=True, max_length=200, null=True)),
                ('tip', models.CharField(blank=True, max_length=200, null=True)),
                ('rating', models.PositiveIntegerField(blank=True, default=0, null=True)),
                ('car', models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, to='base.car')),
            ],
        ),
        migrations.CreateModel(
            name='RequestPickup',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('tracking_number', models.CharField(max_length=20, unique=True)),
                ('request_time', models.DateTimeField(auto_now_add=True)),
                ('date_and_time_pickup', models.DateTimeField(blank=True, null=True)),
                ('recipient_name', models.CharField(max_length=100)),
                ('recipient_phone', models.CharField(max_length=100)),
                ('pickup_location', models.CharField(blank=True, max_length=200, null=True)),
                ('dropoff_location', models.CharField(blank=True, max_length=200, null=True)),
                ('volume', models.CharField(blank=True, max_length=200, null=True)),
                ('weight', models.DecimalField(decimal_places=2, max_digits=5)),
                ('price_to_pay', models.CharField(blank=True, max_length=200, null=True)),
                ('status', models.PositiveSmallIntegerField(blank=True, choices=[(1, 'driver_assigned'), (2, 'picked_up'), (3, 'in_transit'), (4, 'out_for_delivery'), (5, 'delivered'), (6, 'returned')], default=4, null=True)),
                ('customer', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.CreateModel(
            name='RequestPickupImages',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('request_pickup_images', models.ImageField(blank=True, null=True, upload_to='')),
            ],
        ),
        migrations.CreateModel(
            name='WareHouse',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('image', models.ImageField(blank=True, default='NoImage.jpg', null=True, upload_to='')),
                ('address', models.CharField(blank=True, max_length=200, null=True)),
                ('volume', models.CharField(blank=True, max_length=200, null=True)),
                ('cctv', models.BooleanField(default=False)),
                ('armed_response', models.BooleanField(default=False)),
                ('fire_safety_and_management', models.BooleanField(default=False)),
                ('parking_space', models.BooleanField(default=False)),
                ('operating_hours', models.CharField(blank=True, max_length=200, null=True)),
                ('is_approved', models.BooleanField(default=False)),
                ('warehouse_owner', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.CreateModel(
            name='TrackingInfo',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('location', models.CharField(max_length=100)),
                ('timestamp', models.DateTimeField(auto_now_add=True)),
                ('request_pickup', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='base.requestpickup')),
            ],
        ),
        migrations.CreateModel(
            name='RequestPickupMessage',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('message', models.TextField()),
                ('timestamp', models.DateTimeField(auto_now_add=True)),
                ('requestpickup', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='base.requestpickup')),
                ('sender', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.AddField(
            model_name='requestpickup',
            name='images',
            field=models.ManyToManyField(to='base.requestpickupimages'),
        ),
        migrations.CreateModel(
            name='ProofOfDelivery',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('image', models.ImageField(upload_to='proof_of_delivery')),
                ('pickup', models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, to='base.requestpickup')),
            ],
        ),
        migrations.CreateModel(
            name='PickupMessage',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('message', models.TextField()),
                ('timestamp', models.DateTimeField(auto_now_add=True)),
                ('pickup', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='base.pickup')),
                ('sender', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.AddField(
            model_name='pickup',
            name='request_pickup',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, to='base.requestpickup'),
        ),
    ]
