package com.newware.bloodbank;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.newware.bloodbank.adapter.ForDonorList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DonorRegistration extends AppCompatActivity
{

    RelativeLayout relativeLayout;
    EditText etName, etAadhaar, etPhone, etEmail;
    RadioGroup gender;
    TextView tvDOB, tvDOBdata;
    Button btnReg;
    RadioButton RBgenderSex;
    Spinner bloodGroupSpin;
    FirebaseDatabase firebaseDatabase;

    String st = "";
    private DatePicker datePicker;
    private int YEAR, MONTH, DAY;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_registration);
        relativeLayout = findViewById(R.id.relativeRegister);
//        getSupportActionBar().setElevation(0);
        getUi();

        /**
         * Spinner (Drop down menu for bllod group start
         */
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
                // Toast.makeText(DonorRegistration.this, "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();

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
        /**
         * DOB Selector start
         */

        tvDOB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DonorRegistration.this.dateSelector();
                //tvDOBdata.setText(DAY+"/"+MONTH+"/"+YEAR);
            }
        });

        /**
         *  dob ends
         */


        btnReg.setOnClickListener((View v) ->
        {
            String name = etName.getText().toString().trim();
            String aadhaar = etAadhaar.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String dob = tvDOBdata.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            int gen = gender.getCheckedRadioButtonId(); // get selected sex id
            RBgenderSex = findViewById(gen);
            // Toast.makeText(this, ""+dob, Toast.LENGTH_SHORT).show();
            String gender = RBgenderSex.getText().toString();
            if (!isAadhaarCorrect(aadhaar))
            {
                showSnackBar("Please Enter Correct Data");
                etAadhaar.requestFocus();
                return;
            }
            if (!isNameCorrect(name))
            {
                showSnackBar("Please Enter Correct Data");
                etName.requestFocus();
                return;
            }
            if (!isNumberCorrect(phone))
            {
                showSnackBar("Please Enter Correct Data");
                etPhone.requestFocus();
                return;
            }
            if (!isEmailCorrect(email))
            {
                etEmail.requestFocus();
                showSnackBar("Email is wrong");
                return;
            }
            if (tvDOBdata.getText().toString().equals("DD/MM/YYYY") || Calendar.YEAR - YEAR == Calendar.YEAR)
            {
                dateSelector();
                return;
            }

            Calendar Ca = Calendar.getInstance();
            int age = Ca.get(Calendar.YEAR) - YEAR;
            if ((age < 18))
            {
                Toast.makeText(this, "Ops Your are not ELIGIBLE for donation", Toast.LENGTH_SHORT).show();
                return;
            }

//            Toast.makeText(this, ""+age, Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(
                    "Name :\t\t" + name
                            + "\nAadhaar : \t\t\t"
                            + aadhaar + "\nMobile :\t\t\t"
                            + phone + "\nGender:\t\t\t" + gender
                            + "\nBlood Group :\t\t\t" + st
                            + "\nDOB : \t\t\t\t" + dob
                            + "\nEmail  : \t\t" + email);

            builder.setTitle("Please Review Data Before Save");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            })
                    .setPositiveButton("Register", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            DonorRegistrationBean data
                                    = new DonorRegistrationBean(name, gender, st, phone, dob, aadhaar, email, 0);
                            registerUserToFireBase(data);
                        }
                    }).setCancelable(false).show();


        });

    }

    private void dateSelector()
    {
        final Calendar CURRENT_DATE = Calendar.getInstance();

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                DAY = dayOfMonth;
                YEAR = year;
                MONTH = month + 1;
                tvDOBdata.setText(String.valueOf(DAY) + "/" + MONTH + "/" + YEAR);
                tvDOBdata.setTextSize(18);
            }
        }, CURRENT_DATE.get(Calendar.YEAR), CURRENT_DATE.get(Calendar.MONTH), CURRENT_DATE.get(Calendar.DATE)).show();
    }


    private void registerUserToFireBase(DonorRegistrationBean data)
    {
        showDialog();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = firebaseDatabase.getReference("UserData");
        mReference.addListenerForSingleValueEvent(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChild(data.getAadhaar()))
                {
                    Toast.makeText(DonorRegistration.this, "User Already Registered.\nSending to Donation Page wait..", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DonorRegistration.this, DonateBlood.class);
                    String beans[] = {
                            data.getName()
                            , data.getAadhaar()
                            , data.getBloodGroup()
                            , data.getEmailAddress()
                            , data.getDob()
                            , data.getGender()
                            , String.valueOf(data.getDonatedTimes())
                    };
                    intent.putExtra(ForDonorList.AADHAAR_EXTRA, beans);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    DonorRegistration.this.finish();
                }
                else
                {
                    mReference.child(data.getAadhaar()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            dismissDialog();
                            showSnackBar("User Added");
                            Toast.makeText(DonorRegistration.this, "Registered", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(DonorRegistration.this, DonateBlood.class);

                            String beans[] = {
                                    data.getName()
                                    , data.getAadhaar()
                                    , data.getBloodGroup()
                                    , data.getEmailAddress()
                                    , data.getDob()
                                    , data.getGender()
                                    , String.valueOf(data.getDonatedTimes())
                            };
                            intent.putExtra(ForDonorList.AADHAAR_EXTRA, beans);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            DonorRegistration.this.finish();
                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            dismissDialog();
                            Toast.makeText(DonorRegistration.this, "Error\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            showSnackBar("Registration Failed");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(DonorRegistration.this, "Error\n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void getUi()
    {
        etName = findViewById(R.id.etName);
        etAadhaar = findViewById(R.id.et_aadhaar);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_mobile);


        tvDOB = findViewById(R.id.tv_dob);
        tvDOBdata = findViewById(R.id.tvDOBdata);
        btnReg = findViewById(R.id.btnRegister);
        bloodGroupSpin = findViewById(R.id.spinnerBloodGroup);

        gender = findViewById(R.id.RadioGeoupGender); // radio group


    }

    void showSnackBar(String str)
    {
        Snackbar.make(relativeLayout, str, Snackbar.LENGTH_SHORT).show();
    }


    boolean isEmailCorrect(String email)
    {
        if (email.length() < 10)
        {
            etEmail.setError("Email Address is too short");
            return false;
        }
        else if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches()))
        {
            etEmail.setError("Email Address is wrong");
            return false;
        }
        return true;

    }

    boolean isNameCorrect(String name)
    {
        if (name.length() < 5)
        {
            etName.setError("Name is too short");
            return false;
        }

        return true;
    }

    boolean isAadhaarCorrect(String aadhar)
    {
        if (aadhar.length() < 12)
        {
            etAadhaar.setError("Aadhaar must 12 digit long");
            return false;
        }
        else if (!aadhar.matches("^[0-9]{12}"))
        {
            etAadhaar.setError("Only Numbers Are Allowed");
            return false;
        }
        return true;
    }

    boolean isNumberCorrect(String nmbr)
    {
        if (nmbr.length() < 10)
        {
            etPhone.setError("Mobile must 10 digit long");
            return false;
        }
        else if (!nmbr.matches("^[0-9]{10}"))
        {
            etPhone.setError("Only Numbers are allowed");
        }
        return true;
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
