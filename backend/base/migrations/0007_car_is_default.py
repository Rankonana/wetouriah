# Generated by Django 4.2.1 on 2023-10-05 23:41

from django.db import migrations, models


class Migration(migrations.Migration):
    dependencies = [
        ("base", "0006_alter_requestpickup_customer_and_more"),
    ]

    operations = [
        migrations.AddField(
            model_name="car",
            name="is_default",
            field=models.BooleanField(default=False),
        ),
    ]