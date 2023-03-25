package com.example.rdvmanager.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rdvmanager.AppointmentAdapter;
import com.example.rdvmanager.AppointmentModel;
import com.example.rdvmanager.R;

import java.util.ArrayList;
import java.util.Calendar;

/********************/
public class UpcomingAppointmentFragment extends Fragment
{
    /********************/
    @Override public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_upcoming,container,false);

        ArrayList<AppointmentModel> appointments = new ArrayList<>();

        appointments.add(new AppointmentModel("Ophtalmo",Calendar.getInstance(),
                "Eglise de pantin","Dr ma boule","06 60 06 60 06"));
        appointments.add(new AppointmentModel("Titre 2",Calendar.getInstance(),
                "Place d'italie","Dr machin","06 40 73 92 92"));


        RecyclerView recyclerView = view.findViewById(R.id.vertical_recycler_view);
        recyclerView.setAdapter(new AppointmentAdapter(appointments));
        return view;
    }
}