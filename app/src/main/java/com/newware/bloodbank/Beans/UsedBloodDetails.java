package com.newware.bloodbank.Beans;

import java.io.Serializable;

/**
 * Created by Bhuvaneshvar Nath Srivastava on 04-11-2018 at 09:00 PM.
 * Copyright (c) 2018
 **/
public class UsedBloodDetails implements Serializable
{
    private String receiverName;
    private String receiverMobile;
    private String receiverEmail;
    private String receivedDate;
    private String customMsg;
    private String receiverPurpose;
    private String receiverBloodGroup;
    private String bloodId;


    public UsedBloodDetails()
    {
    }

    public UsedBloodDetails(String receiverName, String receiverMobile, String receiverEmail, String receivedDate, String customMsg, String receiverPurpose, String receiverBloodGroup, String bloodId)
    {
        this.receiverName = receiverName;
        this.receiverMobile = receiverMobile;
        this.receiverEmail = receiverEmail;
        this.receivedDate = receivedDate;
        this.customMsg = customMsg;
        this.receiverPurpose = receiverPurpose;
        this.receiverBloodGroup = receiverBloodGroup;
        this.bloodId = bloodId;
    }

    public String getBloodId()
    {
        return bloodId;
    }


    public String getReceiverName()
    {
        return receiverName;
    }

    public String getReceiverMobile()
    {
        return receiverMobile;
    }

    public String getReceiverEmail()
    {
        return receiverEmail;
    }

    public String getReceivedDate()
    {
        return receivedDate;
    }

    public String getCustomMsg()
    {
        return customMsg;
    }

    public String getReceiverPurpose()
    {
        return receiverPurpose;
    }

    public String getReceiverBloodGroup()
    {
        return receiverBloodGroup;
    }
}
