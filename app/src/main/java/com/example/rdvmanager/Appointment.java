package com.example.rdvmanager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/********************/
public class Appointment
{
    private final int aId;//Identifant dans la base de donnée,si -1 = pas créé par la base de donnée
    private String aTitle, aContact, aPhone, aAddress;
    private Calendar aCalendar;
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
    public void setCalendar(Calendar calendar){this.aCalendar = calendar;}
    /********************/
    public String getDate()
    {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.FRANCE);
        return dateFormat.format(this.aCalendar.getTime());
    }
    /********************/
    public String getTime()
    {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        return timeFormat.format(this.aCalendar.getTime());
    }
}
