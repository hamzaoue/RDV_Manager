package com.example.rdvmanager.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.rdvmanager.R;

/********************/
public class PreferencesFragment extends Fragment
{
    /********************/
    public PreferencesFragment(){}
    /********************/
    @Override public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedState)
    {
        View view = inflater.inflate(R.layout.fragment_preferences, container, false);
        SharedPreferences preferences = view.getContext().
                getSharedPreferences("AppointmentPreferences", Context.MODE_PRIVATE);
        this.setUpDarkModeSwitch(view, preferences);
        this.setUpMusicSwitch(view, preferences);
        this.setUpNotificationSwitch(view, preferences);
        this.setUpLanguageSpinner(view, preferences);
        return view;
    }
    /********************/
    private void setUpDarkModeSwitch(View view, SharedPreferences preferences)
    {
        SwitchCompat darkModeSwitch = view.findViewById(R.id.dark_mode);
        darkModeSwitch.setChecked(preferences.getBoolean("DarkMode", true));
        darkModeSwitch.setOnCheckedChangeListener((b, isChecked) -> {
            preferences.edit().putBoolean("DarkMode", isChecked).apply();
            this.requireActivity().recreate();});
    }
    /********************/
    private void setUpMusicSwitch(View view, SharedPreferences preferences)
    {
        SwitchCompat musicSwitch = view.findViewById(R.id.background_music);
        musicSwitch.setChecked(preferences.getBoolean("Music", true));
        musicSwitch.setOnCheckedChangeListener((b, isChecked) ->
                preferences.edit().putBoolean("Music", isChecked).apply());
    }
    /********************/
    private void setUpNotificationSwitch(View view, SharedPreferences preferences)
    {
        SwitchCompat notificationsSwitch = view.findViewById(R.id.notification);
        notificationsSwitch.setChecked(preferences.getBoolean("Notifications", true));
        notificationsSwitch.setOnCheckedChangeListener((b, isChecked) ->
                preferences.edit().putBoolean("Notifications", isChecked).apply());
    }
    /********************/
    private void setUpLanguageSpinner(View view, SharedPreferences preferences)
    {
        Spinner spinner = view.findViewById(R.id.language_spinner);
        spinner.setSelection(preferences.getInt("Language", 0));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean isFirstItemSelected = true;
            @Override public void onNothingSelected(AdapterView<?> parent) {}
            @Override public void onItemSelected(AdapterView<?> p, View view, int position, long id)
            {
                preferences.edit().putInt("Language", position).apply();
                if (isFirstItemSelected) {isFirstItemSelected = false;}
                else {requireActivity().recreate();}
            }});
    }
}
