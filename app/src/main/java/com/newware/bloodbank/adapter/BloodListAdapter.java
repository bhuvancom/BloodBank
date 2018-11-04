package com.newware.bloodbank.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newware.bloodbank.R;

/**
 * Created by Bhuvaneshvar Nath Srivastava on 27-10-2018 at 08:58 PM.
 * Copyright (c) 2018
 **/
public class BloodListAdapter extends RecyclerView.Adapter<BloodListAdapter.MyViewHolder>
{

    private Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.blood_unit_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        public MyViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
