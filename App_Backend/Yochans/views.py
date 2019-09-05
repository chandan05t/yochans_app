from django.shortcuts import render
from Yochans.models import Thoughts_db,Clubs_db,Comments_db,Replies_db,SpecialMsg_db,Authentication_db,Events_db,Blogs_db
from django.http import HttpResponse,HttpRequest,JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.apps import apps
from django.core import serializers
from datetime import date,datetime
import json,random,pytz
# Create your views here.

@csrf_exempt
def add_thought(request):
    Symbols_List = []
    for x in range(1,39):
        Symbols_List.append("kan"+str(x))
        if(x<=25):
            Symbols_List.append("gre"+str(x))
        if(x<=23):
            Symbols_List.append("mis"+str(x))
    if(True):
        New_Thought = Thoughts_db(
        Thoughts = request.POST.get('Thoughts'),
        Backgrounds = request.POST.get('Backgrounds'),
        User = request.POST.get('User'),
        Anonymous = request.POST.get('Anonymous'),
        Symbols = json.dumps(Symbols_List) )
        New_Thought.save()
        New_Thought.ObjectId = New_Thought.PrimeId
        New_Thought.save()
        return HttpResponse('Saved Successfully')

@csrf_exempt
def authentication(request):
    if(True):
        Username = request.POST.get('Username')
        Password = request.POST.get('Password')
        if(Password==Authentication_db.objects.get(Username = Username).Password):
            return HttpResponse('User Verified')
        else:
            return HttpResponse('Not Valid User')

@csrf_exempt
def signup(request):
    if(True):
        New_User = Authentication_db(
        Username = request.POST.get('Username'),
        Password = request.POST.get('Password') )
        New_User.save()
        return HttpResponse('SignUp Successfully')

@csrf_exempt
def add_event(request):
    if(True):
        EDate = (datetime.fromtimestamp(float(request.POST.get('Date'))/1000))
        New_Event = Events_db(
        EventImage = request.FILES['ImageFile'],
        EventName = request.POST.get('EventName'),
        Venue = request.POST.get('Venue'),
        Date = EDate )
        if(request.POST.get('Description')):
            New_Event.Description = request.POST.get('Description')
        New_Event.save()
        return HttpResponse('Saved Successfully')

@csrf_exempt
def add_comment(request):
    if(True):
        New_Comment = Comments_db(
        Comments = request.POST.get('Comments'),
        ThoughtId = request.POST.get('ThoughtId'),
        Username = request.POST.get('Username'),
        Verify = request.POST.get('Verify') )
        if(request.POST.get('Verify') == 'false'):
            New_Comment.Symbol = AssignSymbol(
            New_Comment.ThoughtId,New_Comment.Username)
        New_Comment.save()
        New_Comment.ObjectId = New_Comment.PrimeId
        New_Comment.save()
        SetRecom_and_Trendno(New_Comment.ThoughtId)
        return HttpResponse('Saved Successfully')


@csrf_exempt
def reply(request):
    if(True):
        New_Reply = Replies_db(
        Reply = request.POST.get('Reply'),
        Username = request.POST.get('Username'),
        CommentId = request.POST.get('CommentId'),
        ThoughtId = request.POST.get('ThoughtId'),
        Verified = request.POST.get('Verified')  )
        if(request.POST.get('Verified') == 'false'):
            New_Reply.Symbol = AssignSymbol(
            New_Reply.ThoughtId,New_Reply.Username)
        New_Reply.save()
        New_Reply.ObjectId = New_Reply.PrimeId
        New_Reply.save()
        SetRecom_and_Trendno(New_Reply.ThoughtId)
        SetReplyCount(New_Reply.CommentId)
        return HttpResponse('Saved Successfully')

@csrf_exempt
def add_blog(request):
    if(True):
        New_Blog = Blogs_db(
        Title = request.POST.get('Title'),
        URL = request.POST.get('URL'),
        Author = request.POST.get('Author'),
        User = request.POST.get('User') )
        New_Blog.save()
        return HttpResponse('Saved Successfully')

@csrf_exempt
def get_image(request):
    if(True):
        file_path = request.POST.get("Location")
        file_data = open(file_path,"rb").read()
        ImageHttpResponse = HttpResponse(file_data,content_type='image/jpeg')
        ImageHttpResponse['Name']=file_path.rsplit('/',1)[-1]
        return ImageHttpResponse

@csrf_exempt
def response_query(request):
    if(True):
        Username = request.POST.get('Username')
        ObjectId = request.POST.get('ObjectId')
        Response_Like_Query = Thoughts_db.objects.get(ObjectId = ObjectId).Likeslist
        Response_Dislike_Query = Thoughts_db.objects.get(ObjectId = ObjectId).Dislikeslist
        if(Username in Response_Like_Query):
            return HttpResponse('In LikeList')
        elif(Username in Response_Dislike_Query):
            return HttpResponse('In DislikesList')
        else:
            return HttpResponse('In None')

@csrf_exempt
def like_action(request):
    if(True):
        Username = request.POST.get('Username')
        ObjectId = request.POST.get('ObjectId')
        Thought_Instance = Thoughts_db.objects.get(ObjectId = ObjectId)
        Like_Query = Thought_Instance.Likeslist
        Dislike_Query = Thought_Instance.Dislikeslist
        if(Username in Like_Query):
            Like_Query_new = Like_Query.replace(Username + ',','' )
            Thought_Instance.Likeslist = Like_Query_new
            Thought_Instance.Likes = Thought_Instance.Likes - 1
            Thought_Instance.save()
        elif(Username in Dislike_Query):
            Dislike_Query_new = Dislike_Query.replace(Username + ',','')
            Thought_Instance.Dislikeslist = Dislike_Query_new
            Thought_Instance.Dislikes = Thought_Instance.Dislikes - 1
            Like_Query_new = Like_Query + Username + ','
            Thought_Instance.Likeslist = Like_Query_new
            Thought_Instance.Likes = Thought_Instance.Likes + 1
            Thought_Instance.save()
        else:
            Like_Query_new = Like_Query + Username + ','
            Thought_Instance.Likeslist = Like_Query_new
            Thought_Instance.Likes = Thought_Instance.Likes + 1
            Thought_Instance.save()
        SetRecom_and_Trendno(ObjectId)
        return HttpResponse('Done')

@csrf_exempt
def dislike_action(request):
    if(True):
        Username = request.POST.get('Username')
        ObjectId = request.POST.get('ObjectId')
        Thought_Instance = Thoughts_db.objects.get(ObjectId = ObjectId)
        Like_Query = Thought_Instance.Likeslist
        Dislike_Query = Thought_Instance.Dislikeslist
        if(Username in Like_Query):
            Like_Query_new = Like_Query.replace(Username + ',','' )
            Thought_Instance.Likeslist = Like_Query_new
            Thought_Instance.Likes = Thought_Instance.Likes - 1
            Dislike_Query_new = Dislike_Query + Username +','
            Thought_Instance.Dislikeslist = Dislike_Query_new
            Thought_Instance.Dislikes = Thought_Instance.Dislikes + 1
            Thought_Instance.save()
        elif(Username in Dislike_Query):
            Dislike_Query_new = Dislike_Query.replace(Username + ',','')
            Thought_Instance.Dislikeslist = Dislike_Query_new
            Thought_Instance.Dislikes = Thought_Instance.Dislikes - 1
        else:
            Dislike_Query_new = Dislike_Query + Username +','
            Thought_Instance.Dislikeslist = Dislike_Query_new
            Thought_Instance.Dislikes = Thought_Instance.Dislikes + 1
            Thought_Instance.save()
        SetRecom_and_Trendno(ObjectId)
        return HttpResponse('Done')

@csrf_exempt
def delete_thought(request):
    if(True):
        Thought = Thoughts_db.objects.get(ObjectId = request.POST.get('ThoughtId'))
        for comments in Comments_db.objects.filter(ThoughtId = Thought.ObjectId):
            comments.delete()
        for replies in Replies_db.objects.filter(ThoughtId = Thought.ObjectId):
            replies.delete()
        Thought.delete()
        return HttpResponse('Done')

@csrf_exempt
def delete_blog(request):
    if(True):
        Blog = Blogs_db.objects.get(URL = request.POST.get('URL'))
        Blog.delete()
        return HttpResponse('done')

@csrf_exempt
def database_query(request):
    Database_Table_name = request.POST.get('Database_Table')
    Database = apps.get_model("Yochans", Database_Table_name, require_ready=True)
    if(request.POST.get('Key')):
        Key = request.POST.get('Key')
        Key_Value = request.POST.get('Key_Value')
        kwargs = {
            Key:Key_Value
        }
        if(request.POST.get('AscendingKey')):
            AscendingKey = request.POST.get('AscendingKey')
            result = Database.objects.filter(**kwargs).order_by(AscendingKey)
        elif(request.POST.get('DescendingKey')):
            DescendingKey = '-' + request.POST.get('DescendingKey')
            result = Database.objects.filter(**kwargs).order_by(DescendingKey)
        else:
            result = Database.objects.filter(**kwargs)
    else:
        if(request.POST.get('AscendingKey')):
            AscendingKey = request.POST.get('AscendingKey')
            result = Database.objects.all().order_by(AscendingKey)
        elif(request.POST.get('DescendingKey')):
            DescendingKey = '-' + request.POST.get('DescendingKey')
            result = Database.objects.all().order_by(DescendingKey)
        else:
            result = Database.objects.all()
    result_json = serializers.serialize('json',result)
    output = []
    if(request.POST.get('First') and len(result_json) > 2):
        dict = (json.loads(result_json)[0]['fields'])
        if('Symbols' in dict):
            del dict['Symbols']
        output.append(dict)
    else:
        for i in json.loads(result_json):
            dict = i['fields']
            if('Symbols' in dict):
                del dict['Symbols']
            output.append(dict)
    output_json = json.dumps(output)
    return HttpResponse(output_json, content_type='application/json')

def web_login_page(request):
    return render(request,'')

def web_login_action(request):
    Username = request.POST.get('Clubname')
    Password = request.POST.get('Password')
    if(Password==Authentication_db.objects.get(Username = Username).Password):
        return render(request,'')
    else:
        return HttpResponse('Not Valid User')

def web_event_upload(request):
    Date = datetime.datetime.strptime(request.POST.get('Date'),"%Y-%m-%d").date()
    New_Event = Events_db(
    EventImage = request.FILES['ImageFile'] ,
    EventName = request.POST.get('EventName'),
    Venue = request.POST.get('Venue'),
    Date = request.POST.get(''),
    Description = request.POST.get('Description') )
    New_Event.save()
    return HttpRequest('Saved Successfully')

def AssignSymbol(ThoughtId,Username):
    Query_Comment = Comments_db.objects.filter(ThoughtId = ThoughtId, Username = Username)
    Query_Reply = Replies_db.objects.filter(ThoughtId = ThoughtId, Username = Username)
    if(len(Query_Comment) > 0):
        return Query_Comment.first().Symbol
    elif(len(Query_Reply) > 0):
        return Query_Reply.first().Symbol
    else:
        Thought = Thoughts_db.objects.get(ObjectId = ThoughtId)
        SymbolList = json.loads(Thought.Symbols)
        RandNum = random.randint(0, len(SymbolList) -1)
        SymbolName = SymbolList[RandNum]
        del SymbolList[RandNum]
        Thought.Symbols = json.dumps(SymbolList)
        Thought.save()
        return SymbolName

def SetRecom_and_Trendno(ThoughtId):
    Thought = Thoughts_db.objects.get(ObjectId = ThoughtId)
    Comment = Comments_db.objects.filter(ThoughtId = ThoughtId)
    Reply = Replies_db.objects.filter(ThoughtId = ThoughtId)
    Trendno = 3*len(Comment) + 3*len(Reply) + Thought.Likes + Thought.Dislikes
    Recom = len(Comment) + len(Reply)
    Thought.Recom = Recom
    Thought.Trendno = Trendno
    Thought.save()

def SetReplyCount(CommentId):
    Comment = Comments_db.objects.get(ObjectId = CommentId)
    Reply = Replies_db.objects.filter(CommentId = CommentId)
    Comment.ReplyCount = len(Reply)
    Comment.save()
