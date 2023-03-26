package com.example.rdvmanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rdvmanager.Appointment;
import com.example.rdvmanager.AppointmentAdapter;
import com.example.rdvmanager.DatabaseManager;
import com.example.rdvmanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/********************/
public class MyAppointmentsFragment extends Fragment
{
    private int aSelectedItem = R.id.Upcoming;
    private AppointmentAdapter aAdapter;
    /********************/
    @Override public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_appointments, container, false);

        //Importation de la barre de navigation de MyAppointments
        BottomNavigationView appointmentsNavigationView = view.findViewById(R.id.appointments_navigation_view);
        appointmentsNavigationView.setOnItemSelectedListener(this::appointmentsNavigation);

        //Importation du bouton "Ajouter"
        Button addAppointmentButton = view.findViewById(R.id.add_appointment);
        addAppointmentButton.setOnClickListener(view1->
                this.loadFragment(new SetAppointmentFragment(this , new Appointment())));

        //Adapter
        DatabaseManager databaseManager = new DatabaseManager(view.getContext());
        this.aAdapter = new AppointmentAdapter(this,this.getAppointments());
        databaseManager.close();

        //RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.vertical_recycler_view);
        recyclerView.setAdapter(this.aAdapter);
        return view;
    }
    /********************/
    public boolean appointmentsNavigation(MenuItem item)
    {
        this.aSelectedItem = item.getItemId();
        this.aAdapter.setAppointments(this.getAppointments());
        return true;
    }
    /********************/
    private List<Appointment> getAppointments()
    {
        List<Appointment> appointments = null;
        DatabaseManager db = new DatabaseManager(requireContext());
        if(this.aSelectedItem == R.id.Upcoming)
            appointments=db.getUpcomingAppointments();
        else if(this.aSelectedItem == R.id.Past)
            appointments=db.getPastAppointments();
        db.close();
        return appointments;
    }
    /********************/
    public void loadFragment(Fragment fragment)
    {
        FragmentTransaction transaction = this.getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
