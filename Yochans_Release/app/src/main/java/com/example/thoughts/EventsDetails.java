package com.example.thoughts;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class EventsDetails extends AppCompatActivity {

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_details);

        Intent intent = getIntent();
        final String Venue = intent.getStringExtra("Venue");
        final String User = intent.getStringExtra("EventName");
        String ObjectId = intent.getStringExtra("ObjectId");
        String Date = intent.getStringExtra("Date");
        String ImageAddress = intent.getStringExtra("ImageAddress");
        String Descriptioin = intent.getStringExtra("Description");

        Bitmap ImageBitmap = BitmapFactory.decodeFile(ImageAddress);
        ImageView eventImage = (ImageView) findViewById(R.id.eventImage);
        eventImage.getLayoutParams().height = (int) (ImageBitmap.getHeight() * getScreenWidth() / ImageBitmap.getWidth());
        eventImage.getLayoutParams().width = (int) (getScreenWidth());
        eventImage.setImageBitmap(ImageBitmap);
        TextView EventNameText = (TextView) findViewById(R.id.eventNameText);
        EventNameText.setText(User);
        TextView VenueText = (TextView) findViewById(R.id.venueText);
        VenueText.setText(Venue);
        TextView TimeText = (TextView)findViewById(R.id.timeText);
        TimeText.setText(Date);
        TextView DescriptionText = (TextView) findViewById(R.id.descriptionText);
        DescriptionText.setText(Descriptioin);


    }
}