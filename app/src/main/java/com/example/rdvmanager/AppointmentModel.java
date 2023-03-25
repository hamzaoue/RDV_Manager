package com.example.rdvmanager;
import java.util.Calendar;

/********************/
public class AppointmentModel
{
    public String aTitle, aContact, aPhone, aAddress;
    public Calendar aCalendar;
    /********************/
    public AppointmentModel(String title,Calendar calendar,String address,String contact,String phone)
    {
        this.aTitle = title;
        this.aCalendar = calendar;
        this.aAddress = address;
        this.aContact = contact;
        this.aPhone = phone;
    }
}
