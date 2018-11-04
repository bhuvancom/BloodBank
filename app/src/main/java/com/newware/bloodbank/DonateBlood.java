package com.newware.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.newware.bloodbank.Beans.BloodData;
import com.newware.bloodbank.adapter.ForDonorList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DonateBlood extends AppCompatActivity
{

    private Button btnDonate;
    private String donorAadhaar = "";
    private int donatedTimes = 0;
    private String bloodGroup = "";
    RelativeLayout linearLayout;
    Map<String, Object> updateDonorBloodDonatedCount;
    MaterialDialog materialDialog;
    //final static String[] bloodGroups = {"A+", "A-", "AB+", "AB-", "B+", "B-", "O-", "O+"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_blood);
        updateDonorBloodDonatedCount = new HashMap<>();
        getUi();
        Intent intent = getIntent();
        donorAadhaar = intent.getStringExtra(ForDonorList.AADHAAR_EXTRA);
        String donorName = intent.getStringExtra(ForDonorList.NAME_EXTRA);
        donatedTimes = intent.getIntExtra(ForDonorList.DOATED_TIMES, 0);
        bloodGroup = intent.getStringExtra(ForDonorList.BLOOD_GROUP);
        showSnackBar(donorName);

        long idGen = System.currentTimeMillis() / 10000;
        String bloodId = donorAadhaar.concat(String.valueOf(idGen)); // creating blood id (new each time);

        BloodData bloodData = new BloodData(bloodId, bloodGroup, getToday(), donorAadhaar);

        btnDonate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new MaterialDialog.Builder(DonateBlood.this)
                        .content("Click Donate to Save Blood Record to Database.")
                        .contentColor(getResources().getColor(R.color.colorPrimary))
                        .onNegative(new MaterialDialog.SingleButtonCallback()
                        {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                            {
                                dialog.dismiss();
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback()
                        {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                            {
                                addBloodToDataBase(bloodGroup, bloodData);
                                btnDonate.setEnabled(false);
                                btnDonate.setClickable(false);
                            }
                        })
                        .negativeText("Cancel")
                        .positiveText("Donate")
                        .cancelable(false)
                        .title("DONATE BLOOD ?")
                        .negativeColor(getResources().getColor(R.color.colorPrimaryDark))
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .titleColor(getResources().getColor(R.color.colorPrimary))
                        .backgroundColor(getResources().getColor(R.color.white_color))
                        .show();
            }
        });

    }


    void updateDonorBloodDonatedCount()
    {
        showDialog();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refToUserUpdateBloodDonationCount = database.getReference("UserData").child(donorAadhaar);
        updateDonorBloodDonatedCount.put("donatedTimes", (donatedTimes + 1));

        refToUserUpdateBloodDonationCount.updateChildren(updateDonorBloodDonatedCount).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                dismissDialog();
                Intent intent = new Intent(DonateBlood.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                DonateBlood.this.finish();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                dismissDialog();
                Toast.makeText(DonateBlood.this, "Error\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void addBloodToDataBase(String bloodGroup, BloodData bloodData)
    {
        showDialog();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newData = database.getReference("Blood_List").child(bloodGroup);//save blood according to bloodGroup
        newData.child(bloodData.getBloodId()).setValue(bloodData).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Snackbar.make(linearLayout, "Blood Saved", Snackbar.LENGTH_LONG).show();
                Toast.makeText(DonateBlood.this, "Blood Saved\nThank You", Toast.LENGTH_SHORT).show();
                dismissDialog();
                updateDonorBloodDonatedCount();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(DonateBlood.this, "Error\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static String getToday()
    {
        Date presentTime_Date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss aa");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(presentTime_Date);
    }

    void showSnackBar(String str)
    {
        Snackbar.make(linearLayout, "Donating Blood Of " + str, Snackbar.LENGTH_LONG).show();
    }

    void getUi()
    {
        linearLayout = findViewById(R.id.llDonateBlood);
        btnDonate = findViewById(R.id.btnDonationButton);

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
}
