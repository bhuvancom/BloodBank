package com.newware.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newware.bloodbank.Beans.DonorRegistrationBean;
import com.newware.bloodbank.adapter.ForDonorList;

import java.util.ArrayList;

public class DonorListActivity extends AppCompatActivity
{
    public static final String TAG = "DEBUG_TAG";
    TextView tvAddDonor, tvNoDonr;
    private MaterialDialog materialDialog;

    private ForDonorList adapter;
    private ArrayList<DonorRegistrationBean> donorList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeToSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);


        setUi();

        mSwipeToSwipeRefreshLayout.setRefreshing(true);
        mSwipeToSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (donorList != null && adapter != null)
                    donorList.clear();

                showSnackBar("Refreshing");
                getDataFromFireBase();
                mSwipeToSwipeRefreshLayout.setRefreshing(false);

            }

        });
        donorList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new ForDonorList(donorList, this);
        recyclerView.setAdapter(adapter);


        getDataFromFireBase();

        mSwipeToSwipeRefreshLayout.setRefreshing(false);

        tvAddDonor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(DonorListActivity.this, DonorRegistration.class));
            }
        });


        if (donorList.size() > 0)
            if (tvNoDonr != null)
                tvNoDonr.setVisibility(View.GONE);

    }

    void getDataFromFireBase()
    {
        showDialog();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = firebaseDatabase.getReference("UserData");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                donorList.clear();
                for (DataSnapshot aadhaarKeys : dataSnapshot.getChildren())
                {
                    String aadhaarKey = aadhaarKeys.getKey();
                    Log.d(TAG, "got key ->" + aadhaarKey);
                    if (aadhaarKey != null)
                    {
                        DatabaseReference dbRefToAadharDetails = dbRef.child(aadhaarKey);

                        dbRefToAadharDetails.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                DonorRegistrationBean currentData = dataSnapshot.getValue(DonorRegistrationBean.class);
                                if (currentData != null)
                                {
                                    if (tvNoDonr != null)
                                        tvNoDonr.setVisibility(View.GONE);
                                    donorList.add(new DonorRegistrationBean
                                            (
                                                    currentData.getName(),
                                                    currentData.getGender(),
                                                    currentData.getBloodGroup(),
                                                    currentData.getContactNo(),
                                                    currentData.getDob(),
                                                    currentData.getAadhaar(),
                                                    currentData.getEmailAddress(),
                                                    currentData.getDonatedTimes())
                                    );
                                }

                                adapter.notifyDataSetChanged();
                                dismissDialog();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {
                                Toast.makeText(DonorListActivity.this, "Error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                showSnackBar("Something went wrong");
                            }
                        });
                    }
                    dismissDialog();
                }
                dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(DonorListActivity.this, "Error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                showSnackBar("Something went wrong");
            }
        });
    }

    void setUi()
    {
        tvNoDonr = findViewById(R.id.tvNoDonor);
        mSwipeToSwipeRefreshLayout = findViewById(R.id.STRDonorList);
        tvAddDonor = findViewById(R.id.tvAddDonor);
        recyclerView = findViewById(R.id.rv_donorList);
    }


    public final void showDialog()
    {
        materialDialog = new MaterialDialog.Builder(this)
                .title("Loading")
                .titleColorRes(R.color.colorPrimary)
                .backgroundColorRes(R.color.white_color)
                .contentColorRes(R.color.colorAccent)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .content("Please Wait Getting Donor List ....")
                .progress(true, 0)
                .show();
    }

    public final void dismissDialog()
    {
        if (materialDialog != null)
        {
            materialDialog.dismiss();
        }
    }

    void showSnackBar(String str)
    {
        Snackbar.make(recyclerView, str, Snackbar.LENGTH_SHORT).show();
    }
}
