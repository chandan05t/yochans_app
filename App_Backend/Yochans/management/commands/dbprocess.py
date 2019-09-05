from django.core.management.base import BaseCommand, CommandError
from Yochans.models import Thoughts_db,Clubs_db,Comments_db,Replies_db,Events_db,Thoughts_Archive_db,Comments_Archive_db,Replies_Archive_db
from datetime import datetime
from datetime import timedelta
import time,os

class Command(BaseCommand):
    help = 'Command to do.....Something.....'

    def add_argument(self,parser):
        pass

    def handle(self, *args, **options):
        try:
            while True:
                print("Operation done at...")
                print(datetime.now())
                print('')
                nowminusone = (datetime.now() - timedelta(days=1))
                eventtime = (datetime.now() - timedelta(minutes=30))
                for thoughts in Thoughts_db.objects.all():
                    if(thoughts.Time < nowminusone):
                        for comments in Comments_db.objects.filter(ThoughtId = thoughts.ObjectId):
                            New_Archive_Comment = Comments_Archive_db(
                            CommentId = comments.ObjectId,
                            Comments = comments.Comments,
                            ThoughtId = comments.ThoughtId,
                            Username = comments.Username,
                            ReplyCount = comments.ReplyCount,
                            Symbol = comments.Symbol,
                            Verify = comments.Verify )
                            New_Archive_Comment.save()
                            comments.delete()
                        for replies in Replies_db.objects.filter(ThoughtId = thoughts.ObjectId):
                            New_Archive_Reply = Replies_Archive_db(
                            ReplyId = replies.ObjectId,
                            Reply = replies.Reply,
                            Username = replies.Username,
                            Symbol = replies.Symbol,
                            CommentId = replies.CommentId,
                            ThoughtId = replies.ThoughtId,
                            Verified = replies.Verified )
                            New_Archive_Reply.save()
                            replies.delete()
                        New_Archive_Thought = Thoughts_Archive_db(
                        ThoughtId = thoughts.ObjectId,
                        Thoughts = thoughts.Thoughts,
                        Backgrounds = thoughts.Backgrounds,
                        Likes = thoughts.Likes,
                        Dislikes = thoughts.Dislikes,
                        User = thoughts.User,
                        Likeslist = thoughts.Likeslist,
                        Dislikeslist = thoughts.Dislikeslist,
                        Anonymous = thoughts.Anonymous,
                        Recom = thoughts.Recom,
                        Trendno = thoughts.Trendno )
                        New_Archive_Thought.save()
                        thoughts.delete()
                for events in Events_db.objects.all():
                    if(events.Date < eventtime):
                        os.remove(str(events.EventImage))
                        events.delete()
                time.sleep(1800)

        except Exception as e:
            raise CommandError(repr(e))
