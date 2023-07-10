package com.example.mydrivingscore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    ImageView logo,label,mds;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo=findViewById(R.id.logo);
        label=findViewById(R.id.label);
        mds=findViewById(R.id.mydrivingscore);

        mds.animate().translationY(650).setDuration(500).setStartDelay(400);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        },1300);
    }
}