package com.newware.bloodbank.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newware.bloodbank.R;

/**
 * Created by Bhuvaneshvar Nath Srivastava on 03-11-2018 at 07:49 PM.
 * Copyright (c) 2018
 **/
public class ForBloodUnits extends RecyclerView.Adapter<ForBloodUnits.ViewHolder>
{
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.blood_unit_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvBloodType, tvUnits;

        public ViewHolder(View itemView)
        {
            super(itemView);
            tvUnits = itemView.findViewById(R.id.tvBloodUnits);
            tvBloodType = itemView.findViewById(R.id.tvBloodGroupName);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });
        }
    }
}
