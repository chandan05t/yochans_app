# Generated by Django 2.0.2 on 2018-08-12 18:46

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Yochans', '0004_events_db_objectid'),
    ]

    operations = [
        migrations.AlterField(
            model_name='events_db',
            name='Date',
            field=models.DateTimeField(),
        ),
    ]
