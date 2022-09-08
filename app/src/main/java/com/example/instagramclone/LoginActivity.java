package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG="LoginActivity";
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser()!=null){
            goMainActivity();

        }

        etUsername=findViewById(R.id.etUsername);
        etPassword=findViewById(R.id.etPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnSignUp=findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"Onclick sign up button");
                String username=etUsername.getText().toString();
                String password=etPassword.getText().toString();
                signupUser(username ,password);

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"onClick login button");
                String username=etUsername.getText().toString();
                String password=etPassword.getText().toString();
                LoginUser(username ,password);

            }

            private void LoginUser(String username, String password) {
                Log.i(TAG,"Attempting to login user" + username);
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e!=null){
                            Log.e(TAG,"Issue with login",e);
                            Toast.makeText(LoginActivity.this,"Issues with login",Toast.LENGTH_SHORT).show();
                            return;

                        }
                        goMainActivity();
                        Toast.makeText(LoginActivity.this,"Success",Toast.LENGTH_SHORT).show();

                    }


                });
            }
        });
    }

    private void signupUser(String username, String password) {
        ParseUser user=new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG,"Issue with sign up",e);
                    Toast.makeText(LoginActivity.this,"Issue with sign up",Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this,"Success",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
       Intent i=new Intent(this,MainActivity.class);
       startActivity(i);
       finish();
    }
}