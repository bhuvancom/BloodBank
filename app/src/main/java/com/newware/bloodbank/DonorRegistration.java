package com.newware.bloodbank;

import android.app.DatePickerDialog;
import android.os.Bundle;
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

import com.google.firebase.database.FirebaseDatabase;
import com.newware.bloodbank.Beans.DonorRegistrationBean;

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
                st += parent.getItemAtPosition(position);
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


        btnReg.setOnClickListener(v ->
        {
            String name = etName.getText().toString().trim();
            String aadhaar = etAadhaar.getText().toString().trim();
            long aadhar = 0;
            try
            {
                aadhar = Long.parseLong(aadhaar);

            } catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(this, "Mobile text input is wrong", Toast.LENGTH_SHORT).show();
                return;
            }
            String email = etEmail.getText().toString().trim();
            String dob = tvDOBdata.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            int gen = gender.getCheckedRadioButtonId(); // get selected sex id
            RBgenderSex = findViewById(gen);

            String gender = RBgenderSex.getText().toString();

            String bloodGroup1 = st;

            if (!(isAadhaarCorrect(aadhaar) || isNameCorrect(name) || isNumberCorrect(phone)))
            {
                showSnackBar("Please Enter Correct Data");
                return;
            }
            if (!isEmailCorrect(email))
            {
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
                Toast.makeText(this, "Opps Your are not ELIGIBLE for donation", Toast.LENGTH_SHORT).show();
                return;
            }

//            Toast.makeText(this, ""+age, Toast.LENGTH_SHORT).show();


//                DonorRegistrationBean data
//                        = new DonorRegistrationBean(name, gender, bloodGroup, phone, "dob ", aadhaar, email);


//                Toast.makeText(DonorRegistration.this,
//                        "" + data.getGender() + "\n" + data.getBloodGroup(), Toast.LENGTH_SHORT).show();
            //registerUserToFireBase(data);
            //Toast.makeText(DonorRegistration.this, ""+st+"\n"+genderSex.getTransitionName(), Toast.LENGTH_SHORT).show();

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
                tvDOBdata.setText(DAY + "/" + MONTH + "/" + YEAR);
            }
        }, CURRENT_DATE.get(Calendar.YEAR), CURRENT_DATE.get(Calendar.MONTH), CURRENT_DATE.get(Calendar.DATE)).show();
    }


    private void registerUserToFireBase(DonorRegistrationBean data)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
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
        return true;
    }

    boolean isNumberCorrect(String nmbr)
    {
        if (nmbr.length() < 10)
        {
            etPhone.setError("Mobile must 10 digit long");
            return false;
        }
        return true;
    }
}
