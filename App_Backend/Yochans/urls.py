from django.urls import path,re_path,include
from Yochans import views

urlpatterns = [
    re_path(r'^database_query',views.database_query,name = 'Database Query'),
    re_path(r'^add_thought',views.add_thought,name = 'Add Thought'),
    re_path(r'^reply',views.reply,name = 'Reply'),
    re_path(r'^get_image',views.get_image, name = 'Get Image'),
    re_path(r'^authentication',views.authentication, name = 'Authentication'),
    re_path(r'^signup',views.signup, name = 'Signup'),
    re_path(r'^add_event',views.add_event, name = 'Add Event'),
    re_path(r'^add_comment',views.add_comment, name = 'Add Comment'),
    re_path(r'^response_query',views.response_query, name = 'Response Query'),
    re_path(r'^like_action',views.like_action, name = 'Like Action'),
    re_path(r'^dislike_action',views.dislike_action, name = 'Dislike Action'),
    re_path(r'^delete_thought',views.delete_thought, name = 'Delete Thought'),
    re_path(r'^add_blog',views.add_blog,name = 'Add Blog'),
    re_path(r'^delete_blog',views.delete_blog,name = 'Delete Blog'),
    re_path(r'^web_login_page',views.web_login_page,name = 'Web Login Page'),
    re_path(r'^web_login_action',views.web_login_action,name = 'Web Login Action'),
    re_path(r'^web_event_upload',views.web_event_upload,name ='Web Event Upload'),
]
