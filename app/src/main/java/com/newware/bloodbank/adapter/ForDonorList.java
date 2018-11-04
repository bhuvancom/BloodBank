package com.newware.bloodbank.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newware.bloodbank.Beans.DonorRegistrationBean;
import com.newware.bloodbank.DonateBlood;
import com.newware.bloodbank.R;

import java.util.ArrayList;

/**
 * Created by Bhuvaneshvar Nath Srivastava on 28-10-2018 at 08:31 PM.
 * Copyright (c) 2018
 **/
public class ForDonorList extends RecyclerView.Adapter<ForDonorList.ViewHolderForCurrent>
{
    public static final String AADHAAR_EXTRA = "AADHAAR_EXTRA";


    private ArrayList<DonorRegistrationBean> donorList;
    private Context mContext;

    public ForDonorList(ArrayList<DonorRegistrationBean> donorList, Context mContext)
    {
        this.donorList = donorList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolderForCurrent onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.donor_list, parent, false);
        return new ViewHolderForCurrent(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForCurrent holder, int position)
    {
        DonorRegistrationBean currentUser = donorList.get(position);
        holder.tvDonorName.setText(currentUser.getName());
        holder.tvDonorEmail.setText(currentUser.getEmailAddress());
        holder.tvDonorGender.setText(currentUser.getGender());
        holder.tvDonorDOB.setText(currentUser.getDob());
        holder.tvDonorAadhar.setText(currentUser.getAadhaar());
        holder.tvDonorBlood.setText(currentUser.getBloodGroup());
        holder.tvDonatedTimes.setText(new StringBuilder().append("").append(currentUser.getDonatedTimes()).append(" Times").toString());
    }

    @Override
    public int getItemCount()
    {
        return donorList.size();
    }

    class ViewHolderForCurrent extends RecyclerView.ViewHolder
    {
        private TextView tvDonorName, tvDonorEmail, tvDonorAadhar, tvDonorDOB, tvDonorGender, tvDonorBlood, tvDonatedTimes;

        private ViewHolderForCurrent(View itemView)
        {
            super(itemView);
            tvDonorBlood = itemView.findViewById(R.id.tvDonorListBloodGroup);
            tvDonorAadhar = itemView.findViewById(R.id.tvDonorListAadhaar);
            tvDonorName = itemView.findViewById(R.id.tvDonorListName);
            tvDonorDOB = itemView.findViewById(R.id.tvDonorListDOB);
            tvDonorGender = itemView.findViewById(R.id.tvDonorListGender);
            tvDonorEmail = itemView.findViewById(R.id.tvDonorListEmail);
            tvDonatedTimes = itemView.findViewById(R.id.tvDonorListDonatedTimes);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    DonorRegistrationBean bean = donorList.get(getAdapterPosition());
                    String beans[] = {
                            bean.getName()
                            , bean.getAadhaar()
                            , bean.getBloodGroup()
                            , bean.getEmailAddress()
                            , bean.getDob()
                            , bean.getGender()
                            , String.valueOf(bean.getDonatedTimes())
                    };
                    Intent intent = new Intent(mContext, DonateBlood.class);
                    intent.putExtra(ForDonorList.AADHAAR_EXTRA, beans);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    mContext.startActivity(intent);
                }
            });
        }
    }

}
