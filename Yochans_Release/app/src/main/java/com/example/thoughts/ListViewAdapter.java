package com.example.thoughts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;


/**
 * Created by Chandan T on 06-03-2018.
 */

public class ListViewAdapter extends ArrayAdapter {

    Activity context;
    ArrayList<String> comments;
    ArrayList<String> Verify;
    ArrayList<String> CommentId;
    ArrayList<String> SymbolComment;
    ArrayList<String> ReplyCount;
    String ThoughtUser;
    String ThoughtId;
    ArrayList<String> Symbol = new ArrayList<>();
    ArrayList<String> Reply = new ArrayList<>();
    ArrayList<String> RVerify = new ArrayList<>();
    ReplyCustomAdapter replyCustomAdapter;
    JSONArray SymbolList;

    public ListViewAdapter(Activity context,ArrayList<String> comments,ArrayList<String> Verify,ArrayList<String> CommentId,String ThoughtUser,String ThoughtId,ArrayList<String> SymbolComment,ArrayList<String> ReplyCount){
        super(context,R.layout.commentlayout,comments);
        this.context=context;
        this.comments=comments;
        this.Verify=Verify;
        this.CommentId=CommentId;
        this.ThoughtUser=ThoughtUser;
        this.ThoughtId=ThoughtId;
        this.SymbolComment=SymbolComment;
        this.ReplyCount=ReplyCount;
    }

    @Override
    public View getView(final int position,View rowView, ViewGroup parent) {

        final LayoutInflater inflater = context.getLayoutInflater();
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.commentlayout, null, true);
        }


        TextView mainText = (TextView) rowView.findViewById(R.id.commentTextView);
        mainText.setText(comments.get(position));

        ImageView UserVerifyImageView = (ImageView)rowView.findViewById(R.id.userVerifyImageView);

        if(Verify.get(position).equals("true")){
            UserVerifyImageView.setImageResource(R.mipmap.username_tick);
            RelativeLayout.LayoutParams params =new RelativeLayout.LayoutParams(pxFromDp(context, 15), pxFromDp(context, 15));
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            UserVerifyImageView.setLayoutParams(params);
        }else{
            int resID = context.getResources().getIdentifier(SymbolComment.get(position), "drawable", context.getPackageName());
            RelativeLayout.LayoutParams params =new RelativeLayout.LayoutParams(pxFromDp(context, 25), pxFromDp(context, 25));
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            UserVerifyImageView.setLayoutParams(params);
            UserVerifyImageView.setImageResource(resID);
        }

        final RelativeLayout RelLay = (RelativeLayout)rowView.findViewById(R.id.relLay);

        final TextView ReplyTextView = (TextView)rowView.findViewById(R.id.replayTextView);

        TextView ReplyCountText = (TextView)rowView.findViewById(R.id.replyCountTextView);
        if(Integer.parseInt(ReplyCount.get(position)) == 1){
            ReplyCountText.setText("1 Reply");
            ReplyCountText.setVisibility(View.VISIBLE);
        }else if(Integer.parseInt(ReplyCount.get(position)) > 1) {
            ReplyCountText.setText(ReplyCount.get(position) + " Replies");
            ReplyCountText.setVisibility(View.VISIBLE);
        }else{
            ReplyCountText.setVisibility(View.INVISIBLE);
        }

        ReplyCountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplyTextView.performClick();
            }
        });

        ReplyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replyCustomAdapter = new ReplyCustomAdapter(context,Symbol,Reply,RVerify);

                AlertDialog.Builder ReplyDialog = new AlertDialog.Builder(context);
                View Background = (View)inflater.inflate(R.layout.reply_list,null);
                ReplyDialog.setView(Background);
                View footer = (View)context.getLayoutInflater().inflate(R.layout.replypost,null);

                final ListView ReplyList = (ListView)Background.findViewById(R.id.replyList);

                ReplyList.addFooterView(footer);
                TextView mainText = (TextView) Background.findViewById(R.id.commentTextView);

                if(comments.get(position).length() < 75) {
                    mainText.setText(comments.get(position));
                }else{
                    mainText.setText(comments.get(position).substring(0,75) + "...");
                }
                final EditText ReplyView = (EditText)footer.findViewById(R.id.ReplyText);

                final Button PostButton = (Button)footer.findViewById(R.id.postButton);

                ImageView UserVerifyImageView = (ImageView)Background.findViewById(R.id.userVerifyImageView);
                if(Verify.get(position).equals("true")){
                    UserVerifyImageView.setImageResource(R.mipmap.username_tick);
                }else{
                    int resID = context.getResources().getIdentifier(SymbolComment.get(position), "drawable", context.getPackageName());
                    UserVerifyImageView.setLayoutParams(new RelativeLayout.LayoutParams(pxFromDp(context, 25), pxFromDp(context, 25)));
                    UserVerifyImageView.setImageResource(resID);
                }


                Query(position);

                /*PostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        PostButton.setEnabled(false);
                        if(!ReplyView.getText().toString().isEmpty()){
                            ParseObject object = new ParseObject("Replies");
                            object.put("Reply",ReplyView.getText().toString());
                            object.put("Username", ParseUser.getCurrentUser().getUsername());
                            object.put("Symbol","");
                            object.put("CommentId",CommentId.get(position));
                            object.put("ThoughtId",ThoughtId);
                            if(ThoughtUser.equals(ParseUser.getCurrentUser().getUsername())){
                                object.put("Verified","true");
                            }else{
                                object.put("Verified","false");

                                /*ParseQuery<ParseObject> symbolquery1 = new ParseQuery<ParseObject>("Comments");
                                symbolquery1.whereEqualTo("CommentId",Thought);
                                symbolquery1.whereEqualTo("Username",ParseUser.getCurrentUser().getUsername());

                                ParseQuery<ParseObject> symbolquery2 = new ParseQuery<ParseObject>("Replies");
                                symbolquery2.whereEqualTo("Thought",Thought);
                                symbolquery2.whereEqualTo("Username",ParseUser.getCurrentUser().getUsername());
                                List<ParseObject> Que1;
                                List<ParseObject> Que2;
                                List<ParseObject> Que3;
                                List<ParseObject> Que4;

                                try {
                                    Que1 = symbolquery1.find();
                                    Que2 = symbolquery2.find();

                                    if(Que1.size()==0 && Que2.size()==0) {
                                        ParseQuery<ParseObject> ThoughtSybmols = new ParseQuery<ParseObject>("Thoughts");
                                        ThoughtSybmols.whereEqualTo("objectId", Thought);
                                        Que3 = ThoughtSybmols.find();
                                        ParseObject Thoughtobject = Que3.get(0);
                                        SymbolList = Thoughtobject.getJSONArray("Symbols");
                                        int rand = (int) (Math.random() * SymbolList.length());
                                        object.put("Symbol", SymbolList.get(rand));
                                        SymbolList.remove(rand);
                                        Thoughtobject.put("Symbols", SymbolList);
                                        Thoughtobject.save();
                                        Toast.makeText(context, "Posted", Toast.LENGTH_SHORT).show();
                                    }else if (Que1.size() > 0){
                                        ParseObject symbolobject = Que1.get(0);
                                        object.put("Symbol",symbolobject.getString("Symbol"));
                                        Toast.makeText(context, "Posted", Toast.LENGTH_SHORT).show();
                                    }else if(Que2.size() > 0){
                                        ParseObject symbolobject = Que2.get(0);
                                        object.put("Symbol",symbolobject.getString("Symbol"));
                                        Toast.makeText(context, "Posted", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show();
                                    PostButton.setEnabled(true);
                                }catch (JSONException e){
                                    e.printStackTrace();
                                    Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show();
                                    PostButton.setEnabled(true);
                                }//
                            }
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e==null){
                                        if(view !=null) {
                                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                        }
                                        ReplyView.setText(null);
                                        ReplyView.clearFocus();
                                        PostButton.setEnabled(true);
                                        Toast.makeText(context, "Posted", Toast.LENGTH_SHORT).show();
                                        Query(position);
                                    }else{
                                        PostButton.setEnabled(true);
                                    }
                                }
                            });
                        }else{
                            PostButton.setEnabled(true);
                        }
                    }
                });*/

                PostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        PostButton.setEnabled(false);
                        if(!ReplyView.getText().toString().isEmpty()){
                            DjangoObject djangoObject = new DjangoObject(getContext(),"/reply","Replies_db");
                            djangoObject.put("Reply",ReplyView.getText().toString());
                            djangoObject.put("Username", new DjangoAuthenication(getContext()).getUsername());
                            djangoObject.put("CommentId",CommentId.get(position));
                            djangoObject.put("ThoughtId",ThoughtId);
                            if(ThoughtUser.equals(new DjangoAuthenication(getContext()).getUsername())){
                                djangoObject.put("Verified","true");
                            }else{
                                djangoObject.put("Verified","false");
                            }
                            djangoObject.saveInBackground(new SaveInBackgroundListener() {
                                @Override
                                public void onSaved(Response response) {
                                    if(view !=null) {
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                    }
                                    ReplyView.setText(null);
                                    ReplyView.clearFocus();
                                    PostButton.setEnabled(true);
                                    Toast.makeText(context, "Posted", Toast.LENGTH_SHORT).show();
                                    Query(position);
                                }

                                @Override
                                public void OnFailure(IOException e) {
                                    PostButton.setEnabled(true);
                                }
                            });
                        }else{
                            PostButton.setEnabled(true);
                        }
                    }
                });

                ReplyList.setAdapter(replyCustomAdapter);

                AlertDialog dlg = ReplyDialog.show();
                dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
        });


        return rowView;
    }



    /*public void Query(int position){

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Replies");
        query.whereEqualTo("CommentId",CommentId.get(position));
        query.orderByAscending("createdAt");
        Reply.clear();
        Symbol.clear();
        RVerify.clear();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    if (objects.size() > 0) {

                        for (ParseObject object : objects) {
                            Reply.add(object.getString("Reply"));
                            Symbol.add(object.getString("Symbol"));
                            RVerify.add(object.getString("Verified"));
                            replyCustomAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }
        });
    }*/

    public void Query(int position){

        DjangoQuery djangoQuery = new DjangoQuery(getContext(),"/database_query","Replies_db");
        djangoQuery.whereEqualto("CommentId",CommentId.get(position));
        djangoQuery.orderByAscending("CreatedAt");
        Reply.clear();
        Symbol.clear();
        RVerify.clear();
        djangoQuery.findInBackground(new FindInBackgroundListener() {
            @Override
            public void OnFound(JSONArray DjangoJSONArray) {
                if(DjangoJSONArray.length() > 0){
                    for(int i=0 ;i < DjangoJSONArray.length() ;i++ ){
                        try {
                            Reply.add(DjangoJSONArray.getJSONObject(i).getString("Reply"));
                            Symbol.add(DjangoJSONArray.getJSONObject(i).getString("Symbol"));
                            RVerify.add(DjangoJSONArray.getJSONObject(i).getString("Verified"));
                            replyCustomAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void OnFailure(IOException e) {
                e.printStackTrace();
            }
        });

    }

    public static int pxFromDp(final Context context, final float dp) {
        return (int)(dp * context.getResources().getDisplayMetrics().density);
    }


}
