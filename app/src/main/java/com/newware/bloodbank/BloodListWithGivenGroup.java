package com.newware.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newware.bloodbank.Beans.BloodData;
import com.newware.bloodbank.adapter.BloodListAdapter;
import com.newware.bloodbank.adapter.ForBloodUnits;

import java.util.ArrayList;
import java.util.Arrays;

public class BloodListWithGivenGroup extends AppCompatActivity
{

    BloodListAdapter adapter;
    ArrayList<BloodData> bloodDataArrayList;
    RecyclerView recyclerView;
    TextView tvNoBloodData, tvBloodName, tvBloodCanDonateTo;
    MaterialDialog materialDialog;
    SwipeRefreshLayout STRBloodList;

    /**
     * B_P -> B positive blood can give to -> {}
     * B_N -> B negative blood can give to -> {}
     */
    private String B_N_canGiveTo[] = {"B+", "B-", "AB+", "AB-"};
    private String B_P_canGiveTo[] = {"B+", "AB+"};


    /**
     * A_P -> A positive blood can give to -> {}
     * A_N -> A negative blood can give to -> {}
     */
    private String A_P_canGiveTo[] = {"A+", "AB+"};
    private String A_N_canGiveTo[] = {"A+", "A-", "AB+", "AB-"};


    /**
     * O_P -> O positive blood can give to -> {}
     * O_N -> O negative blood can give to -> {}
     */
    private String O_P_canGiveTo[] = {"O+", "A+", "B+", "AB+"};
    private String O_N_canGiveTo[] = {"Everyone"};


    /**
     * AB_P -> AB positive blood can give to -> {}
     * AB_N -> AB negative blood can give to -> {}
     */
    private String AB_P_canGiveTo[] = {"AB+"};
    private String AB_N_canGiveTo[] = {"AB+", "AB-"};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_list_with_given_group);
        setUi();

        STRBloodList.setRefreshing(true);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

        Intent intent = getIntent();
        String group = intent.getStringExtra(ForBloodUnits.B_GROUP);
        showSnackBar("Getting Blood List For " + group);

        getDataFromFireBase(group);

        STRBloodList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                STRBloodList.setRefreshing(true);
                bloodDataArrayList.clear();
                getDataFromFireBase(group);
            }
        });

        tvBloodName.setText(group + " Can Donate to");
        String bloodDonateTo[] = getBloodCanDonateTo(group);
        tvBloodCanDonateTo.setText(new StringBuilder()
                .append(Arrays.toString(bloodDonateTo)).toString());


    }

    private String[] getBloodCanDonateTo(String group)
    {
        switch (group)
        {
            case "A+":
                return A_P_canGiveTo;
            case "A-":
                return A_N_canGiveTo;
            case "B+":
                return B_P_canGiveTo;
            case "B-":
                return B_N_canGiveTo;
            case "AB+":
                return AB_P_canGiveTo;
            case "AB-":
                return AB_N_canGiveTo;
            case "O-":
                return O_N_canGiveTo;
            case "O+":
                return O_P_canGiveTo;
            default:
                return new String[]{"Error"};
        }
    }


    void setUi()
    {
        recyclerView = findViewById(R.id.rv_BloodList);
        bloodDataArrayList = new ArrayList<>();
        tvBloodCanDonateTo = findViewById(R.id.tvBloodListBloodCanDonateTo);
        tvBloodName = findViewById(R.id.tvBloodListBloodName);
        tvNoBloodData = findViewById(R.id.tvNoBloodFound);
        adapter = new BloodListAdapter(bloodDataArrayList, this);
        STRBloodList = findViewById(R.id.STRBloodList);
    }

    void getDataFromFireBase(String string)
    {
        showDialog();
        removeNotFound();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("Blood_List").child(string);
        dbRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                dismissDialog();
                bloodDataArrayList.clear();
                STRBloodList.setRefreshing(false);
                showDialog();
                for (DataSnapshot bloodId : dataSnapshot.getChildren())
                {
                    String blood = bloodId.getKey();
                    if (blood != null)
                    {
                        dbRef.child(blood).addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                BloodData bloodData = dataSnapshot.getValue(BloodData.class);
                                if (bloodData != null)
                                {
                                    bloodDataArrayList.add
                                            (new BloodData
                                                    (bloodData.getBloodId(),
                                                            bloodData.getBloodGroup(),
                                                            bloodData.getDonatedOnDate(),
                                                            bloodData.getDonorAadhaar())
                                            );
                                }

                                adapter.notifyDataSetChanged();
                                dismissDialog();
                                removeNotFound();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {
                                Toast.makeText(BloodListWithGivenGroup.this, "Error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    dismissDialog();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(BloodListWithGivenGroup.this, "Error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                .content("Please Wait Fetching Blood List ....")
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

    void removeNotFound()
    {
        if (bloodDataArrayList.size() > 0)
        {
            if (tvNoBloodData != null)
                tvNoBloodData.setVisibility(View.GONE);
        }
        else
        {
            if (tvNoBloodData != null)
                tvNoBloodData.setVisibility(View.VISIBLE);
        }
    }
}
