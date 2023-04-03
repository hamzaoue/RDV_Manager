package com.example.rdvmanager;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/********************/
public class Appointment
{
    private long aId;//Identifant dans la base de donnée,si -1 = pas dans la base de donnée
    private String aTitle, aContact, aPhone, aAddress;
    private final Calendar aCalendar;
    public Appointment(int id) {this.aId = id; this.aCalendar = Calendar.getInstance();}
    public Appointment(){this.aId = -1; this.aCalendar = Calendar.getInstance();}
    /********************/
    public long getId(){return this.aId;}
    public String getTitle() {return this.aTitle;}
    public String getContact(){return this.aContact;}
    public String getPhone(){return this.aPhone;}
    public String getAddress(){return this.aAddress;}
    public Calendar getCalendar(){return this.aCalendar;}
    /********************/
    public void setId(long id){this.aId = id;}
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
        int[] fields = {R.string.title,R.string.date,R.string.time,R.string.address,R.string.contact,R.string.phone};
        String[] values = {getTitle(),getDate(),getTime(),getAddress(),getContact(),getPhone()};
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < values.length; i++)
            if(values[i] != null && !values[i].isEmpty())
                sb.append(context.getString(fields[i])).append(" : ").append(values[i]).append("\n");
        return sb.toString();
    }
}
