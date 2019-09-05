package com.example.thoughts;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.airbnb.lottie.L;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class DjangoQuery {

    public DjangoQuery(Context context,String Function, String Table){
        this.Function = Function;
        this.Table = Table;
        this.context = context;
    }

    private Context context;
    private String Function;
    private String Table;
    private String key;
    private String keyvalue;
    private String AscendingKey ;
    private String DescendingKey ;
    private String First;

    public JSONArray find(){
        String url = "http://"+ context.getString(R.string.Django_URL)+":8000/yochans";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("Database_Table", Table);
        if(key!=null && keyvalue!=null){
            builder.add("Key",key);
            builder.add("Key_Value",keyvalue);
        }
        if(AscendingKey!=null){
            builder.add("AscendingKey",AscendingKey);
        }else if(DescendingKey!=null){
            builder.add("DescendingKey",DescendingKey);
        }
        if(First!=null){
            builder.add("First",First);
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url.concat(Function)).post(requestBody).build();
        try {
            Response FindResponse = client.newCall(request).execute();
            String jsonData = FindResponse.body().string();
            JSONArray jsonArray = new JSONArray(jsonData);
            return jsonArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }

    }

    public void findInBackground(final FindInBackgroundListener listener){
        final String url = "http://"+ context.getString(R.string.Django_URL)+":8000/yochans";
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("Database_Table", Table);
                if(key!=null && keyvalue!=null){
                    builder.add("Key",key);
                    builder.add("Key_Value",keyvalue);
                }
                if(AscendingKey != null){
                    builder.add("AscendingKey",AscendingKey);
                }else if(DescendingKey != null){
                    builder.add("DescendingKey",DescendingKey);
                }
                if(First!=null){
                    builder.add("First",First);
                }
                RequestBody requestBody = builder.build();
                Request request = new Request.Builder().url(url.concat(Function)).post(requestBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        if(listener != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.OnFailure(e);
                                }
                            });
                        }
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if(listener != null) {
                            String jsonData = response.body().string();
                            final JSONArray jsonArray ;
                            try {
                                jsonArray = new JSONArray(jsonData);
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.OnFound(jsonArray);

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });
            }
        });
    }

    public static Bitmap getImage(Context context,String Location){
        String image_url = "http://"+context.getString(R.string.Django_URL)+":8000/yochans/get_image";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("Location",Location);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(image_url).post(requestBody).build();
        try {
            Response ImageResponse = client.newCall(request).execute();
            File ImageFile = new File(Environment.getDownloadCacheDirectory(),ImageResponse.header("Name"));
            BufferedSink sink = Okio.buffer(Okio.sink(ImageFile));
            sink.writeAll(ImageResponse.body().source());
            sink.close();
            String ImageFilePath = ImageFile.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(ImageFilePath);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void getImageInBackground(final Context context, final String Location, final FindImageInBackgroundListener listener){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String image_url = "http://"+context.getString(R.string.Django_URL)+":8000/yochans/get_image";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("Location",Location);
                RequestBody requestBody = builder.build();
                Request request = new Request.Builder().url(image_url).post(requestBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        if (listener != null) {
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
                        if (listener != null) {
                            System.out.println("fileLocation " + response.body().source().toString());
                            final File ImageFile = new File(context.getCacheDir(), response.header("Name"));
                            BufferedSink sink = Okio.buffer(Okio.sink(ImageFile));
                            sink.writeAll(response.body().source());
                            sink.close();
                            final String ImageFilePath = ImageFile.getPath();
                            final Bitmap bitmap = BitmapFactory.decodeFile(ImageFilePath);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.OnFound(bitmap,ImageFilePath);
                                }
                            });

                        }
                    }
                });
            }
        });
    }

    public void whereEqualto(String key, String keyvalue){
        this.key=key;
        this.keyvalue=keyvalue;
    }

    public void findInBackground(){
        findInBackground(null);
    }

    public static void getImageInBackground(Context context,String Location){
        getImageInBackground(context,Location,null);
    }

    public void orderByAscending(String FieldName){
        this.AscendingKey=FieldName;
    }

    public void orderByDescending(String FieldName){
        this.DescendingKey=FieldName;
    }

    public void getFirst(){
        this.First="Has been Assigned";
    }

}