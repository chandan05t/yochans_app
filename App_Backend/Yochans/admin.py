from django.contrib import admin
from Yochans.models import Clubs_db,Thoughts_db,Comments_db,Replies_db,SpecialMsg_db,Authentication_db,Events_db,Blogs_db,Thoughts_Archive_db,Comments_Archive_db,Replies_Archive_db
# Register your models here.
admin.site.register(Events_db)
admin.site.register(Clubs_db)
admin.site.register(Thoughts_db)
admin.site.register(Comments_db)
admin.site.register(Replies_db)
admin.site.register(SpecialMsg_db)
admin.site.register(Authentication_db)
admin.site.register(Blogs_db)
admin.site.register(Thoughts_Archive_db)
admin.site.register(Comments_Archive_db)
admin.site.register(Replies_Archive_db)
