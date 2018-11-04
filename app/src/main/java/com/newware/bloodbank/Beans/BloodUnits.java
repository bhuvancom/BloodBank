package com.newware.bloodbank.Beans;

import java.io.Serializable;

/**
 * Created by Bhuvaneshvar Nath Srivastava on 04-11-2018 at 10:13 AM.
 * Copyright (c) 2018
 **/
public class BloodUnits implements Serializable
{
    private String bloodGroup;
    private long units;

    public BloodUnits(String bloodGroup, long units)
    {
        this.bloodGroup = bloodGroup;
        this.units = units;
    }

    public String getBloodGroup()
    {
        return bloodGroup;
    }

    public long getUnits()
    {
        return units;
    }
}
