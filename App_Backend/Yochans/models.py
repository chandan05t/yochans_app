from django.db import models
import uuid

# Create your models here.
class Authentication_db(models.Model):
    PrimeId = models.UUIDField(primary_key = True, default = uuid.uuid4, editable = False)
    Username = models.CharField(max_length = 50, unique = True)
    Password = models.CharField(max_length = 50)
    LastModified = models.DateTimeField(auto_now = True)
    CreatedAt = models.DateTimeField(auto_now_add = True)

class Events_db(models.Model):
    PrimeId = models.UUIDField(primary_key = True, default = uuid.uuid4, editable = False)
    EventName = models.CharField(max_length = 50)
    EventImage = models.FileField('ImageFile',upload_to = "EventImages/")
    Venue = models.CharField(max_length = 50)
    Date = models.DateTimeField()
    Description = models.TextField( blank = True)
    LastModified = models.DateTimeField(auto_now = True)
    CreatedAt = models.DateTimeField(auto_now_add = True)

class Blogs_db(models.Model):
    PrimeId = models.UUIDField(primary_key = True, default = uuid.uuid4, editable = False)
    User = models.CharField(max_length = 40)
    Title = models.TextField()
    URL = models.TextField()
    Author = models.CharField(max_length = 50)
    LastModified = models.DateTimeField(auto_now = True)
    CreatedAt = models.DateTimeField(auto_now_add = True)

class Clubs_db(models.Model):
    PrimeId = models.UUIDField(primary_key = True, default = uuid.uuid4, editable = False)
    Username = models.CharField(max_length = 50, unique = True)
    Password = models.CharField(max_length = 50)
    LastModified = models.DateTimeField(auto_now = True)
    CreatedAt = models.DateTimeField(auto_now_add = True)

class Thoughts_db(models.Model):
    PrimeId = models.UUIDField(primary_key = True, default = uuid.uuid4, editable = False)
    ObjectId = models.CharField(max_length = 40, blank = True)
    Thoughts = models.TextField()
    Backgrounds = models.CharField(max_length = 50)
    Likes = models.IntegerField(default = 0)
    Dislikes = models.IntegerField(default = 0)
    User = models.CharField(max_length = 100)
    Likeslist = models.TextField(blank = True)
    Dislikeslist = models.TextField(blank = True)
    Symbols = models.TextField()
    Anonymous = models.CharField(max_length = 8)
    Time = models.DateTimeField(auto_now_add = True)
    Recom = models.IntegerField(default = 0)
    Trendno = models.IntegerField(default = 0)
    LastModified = models.DateTimeField(auto_now = True)
    CreatedAt = models.DateTimeField(auto_now_add = True)

class Comments_db(models.Model):
    PrimeId = models.UUIDField(primary_key = True, default = uuid.uuid4, editable = False)
    ObjectId = models.CharField(max_length = 40, blank = True)
    Comments  = models.TextField()
    ThoughtId = models.CharField(max_length = 40)
    Username = models.CharField(max_length = 50)
    ReplyCount = models.IntegerField(default = 0)
    Symbol = models.CharField(max_length = 15, blank = True)
    Verify = models.CharField(max_length = 6)
    LastModified = models.DateTimeField(auto_now = True)
    CreatedAt = models.DateTimeField(auto_now_add = True)

class Replies_db(models.Model):
    PrimeId = models.UUIDField(primary_key = True, default = uuid.uuid4, editable = False)
    ObjectId = models.CharField(max_length = 40, blank = True)
    Reply = models.TextField()
    Username = models.CharField(max_length = 50)
    Symbol = models.CharField(max_length = 15, blank = True)
    CommentId = models.CharField(max_length = 40)
    ThoughtId = models.CharField(max_length = 40)
    Verified = models.CharField(max_length = 6)
    LastModified = models.DateTimeField(auto_now = True)
    CreatedAt = models.DateTimeField(auto_now_add = True)

class Thoughts_Archive_db(models.Model):
    PrimeId = models.UUIDField(primary_key = True, default = uuid.uuid4, editable = False)
    ThoughtId = models.CharField(max_length = 40)
    Thoughts = models.TextField()
    Backgrounds = models.CharField(max_length = 50)
    Likes = models.IntegerField(default = 0)
    Dislikes = models.IntegerField(default = 0)
    User = models.CharField(max_length = 50)
    Likeslist = models.TextField(blank = True)
    Dislikeslist = models.TextField(blank = True)
    Anonymous = models.CharField(max_length = 8)
    Recom = models.IntegerField(default = 0)
    Trendno = models.IntegerField(default = 0)
    LastModified = models.DateTimeField(auto_now = True)
    CreatedAt = models.DateTimeField(auto_now_add = True)

class Comments_Archive_db(models.Model):
    PrimeId = models.UUIDField(primary_key = True, default = uuid.uuid4, editable = False)
    CommentId = models.CharField(max_length = 40)
    Comments  = models.TextField()
    ThoughtId = models.CharField(max_length = 40)
    Username = models.CharField(max_length = 50)
    ReplyCount = models.IntegerField(default = 0)
    Symbol = models.CharField(max_length = 15, blank = True)
    Verify = models.CharField(max_length = 6)
    LastModified = models.DateTimeField(auto_now = True)
    CreatedAt = models.DateTimeField(auto_now_add = True)

class Replies_Archive_db(models.Model):
    PrimeId = models.UUIDField(primary_key = True, default = uuid.uuid4, editable = False)
    ReplyId = models.CharField(max_length = 40)
    Reply = models.TextField()
    Username = models.CharField(max_length = 50)
    Symbol = models.CharField(max_length = 15, blank = True)
    CommentId = models.CharField(max_length = 40)
    ThoughtId = models.CharField(max_length = 40)
    Verified = models.CharField(max_length = 6)
    LastModified = models.DateTimeField(auto_now = True)
    CreatedAt = models.DateTimeField(auto_now_add = True)

class SpecialMsg_db(models.Model):
    PrimeId = models.UUIDField(primary_key = True, default = uuid.uuid4, editable = False)
    SpecialMsg = models.FileField(upload_to = "SpecialMsg/")
    Duration = models.IntegerField()
    LastModified = models.DateTimeField(auto_now = True)
    CreatedAt = models.DateTimeField(auto_now_add = True)
