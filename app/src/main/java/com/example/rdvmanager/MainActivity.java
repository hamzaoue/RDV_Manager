package com.example.rdvmanager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.rdvmanager.fragments.MyAppointmentsFragment;
import com.example.rdvmanager.fragments.PreferencesFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
/********************/
public class MainActivity extends AppCompatActivity
{
    private final MyAppointmentsFragment aMAF;
    private final PreferencesFragment aPF;
    /********************/
    public MainActivity()
    {
        this.aMAF = new MyAppointmentsFragment();
        this.aPF = new PreferencesFragment();
    }
    /********************/
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        //Importation de la barre de navigation de page
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(this::bottomNavigation);

        this.loadFragment(this.aMAF);
    }
    /********************/
    @SuppressLint("NonConstantResourceId")
    public boolean bottomNavigation(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.Appointments: loadFragment(this.aMAF); return true;
            case R.id.Preferences: loadFragment(this.aPF); return true;
            default: return false;
        }
    }
    /********************/
    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}