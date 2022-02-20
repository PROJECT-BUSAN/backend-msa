# Generated by Django 3.2.7 on 2022-02-09 06:17

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Api',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=128)),
                ('description', models.TextField(default='', max_length=500)),
                ('upstream_path', models.CharField(max_length=255)),
                ('upstream_host', models.CharField(max_length=255)),
                ('plugin', models.IntegerField(choices=[(0, 'public auth'), (1, 'api auth'), (2, 'superuser auth')], default=0)),
                ('created', models.DateField(auto_now_add=True)),
            ],
        ),
    ]