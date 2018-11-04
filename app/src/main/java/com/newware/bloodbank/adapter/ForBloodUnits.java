package com.newware.bloodbank.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newware.bloodbank.Beans.BloodUnits;
import com.newware.bloodbank.BloodListWithGivenGroup;
import com.newware.bloodbank.R;

import java.util.ArrayList;

/**
 * Created by Bhuvaneshvar Nath Srivastava on 03-11-2018 at 07:49 PM.
 * Copyright (c) 2018
 **/
public class ForBloodUnits extends RecyclerView.Adapter<ForBloodUnits.ViewHolder>
{
    private Context context;
    private ArrayList<BloodUnits> bloodUnits;
    public final static String B_GROUP = "B_GROUP";

    public ForBloodUnits(Context context, ArrayList<BloodUnits> bloodUnits)
    {
        this.context = context;
        this.bloodUnits = bloodUnits;
    }

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
        BloodUnits units = bloodUnits.get(position);
        holder.tvBloodType.setText(units.getBloodGroup());
        holder.tvUnits.setText(String.valueOf(units.getUnits() + " Units"));
    }

    @Override
    public int getItemCount()
    {
        return bloodUnits.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvBloodType, tvUnits;

        private ViewHolder(View itemView)
        {
            super(itemView);
            tvUnits = itemView.findViewById(R.id.tvBloodUnits);
            tvBloodType = itemView.findViewById(R.id.tvBloodGroupName);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    BloodUnits units = bloodUnits.get(getAdapterPosition());
                    Intent intent = new Intent(context, BloodListWithGivenGroup.class);
                    intent.putExtra(ForBloodUnits.B_GROUP, units.getBloodGroup());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
