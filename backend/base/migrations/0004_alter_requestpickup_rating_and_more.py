# Generated by Django 4.2.1 on 2023-09-27 14:18

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('base', '0003_alter_requestpickup_end_datetime_and_more'),
    ]

    operations = [
        migrations.AlterField(
            model_name='requestpickup',
            name='rating',
            field=models.PositiveIntegerField(blank=True, null=True),
        ),
        migrations.AlterField(
            model_name='requestpickup',
            name='tracking_number',
            field=models.CharField(blank=True, max_length=20, null=True),
        ),
    ]
