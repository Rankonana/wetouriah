# Generated by Django 4.2.1 on 2023-10-12 07:54

from django.db import migrations


class Migration(migrations.Migration):
    dependencies = [
        ("base", "0012_declinelist"),
    ]

    operations = [
        migrations.RenameField(
            model_name="declinelist",
            old_name="driver",
            new_name="courier",
        ),
    ]
