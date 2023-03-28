package com.example.rdvmanager.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rdvmanager.Appointment;
import com.example.rdvmanager.AppointmentAdapter;
import com.example.rdvmanager.AppointmentDataBase;
import com.example.rdvmanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/********************/
public class MyAppointmentsFragment extends Fragment
{
    private int aSelectedItem = R.id.Upcoming;
    private AppointmentAdapter aAdapter;
    private ActivityResultLauncher<Intent> aLauncher;
    /********************/
    public MyAppointmentsFragment(){}
    /********************/
    @Override public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedState)
    {
        View view = inflater.inflate(R.layout.fragment_my_appointments, container, false);

        //Importation de la barre de navigation de MyAppointments
        BottomNavigationView appointmentsNavigationView = view.findViewById(R.id.appointments_navigation_view);
        appointmentsNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        //Importation du bouton "Ajouter"
        Button addAppointmentButton = view.findViewById(R.id.add_appointment);
        addAppointmentButton.setOnClickListener(view1->
                this.loadFragment(new SetAppointmentFragment()));
        //Adapter
        this.aAdapter = new AppointmentAdapter(this, view.findViewById(R.id.empty_message));
        this.updateAdapterList();
        RecyclerView recyclerView = view.findViewById(R.id.vertical_recycler_view);
        recyclerView.setAdapter(this.aAdapter);

        //Launcher pour ouvrir d'autres applications
        this.aLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),result->{});
        return view;
    }
    /********************/
    public boolean onNavigationItemSelected(MenuItem item)
    {
        this.aSelectedItem = item.getItemId();
        this.updateAdapterList();
        return true;
    }
    /********************/
    private void updateAdapterList()
    {
        AppointmentDataBase db = new AppointmentDataBase(requireContext());
        if(this.aSelectedItem == R.id.Upcoming)
            this.aAdapter.setAppointments(db.getUpcomingAppointments());
        else if(this.aSelectedItem == R.id.Past)
            this.aAdapter.setAppointments(db.getPastAppointments());
        db.close();
    }
    /********************/
    public void loadFragment(Fragment fragment)
    {
        FragmentTransaction transaction = this.getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    /********************/
    public void openPhone(View view, String phoneNumber)
    {
        if (phoneNumber != null && !phoneNumber.isEmpty() && PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber))
            this.aLauncher.launch(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
        else
            Toast.makeText(view.getContext(),R.string.invalid_number, Toast.LENGTH_SHORT).show();
    }
    /********************/
    public void openMap(View view, String address)
    {
        if (address != null && !address.isEmpty())
        {
            Uri geoLocation = Uri.parse("geo:0,0?q=" + Uri.encode(address));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoLocation);
            mapIntent.setPackage("com.google.android.apps.maps"); // Si l'application Google Maps est installée
            this.aLauncher.launch( mapIntent);
        }
        else
            Toast.makeText(view.getContext(),R.string.invalid_address, Toast.LENGTH_SHORT).show();
    }
    /********************/
    public void shareAppointment(View view, Appointment appointment)
    {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, appointment.toString(view.getContext()));
        sendIntent.setType("text/plain");
        this.aLauncher.launch(sendIntent);
    }
}
