package com.example.thoughts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;

/**
 * Created by Chandan T on 04-03-2018.
 */

public class ThoughtListViewAdapter extends ArrayAdapter {

    private Activity context;
    private ArrayList<String> Thoughts;
    private ArrayList<String> Backgrounds;
    private ArrayList<String> Likes;
    private ArrayList<String> Dislikes;
    private ArrayList<String> ThoughtId;
    private ArrayList<String> User;
    private ArrayList<String> Anonymous;
    private List<Date> Time;
    ArrayList<String> ET = new ArrayList<>();
    ImageView deleteImageView;

    public ThoughtListViewAdapter(Activity context, ArrayList<String> Thoughts, ArrayList<String> Backgrounds, ArrayList<String> Likes, ArrayList<String> Dislikes, ArrayList<String> ThoughtId, ArrayList<String> User, List<Date> Time, ArrayList<String> Anonymous){
        super(context,R.layout.thought_gridview,Thoughts);
        this.context = context;
        this.Thoughts=Thoughts;
        this.Backgrounds=Backgrounds;
        this.Likes=Likes;
        this.Dislikes=Dislikes;
        this.ThoughtId=ThoughtId;
        this.User=User;
        this.Time=Time;
        this.Anonymous=Anonymous;
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
        View rowView ;
        if(position == 0){
            rowView = inflater.inflate(R.layout.list_thoughts_trending_container, null, true);
        }else{
            rowView = inflater.inflate(R.layout.list_thoughts_container, null, true);
        }

        RelativeLayout RelLay = (RelativeLayout) rowView.findViewById(R.id.relLay);
        TextView ThoughtTextView = (TextView) rowView.findViewById(R.id.thoughtTextView);
        TextView LikesTextView = (TextView) rowView.findViewById(R.id.superTextView);
        TextView DislikesTextView = (TextView) rowView.findViewById(R.id.dislikeTextView);
        TextView UserTextView = (TextView) rowView.findViewById(R.id.userTextView);
        UserTextView.setVisibility(View.INVISIBLE);
        if(position != 0) {
            deleteImageView = (ImageView) rowView.findViewById(R.id.deleteImageView);
            deleteImageView.setEnabled(false);
            deleteImageView.setVisibility(View.INVISIBLE);
        }
        CircleImageView ThemeImageView = (CircleImageView) rowView.findViewById(R.id.themeImageView);
        final Intent intent = new Intent(getContext(),ThoughtDetail2.class);

        if(Anonymous.get(position).equals("false")){
            UserTextView.setVisibility(View.VISIBLE);
            if(User.get(position).length() < 20) {
                UserTextView.setText("by : " + User.get(position));
            }else{
                UserTextView.setText("by : " + User.get(position).substring(0,20) + "...");
            }
        }

        String[] lines = Thoughts.get(position).split("\r\n|\r|\n");
        if(Thoughts.get(position).contains("\r\n")|| Thoughts.get(position).contains("\r")||Thoughts.get(position).contains("\n")){
            if(lines[0].length() < 55) {
                ThoughtTextView.setText(lines[0]);
            }else{
                ThoughtTextView.setText(lines[0].substring(0,55) + "...");
            }
        }else if(Thoughts.get(position).length() < 55) {
            ThoughtTextView.setText(Thoughts.get(position));
        }else{
            ThoughtTextView.setText(Thoughts.get(position).substring(0,55) + "...");
        }
        LikesTextView.setText(Likes.get(position).concat(" /"));
        DislikesTextView.setText(Dislikes.get(position));
        TextView TimeTextView = (TextView)rowView.findViewById(R.id.timeTextView);

        if(new DjangoAuthenication(getContext()).getUsername().equals(User.get(position))){
            if(position != 0) {
                deleteImageView.setEnabled(true);
                deleteImageView.setVisibility(View.VISIBLE);
            }
        }

        if(position != 0) {
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Alert(ThoughtId.get(position));
                }
            });
        }

        if(((int)((Calendar.getInstance().getTimeInMillis() - Time.get(position).getTime())/3600000))>0){
            ET.add(position,Integer.toString(((int)((Calendar.getInstance().getTimeInMillis() - Time.get(position).getTime())/3600000)))+" h");
        }else if(((int)((Calendar.getInstance().getTimeInMillis() - Time.get(position).getTime())/60000))>0){
            ET.add(position,Integer.toString(((int)((Calendar.getInstance().getTimeInMillis() - Time.get(position).getTime())/60000)))+" m");
        }else{
            ET.add(position,"0 m");
        }
        intent.putExtra("Time",ET.get(position));
        TimeTextView.setText(ET.get(position).concat(" /"));
        int resID = context.getResources().getIdentifier(Backgrounds.get(position).concat("_ss"), "drawable", context.getPackageName());

        ThemeImageView.setImageResource(resID);

        RelLay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            intent.putExtra("Thought",Thoughts.get(position));
            intent.putExtra("Likes",Likes.get(position));
            intent.putExtra("Dislikes",Dislikes.get(position));
            intent.putExtra("Background",Backgrounds.get(position));
            intent.putExtra("ThoughtId",ThoughtId.get(position));
            intent.putExtra("User",User.get(position));
            intent.putExtra("Anonymous",Anonymous.get(position));
            context.startActivity(intent);
        }
    });


        return rowView;
    }

    public void Alert(final String ThoughtId){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Are You Sure?");
        alertDialogBuilder.setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                DeleteThought(ThoughtId);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void DeleteThought(String ThoughtId){
        DjangoObject djangoObject = new DjangoObject(getContext(),"/delete_thought","Thoughts_db");
        djangoObject.put("ThoughtId",ThoughtId);
        djangoObject.saveInBackground(new SaveInBackgroundListener() {
            @Override
            public void onSaved(Response response) {
                Intent Tintent = new Intent(getContext(),ThoughtsMain.class);
                context.startActivity(Tintent);
                context.finish();
            }

            @Override
            public void OnFailure(IOException e) {
                e.printStackTrace();
            }
        });
    }

}
