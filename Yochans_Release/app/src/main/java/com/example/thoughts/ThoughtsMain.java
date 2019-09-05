package com.example.thoughts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThoughtsMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SwipeRefreshLayout swiperefresh;
    LottieAnimationView Loading;
    ListView ThoughtListView;
    ArrayList<String> Thoughts = new ArrayList<String>();
    ArrayList<String> Backgrounds = new ArrayList<String>();
    ArrayList<String> Likes = new ArrayList<String>();
    ArrayList<String> Dislikes = new ArrayList<String>();
    List<Date> Time = new ArrayList<Date>();
    ArrayList<String> ThoughtId = new ArrayList<String>();
    ArrayList<String> User = new ArrayList<String>();
    ArrayList<String> Anonymous = new ArrayList<String>();
    int objectsSize;
    int itemno;
    int i;
    Boolean scroll = true;
    ThoughtListViewAdapter thoughtAdapter;
    RelativeLayout MainRelative;
    TextView ContentTextView;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoughts_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddThought.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ContentTextView = (TextView)findViewById(R.id.contentTextView);
        ThoughtListView = (ListView) findViewById(R.id.thoughtListView);
        thoughtAdapter = new ThoughtListViewAdapter(ThoughtsMain.this,Thoughts,Backgrounds,Likes,Dislikes,ThoughtId,User,Time,Anonymous);
        ThoughtListView.setAdapter(thoughtAdapter);

        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        // Setup refresh listener which triggers new data loading
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ThoughtListView.setEnabled(false);
                if(ThoughtListView.getChildAt(0)!=null){
                    ThoughtListView.getChildAt(0).setClickable(false);}
                if(ThoughtListView.getChildAt(1)!=null){
                    ThoughtListView.getChildAt(1).setClickable(false);}
                if(ThoughtListView.getChildAt(2)!=null){
                    ThoughtListView.getChildAt(2).setClickable(false);}
                if(ThoughtListView.getChildAt(3)!=null){
                    ThoughtListView.getChildAt(3).setClickable(false);}
                if(ThoughtListView.getChildAt(4)!=null){
                    ThoughtListView.getChildAt(4).setClickable(false);}
                if(ThoughtListView.getChildAt(5)!=null){
                    ThoughtListView.getChildAt(5).setClickable(false);}
                if(ThoughtListView.getChildAt(6)!=null){
                    ThoughtListView.getChildAt(6).setClickable(false);}
                if(ThoughtListView.getChildAt(7)!=null){
                    ThoughtListView.getChildAt(7).setClickable(false);}
                if(ThoughtListView.getChildAt(8)!=null){
                    ThoughtListView.getChildAt(8).setClickable(false);}
                if(ThoughtListView.getChildAt(9)!=null){
                    ThoughtListView.getChildAt(9).setClickable(false);}
                if(ThoughtListView.getChildAt(10)!=null){
                    ThoughtListView.getChildAt(10).setClickable(false);}
                ContentTextView.setVisibility(View.INVISIBLE);
                Thoughts.clear();
                Backgrounds.clear();
                Likes.clear();
                Dislikes.clear();
                Time.clear();
                ThoughtId.clear();
                User.clear();
                Anonymous.clear();
                itemno =0;
                Query();

            }
        });
        // Configure the refreshing colors
        swiperefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        Loading = (LottieAnimationView)findViewById(R.id.animation_view);
        Loading.bringToFront();


        Query();

        ThoughtListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0 && scroll && itemno != objectsSize)
                {
                    scroll = false;
                    Addition();
                }

            }
        });

    }

    DjangoQuery djangoQuery;
    JSONArray jsonArray;

    public void Query(){

        DjangoQuery query = new DjangoQuery(this,"/database_query","Thoughts_db");
        query.orderByDescending("Trendno");
        query.getFirst();
        query.findInBackground(new FindInBackgroundListener() {
            @Override
            public void OnFound(JSONArray DjangoJSONArray) {
                if(DjangoJSONArray.length() > 0){
                    try {
                        Thoughts.add(DjangoJSONArray.getJSONObject(0).getString("Thoughts"));
                        Backgrounds.add(DjangoJSONArray.getJSONObject(0).getString("Backgrounds"));
                        Likes.add(DjangoJSONArray.getJSONObject(0).getString("Likes"));
                        Dislikes.add(DjangoJSONArray.getJSONObject(0).getString("Dislikes"));
                        ThoughtId.add(DjangoJSONArray.getJSONObject(0).getString("ObjectId"));
                        User.add(DjangoJSONArray.getJSONObject(0).getString("User"));
                        Anonymous.add(DjangoJSONArray.getJSONObject(0).getString("Anonymous"));
                        String Time_S = DjangoJSONArray.getJSONObject(0).getString("Time");
                        Date Times = null;
                        try {
                            Times = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(Time_S.replaceAll("Z$", ""));
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        Time.add(Times);

                        QueryII();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    ContentTextView.setVisibility(View.VISIBLE);
                    swiperefresh.setRefreshing(false);
                    Loading.cancelAnimation();
                    Loading.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void OnFailure(IOException e) {

            }
        });
    }

    public void QueryII(){
        djangoQuery = new DjangoQuery(this,"/database_query","Thoughts_db");
        djangoQuery.orderByDescending("CreatedAt");
        djangoQuery.findInBackground(new FindInBackgroundListener() {
            @Override
            public void OnFound(JSONArray DjangoJSONArray) {
                if(DjangoJSONArray.length() > 0){
                    objectsSize = DjangoJSONArray.length();
                    jsonArray = DjangoJSONArray;
                    Addition();
                }else{
                    ContentTextView.setVisibility(View.VISIBLE);
                    swiperefresh.setRefreshing(false);
                    Loading.cancelAnimation();
                    Loading.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void OnFailure(IOException e) {

            }
        });
    }

public void Addition(){
    for(i=0; i < 10 && itemno <= objectsSize-1 ;i++){
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(itemno);
            Thoughts.add(jsonObject.getString("Thoughts"));
            Backgrounds.add(jsonObject.getString("Backgrounds"));
            Likes.add(jsonObject.getString("Likes"));
            Dislikes.add(jsonObject.getString("Dislikes"));
            ThoughtId.add(jsonObject.getString("ObjectId"));
            User.add(jsonObject.getString("User"));
            Anonymous.add(jsonObject.getString("Anonymous"));
            String Time_S = jsonObject.getString("Time");
            Date Times = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(Time_S.replaceAll("Z$", ""));
            Time.add(Times);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch(java.text.ParseException e){
            e.printStackTrace();
    }
        itemno ++;
    }
    thoughtAdapter.notifyDataSetChanged();
    swiperefresh.setRefreshing(false);
    ThoughtListView.setEnabled(true);
    Loading.cancelAnimation();
    Loading.setVisibility(View.INVISIBLE);
    scroll = true;

}



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.thoughts_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_addthought) {
            Intent intent = new Intent(getApplicationContext(),AddThought.class);
            startActivity(intent);

         } else if (id == R.id.nav_about) {
            Intent intent = new Intent(getApplicationContext(),About.class);
            startActivity(intent);

        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(getApplicationContext(),EventsMain.class);
            startActivity(intent);

         } else if (id == R.id.nav_logout) {
            new DjangoAuthenication(this).Logout();
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
