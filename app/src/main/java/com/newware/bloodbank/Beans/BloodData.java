package com.newware.bloodbank.Beans;

/**
 * Created by Bhuvaneshvar Nath Srivastava on 28-10-2018 at 08:27 PM.
 * Copyright (c) 2018
 **/
public class BloodData
{
    private String bloodId;
    private String bloodGroup;
    private String donatedOnDate;
    private String donorAadhaar;

    public BloodData(String bloodId, String bloodGroup, String donatedOnDate, String donorAadhaar)
    {
        this.bloodId = bloodId;
        this.bloodGroup = bloodGroup;
        this.donatedOnDate = donatedOnDate;
        this.donorAadhaar = donorAadhaar;
    }

    public BloodData()
    {
    }

    public String getBloodId()
    {
        return bloodId;
    }

    public String getBloodGroup()
    {
        return bloodGroup;
    }

    public String getDonatedOnDate()
    {
        return donatedOnDate;
    }

    public String getDonorAadhaar()
    {
        return donorAadhaar;
    }
}
