package com.example.thoughts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowActivity extends AppCompatActivity {

    ImageView imageView;
    LottieAnimationView animationView;
    int dur,i;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        imageView = (ImageView)findViewById(R.id.imageView);
        animationView = (LottieAnimationView)findViewById(R.id.animationView);
        progressBar = findViewById(R.id.progressBarH);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        DjangoQuery djangoQuery = new DjangoQuery(this,"/database_query","SpecialMsg_db");
        djangoQuery.findInBackground(new FindInBackgroundListener() {
            @Override
            public void OnFound(final JSONArray DjangoJSONArray) {
                try {
                    final JSONObject jsonObject = DjangoJSONArray.getJSONObject(0);
                    String filepath = jsonObject.getString("SpecialMsg");
                    filepath = filepath.substring(filepath.lastIndexOf("/")).trim();
                    filepath = getCacheDir().toString() + filepath;
                    File ImageFile = new File(filepath);
                    if (ImageFile.exists()) {
                        Bitmap ImageBitmap = BitmapFactory.decodeFile(filepath);
                        imageView.setImageBitmap(ImageBitmap);
                        animationView.setVisibility(View.INVISIBLE);
                        try {
                            if (jsonObject.get("Duration") != null) {
                                dur = (int) DjangoJSONArray.getJSONObject(0).get("Duration");
                            } else {
                                dur = 3;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        timer(dur);
                    } else {
                        try {
                            DjangoQuery.getImageInBackground(getApplicationContext(), DjangoJSONArray.getJSONObject(0).getString("SpecialMsg"), new FindImageInBackgroundListener() {
                                @Override
                                public void OnFound(Bitmap ImageBitmap, String ImageAddress) {
                                    imageView.setImageBitmap(ImageBitmap);
                                    animationView.setVisibility(View.INVISIBLE);
                                    try {
                                        if (jsonObject.get("Duration") != null) {
                                            dur = (int)jsonObject.get("Duration");
                                        } else {
                                            dur = 3;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    timer(dur);
                                }

                                @Override
                                public void OnFailure(IOException e) {
                                    timer(1);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            timer(1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    timer(1);
                }
            }

            @Override
            public void OnFailure(IOException e) {
                timer(1);
            }
        });
    }

    public void timer(final int dur){
        new CountDownTimer(dur * 1000, 50) {

            public void onTick(final long millisUntilFinished) {
                i++;
                progressBar.setProgress((int)i*100/(dur*20));
            }

            public void onFinish() {
                progressBar.setProgress(100);
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
