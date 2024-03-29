# Generated by Django 2.0.2 on 2018-08-22 13:57

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Yochans', '0008_auto_20180813_0025'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='events_db',
            name='ObjectId',
        ),
        migrations.AddField(
            model_name='thoughts_db',
            name='Anonymous',
            field=models.CharField(default='true', max_length=8),
            preserve_default=False,
        ),
        migrations.AlterField(
            model_name='events_db',
            name='Date',
            field=models.DateTimeField(),
        ),
        migrations.AlterField(
            model_name='events_db',
            name='EventName',
            field=models.CharField(max_length=50),
        ),
        migrations.AlterField(
            model_name='events_db',
            name='Venue',
            field=models.CharField(max_length=50),
        ),
    ]
