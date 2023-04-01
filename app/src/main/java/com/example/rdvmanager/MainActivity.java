package com.example.rdvmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rdvmanager.fragments.MyAppointmentsFragment;
import com.example.rdvmanager.fragments.PreferencesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;
import java.util.Objects;

/********************/
public class MainActivity extends AppCompatActivity
{
    private int SELECTED_ITEM_ID = R.id.Appointments;
    private final Fragment aMAF,  aPF;
    /********************/
    public MainActivity()
    {
        this.aMAF = new MyAppointmentsFragment();
        this.aPF = new PreferencesFragment();
    }
    /********************/
    public void setActionBarTitle(int title){
        Objects.requireNonNull(this.getSupportActionBar()).setTitle(title);}
    /********************/
    @Override public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("Fragment", SELECTED_ITEM_ID);
    }
    /********************/
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.MyTheme);
        this.setupDarkMode();
        this.setupDefaultLanguage();
        super.setContentView(R.layout.activity_main);
        this.setupNavigationBar(savedInstanceState);
        this.loadSelectedFragment();
    }
    /********************/
    private void setupNavigationBar(Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
            SELECTED_ITEM_ID = savedInstanceState.getInt("Fragment",R.id.Appointments);
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(item->
        {SELECTED_ITEM_ID = item.getItemId();this.loadSelectedFragment();return true;});
    }
    /********************/
    private void loadSelectedFragment()
    {
        Fragment fragment = (SELECTED_ITEM_ID == R.id.Preferences) ? this.aPF:this.aMAF;
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    /********************/
    private void setupDarkMode()
    {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getBoolean("DarkMode",false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    /********************/
    private void setupDefaultLanguage()
    {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        Resources resources = this.getBaseContext().getResources();
        String[] languages = resources.getStringArray(R.array.languages);
        String language = languages[preferences.getInt("Language",0)];
        Locale.setDefault(new Locale(language.toLowerCase()));
        Configuration config = resources.getConfiguration();
        config.setLocale(Locale.getDefault());
        resources.updateConfiguration(config,resources.getDisplayMetrics());
    }
}