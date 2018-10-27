package com.newware.bloodbank;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class DonorRegistration extends AppCompatActivity
{

    RelativeLayout relativeLayout;
    EditText etName, etAadhaar, etDOB, etPhone, etEmail;
    RadioGroup gender;
    RadioButton genderSex;
    Spinner bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_registration);
        relativeLayout = findViewById(R.id.relativeRegister);
//        getSupportActionBar().setElevation(0);
        getUi();
        final String[] bloodGroup = {"A+", "A-", "AB+", "AB-", "B+", "B-", "O-", "O+"};

    }

    private void getUi()
    {
        etName = findViewById(R.id.etName);
        etAadhaar = findViewById(R.id.et_aadhaar);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_mobile);

        bloodGroup = findViewById(R.id.spinnerBloodGroup);

        gender = findViewById(R.id.gender); // radio group

        int gen = gender.getCheckedRadioButtonId(); // get selected sex id
        genderSex = findViewById(gen);// set identification to checked id

    }

    void showSnackBar(String str)
    {
        Snackbar.make(relativeLayout, str, Snackbar.LENGTH_SHORT).show();
    }
}
