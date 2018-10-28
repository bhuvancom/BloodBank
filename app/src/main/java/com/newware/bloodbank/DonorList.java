package com.newware.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class DonorList extends AppCompatActivity
{
    TextView tvAddDonor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);


        setUi();

        tvAddDonor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(DonorList.this, DonorRegistration.class));
            }
        });
    }

    void setUi()
    {
        tvAddDonor = findViewById(R.id.tvAddDonor);
    }
}
