package com.example.thoughts;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class ThoughtDetail extends AppCompatActivity {

    ListView CommentListView;
    ArrayList<String> Comments = new ArrayList<>();
    ArrayList<String> Verify = new ArrayList<>();
    ArrayList<String> CommentId = new ArrayList<>();
    ArrayList<String> SymbolComment = new ArrayList<>();
    ArrayList<String> ReplyCount = new ArrayList<>();
    JSONArray SymbolList;
    ListViewAdapter adapter;
    Boolean Liked = false;
    Boolean Disliked = false;
    Intent intent;
    TextView ThoughtText;
    TextView LikesText;
    TextView DislikesText;
    ImageView SuperImageView;
    ImageView DislikeImageView;
    TextView TimeTextView;
    ImageView BackgroundImageView;
    ProgressBar progressBar;
    String user,ThoughtId;
    AsyncAdapter ListTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thought_detail);

        intent = getIntent();

        CommentListView = (ListView) findViewById(R.id.commentListView);
        View header = (View) getLayoutInflater().inflate(R.layout.thought_large, null);
        CommentListView.addHeaderView(header);

        BackgroundImageView = findViewById(R.id.backgroundImageView);
        ThoughtText = findViewById(R.id.thoughtTextView);
        ThoughtText.setText(intent.getStringExtra("Thought"));
        LikesText = findViewById(R.id.superTextView);
        LikesText.setText(intent.getStringExtra("Likes"));
        DislikesText = findViewById(R.id.dislikeTextView);
        DislikesText.setText(intent.getStringExtra("Dislikes"));
        TimeTextView = findViewById(R.id.timeTextView);
        TimeTextView.setText(intent.getStringExtra("Time"));
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        RelativeLayout RelLayBG = (RelativeLayout) findViewById(R.id.relLayBG);
        final RelativeLayout RelLay = (RelativeLayout) findViewById(R.id.relLay);
        String Background = intent.getStringExtra("Background");
        int resID = getApplicationContext().getResources().getIdentifier(Background, "drawable", getApplicationContext().getPackageName());
        BackgroundImageView.setBackgroundResource(resID);
        int width = (int) getScreenWidth() - 20;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, (16 * width / 9) - 120);
        RelLayBG.setLayoutParams(layoutParams);

        LinearLayout UserLinearLayout = (LinearLayout)findViewById(R.id.userLinearLayout);
        UserLinearLayout.setVisibility(View.INVISIBLE);
        TextView UserTextView = (TextView) findViewById(R.id.userTextView);
        UserTextView.setVisibility(View.INVISIBLE);

        if(intent.getStringExtra("Anonymous").equals("false")){
            UserLinearLayout.setVisibility(View.VISIBLE);
            UserTextView.setText("by : " + intent.getStringExtra("User"));
        }

        ArrayList<String> planetList = new ArrayList<>();
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, planetList);
        CommentListView.setAdapter(listAdapter);
        ListTask = new AsyncAdapter();
        ListTask.execute();

        final Button PostButton = (Button) findViewById(R.id.postButton);
        PostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PostButton.setEnabled(false);
                System.out.println("Started Operation");
                final EditText CommentEditText = (EditText) findViewById(R.id.commentTextView);
                if (!CommentEditText.getText().toString().isEmpty()) {
                    DjangoObject djangoObject = new DjangoObject(getApplicationContext(),"/add_comment","Comments_db");
                    djangoObject.put("Comments", CommentEditText.getText().toString());
                    djangoObject.put("ThoughtId", intent.getStringExtra("ThoughtId"));
                    djangoObject.put("Username", new DjangoAuthenication(getApplicationContext()).getUsername());
                    if (intent.getStringExtra("User").equals(new DjangoAuthenication(getApplicationContext()).getUsername())) {
                        djangoObject.put("Verify", "true");
                    } else {
                        djangoObject.put("Verify", "false");
                    }
                    djangoObject.saveInBackground(new SaveInBackgroundListener() {
                        @Override
                        public void onSaved(Response response) {
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            CommentEditText.clearFocus();
                            CommentEditText.setText(null);
                            PostButton.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_SHORT).show();
                            if(ListTask.getStatus()==AsyncTask.Status.FINISHED){
                                ListTask = new AsyncAdapter();
                                ListTask.execute();
                            }
                        }

                        @Override
                        public void OnFailure(IOException e) {
                            e.printStackTrace();
                            PostButton.setEnabled(true);
                        }
                    });


                } else {
                    PostButton.setEnabled(true);
                }
            }
        });

        SuperImageView = findViewById(R.id.superImageView);
        DislikeImageView = findViewById(R.id.dislikeImageView);

        SuperImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikeAction();
            }
        });


        DislikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DislikeAction();
            }
        });

        LikesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikeAction();
            }
        });

        DislikesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DislikeAction();
            }
        });

       DjangoObject djangoObject = new DjangoObject(this,"/response_query","Thoughts_db");
       djangoObject.put("ObjectId",intent.getStringExtra("ThoughtId"));
       djangoObject.put("Username",new DjangoAuthenication(this).getUsername());
       djangoObject.saveInBackground(new SaveInBackgroundListener() {
           @Override
           public void onSaved(Response response) {
               try {
                   String response_query = response.body().string();
                   if(response_query.equals("In LikeList")){
                       Liked = true;
                       SuperImageView.setImageResource(R.mipmap.superafter);
                   }else if(response_query.equals("In DislikesList")){
                       Disliked = true;
                       DislikeImageView.setImageResource(R.mipmap.mfafter);
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

           @Override
           public void OnFailure(IOException e) {

           }
       });

        }


    private class AsyncAdapter extends AsyncTask<Void,Void,ListViewAdapter>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            Comments.clear();
            Verify.clear();
            SymbolComment.clear();
            CommentId.clear();
            ReplyCount.clear();
        }

        @Override
        protected ListViewAdapter doInBackground(Void... voids) {
            DjangoQuery djangoQuery = new DjangoQuery(getApplicationContext(),"/database_query","Comments_db");
            djangoQuery.whereEqualto("ThoughtId",intent.getStringExtra("ThoughtId"));
            djangoQuery.orderByAscending("CreatedAt");

            JSONArray jsonArray = djangoQuery.find();
            for(int i = 0 ; i < jsonArray.length() ; i++ ){
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Comments.add(jsonObject.getString("Comments"));
                    Verify.add(jsonObject.getString("Verify"));
                    CommentId.add(jsonObject.getString("ObjectId"));
                    SymbolComment.add(jsonObject.getString("Symbol"));
                    ReplyCount.add(jsonObject.getString("ReplyCount"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    ReplyCount.add("0");
                }
            }

            adapter = new ListViewAdapter(ThoughtDetail.this, Comments, Verify, CommentId, intent.getStringExtra("User"),intent.getStringExtra("ThoughtId"), SymbolComment, ReplyCount);
            return adapter;
        }

        @Override
        protected void onPostExecute(ListViewAdapter listViewAdapter) {
            super.onPostExecute(listViewAdapter);
            progressBar.setVisibility(View.INVISIBLE);
            CommentListView.setAdapter(adapter);
        }
    }

    public void LikeAction() {
        LikesText.setEnabled(false);
        SuperImageView.setEnabled(false);
        DislikesText.setEnabled(false);
        DislikeImageView.setEnabled(false);

        if (!Liked) {
            SuperImageView.setImageResource(R.mipmap.superafter);
            LikesText.setText(String.valueOf(Integer.parseInt(LikesText.getText().toString()) + 1));
            Liked = true;
            if (Disliked) {
                DislikeImageView.setImageResource(R.mipmap.mfbefore);
                DislikesText.setText(String.valueOf(Integer.parseInt(DislikesText.getText().toString()) - 1));
                Disliked = false;
            }
        } else {
            SuperImageView.setImageResource(R.mipmap.superbefore);
            LikesText.setText(String.valueOf(Integer.parseInt(LikesText.getText().toString()) - 1));
            Liked = false;
        }

        DjangoObject djangoObject = new DjangoObject(this,"/like_action", "Thoughts_db");
        djangoObject.put("Username", new DjangoAuthenication(this).getUsername());
        djangoObject.put("ObjectId", intent.getStringExtra("ThoughtId"));
        djangoObject.saveInBackground();

        LikesText.setEnabled(true);
        SuperImageView.setEnabled(true);
        DislikesText.setEnabled(true);
        DislikeImageView.setEnabled(true);
    }


public void DislikeAction(){
    LikesText.setEnabled(false);
    SuperImageView.setEnabled(false);
    DislikesText.setEnabled(false);
    DislikeImageView.setEnabled(false);
    if (!Disliked) {
        DislikeImageView.setImageResource(R.mipmap.mfafter);
        DislikesText.setText(String.valueOf(Integer.parseInt(DislikesText.getText().toString()) + 1));
        Disliked = true;
        if (Liked) {
            SuperImageView.setImageResource(R.mipmap.superbefore);
            LikesText.setText(String.valueOf(Integer.parseInt(LikesText.getText().toString()) - 1));
            Liked = false;
            }
    }else {
        DislikeImageView.setImageResource(R.mipmap.mfbefore);
        DislikesText.setText(String.valueOf(Integer.parseInt(DislikesText.getText().toString()) - 1));
        Disliked = false;
    }

    DjangoObject djangoObject = new DjangoObject(getApplicationContext(),"/dislike_action", "Thoughts_db");
    djangoObject.put("Username", new DjangoAuthenication(this).getUsername());
    djangoObject.put("ObjectId", intent.getStringExtra("ThoughtId"));
    djangoObject.saveInBackground();

    LikesText.setEnabled(true);
    SuperImageView.setEnabled(true);
    DislikesText.setEnabled(true);
    DislikeImageView.setEnabled(true);
    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onBackPressed() {
        if(ListTask!=null){ListTask.cancel(true);}
        super.onBackPressed();

    }

}