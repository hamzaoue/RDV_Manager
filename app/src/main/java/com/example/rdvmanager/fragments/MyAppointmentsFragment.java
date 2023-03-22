package com.example.rdvmanager.fragments;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rdvmanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
/********************/
public class MyAppointmentsFragment extends Fragment
{
    private final UpcomingAppointmentFragment aUAF;
    private final PastAppointmentFragment aPAF;
    /********************/
    public MyAppointmentsFragment()
    {
        this.aUAF = new UpcomingAppointmentFragment();
        this.aPAF = new PastAppointmentFragment();
    }
    /********************/
    @Override public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_appointments, container, false);

        //Importation de la barre de navigation de MyAppointments
        BottomNavigationView appointmentsNavigationView = view.findViewById(R.id.appointments_navigation_view);
        appointmentsNavigationView.setOnItemSelectedListener(this::appointmentsNavigation);

        //Importation du bouton "Ajouter"
        Button addAppointmentButton = view.findViewById(R.id.add_appointment);
        addAppointmentButton.setOnClickListener(this::addAppointment);

        this.loadFragment( this.aUAF);
        return view;
    }
    /********************/
    @SuppressLint("NonConstantResourceId")
    public boolean appointmentsNavigation(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.Upcoming: loadFragment( this.aUAF); return true;
            case R.id.Past: loadFragment( this.aPAF); return true;
            default: return false;
        }
    }
    /********************/
    private void addAppointment(View view)
    {
        FragmentTransaction transaction = this.getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SetAppointmentFragment(this));
        transaction.addToBackStack(null);
        transaction.commit();
    }
    /********************/
    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction transaction = this.getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.sub_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
