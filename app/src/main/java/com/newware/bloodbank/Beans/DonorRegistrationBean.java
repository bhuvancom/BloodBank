package com.newware.bloodbank.Beans;

import java.io.Serializable;

/**
 * Created by Bhuvaneshvar Nath Srivastava on 28-10-2018 at 12:38 AM.
 * Copyright (c) 2018
 **/
public class DonorRegistrationBean implements Serializable
{
    private String name;
    private String gender;
    private String bloodGroup;
    private String contactNo;
    private String dob;
    private String aadhaar;
    private String emailAddress;

    public DonorRegistrationBean()
    {
    }

    public DonorRegistrationBean(String name, String gender, String bloodGroup, String contactNo, String dob, String aadhaar, String emailAddress)
    {
        this.name = name;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.contactNo = contactNo;
        this.dob = dob;
        this.aadhaar = aadhaar;
        this.emailAddress = emailAddress;
    }


    public String getName()
    {
        return name;
    }

    public String getGender()
    {
        return gender;
    }

    public String getBloodGroup()
    {
        return bloodGroup;
    }

    public String getContactNo()
    {
        return contactNo;
    }

    public String getDob()
    {
        return dob;
    }

    public String getAadhaar()
    {
        return aadhaar;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }
}
