package com.example.mydrivingscore;

import android.util.Log;

public class Background_task
{
    public static void execute()
    {

        for(int i=0;i<1000;i++)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("Hello", "Hi");
        }
    }
}
