package com.newware.bloodbank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newware.bloodbank.Beans.UsedBloodDetails;
import com.newware.bloodbank.adapter.ForUsedBloodList;

import java.util.ArrayList;

public class UsedBloodList extends AppCompatActivity
{

    RecyclerView recyclerView;
    ArrayList<UsedBloodDetails> usedBloodDetails;
    ForUsedBloodList adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used_blood_list);


        getUi();

        swipeRefreshLayout.setRefreshing(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        getDataFromFireBase();

        showSnackBar("Getting Data");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                showSnackBar("Refreshing..");
                usedBloodDetails.clear();
                getDataFromFireBase();
            }
        });
    }

    void getDataFromFireBase()
    {
        showDialog();
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference db = fb.getReference("Used_Blood");
        db.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                swipeRefreshLayout.setRefreshing(false);
                usedBloodDetails.clear();
                dismissDialog();
                for (DataSnapshot bllodId : dataSnapshot.getChildren())
                {
                    String id = bllodId.getKey();
                    if (id != null)
                    {
                        db.child(id).addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                UsedBloodDetails bloodDetails = dataSnapshot.getValue(UsedBloodDetails.class);
                                if (bloodDetails != null)
                                {
                                    usedBloodDetails.add(new UsedBloodDetails
                                            (
                                                    bloodDetails.getReceiverName()
                                                    , bloodDetails.getReceiverMobile()
                                                    , bloodDetails.getReceiverEmail()
                                                    , bloodDetails.getReceivedDate()
                                                    , bloodDetails.getCustomMsg()
                                                    , bloodDetails.getReceiverPurpose()
                                                    , bloodDetails.getReceiverBloodGroup()
                                                    , bloodDetails.getBloodId()
                                            ));
                                }
                                dismissDialog();
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {
                                Toast.makeText(UsedBloodList.this, "Error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(UsedBloodList.this, "Error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void getUi()
    {
        recyclerView = findViewById(R.id.rv_UsedBloodList);
        usedBloodDetails = new ArrayList<>();
        adapter = new ForUsedBloodList(this, usedBloodDetails);
        swipeRefreshLayout = findViewById(R.id.STRUsedBloodList);
    }

    public final void showDialog()
    {
        materialDialog = new MaterialDialog.Builder(this)
                .title("Loading")
                .titleColorRes(R.color.colorPrimary)
                .backgroundColorRes(R.color.white_color)
                .contentColorRes(R.color.colorAccent)
                .cancelable(true)
                .canceledOnTouchOutside(false)
                .content("Please Wait Getting Blood List ....")
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

    @Override
    protected void onResume()
    {
        if (usedBloodDetails != null && adapter != null)
        {
            usedBloodDetails.clear();
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}
