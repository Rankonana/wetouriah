# Generated by Django 4.2.1 on 2023-10-11 22:48

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):
    dependencies = [
        ("base", "0007_car_is_default"),
    ]

    operations = [
        migrations.RemoveField(
            model_name="driverslicense",
            name="code",
        ),
        migrations.RemoveField(
            model_name="driverslicense",
            name="country_of_issue",
        ),
        migrations.RemoveField(
            model_name="driverslicense",
            name="date_of_birth",
        ),
        migrations.RemoveField(
            model_name="driverslicense",
            name="date_of_issue",
        ),
        migrations.RemoveField(
            model_name="driverslicense",
            name="gender",
        ),
        migrations.RemoveField(
            model_name="driverslicense",
            name="identity_number",
        ),
        migrations.RemoveField(
            model_name="driverslicense",
            name="restrictions",
        ),
        migrations.CreateModel(
            name="DriverAvailability",
            fields=[
                (
                    "id",
                    models.BigAutoField(
                        auto_created=True,
                        primary_key=True,
                        serialize=False,
                        verbose_name="ID",
                    ),
                ),
                ("available", models.BooleanField(default=False)),
                ("location", models.CharField(blank=True, max_length=200, null=True)),
                (
                    "driver",
                    models.ForeignKey(
                        on_delete=django.db.models.deletion.CASCADE,
                        to=settings.AUTH_USER_MODEL,
                    ),
                ),
            ],
        ),
    ]
