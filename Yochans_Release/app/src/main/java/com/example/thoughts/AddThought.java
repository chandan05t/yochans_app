package com.example.thoughts;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Response;

public class AddThought extends AppCompatActivity {

    EditText ThoughtText;
    EditText ThemeText;
    ImageView imageView;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    ImageView imageView7;
    ImageView imageView8;
    ImageView imageView9;
    ImageView imageView10;
    ImageView imageView11;
    ImageView imageView12;
    ImageView imageView13;
    ImageView imageView14;
    ImageView imageView15;
    ImageView imageView16;
    ImageView imageView17;
    ImageView imageView18;
    ImageView imageView19;
    ImageView imageView20;
    ImageView imageView21;
    ImageView imageView22;
    ImageView imageView23;
    ImageView imageView24;
    ImageView imageView25;
    ImageView imageView26;
    ImageView imageView27;
    Button Post;
    int i;
    ArrayList<String> Symbols = new ArrayList<>();
    Switch Anonymous;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thought);

        Anonymous = (Switch)findViewById(R.id.anonymousSwitch);

        for(i=1;i<=38;i++){
            Symbols.add("kan"+Integer.toString(i));
            if(i<=25){
                Symbols.add("gre"+Integer.toString(i));
            }
            if(i<=23){
                Symbols.add("mis"+Integer.toString(i));
            }
        }

        ThoughtText = (EditText)findViewById(R.id.thoughtText);
        ThemeText = (EditText)findViewById(R.id.themeText);

        ThemeText.setEnabled(false);

        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);
        imageView10 = findViewById(R.id.imageView10);
        imageView11 = findViewById(R.id.imageView11);
        imageView12 = findViewById(R.id.imageView12);
        imageView13 = findViewById(R.id.imageView13);
        imageView14 = findViewById(R.id.imageView14);
        imageView15 = findViewById(R.id.imageView15);
        imageView16 = findViewById(R.id.imageView16);
        imageView17 = findViewById(R.id.imageView17);
        imageView18 = findViewById(R.id.imageView18);
        imageView19 = findViewById(R.id.imageView19);
        imageView20 = findViewById(R.id.imageView20);
        imageView21 = findViewById(R.id.imageView21);
        imageView22 = findViewById(R.id.imageView22);
        imageView23 = findViewById(R.id.imageView23);
        imageView24 = findViewById(R.id.imageView24);
        imageView25 = findViewById(R.id.imageView25);
        imageView26 = findViewById(R.id.imageView26);
        imageView27 = findViewById(R.id.imageView27);
        Post = (Button)findViewById(R.id.postButton);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("IITG");
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Barak");
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Brahmaputra");
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Dibang");
            }
        });
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Dihing");
            }
        });
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Kameng");
            }
        });
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Kapili");
            }
        });
        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Dhansiri");
            }
        });
        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Manas");
            }
        });
        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Lohit");
            }
        });
        imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Siang");
            }
        });
        imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Umiam");
            }
        });
        imageView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("BioTech");
            }
        });
        imageView14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Chemical");
            }
        });
        imageView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Civil");
            }
        });
        imageView16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("CSE");
            }
        });
        imageView17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("CST");
            }
        });
        imageView18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("ECE");
            }
        });
        imageView19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("EEE");
            }
        });
        imageView20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("EP");
            }
        });
        imageView21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Mech");
            }
        });
        imageView22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("MNC");
            }
        });
        imageView23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Curious");
            }
        });
        imageView24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Exam");
            }
        });
        imageView25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Love");
            }
        });
        imageView26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Nature");
            }
        });
        imageView27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeText.setText("Suicidal");
            }
        });

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post.setEnabled(false);
                if(!(ThoughtText.getText().toString().isEmpty()) && ThoughtText.getText().toString().length() <= 2000){
                    if(!(ThemeText.getText().toString().isEmpty())){

                    DjangoObject djangoObject = new DjangoObject(getApplicationContext(),"/add_thought","Thought_db");
                    djangoObject.put("Thoughts",ThoughtText.getText().toString());
                    djangoObject.put("Backgrounds",ThemeText.getText().toString().toLowerCase());
                    djangoObject.put("User",new DjangoAuthenication(getApplicationContext()).getUsername());
                    djangoObject.put("Anonymous",String.valueOf(Anonymous.isChecked()));
                    djangoObject.saveInBackground(new SaveInBackgroundListener() {
                        @Override
                        public void onSaved(Response response) {
                            Toast.makeText(getApplicationContext(),"Your Thought has been Expressed !",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent (getApplicationContext(),ThoughtsMain.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void OnFailure(IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"There seems to be a network problem. Please try again",Toast.LENGTH_LONG).show();
                            Post.setEnabled(true);
                        }
                    });


                    }else{
                        Toast.makeText(getApplicationContext(),"Choose the Thought Theme",Toast.LENGTH_LONG).show();
                        Post.setEnabled(true);
                    }
                }else {
                    if (ThoughtText.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Express your Thought First", Toast.LENGTH_LONG).show();
                        Post.setEnabled(true);
                    }else if(ThoughtText.getText().toString().length() > 2000){
                        Toast.makeText(getApplicationContext(), "Your Thought is too long to Express !, Reduce and try again", Toast.LENGTH_LONG).show();
                        Post.setEnabled(true);
                    }
                }
            }
        });
    }
}
