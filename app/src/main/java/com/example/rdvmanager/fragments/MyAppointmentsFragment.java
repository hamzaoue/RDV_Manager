package com.example.rdvmanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rdvmanager.Appointment;
import com.example.rdvmanager.AppointmentAdapter;
import com.example.rdvmanager.AppointmentDataBase;
import com.example.rdvmanager.MainActivity;
import com.example.rdvmanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

/********************/
public class MyAppointmentsFragment extends Fragment
{
    /********************/
    public MyAppointmentsFragment(){}
    /********************/
    @Override public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedState)
    {
        View view = inflater.inflate(R.layout.fragment_my_appointments, container, false);
        ((MainActivity)this.requireActivity()).setActionBarTitle(R.string.my_appointments);
        AppointmentAdapter adapter = this.createAdapter(view);
        this.setupRecyclerView(view, adapter);
        this.setupNavigationView(view, adapter);
        this.setupAddButton(view);
        return view;
    }
    /********************/
    @Override public void onResume()
    {
        super.onResume();
        BottomNavigationView appointmentsNavigationView;
        appointmentsNavigationView = requireView().findViewById(R.id.appointments_navigation_view);
        RecyclerView recyclerView = requireView().findViewById(R.id.vertical_recycler_view);
        ((AppointmentAdapter) Objects.requireNonNull(recyclerView.getAdapter()))
                .setSelectedList(appointmentsNavigationView.getSelectedItemId());
    }
    /********************/
    private AppointmentAdapter createAdapter(View view)
    {
        TextView textView = view.findViewById(R.id.empty_message);
        AppointmentDataBase db = new AppointmentDataBase(view.getContext());
        List<Appointment> upcoming = db.getUpcomingAppointments();
        List<Appointment> past = db.getPastAppointments();
        db.close();
        return new AppointmentAdapter(upcoming, past, textView);
    }
    /********************/
    private void setupRecyclerView(View view, AppointmentAdapter adapter)
    {
        RecyclerView recyclerView = view.findViewById(R.id.vertical_recycler_view);
        recyclerView.setAdapter(adapter);
    }
    /********************/
    private void setupNavigationView(View view, AppointmentAdapter adapter)
    {
        BottomNavigationView appointmentsNavigationView;
        appointmentsNavigationView = view.findViewById(R.id.appointments_navigation_view);
        appointmentsNavigationView.setOnItemSelectedListener(item ->
        {adapter.setSelectedList(item.getItemId());return true;});
    }
    /********************/
    private void setupAddButton(View view)
    {
        Button addAppointmentButton = view.findViewById(R.id.add_appointment);
        addAppointmentButton.setOnClickListener(view1 ->
        {
            FragmentTransaction transaction = this.getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new SetAppointmentFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }
}
