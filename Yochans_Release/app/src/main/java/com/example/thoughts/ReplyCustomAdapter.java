package com.example.thoughts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chandan T on 14-03-2018.
 */

public class ReplyCustomAdapter extends ArrayAdapter {

    Activity context;
    ArrayList<String> Symbol;
    ArrayList<String> Reply;
    ArrayList<String> Verified;

    public ReplyCustomAdapter(Activity context,ArrayList<String> Symbol, ArrayList<String> Reply,ArrayList<String> Verified){
        super(context,R.layout.reply_container,Symbol);
        this.context=context;
        this.Symbol=Symbol;
        this.Reply=Reply;
        this.Verified=Verified;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.reply_container, null, true);

        TextView ReplyTextView = (TextView) rowView.findViewById(R.id.replyTextView);
        ReplyTextView.setText(Reply.get(position));
        ImageView UserVerifyImageView = (ImageView)rowView.findViewById(R.id.userVerifyImageView);
        if(Verified.get(position).equals("true")){
            UserVerifyImageView.setImageResource(R.mipmap.username_tick);
        }else{
            int resID = context.getResources().getIdentifier(Symbol.get(position), "drawable", context.getPackageName());
            UserVerifyImageView.setLayoutParams(new RelativeLayout.LayoutParams(pxFromDp(context, 25), pxFromDp(context, 25)));
            UserVerifyImageView.setImageResource(resID);
        }

        return rowView;
    }

    public static int pxFromDp(final Context context, final float dp) {
        return (int)(dp * context.getResources().getDisplayMetrics().density);
    }

}
