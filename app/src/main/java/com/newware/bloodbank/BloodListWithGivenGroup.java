package com.newware.bloodbank;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.newware.bloodbank.Beans.BloodData;
import com.newware.bloodbank.adapter.BloodListAdapter;

import java.util.ArrayList;

public class BloodListWithGivenGroup extends AppCompatActivity
{

    BloodListAdapter adapter;
    ArrayList<BloodData> bloodDataArrayList;
    RecyclerView recyclerView;
    TextView tvNoBloodData, tvBloodName, tvBloodCanDonateTo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_list_with_given_group);
        setUi();


    }


    void setUi()
    {
        recyclerView = findViewById(R.id.rv_BloodList);
        adapter = new BloodListAdapter();
        bloodDataArrayList = new ArrayList<>();
        tvBloodCanDonateTo = findViewById(R.id.tvBloodListBloodCanDonateTo);
        tvBloodName = findViewById(R.id.tvBloodListBloodName);
        tvNoBloodData = findViewById(R.id.tvNoBlood);
    }
}
