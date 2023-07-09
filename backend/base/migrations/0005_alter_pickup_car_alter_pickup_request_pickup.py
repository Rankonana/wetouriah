# Generated by Django 4.2.1 on 2023-06-20 10:07

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('base', '0004_alter_pickup_last_known_location'),
    ]

    operations = [
        migrations.AlterField(
            model_name='pickup',
            name='car',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, to='base.car'),
        ),
        migrations.AlterField(
            model_name='pickup',
            name='request_pickup',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, to='base.requestpickup'),
        ),
    ]
