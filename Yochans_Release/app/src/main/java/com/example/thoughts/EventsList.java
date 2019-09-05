package com.example.thoughts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EventsList extends AppCompatActivity {

    List<Bitmap> bmaps = new ArrayList<>();
    int objectsSize;
    ArrayList<String> eventName = new ArrayList<>();
    ArrayList<String> venue = new ArrayList<>();
    ArrayList<Date> date = new ArrayList<>();
    ArrayList<String> ImageAddress = new ArrayList<>();
    ArrayList<String> Description = new ArrayList<>();
    Boolean scroll = true;
    Boolean Addit = true;
    CustomList2 adapter;
    TextView ErrorText;
    TextView NoEventsText;
    RelativeLayout RelLoading;
    SwipeRefreshLayout swiperefresh;
    int i,itemno=0;
    ListView listView;
    LottieAnimationView Loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        ErrorText = (TextView)findViewById(R.id.errorText);
        ErrorText.setVisibility(View.INVISIBLE);
        RelLoading = (RelativeLayout)findViewById(R.id.relLoading);
        NoEventsText = (TextView)findViewById(R.id.noEventsText);
        NoEventsText.setVisibility(View.INVISIBLE);
        Loading = (LottieAnimationView)findViewById(R.id.animation_view);
        Loading.bringToFront();

        listView=(ListView)findViewById(R.id.listView);
        adapter = new CustomList2(EventsList.this,bmaps,eventName,venue,date,ImageAddress,Description);
        listView.setAdapter(adapter);

        Query();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0 && scroll)
                {
                    scroll =false;
                  AdditionEvent();
                }


            }


        });

        final ImageView popupMenu = (ImageView) findViewById(R.id.popupMenu);
        popupMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                popup.inflate(R.menu.popup_main);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item1:
                                Intent logintent = new Intent(getApplicationContext(), LoginEvents.class);
                                startActivity(logintent);
                                return true;
                        }
                        return false;
                    }
                });
            }
        });

        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        // Setup refresh listener which triggers new data loading
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listView.setEnabled(false);
                if(listView.getChildAt(0)!=null){
                    listView.getChildAt(0).setClickable(false);}
                if(listView.getChildAt(1)!=null){
                    listView.getChildAt(1).setClickable(false);}
                if(listView.getChildAt(2)!=null){
                    listView.getChildAt(2).setClickable(false);}
                if(listView.getChildAt(3)!=null){
                    listView.getChildAt(3).setClickable(false);}
                if(listView.getChildAt(4)!=null){
                    listView.getChildAt(4).setClickable(false);}
                bmaps.clear();
                eventName.clear();
                venue.clear();
                date.clear();
                ImageAddress.clear();
                Description.clear();
                itemno=0;
                Query();
            }
        });
        // Configure the refreshing colors
        swiperefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    DjangoQuery djangoQuery;
    JSONArray jsonArray;

    public void Query(){
        djangoQuery = new DjangoQuery(this,"/database_query","Events_db");
        djangoQuery.orderByAscending("Date");
        djangoQuery.findInBackground(new FindInBackgroundListener() {
            @Override
            public void OnFound(JSONArray DjangoJSONArray) {
                if (DjangoJSONArray.length() > 0){
                    objectsSize = DjangoJSONArray.length();
                    jsonArray = DjangoJSONArray;
                    AdditionEvent();
                }else{
                    NoEventsText.setText("There are No Events at this Time !!");
                    NoEventsText.setVisibility(View.VISIBLE);
                    Loading.cancelAnimation();
                    Loading.setVisibility(View.INVISIBLE);
                    RelLoading.setVisibility(View.INVISIBLE);
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void OnFailure(IOException e) {
                swiperefresh.setRefreshing(false);
                Loading.cancelAnimation();
                Loading.setVisibility(View.INVISIBLE);
                RelLoading.setVisibility(View.INVISIBLE);
                ErrorText.setText("There seems to be some network issues..");
                ErrorText.setVisibility(View.VISIBLE);
            }
        });
    }

    public void AdditionEvent(){
        for (i = 0; i < 3 && itemno <= objectsSize - 1 && Addit; i++) {
            try {
                Addit = false;
                final JSONObject jsonObject = jsonArray.getJSONObject(itemno);
                String filepath = jsonObject.getString("EventImage");
                filepath = filepath.substring(filepath.lastIndexOf("/")).trim();
                filepath = getCacheDir().toString() + filepath;
                File ImageFile = new File(filepath);
                if (ImageFile.exists()) {
                    try {
                        Bitmap ImageBitmap = BitmapFactory.decodeFile(filepath);
                        String event_date = jsonObject.getString("Date");
                        Date eventDate = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(event_date.replaceAll("Z$", ""));
                        bmaps.add(ImageBitmap);
                        eventName.add(jsonObject.getString("EventName"));
                        venue.add(jsonObject.getString("Venue"));
                        date.add(eventDate);
                        ImageAddress.add(filepath);
                        Description.add(jsonObject.getString("Description"));
                        swiperefresh.setRefreshing(false);
                        Loading.cancelAnimation();
                        Loading.setVisibility(View.INVISIBLE);
                        RelLoading.setVisibility(View.INVISIBLE);
                        scroll = true;
                        adapter.notifyDataSetChanged();
                        Addit = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        DjangoQuery.getImageInBackground(this, jsonObject.getString("EventImage"), new FindImageInBackgroundListener() {
                            @Override
                            public void OnFound(Bitmap ImageBitmap, String ImageAddr) {
                                bmaps.add(ImageBitmap);
                                try {
                                    eventName.add(jsonObject.getString("EventName"));
                                    venue.add(jsonObject.getString("Venue"));
                                    String event_date = jsonObject.getString("Date");
                                    Date eventDate = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(event_date.replaceAll("Z$", ""));
                                    date.add(eventDate);
                                    ImageAddress.add(ImageAddr);
                                    Description.add(jsonObject.getString("Description"));
                                    adapter.notifyDataSetChanged();
                                    swiperefresh.setRefreshing(false);
                                    Loading.cancelAnimation();
                                    Loading.setVisibility(View.INVISIBLE);
                                    RelLoading.setVisibility(View.INVISIBLE);
                                    scroll = true;
                                    Addit = true;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void OnFailure(IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            itemno++;
        }
    }
}
