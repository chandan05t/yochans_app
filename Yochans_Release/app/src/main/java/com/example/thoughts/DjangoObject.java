package com.example.thoughts;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DjangoObject {

    public DjangoObject(Context context,String Function, String Table){
        this.Function = Function;
        this.Table = Table;
        this.context = context;
    }

    private Context context;
    private String Function;
    private String Table;
    private ArrayList<String> ObjectArrayList = new ArrayList();
    private ArrayList<String> ValueArrayList = new ArrayList();
    private ArrayList<File> djangoObjectFileList = new ArrayList<>();
    private ArrayList<String> djangoObjectFileField = new ArrayList<>();
    private ArrayList<String> djangoObjectFileNameList = new ArrayList<>();
    private int i;

    public void put(String Object, String Value){
        ObjectArrayList.add(Object);
        ValueArrayList.add(Value);
    }

    public void putFile(String FileField, File file){
        djangoObjectFileField.add(FileField);
        djangoObjectFileList.add(file);
        djangoObjectFileNameList.add(file.getName());
    }

    public void putFile(String FileField, File file, String Filename ){
        djangoObjectFileField.add(FileField);
        djangoObjectFileList.add(file);
        djangoObjectFileNameList.add(Filename);
    }


    public Response save(){
        String url = "http://"+ context.getString(R.string.Django_URL) +":8000/yochans";
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (i=0;i<ObjectArrayList.size();i++){
            builder.addFormDataPart(ObjectArrayList.get(i),ValueArrayList.get(i));
        }
        for (i=0;i<djangoObjectFileField.size();i++){
            builder.addFormDataPart(djangoObjectFileField.get(i), djangoObjectFileNameList.get(i),
                    RequestBody.create(MediaType.parse("multipart/form-data"), djangoObjectFileList.get(i)));
        }
        builder.addPart(builder.build());
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url.concat(Function)).post(requestBody).build();
        try {
           Response SaveResponse = client.newCall(request).execute();
            return SaveResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveInBackground(final SaveInBackgroundListener listener){
        final String url = "http://"+ context.getString(R.string.Django_URL) +":8000/yochans";
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                for (i=0;i<ObjectArrayList.size();i++){
                    builder.addFormDataPart(ObjectArrayList.get(i),ValueArrayList.get(i));
                }
                for (i=0;i<djangoObjectFileField.size();i++){
                    builder.addFormDataPart(djangoObjectFileField.get(i), djangoObjectFileNameList.get(i),
                            RequestBody.create(MediaType.parse("multipart/form-data"), djangoObjectFileList.get(i)));
                }
                builder.addPart(builder.build());
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
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onSaved(response);
                            }
                        });

                        }
                    }
                });
            }
        });

    }

    public static Response SendImage(Context context, File ImageFile,String FileName){
        final String ImageUrl = "http://"+ context.getString(R.string.Django_URL)+":8000/yochans/upload_image";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("ImageFile", FileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), ImageFile))
                .build();
        Request request = new Request.Builder().url(ImageUrl).post(formBody).build();
        try {
            Response ImageResponse = client.newCall(request).execute();
            return ImageResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void SendImageInBackground(Context context,File ImageFile,String FileName, final SendImageInBackgroundListener listener){
        final String ImageUrl = "http://"+ context.getString(R.string.Django_URL)+":8000/yochans/upload_image";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("ImageFile", FileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), ImageFile))
                .build();
        Request request = new Request.Builder().url(ImageUrl).post(formBody).build();
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
            public void onResponse(Call call, final Response response) throws IOException {
                if(listener != null){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listener.OnImageSaved(response);
                        }
                    });
                }
            }
        });
    }

    public void saveInBackground(){
        saveInBackground(null);
    }

    public void SendImage(Context context,File ImageFile){
        SendImage(context,ImageFile,ImageFile.getName());
    }

    public static void SendImageInBackground(Context context,File ImageFile){
        SendImageInBackground(context,ImageFile,ImageFile.getName(),null);
    }

    public static void SendImageInBackground(Context context, File ImageFile,String FileName){
        SendImageInBackground(context,ImageFile,FileName,null);
    }
}
