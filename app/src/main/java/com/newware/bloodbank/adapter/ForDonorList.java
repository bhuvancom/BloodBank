package com.newware.bloodbank.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bhuvaneshvar Nath Srivastava on 28-10-2018 at 08:31 PM.
 * Copyright (c) 2018
 **/
public class ForDonorList extends RecyclerView.Adapter<ForDonorList.ViewHolderForCurrent>
{


    @NonNull
    @Override
    public ViewHolderForCurrent onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForCurrent holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    class ViewHolderForCurrent extends RecyclerView.ViewHolder
    {
        public ViewHolderForCurrent(View itemView)
        {
            super(itemView);
        }
    }

}
