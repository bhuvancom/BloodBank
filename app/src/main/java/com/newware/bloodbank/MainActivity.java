package com.newware.bloodbank;

/**
 * started on 21 sept 2018,
 * Bhuvaneshvar Nath Srivastava
 **/

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    Button btnRegistartion;
    private boolean isConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checkConnection();
        setUi();

        btnRegistartion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, DonorList.class));
            }
        });
    }

    private void setUi()
    {
        btnRegistartion = findViewById(R.id.btnDonorList);
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
