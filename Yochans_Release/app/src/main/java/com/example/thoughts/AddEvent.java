package com.example.thoughts;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import okhttp3.Response;

import static okhttp3.internal.Util.UTC;

public class AddEvent extends AppCompatActivity {

    ImageView EventImage;
    EditText EventName;
    EditText Venue;
    EditText DD;
    EditText MMDate;
    EditText YYYY;
    EditText HH;
    EditText MMTime;
    EditText Description;
    File Ufile;

    private int PICK_IMAGE_REQUEST = 1;

    RelativeLayout RelativeLoading;
    RelativeLayout RelativeMain;

    Button AddImageButton ;
    Button ChangeButton ;
    Button AddEventButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        RelativeLoading = (RelativeLayout)findViewById(R.id.relativeLoading);
        RelativeMain = (RelativeLayout)findViewById(R.id.relativeMain);

        AddImageButton = (Button)findViewById(R.id.addImageButton);
        ChangeButton = (Button)findViewById(R.id.changeButton);
        AddEventButton = (Button)findViewById(R.id.addEventButton);

        RelativeLoading.setVisibility(View.INVISIBLE);

        EventName = (EditText)findViewById(R.id.eventNameText);
        Venue = (EditText)findViewById(R.id.venueText);
        DD = (EditText)findViewById(R.id.dd);
        MMDate = (EditText)findViewById(R.id.mmDate);
        YYYY = (EditText)findViewById(R.id.yyyy);
        HH = (EditText)findViewById(R.id.hh);
        MMTime = (EditText)findViewById(R.id.mmTime);
        Description = (EditText)findViewById(R.id.descriptionText);

        AddImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT < 23){

                    getImage();

                }else {

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(AddEvent.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {

                        getImage();
                    }

                }

            }
        });

        ChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        AddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( EventImage != null && !(EventName.getText().toString().isEmpty()) && !(Venue.getText().toString().isEmpty()) &&
                        !(DD.getText().toString().isEmpty()) && !(MMDate.getText().toString().isEmpty()) &&
                        !(YYYY.getText().toString().isEmpty()) && !(HH.getText().toString().isEmpty()) && !(MMTime.getText().toString().isEmpty() )){

                    Alert();

                }else{
                    Toast.makeText(getApplicationContext(),"Enter all the Details",Toast.LENGTH_LONG).show();
                }
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                EventImage = (ImageView) findViewById(R.id.EventImage);

                Ufile = new File(FilePath.getFilePath(getApplicationContext(),uri));
                int file_size = Integer.parseInt(String.valueOf(Ufile.length()/1024));

                EventImage.setImageBitmap(ImageCompress.getModImage(bitmap,file_size,Ufile));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void Alert(){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are You Sure?");
        alertDialogBuilder.setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                AddEventFinal();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(),AddEvent.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void AddEventFinal(){

        RelativeLoading.setVisibility(View.VISIBLE);
        RelativeMain.setAlpha((float)0.1);
        AddEventButton.setEnabled(false);
        ChangeButton.setEnabled(false);
        EventName.setEnabled(false);
        Venue.setEnabled(false);
        DD.setEnabled(false);
        MMDate.setEnabled(false);
        YYYY.setEnabled(false);
        HH.setEnabled(false);
        MMTime.setEnabled(false);
        Description.setEnabled(false);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(YYYY.getText().toString()),Integer.parseInt(MMDate.getText().toString())-1,
                Integer.parseInt(DD.getText().toString()),Integer.parseInt(HH.getText().toString()),
                Integer.parseInt(MMTime.getText().toString()),0);
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        calendar.setTimeZone(timeZone);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        dateFormat.setTimeZone(timeZone);
        dateFormat.format(calendar.getTime());


            final Bitmap uploadBitmap = ((BitmapDrawable)EventImage.getDrawable()).getBitmap();
            final DjangoObject djangoObject = new DjangoObject(this,"/add_event","Events_db");
            djangoObject.put("EventName",EventName.getText().toString());
            djangoObject.put("Venue",Venue.getText().toString());
            djangoObject.put("Date",Float.toString(calendar.getTimeInMillis()));
            if(Description.getText().toString() != null){
                djangoObject.put("Description",Description.getText().toString());
            }
            djangoObject.putFile("ImageFile",FilefromBitmap(uploadBitmap),Ufile.getName());
            djangoObject.saveInBackground(new SaveInBackgroundListener() {
                @Override
                public void onSaved(Response response) {
                    Toast.makeText(getApplicationContext(),"Event Details Uploaded",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), EventsList.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void OnFailure(IOException e) {
                    Toast.makeText(getApplicationContext(),"Failed to Upload, Please try again",Toast.LENGTH_LONG).show();
                    RelativeLoading.setVisibility(View.INVISIBLE);
                    RelativeMain.setAlpha(1);
                    AddEventButton.setEnabled(true);
                    ChangeButton.setEnabled(true);
                    EventName.setEnabled(true);
                    Venue.setEnabled(true);
                    DD.setEnabled(true);
                    MMDate.setEnabled(true);
                    YYYY.setEnabled(true);
                    HH.setEnabled(true);
                    MMTime.setEnabled(true);
                    Description.setEnabled(true);
                }
            });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                getImage();

            }
        }
    }

    public void getImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        ChangeButton.setAlpha(1);
        AddImageButton.setAlpha(0);
    }

    public File FilefromBitmap(Bitmap ImageBitmap){
        File ImageFile = new File(getApplicationContext().getCacheDir(),"ImageFile");
        try {
            ImageFile.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageBitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(ImageFile);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return ImageFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

