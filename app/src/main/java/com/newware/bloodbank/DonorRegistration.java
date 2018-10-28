package com.newware.bloodbank;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.newware.bloodbank.Beans.DonorRegistrationBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DonorRegistration extends AppCompatActivity
{

    RelativeLayout relativeLayout;
    EditText etName, etAadhaar, etDOB, etPhone, etEmail;
    RadioGroup gender;
    Button btnReg;
    RadioButton genderSex;
    Spinner bloodGroupSpin;

    String st = "";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_registration);
        relativeLayout = findViewById(R.id.relativeRegister);
//        getSupportActionBar().setElevation(0);
        getUi();
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
                st += parent.getItemAtPosition(position);
                Toast.makeText(DonorRegistration.this, "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        btnReg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DonorRegistrationBean data = new DonorRegistrationBean();
                //Toast.makeText(DonorRegistration.this, ""+st+"\n"+genderSex.getTransitionName(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void getUi()
    {
        etName = findViewById(R.id.etName);
        etAadhaar = findViewById(R.id.et_aadhaar);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_mobile);

        //todo: add dob
        btnReg = findViewById(R.id.btnRegister);
        bloodGroupSpin = findViewById(R.id.spinnerBloodGroup);

        gender = findViewById(R.id.gender); // radio group
        int gen = gender.getCheckedRadioButtonId(); // get selected sex id
        genderSex = findViewById(gen);// set identification to checked id

    }

    void showSnackBar(String str)
    {
        Snackbar.make(relativeLayout, str, Snackbar.LENGTH_SHORT).show();
    }
}
