package com.newware.bloodbank.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.newware.bloodbank.MainActivity;
import com.newware.bloodbank.R;

public class SplashScreen extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //start of full screen code
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // end of full screen code
        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash_screen);

        SplashMaker maker = new SplashMaker();
        maker.start();
    }

    class SplashMaker extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            SplashScreen.this.finish();
        }


    }
}
