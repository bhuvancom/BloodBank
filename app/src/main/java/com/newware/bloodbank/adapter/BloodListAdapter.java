package com.newware.bloodbank.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.newware.bloodbank.Beans.BloodData;
import com.newware.bloodbank.BloodReceiver;
import com.newware.bloodbank.R;

import java.util.ArrayList;

/**
 * Created by Bhuvaneshvar Nath Srivastava on 27-10-2018 at 08:58 PM.
 * Copyright (c) 2018
 **/
public class BloodListAdapter extends RecyclerView.Adapter<BloodListAdapter.MyViewHolder>
{
    private ArrayList<BloodData> bloodDataArrayList;
    private Context context;
    public static String BLOOD_DATA_EXTRA = "BLOOD_DATA_EXTRA";

    public BloodListAdapter(ArrayList<BloodData> bloodDataArrayList, Context context)
    {
        this.bloodDataArrayList = bloodDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.blood_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        BloodData bloodData = bloodDataArrayList.get(position);
        holder.tvBloodGroup.setText(bloodData.getBloodGroup());
        holder.tvBloodId.setText(bloodData.getBloodId());
        holder.tvBloodDonorAadhaar.setText(bloodData.getDonorAadhaar());
        holder.tvBloodDonatedOn.setText(bloodData.getDonatedOnDate());
    }

    @Override
    public int getItemCount()
    {
        return bloodDataArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvBloodGroup, tvBloodId, tvBloodDonorAadhaar, tvBloodDonatedOn;

        MyViewHolder(View itemView)
        {
            super(itemView);
            tvBloodDonatedOn = itemView.findViewById(R.id.tvBloodDonatedOn);
            tvBloodDonorAadhaar = itemView.findViewById(R.id.tvBloodDonorAadhaar);
            tvBloodId = itemView.findViewById(R.id.tvBloodId);
            tvBloodGroup = itemView.findViewById(R.id.tvBloodListGroupName);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    BloodData bloodData = bloodDataArrayList.get(getAdapterPosition());

                    String data[] = {bloodData.getBloodId(), bloodData.getBloodGroup(), bloodData.getDonorAadhaar()};

                    new MaterialDialog.Builder(context)
                            .title("Donate this blood with id " + data[0] + " ?")
                            .cancelable(false)
                            .titleColor(context.getResources().getColor(R.color.colorPrimary))
                            .positiveColor(context.getResources().getColor(R.color.colorPrimaryDark))
                            .onPositive(new MaterialDialog.SingleButtonCallback()
                            {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                                {
                                    Intent intent = new Intent(context, BloodReceiver.class);
                                    intent.putExtra(BLOOD_DATA_EXTRA, data);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    context.startActivity(intent);
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback()
                            {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                                {
                                    dialog.dismiss();
                                }
                            })
                            .positiveText("Donate")
                            .positiveColor(context.getResources().getColor(R.color.colorAccent))
                            .negativeText("Cancel")
                            .content("Click \'Donate\' to goto to Blood Receiving Page.")
                            .contentColor(context.getResources().getColor(R.color.colorAccent))
                            .show();
                }
            });
        }
    }

}
