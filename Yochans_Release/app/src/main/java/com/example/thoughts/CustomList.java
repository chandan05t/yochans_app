package com.example.thoughts;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Chandan T on 08-03-2018.
 */

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final List<Bitmap> bmaps;
    private final ArrayList<String> eventName;
    private final ArrayList<String> venue;
    private final ArrayList<Date> date;
    private final ArrayList<String> imageAddr;
    private final ArrayList<String> Description;

    public CustomList(Activity context, List<Bitmap> bmaps, ArrayList<String> eventName,ArrayList<String> venue,ArrayList<Date> date,ArrayList<String> imageAddr,ArrayList<String> Description){
        super(context, R.layout.listlayout,eventName);
        this.context=context;
        this.eventName=eventName;
        this.bmaps=bmaps;
        this.venue=venue;
        this.date=date;
        this.imageAddr=imageAddr;
        this.Description=Description;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listlayout, null, true);

        TextView mainText = (TextView) rowView.findViewById(R.id.eventText);
        ImageView eventImage = (ImageView) rowView.findViewById(R.id.eventImage);
        float scale = eventImage.getResources().getDisplayMetrics().density;
        eventImage.getLayoutParams().height = Math.min((int) (bmaps.get(position).getHeight() * getScreenWidth() / bmaps.get(position).getWidth()), (int) (getScreenHeight() * 0.50));
        eventImage.getLayoutParams().width = (int) (getScreenWidth());
        eventImage.setImageBitmap(bmaps.get(position));
        mainText.setText(eventName.get(position));
        TextView venueText = (TextView) rowView.findViewById(R.id.venueText);
        TextView timeText = (TextView) rowView.findViewById(R.id.timeText);
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        final DateFormat dateFormat = new SimpleDateFormat("hh:mm a, E-dd MMM yyyy");
        dateFormat.setTimeZone(timeZone);
        venueText.setText("Venue : " + venue.get(position));
        timeText.setText(dateFormat.format(date.get(position)));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),EventsDetails.class);
                intent.putExtra("Venue",venue.get(position));
                intent.putExtra("EventName",eventName.get(position));
                intent.putExtra("Date",dateFormat.format(date.get(position)).toString());
                intent.putExtra("ImageAddress",imageAddr.get(position));
                intent.putExtra("Description",Description.get(position));
                context.startActivity(intent);
            }
        });

        return rowView;
    }

}
