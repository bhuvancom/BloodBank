package com.newware.bloodbank.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newware.bloodbank.Beans.UsedBloodDetails;
import com.newware.bloodbank.R;

import java.util.ArrayList;

/**
 * Created by Bhuvaneshvar Nath Srivastava on 03-11-2018 at 07:56 PM.
 * Copyright (c) 2018
 **/
public class ForUsedBloodList extends RecyclerView.Adapter<ForUsedBloodList.ViewHolder>
{
    private Context context;
    ArrayList<UsedBloodDetails> usedBloodDetails;

    public ForUsedBloodList(Context context, ArrayList<UsedBloodDetails> usedBloodDetails)
    {
        this.context = context;
        this.usedBloodDetails = usedBloodDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.used_blood_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        UsedBloodDetails bloodDetails = usedBloodDetails.get(position);
        holder.tvBloodId.setText(bloodDetails.getBloodId());
        holder.tvReceiverContact.setText(bloodDetails.getReceiverMobile());
        holder.tvPurpose.setText(bloodDetails.getReceiverPurpose());
        holder.tvBloodGroup.setText(bloodDetails.getReceiverBloodGroup());
        holder.tvReceivedDate.setText(bloodDetails.getReceivedDate());
        holder.tvvName.setText(bloodDetails.getReceiverName());
    }

    @Override
    public int getItemCount()
    {
        return usedBloodDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvBloodId, tvPurpose, tvReceivedDate, tvBloodGroup, tvReceiverContact, tvvName;

        ViewHolder(View itemView)
        {
            super(itemView);
            tvBloodId = itemView.findViewById(R.id.tvReceiverBloodId);
            tvBloodGroup = itemView.findViewById(R.id.tvReceiverGroupName);
            tvReceivedDate = itemView.findViewById(R.id.tvBloodDonatedOnReceiver);
            tvPurpose = itemView.findViewById(R.id.tvReceiverPurpose);
            tvReceiverContact = itemView.findViewById(R.id.tvReceiverContact);
            tvvName = itemView.findViewById(R.id.tvReceiverName);
        }
    }
}
