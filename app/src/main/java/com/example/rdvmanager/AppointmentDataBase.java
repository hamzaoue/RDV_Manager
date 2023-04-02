package com.example.rdvmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/********************/
public class AppointmentDataBase extends SQLiteOpenHelper
{
    private static final String TABLE_NAME = "appointments";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String CALENDAR = "calendar";
    private static final String ADDRESS = "address";
    private static final String CONTACT = "contact";
    private static final String PHONE = "phone";
    private static final String DATABASE_NAME = "AppointmentsBase";
    private static final int DATABASE_VERSION = 1;
    private final SimpleDateFormat aDateFormat;
    /********************/
    public AppointmentDataBase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.aDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRANCE);
    }
    /********************/
    @Override public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE + " TEXT,"
                + CALENDAR + " TEXT,"
                + ADDRESS + " TEXT,"
                + CONTACT + " TEXT,"
                + PHONE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }
    /********************/
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
    /********************/
    public List<Appointment> getPastAppointments()
    {
        String condition = " WHERE datetime(" + CALENDAR + ") < datetime('now', '+2 hour')";
        return this.getAppointments(condition);
    }
    /********************/
    public List<Appointment> getUpcomingAppointments()
    {
        String condition = " WHERE datetime(" + CALENDAR + ") >= datetime('now', '+2 hour')";
        return this.getAppointments(condition);
    }
    /********************/
    public List<Appointment> getAppointments(String condition)
    {
        List<Appointment> appointmentsList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + condition + " ORDER BY " + CALENDAR + " ASC";
        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {appointmentsList.add(this.getAppointment(cursor));}
            while (cursor.moveToNext());}
        return appointmentsList;
    }
    /**************/
    public Appointment getAppointment(Cursor cursor)
    {
        Appointment appointment = new Appointment(cursor.getInt(0));
        appointment.setTitle(cursor.getString(1));
        appointment.setAddress(cursor.getString(3));
        appointment.setContact(cursor.getString(4));
        appointment.setPhone(cursor.getString(5));
        try {Date date = this.aDateFormat.parse(cursor.getString(2));
            appointment.getCalendar().setTime(Objects.requireNonNull(date));}
        catch (Exception ignore) {}
        return appointment;
    }
    /********************/
    public void addAppointment(Appointment appointment)
    {
        ContentValues values = new ContentValues();
        values.put(TITLE, appointment.getTitle());
        values.put(CALENDAR, this.aDateFormat.format(appointment.getCalendar().getTime()));
        values.put(ADDRESS, appointment.getAddress());
        values.put(CONTACT, appointment.getContact());
        values.put(PHONE, appointment.getPhone());

        //Ajoute ou modifie si deja présent
        SQLiteDatabase db = this.getWritableDatabase();
        if (appointment.getId() == -1)
            appointment.setId(db.insert(TABLE_NAME, null, values));
        else
            db.update(TABLE_NAME, values, ID + " = ?", new String[]{"" + appointment.getId()});
    }
    /********************/
    public void deleteAppointment(long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?", new String[]{String.valueOf(id)});
    }
}