# Generated by Django 2.0.2 on 2018-08-09 14:10

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Yochans', '0002_auto_20180809_1829'),
    ]

    operations = [
        migrations.AlterField(
            model_name='comments_db',
            name='ThoughtId',
            field=models.CharField(max_length=40),
        ),
        migrations.AlterField(
            model_name='replies_db',
            name='CommentId',
            field=models.CharField(max_length=40),
        ),
        migrations.AlterField(
            model_name='replies_db',
            name='ThoughtId',
            field=models.CharField(max_length=40),
        ),
    ]