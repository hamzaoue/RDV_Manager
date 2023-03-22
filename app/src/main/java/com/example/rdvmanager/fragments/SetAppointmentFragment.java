package com.example.rdvmanager.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rdvmanager.R;

import java.util.Locale;

/********************/
public class SetAppointmentFragment extends Fragment
{
    private final MyAppointmentsFragment aMAF;
    /********************/
    public  SetAppointmentFragment(MyAppointmentsFragment fragment)
    {
        this.aMAF = fragment;
    }
    /********************/
    @Override public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle bundle)
    {
        View view = inflater.inflate(R.layout.fragment_set_appointment,container,false);
        view.findViewById(R.id.cancel_button).setOnClickListener(this::backToMyAppointment);
        view.findViewById(R.id.save_button).setOnClickListener(this::saveAppointment);
        view.findViewById(R.id.title_button).setOnClickListener(this::openTitleDialog);
        view.findViewById(R.id.date_button).setOnClickListener(this::openDatePicker);
        view.findViewById(R.id.time_button).setOnClickListener(this::openTimePicker);
        view.findViewById(R.id.address_button).setOnClickListener(this::openAddressDialog);
        view.findViewById(R.id.contact_button).setOnClickListener(this::openContactDialog);
        view.findViewById(R.id.phone_button).setOnClickListener(this::openPhoneDialog);

        return view;
    }
    /********************/
    private void saveAppointment(View view)
    {
        //FragmentTransaction pour gérer les transactions de fragments
        FragmentTransaction transaction = this.getParentFragmentManager().beginTransaction();

        // Remplacer le fragment actuel par le fragment aMAF
        transaction.replace(R.id.fragment_container, this.aMAF);

        // Appliquer les modifications de la transaction
        transaction.commit();

        // Détruire la vue du fragment actuel pour libérer les ressources
        this.onDestroyView();
    }
    /********************/
    private void backToMyAppointment(View view)
    {
        //FragmentTransaction pour gérer les transactions de fragments
        FragmentTransaction transaction = this.getParentFragmentManager().beginTransaction();

        // Remplacer le fragment actuel par le fragment aMAF
        transaction.replace(R.id.fragment_container, this.aMAF);

        // Appliquer les modifications de la transaction
        transaction.commit();

        // Détruire la vue du fragment actuel pour libérer les ressources
        this.onDestroyView();
    }
    /********************/
    private void openDatePicker(View view)
    {
        // Récupère la vue contenant le texte à modifier
        TextView dateView = view.findViewById(R.id.date);

        // Créer un listener pour écouter les événements de sélection de date
        DatePickerDialog.OnDateSetListener listener = (view1, year, month, day) ->
                dateView.setText(String.format(Locale.getDefault(),
                        "%d/%02d/%d", day, month + 1, year));

        // Créer une boîte de dialogue de sélection de date
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(this.requireContext(), R.style.MyDialogTheme);
        datePickerDialog.setOnDateSetListener(listener);
        datePickerDialog.show();
    }
    /********************/
    private void openTimePicker(View view)
    {
        // Obtient l'heure par défaut à partir du TextView
        TextView timeView = view.findViewById(R.id.time);
        int dHours = 12 , dMins = 30;
        String[] timeString = timeView.getText().toString().split(":");
        try{dHours = Integer.parseInt(timeString[0]); dMins = Integer.parseInt(timeString[1]);}
        catch(Exception ignore){}

        // Créer un listener pour écouter les événements de sélection d'heure
        TimePickerDialog.OnTimeSetListener listener = (view1, hours, minutes) ->
                timeView.setText(new StringBuilder().append(hours).append(":").append(minutes));

        // Créer une boîte de dialogue de sélection d'heure
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.requireContext(),
                R.style.MyDialogTheme, listener,  dHours , dMins, true);
        timePickerDialog.show();
    }
    /********************/
    private void openTitleDialog(View view)
    {
        //Variables
        AlertDialog.Builder builder = this.createBuilder(R.string.modify_title);
        View content = this.getLayoutInflater().inflate(R.layout.alert_dialog_content,null);
        ImageButton imageButton = content.findViewById(R.id.set_section_button);
        EditText editText = content.findViewById(R.id.set_section_edit_text);
        TextView textView = view.findViewById(R.id.title);

        //Modifications
        editText.setText(textView.getText());
        imageButton.setImageResource(R.drawable.ic_title);
        imageButton.setOnClickListener(view1-> editText.setText(R.string.title));
        builder.setPositiveButton(R.string.save, (dialog,whichButton)->
                textView.setText(editText.getText()));
        //Affichage du dialog
        builder.setView(content);
        builder.create().show();
    }
    /********************/
    private void openAddressDialog(View view)
    {
        //Variables
        AlertDialog.Builder builder = this.createBuilder(R.string.modify_address);
        View content = this.getLayoutInflater().inflate(R.layout.alert_dialog_content,null);
        ImageButton imageButton = content.findViewById(R.id.set_section_button);
        EditText editText = content.findViewById(R.id.set_section_edit_text);
        TextView textView = view.findViewById(R.id.address);

        //Modifications
        editText.setText(textView.getText());
        imageButton.setImageResource(R.drawable.ic_address);
        imageButton.setOnClickListener(view1 ->
                this.openGoogleMap(editText.getText().toString()));
        builder.setPositiveButton(R.string.save, (dialog,whichButton)->
                textView.setText(editText.getText()));
        //Affichage du dialog
        builder.setView(content);
        builder.create().show();
    }
    /********************/
    private void openContactDialog(View view)
    {
        //Variables
        AlertDialog.Builder builder = this.createBuilder(R.string.modify_contact);
        View content = this.getLayoutInflater().inflate(R.layout.alert_dialog_content,null);
        ImageButton imageButton = content.findViewById(R.id.set_section_button);
        EditText editText = content.findViewById(R.id.set_section_edit_text);
        TextView textView = view.findViewById(R.id.contact);

        //Modifications
        editText.setText(textView.getText());
        imageButton.setImageResource(R.drawable.ic_contact);
        imageButton.setOnClickListener(view1 ->
                this.requestPermission());
        builder.setPositiveButton(R.string.save, (dialog,whichButton)->
                textView.setText(editText.getText()));
        //Affichage du dialog
        builder.setView(content);
        builder.create().show();
    }
    /********************/
    private void openPhoneDialog(View view)
    {
        //Variables
        AlertDialog.Builder builder = this.createBuilder(R.string.modify_phone);
        View content = this.getLayoutInflater().inflate(R.layout.alert_dialog_content,null);
        ImageButton imageButton = content.findViewById(R.id.set_section_button);
        EditText editText = content.findViewById(R.id.set_section_edit_text);
        TextView textView = view.findViewById(R.id.phone);

        //Modifications
        editText.setText(textView.getText());
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
        imageButton.setImageResource(R.drawable.ic_phone);
        imageButton.setOnClickListener(view1 ->
                this.requestPermission());
        builder.setPositiveButton(R.string.save, (dialog,whichButton)->
                textView.setText(editText.getText()));
        //Affichage du dialog
        builder.setView(content);
        builder.create().show();
    }
    /********************/
    private AlertDialog.Builder createBuilder(int title_Id)
    {
        AlertDialog.Builder builder ;
        builder = new AlertDialog.Builder(this.requireContext(),R.style.MyDialogTheme);
        builder.setPositiveButton(R.string.save, null);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setTitle(title_Id);
        return builder;
    }
    /********************/
    private void openGoogleMap(String address)
    {
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
    /********************/
    private void requestPermission()
    {
        if (ContextCompat.checkSelfPermission(this.requireContext(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this.requireActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
        int CONTACT_PICKER_RESULT = 0;
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
        
    }

}
