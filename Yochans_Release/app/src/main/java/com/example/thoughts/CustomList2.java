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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chandan T on 08-03-2018.
 */

public class CustomList2 extends ArrayAdapter<String> {

    private final Activity context;
    private final List<Bitmap> bmaps;
    private final ArrayList<String> eventName;
    private final ArrayList<String> venue;
    private final ArrayList<Date> date;
    private final ArrayList<String> imageAddr;
    private final ArrayList<String> Description;

    public CustomList2(Activity context, List<Bitmap> bmaps, ArrayList<String> eventName, ArrayList<String> venue, ArrayList<Date> date, ArrayList<String> imageAddr, ArrayList<String> Description){
        super(context, R.layout.listlayout2,eventName);
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
        View rowView = inflater.inflate(R.layout.listlayout2, null, true);

        TextView mainText = (TextView) rowView.findViewById(R.id.eventText);
        CircleImageView EventImage2 = rowView.findViewById(R.id.eventImage2);
        EventImage2.setImageBitmap(bmaps.get(position));
        if(eventName.get(position).length() < 25) {
            mainText.setText(eventName.get(position));
        }else{
            mainText.setText(eventName.get(position).substring(0,25) + "...");
        }
        TextView venueText = (TextView) rowView.findViewById(R.id.venueText);
        if(venue.get(position).length() < 25) {
            venueText.setText(venue.get(position));
        }else{
            venueText.setText(venue.get(position).substring(0,25) + "...");
        }
        TextView timeText = (TextView) rowView.findViewById(R.id.timeText);
        final DateFormat dateFormat = new SimpleDateFormat("hh:mm a, E-dd MMM yyyy");
        timeText.setText(dateFormat.format(date.get(position)));
        final Intent intent = new Intent(getContext(),EventsDetails.class);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
