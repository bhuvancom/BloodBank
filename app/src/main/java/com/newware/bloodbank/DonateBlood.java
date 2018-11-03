package com.newware.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DonateBlood extends AppCompatActivity
{

    private String donorAadhaar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_blood);
        Intent intent = getIntent();
        donorAadhaar = intent.getStringExtra("aadhaar");
    }
}
