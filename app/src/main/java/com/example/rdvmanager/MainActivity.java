package com.example.rdvmanager;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rdvmanager.fragments.MyAppointmentsFragment;
import com.example.rdvmanager.fragments.PreferencesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

/********************/
public class MainActivity extends AppCompatActivity
{
    private int SELECTED_FRAGMENT;
    private final MyAppointmentsFragment aMAF;
    private final PreferencesFragment aPF;
    /********************/
    public MainActivity()
    {
        this.aMAF = new MyAppointmentsFragment();
        this.aPF = new PreferencesFragment();
    }
    /********************/
    @Override public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        //Mémorise la page au moment de la suppression de l'activité pour pouvoir y retourner
        outState.putInt("Fragment", SELECTED_FRAGMENT);
    }
    /********************/
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setDarkMode();
        this.setDefaultLanguage();
        this.setContentView(R.layout.activity_main);

        //Importation de la barre de navigation de page
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(this::bottomNavigation);

        //Si l'activité à été recrée, retourne au fragment d'avant
        if(savedInstanceState != null)
            SELECTED_FRAGMENT = savedInstanceState.getInt("Fragment",R.id.Appointments);
        else
            SELECTED_FRAGMENT = R.id.Appointments;
        this.loadSelectedFragment();
    }

    /********************/
    public boolean bottomNavigation(MenuItem item)
    {
        SELECTED_FRAGMENT = item.getItemId();
        this.loadSelectedFragment();
        return true;
    }
    /********************/
    private void loadSelectedFragment()
    {
        Fragment fragment = this.aMAF;
        if(SELECTED_FRAGMENT == R.id.Appointments)
            ((TextView)this.findViewById(R.id.page_title)).setText(R.string.my_appointments);
        else if(SELECTED_FRAGMENT == R.id.Preferences)
        {
            ((TextView)this.findViewById(R.id.page_title)).setText(R.string.preferences);
            fragment = this.aPF;
        }
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    /********************/
    private void setDarkMode()
    {
        AppCompatDelegate.setDefaultNightMode(this.getSharedPreferences( "AppointmentPreferences"
                , Context.MODE_PRIVATE).getBoolean("DarkMode",true)?
                AppCompatDelegate.MODE_NIGHT_YES:AppCompatDelegate.MODE_NIGHT_NO);
    }
    /********************/
    private void setDefaultLanguage()
    {
        String[] languages = getResources().getStringArray(R.array.languages);
        String languageToLoad = languages[this.getSharedPreferences("AppointmentPreferences"
                , Context.MODE_PRIVATE).getInt("Language",0)].toLowerCase();
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = this.getBaseContext().getResources().getConfiguration();
        config.setLocale(locale);
        this.getBaseContext().getResources().updateConfiguration(config,
                this.getBaseContext().getResources().getDisplayMetrics());
    }
}