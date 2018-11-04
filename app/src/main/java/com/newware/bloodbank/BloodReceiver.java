package com.newware.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newware.bloodbank.Beans.DonorRegistrationBean;
import com.newware.bloodbank.Beans.UsedBloodDetails;
import com.newware.bloodbank.adapter.BloodListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BloodReceiver extends AppCompatActivity
{

    MaterialDialog materialDialog;
    RelativeLayout relativeLayout;
    String bloodId = "";
    String donorAadhaar = "";
    String bloodGroup = "";
    EditText etReceiverName, etContact, etEmail, etMsg, etPurpose;
    ArrayList<String> donorDetails;
    Spinner bloodGroupSpin;
    String st = "";
    Button btnGiveBlood;

    private String receiverName;
    private String receiverMobile;
    private String receiverEmail;
    private String receivedDate;
    private String customMsg;
    private String receiverPurpose;
    private String receiverBloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_receiver);

        getUi();

        Intent intent = getIntent();

        String data[] = intent.getStringArrayExtra(BloodListAdapter.BLOOD_DATA_EXTRA);
        if (data != null)
        {
            bloodId = data[0];
            bloodGroup = data[1];
            donorAadhaar = data[2];
        }

        final String[] bloodGroup = {"A+", "A-", "AB+", "AB-", "B+", "B-", "O-", "O+"};
        List<String> bloodList = new ArrayList<>(Arrays.asList(bloodGroup));

        // creating arrayAdapter and setting to spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodList);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //setting layout type for dropdown
        bloodGroupSpin.setAdapter(arrayAdapter);//setting adapter

        bloodGroupSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                st = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        /**
         * Spinner ends
         */
///===================================================================//
        getDonorDetails(donorAadhaar);


        btnGiveBlood.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                receiverName = etReceiverName.getText().toString().trim();
                receivedDate = getToday();
                receiverBloodGroup = st;
                receiverEmail = etEmail.getText().toString().trim();
                receiverMobile = etContact.getText().toString().trim();
                receiverPurpose = etPurpose.getText().toString().trim();
                customMsg = etMsg.getText().toString().trim();

                if ((!isNameCorrect(receiverName)) || (!isNumberCorrect(receiverMobile) || (!isEmailCorrect(receiverEmail))))
                {
                    showSnackBar("Please enter correct details");
                    return;
                }
                UsedBloodDetails bloodDetails = new UsedBloodDetails(
                        receiverName,
                        receiverMobile,
                        receiverEmail,
                        receivedDate,
                        customMsg,
                        receiverPurpose,
                        receiverBloodGroup,
                        bloodId
                );
                new MaterialDialog.Builder(BloodReceiver.this)
                        .positiveColor(getResources().getColor(R.color.colorAccent))
                        .negativeText("Cancel")
                        .title("Please Review Data Before Receiving Blood")
                        .titleColor(getResources().getColor(R.color.colorPrimary))
                        .content(
                                "Name :\t" + receiverName + "\n"
                                        + "Blood Group :\t" + receiverBloodGroup + "\n"
                                        + "Email:\t" + receiverEmail)
                        .contentColor(getResources().getColor(R.color.colorPrimaryDark))
                        .positiveText("Give Blood")
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
                                //todo : send msg to donor with text msg;
                                //giveBloodAndSendDetailsToDb(bloodDetails);
                                Toast.makeText(BloodReceiver.this, "Done\nThank you", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

    }


    void getDonorDetails(String aadhaar)
    {
        showDialog();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = firebaseDatabase.getReference("UserData").child(aadhaar);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                dismissDialog();
                DonorRegistrationBean bean = dataSnapshot.getValue(DonorRegistrationBean.class);
                if (bean != null)
                {
                    donorDetails = new ArrayList<>();
                    donorDetails.add(bean.getContactNo());
                    donorDetails.add(bean.getEmailAddress());
                    donorDetails.add(bean.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(BloodReceiver.this, "Error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void giveBloodAndSendDetailsToDb(UsedBloodDetails usedBloodDetails)
    {
        showDialog();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = firebaseDatabase.getReference("Used_Blood");
        dbRef.child(bloodId).setValue(usedBloodDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        removeUsedBloodFromAvailableBlood();
                        dismissDialog();
                    }
                }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(BloodReceiver.this, "Error\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void removeUsedBloodFromAvailableBlood()
    {
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference dbTo = fb.getReference("Blood_List");
        dbTo.child(bloodGroup).child(bloodId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        Toast.makeText(BloodReceiver.this, "Blood Donation Complete", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(BloodReceiver.this, MainActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        BloodReceiver.this.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(BloodReceiver.this, "Error\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static String getToday()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy, hh:mm:ss aa");
        return df.format(c.getTime());
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
                .content("Please Wait Getting Request Done....")
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
        Snackbar.make(relativeLayout, str, Snackbar.LENGTH_SHORT).show();
    }

    void getUi()
    {

        bloodGroupSpin = findViewById(R.id.spinnerBloodGroupReceiver);
        etReceiverName = findViewById(R.id.etNameReceiver);
        etContact = findViewById(R.id.et_mobileReceiver);
        etEmail = findViewById(R.id.et_emailReceiver);
        etPurpose = findViewById(R.id.et_Bimari);
        etMsg = findViewById(R.id.et_CustomMsg);
        btnGiveBlood = findViewById(R.id.btnGiveBlood);
        relativeLayout = findViewById(R.id.rvBloodReceiver);
    }

    boolean isEmailCorrect(String email)
    {
        if (email.length() < 8)
        {
            etEmail.setError("Email Address is too short");
            etEmail.requestFocus();
            return false;
        }
        else if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches()))
        {
            etEmail.setError("Email Address is wrong");
            etEmail.requestFocus();
            return false;
        }
        return true;

    }

    boolean isNameCorrect(String name)
    {
        if (name.length() < 3)
        {
            etReceiverName.setError("Name is too short");
            etReceiverName.requestFocus();
            return false;
        }

        return true;
    }


    boolean isNumberCorrect(String nmbr)
    {
        if (nmbr.length() < 10)
        {
            etContact.setError("Mobile must 10 digit long");
            etContact.requestFocus();
            return false;
        }
        else if (!nmbr.matches("^[0-9]{10}"))
        {
            etContact.setError("Only Numbers are allowed");
            etContact.requestFocus();
            return false;
        }
        return true;
    }

}
