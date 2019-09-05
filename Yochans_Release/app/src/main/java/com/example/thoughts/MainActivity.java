package com.example.thoughts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    LottieAnimationView AnimationView;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        AnimationView = (LottieAnimationView)findViewById(R.id.animation);

        if (Build.VERSION.SDK_INT < 23) {

            if(internetConnectionAvailable(3000)){
                checkSpecialMessege();
            }else{
                Alert();
            }


        } else {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 2);
            } else {

                if(internetConnectionAvailable(3000)){
                    checkSpecialMessege();
                }else{
                    Alert();
                }

            }

        }

    }


    public void Alert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Internet Connection not detected  :-( ");
        alertDialogBuilder.setPositiveButton("Close App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new CountDownTimer(1500, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        if(internetConnectionAvailable(3000)){
                            checkSpecialMessege();
                        }else{
                            Alert();
                        }
                        AnimationView.animate();
                    }
                }.start();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        AnimationView.cancelAnimation();
    }

    private boolean internetConnectionAvailable(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
            Alert();
        } catch (ExecutionException e) {
            Alert();
        } catch (TimeoutException e) {
            Alert();
        }
        return inetAddress != null && !inetAddress.equals("");

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {

                    if(internetConnectionAvailable(3000)){
                        checkSpecialMessege();
                    }else{
                        Alert();
                    }

                }
            }
        }

    }

    public void timer(){
        new CountDownTimer(1000, 500) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }


    public void checkSpecialMessege(){
        DjangoQuery query = new DjangoQuery(getApplicationContext(),"/database_query","SpecialMsg_db");
        query.findInBackground(new FindInBackgroundListener() {
            @Override
            public void OnFound(JSONArray DjangoJSONArray) {
                if (DjangoJSONArray.length()!=0){
                    Intent splintent = new Intent(getApplicationContext(),ShowActivity.class);
                    startActivity(splintent);
                    finish();
                }else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            timer();
                        }
                    });
                }
            }

            @Override
            public void OnFailure(IOException e) {
               e.printStackTrace();
               new Handler(Looper.getMainLooper()).post(new Runnable() {
                   @Override
                   public void run() {
                       timer();
                   }
               });
            }
        });
    }

}

