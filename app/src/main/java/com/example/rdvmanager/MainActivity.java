package com.example.rdvmanager;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
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
    private MediaPlayer aMediaPlayer;
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
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.bottom_navigation_view);
        outState.putInt("SelectedItemId", bottomNavigationView.getSelectedItemId());
    }
    /********************/
    @Override protected void onCreate(Bundle save)
    {
        super.onCreate(save);
        this.setupDefaultLanguage();
        this.setupDarkMode();
        this.setupMusic();
        super.setContentView(R.layout.activity_main);
        this.setupBottomNavigationBar();
        this.loadFragment((save == null) ? R.id.Appointments : save.getInt("SelectedItemId"));
    }
    /********************/
    @Override protected void onResume()
    {
        super.onResume();
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        if(!this.aMediaPlayer.isPlaying() && preferences.getBoolean("Music",false))
            this.aMediaPlayer.start();
    }
    /********************/
    @Override protected void onPause()
    {
        super.onPause();
        if(this.aMediaPlayer.isPlaying())
            this.aMediaPlayer.pause();
    }
    /********************/
    private void setupMusic()
    {
        this.aMediaPlayer = MediaPlayerManager.getInstance(this).getMediaPlayer();
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        if(this.aMediaPlayer.isPlaying() && !preferences.getBoolean("Music",false))
            this.aMediaPlayer.pause();
        else if(!this.aMediaPlayer.isPlaying() && preferences.getBoolean("Music",false))
            this.aMediaPlayer.start();
    }
    /********************/
    private void setupBottomNavigationBar()
    {
        BottomNavigationView BottomNavigationView = this.findViewById(R.id.bottom_navigation_view);
        BottomNavigationView.setOnItemSelectedListener(item ->{
            loadFragment(item.getItemId());return true;});
    }
    /********************/
    private void loadFragment(int selectedItemId)
    {
        Fragment fragment = (selectedItemId == R.id.Appointments) ? this.aMAF : this.aPF;
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    /********************/
    private void setupDarkMode()
    {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkModeOn = preferences.getBoolean("DarkMode",false);
        int mode = darkModeOn ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(mode);
    }
    /********************/
    private void setupDefaultLanguage()
    {
        Resources resources = this.getBaseContext().getResources();
        String[] languages = resources.getStringArray(R.array.languages);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = languages[preferences.getInt("Language",0)];
        Locale.setDefault(new Locale(selectedLanguage.toLowerCase()));
        resources.getConfiguration().setLocale(Locale.getDefault());
        resources.updateConfiguration(resources.getConfiguration(),resources.getDisplayMetrics());
    }
}