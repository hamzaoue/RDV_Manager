package com.example.rdvmanager;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/********************/
public class Appointment
{
    private final int aId;//Identifant dans la base de donnée,si -1 = pas créé par la base de donnée
    private String aTitle, aContact, aPhone, aAddress;
    private final Calendar aCalendar;
    /********************/
    public Appointment(int id) {this.aId = id; this.aCalendar = Calendar.getInstance();}
    public Appointment(){this.aId = -1; this.aCalendar = Calendar.getInstance();}
    /********************/
    public String getTitle() {return this.aTitle;}
    public String getContact(){return this.aContact;}
    public String getPhone(){return this.aPhone;}
    public String getAddress(){return this.aAddress;}
    public Calendar getCalendar(){return this.aCalendar;}
    public int getId(){return this.aId;}
    /********************/
    public void setTitle(String title) {this.aTitle = title;}
    public void setContact(String contact){this.aContact = contact;}
    public void setPhone(String phone){this.aPhone = phone;}
    public void setAddress(String address){this.aAddress = address;}
    /********************/
    public String getDate()
    {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        return dateFormat.format(this.aCalendar.getTime());
    }
    /********************/
    public String getTime()
    {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(this.aCalendar.getTime());
    }
    /********************/
    public String toString(Context context)
    {
        StringBuilder sb = new StringBuilder();
        if (this.getTitle() != null && !this.getTitle().isEmpty())
            sb.append(context.getString(R.string.title)).append(" : ").append(this.getTitle()).append("\n");
        if (this.getDate() != null && !this.getDate().isEmpty())
            sb.append(context.getString(R.string.date)).append(" : ").append(this.getDate()).append("\n");
        if (this.getTime() != null && !this.getTime().isEmpty())
            sb.append(context.getString(R.string.time)).append(" : ").append(this.getTime()).append("\n");
        if (this.getAddress() != null && !this.getAddress().isEmpty())
            sb.append(context.getString(R.string.address)).append(" : ").append(this.getAddress()).append("\n");
        if (this.getContact() != null && !this.getContact().isEmpty())
            sb.append(context.getString(R.string.contact)).append(" : ").append(this.getContact()).append("\n");
        if (this.getPhone() != null && !this.getPhone().isEmpty())
            sb.append(context.getString(R.string.phone)).append(" : ").append(this.getPhone()).append("\n");
        return sb.toString();
    }
}
