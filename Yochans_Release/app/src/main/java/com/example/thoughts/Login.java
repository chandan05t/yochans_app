package com.example.thoughts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class Login extends AppCompatActivity {

    Button LoginButton;
    Button SignupButton;
    EditText Username;
    EditText Password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        /*if(ParseUser.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(),ThoughtsMain.class);
            startActivity(intent);
            finish();
        }*/
        if(new DjangoAuthenication(this).getUsername() != null){
            Intent intent = new Intent(getApplicationContext(),ThoughtsMain.class);
            startActivity(intent);
            finish();
        }

        LoginButton = (Button)findViewById(R.id.loginButton);
        SignupButton = (Button)findViewById(R.id.signupButton);
        Username = (EditText)findViewById(R.id.usernameText);
        Password = (EditText)findViewById(R.id.passwordText);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setEnabled(false);
                SignupButton.setEnabled(false);
                Username.setEnabled(false);
                Password.setEnabled( false);
                progressBar.setVisibility(View.VISIBLE);
                DjangoAuthenication djangoAuthenication = new DjangoAuthenication(getApplicationContext());
                djangoAuthenication.setUsername(Username.getText().toString());
                djangoAuthenication.setPassword(Password.getText().toString());
                djangoAuthenication.LoginInBackground(new LoginInBackgroundListener() {
                    @Override
                    public void OnFailure(IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_LONG).show();
                        LoginButton.setEnabled(true);
                        SignupButton.setEnabled(true);
                        Username.setEnabled(true);
                        Password.setEnabled( true);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void OnLoginChecked(Boolean Verification) {
                        if(Verification == true){
                            Intent intent = new Intent(getApplicationContext(), ThoughtsMain.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"Enter correct Username and Password",Toast.LENGTH_LONG).show();
                            LoginButton.setEnabled(true);
                            SignupButton.setEnabled(true);
                            Username.setEnabled(true);
                            Password.setEnabled( true);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });


                    /*ParseUser.logInInBackground(Username.getText().toString(), Password.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(e==null && user!=null) {
                                Intent intent = new Intent(getApplicationContext(), ThoughtsMain.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),"Enter correct Username and Password",Toast.LENGTH_LONG).show();
                                LoginButton.setEnabled(true);
                                SignupButton.setEnabled(true);
                                Username.setEnabled(true);
                                Password.setEnabled( true);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });*/
            }
        });

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
