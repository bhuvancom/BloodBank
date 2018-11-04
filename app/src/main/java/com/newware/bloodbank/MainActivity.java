package com.newware.bloodbank;

/**
 * started on 21 sept 2018,
 * Bhuvaneshvar Nath Srivastava
 **/

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
    Button btnRegistartion;
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean isConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checkConnection();
        setUi();
        getDataFromFirebase();

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
    }

    private void getDataFromFirebase()
    {
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = fb.getReference("Blood_List");
        dbRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
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
                                Toast.makeText(MainActivity.this, "hehe ->" + groupName + " units -> " + count, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {
                                Toast.makeText(MainActivity.this, "error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(MainActivity.this, "error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
