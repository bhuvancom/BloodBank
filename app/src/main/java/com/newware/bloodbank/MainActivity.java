package com.newware.bloodbank;

/**
 * started on 21 sept 2018,
 * Bhuvaneshvar Nath Srivastava
 **/

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newware.bloodbank.Beans.BloodUnits;
import com.newware.bloodbank.adapter.ForBloodUnits;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    Button btnRegistartion;
    private ArrayList<BloodUnits> bloodUnits;
    private RecyclerView recyclerView;
    private ForBloodUnits adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private MaterialDialog materialDialog;
    private boolean isConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checkConnection();
        setUi();

        showSnackBar("Getting Blood List");
        swipeRefreshLayout.setRefreshing(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        getDataFromFirebase();

        adapter.notifyDataSetChanged();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                showSnackBar("Refreshing...... wait");
                bloodUnits.clear();
                getDataFromFirebase();
            }
        });

        btnRegistartion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, DonorListActivity.class));
            }
        });
    }

    private void setUi()
    {
        swipeRefreshLayout = findViewById(R.id.STRBloodUnits);
        btnRegistartion = findViewById(R.id.btnDonorList);
        recyclerView = findViewById(R.id.rv_MainActivity);
        bloodUnits = new ArrayList<>();
        adapter = new ForBloodUnits(this, bloodUnits);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_get_used_blood, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_getUsedBlood:
            {
                startActivity(new Intent(MainActivity.this, UsedBloodList.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataFromFirebase()
    {
        showDialog();
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fb.getReference("Blood_List");
        dbRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                dismissDialog();
                swipeRefreshLayout.setRefreshing(false);
                bloodUnits.clear();
                showDialog();
                for (DataSnapshot bloodGrps : dataSnapshot.getChildren())
                {
                    String groupName = bloodGrps.getKey();
                    if (groupName != null)
                    {
                        dbRef.child(groupName).addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                long count = dataSnapshot.getChildrenCount();
                                bloodUnits.add(new BloodUnits(groupName, count));
                                adapter.notifyDataSetChanged();
                                dismissDialog();
                                // Toast.makeText(MainActivity.this, " ->" + groupName + " units -> " + count, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {
                                Toast.makeText(MainActivity.this, "Error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                dismissDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(MainActivity.this, "error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

//    @Override
//    public void onNetworkConnectionChanged(boolean isConnected)
//    {
//        isConnection = isConnected;
//    }
//
//    private void checkConnection() {
//        //MyApplication.getInstance().setConnectivityListener(this);
//        isConnection = ConnectivityReceiver.isConnected();
//        if (!isConnection) {
//            //Snackbar.make(linearLayout, "Internet Connection loss", Snackbar.LENGTH_LONG).show();
//            AlertDialog.Builder ab = new AlertDialog.Builder(this);
//            ab.setMessage("No internet Connection press Cancel to exit.\nPress Retry if internet connection is resumed.")
//                    .setPositiveButton("Retry", new DialogInterface.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which)
//                        {
//                            checkConnection();
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which)
//                        {
//                            dialog.dismiss();
//                            finish();
//                        }
//                    })
//                    .show().setTitle("No Internet Found");
//        }
//    }
}
