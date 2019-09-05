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
import java.util.List;

public class Signup extends AppCompatActivity {

    Button SignupButton;
    EditText Username;
    EditText Password;
    EditText ConPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        if(new DjangoAuthenication(this).getUsername() != null){
            Intent intent = new Intent(getApplicationContext(),ThoughtsMain.class);
            startActivity(intent);
            finish();
        }

        SignupButton = (Button)findViewById(R.id.signupButton);
        Username = (EditText)findViewById(R.id.usernameText);
        Password = (EditText)findViewById(R.id.passwordText);
        ConPassword = (EditText)findViewById(R.id.conpasswordText);

        final DjangoAuthenication djangoAuthenication = new DjangoAuthenication(this);

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Username.setEnabled(false);
                Password.setEnabled(false);
                ConPassword.setEnabled(false);
                SignupButton.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                if(!(Username.getText().toString().isEmpty()) && !(Password.getText().toString().isEmpty()) &&
                        !(ConPassword.getText().toString().isEmpty()) &&
                        (Password.getText().toString().equals(ConPassword.getText().toString()))){
                        djangoAuthenication.setUsername(Username.getText().toString());
                        djangoAuthenication.setPassword(Password.getText().toString());
                        djangoAuthenication.SignUpInBackground(new SignUpInBackgroundListener() {
                            @Override
                            public void OnSignUpChecked(Boolean Verification) {
                                if(Verification == true){
                                    Intent intent = new Intent(getApplicationContext(), ThoughtsMain.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Username already Taken", Toast.LENGTH_LONG).show();
                                    Username.setEnabled(true);
                                    Password.setEnabled(true);
                                    ConPassword.setEnabled(true);
                                    SignupButton.setEnabled(true);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void OnFailure(IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();
                                Username.setEnabled(true);
                                Password.setEnabled(true);
                                ConPassword.setEnabled(true);
                                SignupButton.setEnabled(true);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                }else{
                    Toast.makeText(getApplicationContext(),"Enter Information Correctly",Toast.LENGTH_LONG).show();
                    Username.setEnabled(true);
                    Password.setEnabled(true);
                    ConPassword.setEnabled(true);
                    SignupButton.setEnabled(true);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


}
