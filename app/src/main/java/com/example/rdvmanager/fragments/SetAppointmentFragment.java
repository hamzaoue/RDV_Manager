package com.example.rdvmanager.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rdvmanager.CustomDialog;
import com.example.rdvmanager.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/********************/
public class SetAppointmentFragment extends Fragment
{
    private final MyAppointmentsFragment aMAF;
    private ActivityResultLauncher<Intent> aContactLauncher;
    private ActivityResultLauncher<Intent> aMapLauncher;
    private ActivityResultLauncher<String> aRequestPermissionLauncher;
    private EditText aTitleEditText, aAddressEditText, aContactEditText, aPhoneEditText;
    /********************/
    public SetAppointmentFragment(MyAppointmentsFragment fragment) {this.aMAF = fragment;}
    /********************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {

        aContactLauncher=registerForActivityResult(new StartActivityForResult(),this::onContactPicked);
        aMapLauncher = registerForActivityResult(new StartActivityForResult(),this::onLocationPicked);
        aRequestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),this::onRequestResult);
        View view = inflater.inflate(R.layout.fragment_set_appointment,container,false);
        Places.initialize(view.getContext(), "");
        view.findViewById(R.id.cancel_button).setOnClickListener(this::backToMyAppointment);
        view.findViewById(R.id.save_button).setOnClickListener(this::saveAppointment);
        this.setDialogs(view);
        return view;
    }
    /********************/
    private void setDialogs(View view)
    {
        //Initialise les boîtes de dialogue
        this.setTimePicker(view, R.id.time_button);
        this.setDatePicker(view, R.id.date_button);
        this.aTitleEditText = this.createDialog(view, R.id.title_button, R.string.modify_title,
                R.drawable.ic_title, R.id.title, this::generateTitle, InputType.TYPE_CLASS_TEXT);
        this.aAddressEditText = this.createDialog(view, R.id.address_button, R.string.modify_address,
                R.drawable.ic_address,R.id.address, this::pickLocation, InputType.TYPE_CLASS_TEXT);
        this.aContactEditText = this.createDialog(view, R.id.contact_button, R.string.modify_contact,
                R.drawable.ic_contact, R.id.contact, this::pickContact, InputType.TYPE_CLASS_TEXT);
        this.aPhoneEditText = this.createDialog(view, R.id.phone_button, R.string.modify_phone,
                R.drawable.ic_phone, R.id.phone, this::pickContact, InputType.TYPE_CLASS_PHONE);
    }
    /********************/
    private void generateTitle(View view)
    {
        this.aTitleEditText.setText(R.string.appointment);
    }
    /********************/
    private void pickContact(View view)
    {
        String permission =Manifest.permission.READ_CONTACTS ;
        int hasPermission = ContextCompat.checkSelfPermission(this.requireContext(), permission);
        if(hasPermission != PackageManager.PERMISSION_GRANTED)
            aRequestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        else
        {
            Intent toContact = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
            aContactLauncher.launch(toContact);
        }
    }
    /********************/
    private void pickLocation(View view)
    {
        // Création de l'Intent pour l'API Places Autocomplete
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields).build(this.requireContext());

        // Démarrage de l'activité d'Autocomplete avec l'Intent créé
        aMapLauncher.launch(intent);
    }
    /********************/
    private EditText createDialog(View view, int button_id, int title_id, int icon_id,
                                      int textView_id, View.OnClickListener listener, int inputType)
    {
        // Créer la boîte de dialogue
        Context context = this.requireContext();
        CustomDialog dialog = new CustomDialog(context, title_id, icon_id);

        //Configure l'action du bouton ok pour remplir le textView
        TextView textView = view.findViewById(textView_id);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,context.getString(R.string.save),
                (dialog1,which)->textView.setText(dialog.getEditText().getText()));

        //Bouton qui ouvre le CustomDialog
        view.findViewById(button_id).setOnClickListener(view1 -> dialog.show());

        //Configure l'action de l'ImageButton avec une action définie en paramètre
        dialog.getImageButton().setOnClickListener(listener);

        //Configure le type de clavier de l'editText (chiffre ou lettre)
        dialog.getEditText().setInputType(inputType);
        return dialog.getEditText();
    }
    /********************/
    private void setTimePicker(View view, int button_id)
    {
        //Configure l'action du bouton ok pour remplir le textView avec l'heure au bon format
        TextView timeView = view.findViewById(R.id.time);
        TimePickerDialog.OnTimeSetListener listener = (view1, hours, minutes) ->
                timeView.setText(String.format(Locale.FRANCE,"%02d:%02d", hours, minutes));

        // Créer la boîte de dialogue
        TimePickerDialog dialog = new TimePickerDialog(this.requireContext(),
                R.style.MyDialogTheme, listener, 12 , 0, true);

        //Bouton qui ouvre le timePicker
        view.findViewById(button_id).setOnClickListener(view1 -> dialog.show());
    }
    /********************/
    private void setDatePicker(View view, int button_id)
    {
        // Créer la boîte de dialogue
        DatePickerDialog dialog = new DatePickerDialog(this.requireContext(),R.style.MyDialogTheme);

        //Bouton qui ouvre le datePicker
        view.findViewById(button_id).setOnClickListener(view1 -> dialog.show());

        //Configure le bouton ok pour remplir le textView avec la date au bon format
        Calendar calendar = Calendar.getInstance();
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        dialog.setOnDateSetListener((view1, year, month, day) -> {calendar.set(year,month,day);
            ((TextView)view.findViewById(R.id.date)).setText(format.format(calendar.getTime()));});
    }
    /********************/
    private void backToMyAppointment(View view)
    {
        FragmentTransaction transaction = this.getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, this.aMAF);
        transaction.commit();
        this.onDestroyView();
    }
    /********************/
    private void saveAppointment(View view)
    {

    }
    /********************/
    private void onRequestResult(boolean isGranted)
    {
        this.aContactLauncher.launch(new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI));
        if (!isGranted)
            this.aContactLauncher.launch(new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI));
    }
    /********************/
    private void onLocationPicked(ActivityResult result)
    {
        // Vérifie si le résultat est ok et si les données sont présentes
        if (result.getData() == null || result.getResultCode() != Activity.RESULT_OK){return;}

        Intent intent = result.getData();
        Place place = Autocomplete.getPlaceFromIntent(intent);
        this.aAddressEditText.setText(place.getAddress());
    }
    /********************/
    private void onContactPicked(ActivityResult result)
    {
        // Vérifie si le résultat est ok et si les données sont présentes
        if (result.getData() == null || result.getResultCode() != Activity.RESULT_OK){return;}

        // Récupérer les informations du contact sélectionné
        ContentResolver resolver = this.requireContext().getContentResolver();
        Cursor cursor = resolver.query(result.getData().getData(), null , null, null, null);
        if (cursor == null || !cursor.moveToFirst()){return;}

        // Afficher le nom et le numéro dans les boîtes de dialogue correspondante
        this.aContactEditText.setText(this.getContactName(cursor));
        this.aPhoneEditText.setText(this.getContactPhone(cursor));
        cursor.close();
    }
    /********************/
    private String getContactName(Cursor cursor)
    {
        // Récupère l'index de la colonne contenant le nom
        int nameColumnIndex = cursor.getColumnIndex(Phone.DISPLAY_NAME);
        if(nameColumnIndex == -1){return null;}
        return cursor.getString(nameColumnIndex);
    }
    /********************/
    private String getContactPhone(Cursor cursor)
    {
        // Récupère l'index de la colonne contenant l'ID du contact
        int idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        if(idColumnIndex == -1) {return null;}

        // Crée une clause de sélection pour récupérer les numéros de téléphone liés à l'ID
        String selection = Phone.CONTACT_ID + " = " + cursor.getString(idColumnIndex);
        ContentResolver resolver = this.requireContext().getContentResolver();
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,null,selection,null,null,null);
        if (phoneCursor == null || !phoneCursor.moveToFirst()) {return null;}

        // Récupère l'index de la colonne contenant le numéro de téléphone
        int phoneColumnIndex = phoneCursor.getColumnIndex(Phone.NUMBER);
        String number = phoneCursor.getString(phoneColumnIndex);
        phoneCursor.close();
        return number;
    }
}
