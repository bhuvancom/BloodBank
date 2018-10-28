package com.newware.bloodbank;

/**
 * started on 21 sept 2018,
 * Bhuvaneshvar Nath Srivastava
 **/

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    Button btnRegistartion;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUi();


        btnRegistartion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, DonorList.class));
            }
        });
    }

    private void setUi()
    {
        btnRegistartion = findViewById(R.id.btnDonorList);
    }
}
