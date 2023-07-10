package com.example.mydrivingscore;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button loginButton;
    TextView loginOTP;
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        lottie=findViewById(R.id.lottieeee);
        loginOTP=findViewById(R.id.loginOTP);

        lottie.setRepeatCount(Animation.INFINITE);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((username.getText().toString().trim().equals("joy")  ||
                        username.getText().toString().trim().equals("sonu") || username.getText().toString().trim().equals("avtar") ||
                        username.getText().toString().trim().equals("oishik")) && password.getText().toString().equals("1234"))
                {
                    Toast.makeText(MainActivity.this, "Login Successful ✔", Toast.LENGTH_SHORT).show();

                    Intent i=new Intent(getApplicationContext(),dashboard.class);
                    i.putExtra("user",username.getText().toString().trim());
                    i.putExtra("login_time",String.valueOf(System.currentTimeMillis()));


                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Username or password incorrect.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        loginOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Login with OTP in clicked 👆", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean foregroundServiceRunning()
    {
        ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service:activityManager.getRunningServices(Integer.MAX_VALUE))
        {
            if(MyForegroundService.class.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }
}