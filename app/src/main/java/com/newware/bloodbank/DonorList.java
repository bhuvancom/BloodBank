package com.newware.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class DonorList extends AppCompatActivity
{
    TextView tvAddDonor;
    private MaterialDialog materialDialog;

    private ForDonorList adapter;
    private ArrayList<DonorRegistrationBean> donorList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);


        setUi();

        donorList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new ForDonorList(donorList, this);
        recyclerView.setAdapter(adapter);


        getDataFromFireBase();

        tvAddDonor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(DonorList.this, DonorRegistration.class));
            }
        });
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
                    Log.d("TAG", "got key ->" + aadhaarKey);
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
                                    donorList.add(new DonorRegistrationBean
                                            (
                                                    currentData.getName(),
                                                    currentData.getGender(),
                                                    currentData.getBloodGroup(),
                                                    currentData.getContactNo(),
                                                    currentData.getDob(),
                                                    currentData.getAadhaar(),
                                                    currentData.getEmailAddress())
                                    );
                                }

                                //todo : chk below code if list data are mismatched
                                adapter.notifyItemInserted(donorList.size() + 1);
                                dismissDialog();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {
                                Toast.makeText(DonorList.this, "Error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(DonorList.this, "Error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setUi()
    {
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
                .content("Please Wait...Adding")
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
}
