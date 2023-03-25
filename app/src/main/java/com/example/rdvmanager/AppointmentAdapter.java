package com.example.rdvmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/********************/
public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>
{
    private final ArrayList<AppointmentModel> aAppointments;
    /********************/
    public AppointmentAdapter(ArrayList<AppointmentModel> appointments)
    {
        this.aAppointments = appointments;
    }
    /********************/
    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_appointment,parent,false));
    }
    /********************/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        AppointmentModel currentAppointment = this.aAppointments.get(position);
        holder.aAppointmentTitle.setText(currentAppointment.aTitle);

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.FRANCE);
        holder.aAppointmentDate.setText(dateFormat.format(currentAppointment.aCalendar.getTime()));

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        holder.aAppointmentTime.setText(timeFormat.format(currentAppointment.aCalendar.getTime()));
    }
    /********************/
    @Override
    public int getItemCount()
    {
        return aAppointments.size();
    }
    /********************/
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView aAppointmentTitle, aAppointmentDate, aAppointmentTime;
        /********************/
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.aAppointmentTitle= itemView.findViewById(R.id.appointment_title);
            this.aAppointmentDate= itemView.findViewById(R.id.appointment_date);
            this.aAppointmentTime = itemView.findViewById(R.id.appointment_time);
        }
    }
}
