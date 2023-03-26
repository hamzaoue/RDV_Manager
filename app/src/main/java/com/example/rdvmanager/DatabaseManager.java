package com.example.rdvmanager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/********************/
public class DatabaseManager extends SQLiteOpenHelper
{
    private static final String TABLE_NAME = "appointments";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_CALENDAR = "calendar";
    private static final String COL_ADDRESS = "address";
    private static final String COL_CONTACT = "contact";
    private static final String COL_PHONE = "phone";
    private static final String DATABASE_NAME = "AppointmentsBase";
    private static final int DATABASE_VERSION = 1;
    private final SimpleDateFormat aDateFormat;
    /********************/
    @SuppressLint("SimpleDateFormat")
    public DatabaseManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.aDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }
    /********************/
    @Override public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT,"
                + COL_CALENDAR + " TEXT,"
                + COL_ADDRESS + " TEXT,"
                + COL_CONTACT + " TEXT,"
                + COL_PHONE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }
    /********************/
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    /********************/
    public void addAppointment(Appointment appointment)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, appointment.getTitle());
        values.put(COL_CALENDAR, this.aDateFormat.format(appointment.getCalendar().getTime()));
        values.put(COL_ADDRESS, appointment.getAddress());
        values.put(COL_CONTACT, appointment.getContact());
        values.put(COL_PHONE, appointment.getPhone());
        if(appointment.getId() == -1)
            db.insert(TABLE_NAME, null, values);
        else
            db.update(TABLE_NAME, values, COL_ID + " = ?",new String[]{""+appointment.getId()});
        db.close();
    }
    /********************/
    public void deleteAppointment(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    /********************/
    public List<Appointment>  getPastAppointments()
    {
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE datetime(" + COL_CALENDAR +
                ", 'localtime') < datetime('now', 'localtime', '+2 hour')";
        return this.getAppointments(selectQuery);
    }
    /********************/
    public List<Appointment>  getUpcomingAppointments()
    {
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE datetime(" + COL_CALENDAR +
                ", 'localtime') >= datetime('now', 'localtime', '+2 hour')";
        return this.getAppointments(selectQuery);
    }
    /********************/
    public List<Appointment> getAppointments(String selectQuery)
    {
        List<Appointment> appointmentsList = new ArrayList<>();
        try(SQLiteDatabase db = this.getWritableDatabase();Cursor cursor=db.rawQuery(
                selectQuery + " ORDER BY " + COL_CALENDAR + " ASC",null))
        {
            if (cursor.moveToFirst())
            {
                while (!cursor.isAfterLast())
                {
                    Appointment appointment = this.getAppointment(cursor);
                    appointmentsList.add(appointment);
                    cursor.moveToNext();
                }
            }
        } catch (Exception  ignore) {}
        return appointmentsList;
    }
    /**************/
    public Appointment getAppointment(Cursor cursor)
    {
        Appointment appointment = new Appointment(cursor.getInt(0));
        appointment.setTitle(cursor.getString(1));
        appointment.setAddress(cursor.getString(3));
        appointment.setContact(cursor.getString(4));
        appointment.setPhone(cursor.getString( 5));
        try
        {
            Date date = this.aDateFormat.parse(cursor.getString(2));
            assert date != null;
            appointment.getCalendar().setTime(date);
        }
        catch(Exception ignore){}
        return appointment;
    }
}
