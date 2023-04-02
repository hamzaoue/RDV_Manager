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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.rdvmanager.Appointment;
import com.example.rdvmanager.AppointmentDataBase;
import com.example.rdvmanager.CustomDialog;
import com.example.rdvmanager.MainActivity;
import com.example.rdvmanager.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/********************/
public class SetAppointmentFragment extends Fragment
{
    private final Appointment aAppointment;
    private ActivityResultLauncher<String> aRequestPermissionLauncher;
    private ActivityResultLauncher<Intent> aContactLauncher , aLocationLauncher;
    private EditText aTitleEditText, aAddressEditText, aContactEditText, aPhoneEditText;
    /********************/
    public SetAppointmentFragment() {this.aAppointment = new Appointment();}
    public SetAppointmentFragment(Appointment appointment) {this.aAppointment = appointment;}
    /********************/
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.fragment_set_appointment,container,false);
        int title = aAppointment.getId()!=-1?R.string.appointment_information:R.string.new_appointment;
        ((MainActivity)requireActivity()).setActionBarTitle(title);
        view.findViewById(R.id.cancel_button).setOnClickListener(this::backToMyAppointment);
        view.findViewById(R.id.save_button).setOnClickListener(this::saveAppointment);
        this.setLaunchers(view);
        this.setDialogs(view);
        this.fillTextViews(view);
        return view;
    }
    /********************/
    private void setLaunchers(View view)
    {
        this.aContactLauncher= registerForActivityResult(
                new StartActivityForResult(),this::onContactPicked);

        Places.initialize(view.getContext(), view.getContext().getString(R.string.api_key));
        this.aLocationLauncher = registerForActivityResult(
                new StartActivityForResult(),this::onLocationPicked);

        this.aRequestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),this::onRequestResult);
    }
    /********************/
    private void backToMyAppointment(View view)
    {
        this.requireActivity().onBackPressed();
        this.onDestroyView();
    }
    /********************/
    private void saveAppointment(View view)
    {
        if(isEmpty(R.id.title) || isEmpty(R.id.date) ||isEmpty(R.id.time)){return;}
        this.updateAppointment();
        AppointmentDataBase appointmentDataBase = new AppointmentDataBase(view.getContext());
        appointmentDataBase.addAppointment(this.aAppointment);
        appointmentDataBase.close();
        this.backToMyAppointment(view);
    }
    /********************/
    public boolean isEmpty(int textViewId)
    {
        TextView textView = requireView().findViewById(textViewId);
        String text = textView.getText().toString().trim();
        textView.setError("");
        if(TextUtils.isEmpty(text))
            Toast.makeText(this.requireContext(),R.string.empty_field, Toast.LENGTH_SHORT).show();
        else
            textView.setError(null);
        return TextUtils.isEmpty(text);
    }
    /********************/
    private void updateAppointment()
    {
        aAppointment.setTitle(((TextView)requireView().findViewById(R.id.title)).getText().toString());
        aAppointment.setAddress(((TextView)requireView().findViewById(R.id.address)).getText().toString());
        aAppointment.setContact(((TextView)requireView().findViewById(R.id.contact)).getText().toString());
        aAppointment.setPhone(((TextView)requireView().findViewById(R.id.phone)).getText().toString());
    }
    /********************/
    private void fillTextViews(View view)
    {
        if(this.aAppointment.getId()==-1){return;}
        int[] textViewId = {R.id.title,R.id.address,R.id.contact,R.id.date,R.id.time,R.id.phone};
        String[] content = {aAppointment.getTitle(),aAppointment.getAddress(),aAppointment.getContact(),
                aAppointment.getDate(),aAppointment.getTime(),aAppointment.getPhone()};
        for(int i = 0; i < content.length; i++)
            ((TextView)view.findViewById(textViewId[i])).setText(content[i]);
        this.aTitleEditText.setText(aAppointment.getTitle());
        this.aContactEditText.setText(aAppointment.getContact());
        this.aPhoneEditText.setText(aAppointment.getPhone());
        this.aAddressEditText.setText(aAppointment.getAddress());
    }
    /********************/
    private void setDialogs(View view)
    {
        this.setTimePicker(view, R.id.time_button);
        this.setDatePicker(view, R.id.date_button);
        this.aTitleEditText = this.setDialog(view, R.id.title_button, R.string.modify_title,
                R.drawable.ic_title, R.id.title, this::generateTitle, InputType.TYPE_CLASS_TEXT);
        this.aAddressEditText = this.setDialog(view, R.id.address_button, R.string.modify_address,
                R.drawable.ic_address,R.id.address, this::pickLocation, InputType.TYPE_CLASS_TEXT);
        this.aContactEditText = this.setDialog(view, R.id.contact_button, R.string.modify_contact,
                R.drawable.ic_contact, R.id.contact, this::pickContact, InputType.TYPE_CLASS_TEXT);
        this.aPhoneEditText = this.setDialog(view, R.id.phone_button, R.string.modify_phone,
                R.drawable.ic_phone, R.id.phone, this::pickContact, InputType.TYPE_CLASS_PHONE);
    }
    /********************/
    private EditText setDialog(View view, int button_id, int title_id, int icon_id,
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
        TimePickerDialog.OnTimeSetListener listener = (view1, hours, minutes) -> {
            this.aAppointment.getCalendar().set(Calendar.HOUR_OF_DAY, hours);
            this.aAppointment.getCalendar().set(Calendar.MINUTE, minutes);
            timeView.setText(this.aAppointment.getTime());
        };
        TimePickerDialog dialog = new TimePickerDialog(this.requireContext(),listener,12,0,true);
        view.findViewById(button_id).setOnClickListener(view1 -> dialog.show());
    }
    /********************/
    private void setDatePicker(View view, int button_id)
    {
        //Configure le bouton ok pour remplir le textView avec la date au bon format
        DatePickerDialog dialog = new DatePickerDialog(this.requireContext());
        TextView dateView = view.findViewById(R.id.date);
        dialog.setOnDateSetListener((view1, year, month, day) -> {
            this.aAppointment.getCalendar().set(year,month,day);
            dateView.setText(this.aAppointment.getDate());
        });
        view.findViewById(button_id).setOnClickListener(view1 -> dialog.show());
    }
    /********************/
    private void generateTitle(View view)
    {
        this.aTitleEditText.setText(R.string.appointment);
    }
    /********************/
    private void pickLocation(View view)
    {
        // Création de l'Intent pour l'API Places Autocomplete
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields).build(this.requireContext());
        aLocationLauncher.launch(intent);
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
    private void pickContact(View view)
    {
        String permission = Manifest.permission.READ_CONTACTS ;
        int hasPermission = ContextCompat.checkSelfPermission(this.requireContext(), permission);
        if(hasPermission != PackageManager.PERMISSION_GRANTED)
            aRequestPermissionLauncher.launch(permission);
        else
            aContactLauncher.launch(new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI));
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
    /********************/
    private void onRequestResult(boolean isGranted)
    {
        this.aContactLauncher.launch(new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI));
        if (!isGranted)
            this.aContactLauncher.launch(new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI));
    }
}
