package com.example.thoughts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class LoginEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_event);

        final EditText Username = (EditText)findViewById(R.id.usernameText);
        final EditText Password = (EditText)findViewById(R.id.passwordText);

        final Button Login = (Button)findViewById(R.id.loginButton);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login.setEnabled(false);
                DjangoQuery query = new DjangoQuery(getApplicationContext(),"/database_query","Clubs_db");
                query.whereEqualto("Username",Username.getText().toString());
                query.findInBackground(new FindInBackgroundListener() {
                    @Override
                    public void OnFound(JSONArray DjangoJSONArray) {
                        if (DjangoJSONArray.length() > 0){
                            try {
                                if (DjangoJSONArray.getJSONObject(0).getString("Password").equals(Password.getText().toString())){
                                    Intent intent = new Intent(getApplicationContext(),AddEvent.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Enter Correct Username and Password",Toast.LENGTH_LONG).show();
                                    Login.setEnabled(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Enter Correct Username and Password",Toast.LENGTH_LONG).show();
                            Login.setEnabled(true);
                        }
                    }

                    @Override
                    public void OnFailure(IOException e) {
                        Toast.makeText(getApplicationContext(),"There seems some network problem. Please Try again",Toast.LENGTH_LONG).show();
                        Login.setEnabled(true);
                    }
                });
            }
        });
    }
}
