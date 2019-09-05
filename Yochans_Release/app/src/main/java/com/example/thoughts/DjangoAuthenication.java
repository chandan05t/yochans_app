package com.example.thoughts;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DjangoAuthenication {

    public DjangoAuthenication(Context context){
        this.context=context;
    }

    private Context context;
    private String Username;
    private String Password;

    public void setUsername(String Username){
        this.Username = Username;
    }

    public void setPassword(String Password){
        this.Password = Password;
    }

    public Boolean Login() {
        String LoginUrl = "http://"+context.getString(R.string.Django_URL)+":8000/yochans/authentication";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("Username", Username);
        builder.add("Password", Password);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(LoginUrl).post(requestBody).build();
        try {
            Response LoginResponse = client.newCall(request).execute();
            if (LoginResponse.body().string().equals("User Verified")) {
                SharedPreferences sharedPref = context.getSharedPreferences("Userbase",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Username",Username);
                editor.apply();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void LoginInBackground(final LoginInBackgroundListener listener){
        final String LoginUrl = "http://"+context.getString(R.string.Django_URL)+":8000/yochans/authentication";
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("Username", Username);
                builder.add("Password", Password);
                RequestBody requestBody = builder.build();
                Request request = new Request.Builder().url(LoginUrl).post(requestBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        if(listener != null){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.OnFailure(e);
                                }
                            });

                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(listener != null && response.body().string().equals("User Verified")){
                            SharedPreferences sharedPref = context.getSharedPreferences("Userbase",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("Username",Username);
                            editor.apply();
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.OnLoginChecked(true);
                                }
                            });
                        }else if(listener != null){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.OnLoginChecked(false);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public Boolean SignUp(){
        String SignupUrl = "http://"+context.getString(R.string.Django_URL)+":8000/yochans/signup";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("Username", Username);
        builder.add("Password", Password);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(SignupUrl).post(requestBody).build();
        try {
            Response SignupResponse = client.newCall(request).execute();
            if (SignupResponse.body().string().equals("SignUp Successfully")) {
                SharedPreferences sharedPref = context.getSharedPreferences("Userbase",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Username",Username);
                editor.apply();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void SignUpInBackground(final SignUpInBackgroundListener listener){
        final String SignupUrl = "http://"+context.getString(R.string.Django_URL)+":8000/yochans/signup";
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("Username", Username);
                builder.add("Password", Password);
                RequestBody requestBody = builder.build();
                Request request = new Request.Builder().url(SignupUrl).post(requestBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        if(listener != null){
                           new Handler(Looper.getMainLooper()).post(new Runnable() {
                               @Override
                               public void run() {
                                   listener.OnFailure(e);
                               }
                           });
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (listener != null && response.body().string().equals("SignUp Successfully")){
                            SharedPreferences sharedPref = context.getSharedPreferences("Userbase",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("Username",Username);
                            editor.apply();
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.OnSignUpChecked(true);
                                }
                            });

                        }else if(listener != null){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.OnSignUpChecked(false);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void LoginInBackground(){
        LoginInBackground(null);
    }

    public String getUsername(){
        SharedPreferences sharedPref = context.getSharedPreferences("Userbase",Context.MODE_PRIVATE);
        return sharedPref.getString("Username",null);
    }

    public void Logout(){
        SharedPreferences sharedPref = context.getSharedPreferences("Userbase",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Username",null);
        editor.apply();
    }
}
