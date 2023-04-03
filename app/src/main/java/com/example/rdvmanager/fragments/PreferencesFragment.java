package com.example.rdvmanager.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.rdvmanager.MainActivity;
import com.example.rdvmanager.NotificationManager;
import com.example.rdvmanager.R;

import java.util.concurrent.atomic.AtomicReference;

/********************/
public class PreferencesFragment extends Fragment
{
    /********************/
    public PreferencesFragment(){}
    /********************/
    @Override public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedState)
    {
        View view = inflater.inflate(R.layout.fragment_preferences, container, false);
        ((MainActivity)requireActivity()).setActionBarTitle(R.string.preferences);
        this.setupSwitch(view,R.id.notification,"Notifications",false);
        this.setupSwitch(view,R.id.dark_mode,"DarkMode",true);
        this.setupSwitch(view,R.id.music,"Music",false);
        this.setupLanguageSpinner(view);
        this.setupNotificationButton(view);
        return view;
    }
    /********************/
    private void setupSwitch(View view, int SwitchId, String prefsKey, boolean recreate)
    {
        SwitchCompat switchCompat = view.findViewById(SwitchId);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        switchCompat.setChecked(prefs.getBoolean(prefsKey, false));
        switchCompat.setOnCheckedChangeListener((b, isChecked) -> {
            prefs.edit().putBoolean(prefsKey, isChecked).apply();
            if(recreate) this.requireActivity().recreate();});
    }
    /********************/
    private void setupLanguageSpinner(View view)
    {
        // Récupère le spinner depuis la vue et définie selon les préférences utilisateur
        Spinner spinner = view.findViewById(R.id.language_spinner);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        spinner.setSelection(prefs.getInt("Language", 0));

        //Vérifie si onItemSelected est enclenché par l'utilisateur ou autre (car spinner full bug)
        AtomicReference<Boolean> touched = new AtomicReference<>(false);
        spinner.setOnTouchListener((v, event) -> {touched.set(true);return v.performClick();});
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onNothingSelected(AdapterView<?> parent) {}
            @Override public void onItemSelected(AdapterView<?> p, View view, int position, long id){
                prefs.edit().putInt("Language", position).apply();
                if (touched.get()) requireActivity().recreate();}});
    }
    /********************/
    private void setupNotificationButton(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        RadioGroup radioGroup = this.createNotificationRadioGroup(view.getContext());
        builder.setView(radioGroup);
        builder.setTitle(R.string.send_notification);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.save, (dialog, which) -> {
            NotificationManager.updateAllAlarms(view.getContext());
            prefs.edit().putInt("NotificationIndex",radioGroup.getCheckedRadioButtonId()).apply();});
        AlertDialog dialog = builder.create();
        view.findViewById(R.id.notification_button).setOnClickListener(v -> {
            radioGroup.check(prefs.getInt("NotificationIndex",0));dialog.show();});
    }
    /********************/
    private RadioGroup createNotificationRadioGroup(Context context)
    {
        int[] optionsId = { R.string.two_hours, R.string.one_day, R.string.one_week };
        RadioGroup radioGroup = new RadioGroup(context);
        int margin = (int) context.getResources().getDimension(R.dimen.default_margin);
        radioGroup.setPadding(margin, margin, margin, margin);
        for (int i = 0; i < optionsId.length; i++) {
            RadioButton radioButton = new RadioButton(context);
            String option = getString(optionsId[i]) + " " + getString(R.string.before);
            radioButton.setText(option);
            radioButton.setId(i);
            radioGroup.addView(radioButton);
        }
        return radioGroup;
    }
}

