package com.example.rdvmanager.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.rdvmanager.MainActivity;
import com.example.rdvmanager.R;

import java.util.concurrent.atomic.AtomicReference;

/********************/
public class PreferencesFragment extends Fragment
{
    private ActivityResultLauncher<String> aRequestPermissionLauncher;
    /********************/
    public PreferencesFragment(){}
    /********************/
    @Override public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedState)
    {
        View view = inflater.inflate(R.layout.fragment_preferences, container, false);
        ((MainActivity)requireActivity()).setActionBarTitle(R.string.preferences);
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(view.getContext());
        this.setupDarkModeSwitch(view, preferences);
        this.setupMusicSwitch(view, preferences);
        this.setupNotificationSwitch(view, preferences);
        this.setupLanguageSpinner(view, preferences);
        this.setupNotificationButton(view, preferences);
        return view;
    }
    /********************/
    private void setupNotificationButton(View view, SharedPreferences preferences)
    {
        View notificationButton = view.findViewById(R.id.notification_button);
        notificationButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.MyDialogTheme);
            builder.setTitle(R.string.send_notification);

            // Créer un radiogroupe
            final RadioGroup radioGroup = new RadioGroup(getContext());
            radioGroup.setOrientation(RadioGroup.VERTICAL);
            radioGroup.setId(View.generateViewId()); // Définir un ID pour le RadioGroup

            // Créer les boutons radio pour les options de notification
            final RadioButton radioButton1 = new RadioButton(getContext());
            radioButton1.setText(R.string.two_hours_before);
            radioButton1.setId(View.generateViewId()); // Définir un ID pour le RadioButton
            radioGroup.addView(radioButton1);

            final RadioButton radioButton2 = new RadioButton(getContext());
            radioButton2.setText(R.string.one_day_before);
            radioButton2.setId(View.generateViewId()); // Définir un ID pour le RadioButton
            radioGroup.addView(radioButton2);

            final RadioButton radioButton3 = new RadioButton(getContext());
            radioButton3.setText(R.string.one_week_before);
            radioButton3.setId(View.generateViewId()); // Définir un ID pour le RadioButton
            radioGroup.addView(radioButton3);

            // Ajouter le radiogroupe au dialogue
            builder.setView(radioGroup);

            // Ajouter les boutons pour le dialogue
            builder.setPositiveButton("OK", (dialog,which)->{});
            builder.setNegativeButton("Annuler", null);

            // Afficher le dialogue
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
    /********************/
    private void setupDarkModeSwitch(View view, SharedPreferences preferences)
    {
        SwitchCompat darkModeSwitch = view.findViewById(R.id.dark_mode);
        darkModeSwitch.setChecked(preferences.getBoolean("DarkMode", false));
        darkModeSwitch.setOnCheckedChangeListener((b, isChecked) -> {
            preferences.edit().putBoolean("DarkMode", isChecked).apply();
            this.requireActivity().recreate();});
    }
    /********************/
    private void setupMusicSwitch(View view, SharedPreferences preferences)
    {
        SwitchCompat musicSwitch = view.findViewById(R.id.background_music);
        musicSwitch.setChecked(preferences.getBoolean("Music", false));
        musicSwitch.setOnCheckedChangeListener((b, isChecked) ->
                preferences.edit().putBoolean("Music", isChecked).apply());
    }
    /********************/
    private void setupNotificationSwitch(View view, SharedPreferences preferences)
    {
        this.aRequestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),isGranted->{});
        SwitchCompat notificationsSwitch = view.findViewById(R.id.notification);
        notificationsSwitch.setChecked(preferences.getBoolean("Notifications", false));
        notificationsSwitch.setOnCheckedChangeListener((b, isChecked) ->
        {
            preferences.edit().putBoolean("Notifications", isChecked).apply();
            String permission = Manifest.permission.SET_ALARM ;
            int hasPermission = ContextCompat.checkSelfPermission(this.requireContext(),permission);
            if(hasPermission != PackageManager.PERMISSION_GRANTED)
                this.aRequestPermissionLauncher.launch(permission);
        });
    }
    /********************/
    @SuppressLint("ClickableViewAccessibility")
    private void setupLanguageSpinner(View view, SharedPreferences preferences)
    {
        Spinner spinner = view.findViewById(R.id.language_spinner);
        spinner.setSelection(preferences.getInt("Language", 0));
        AtomicReference<Boolean> touched = new AtomicReference<>(false);
        spinner.setOnTouchListener((v, event) -> {touched.set(true);return false;});
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onNothingSelected(AdapterView<?> parent) {}
            @Override public void onItemSelected(AdapterView<?> p, View view, int position, long id)
            {
                preferences.edit().putInt("Language", position).apply();
                if (touched.get())
                    requireActivity().recreate();
            }});
    }
}
