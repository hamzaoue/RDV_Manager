package com.example.rdvmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rdvmanager.fragments.SetAppointmentFragment;

import java.util.List;

/********************/
public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>
{
    private int aSelectedListId;
    private final TextView aEmptyMessageTextView;
    private final List<Appointment> aUpcomingAppointments,  aPastAppointments;
    /********************/
    public AppointmentAdapter(List<Appointment> upcoming, List<Appointment> past, TextView textView)
    {
        this.aEmptyMessageTextView = textView;
        this.aSelectedListId = R.id.Upcoming;
        this.aUpcomingAppointments = upcoming;
        this.aPastAppointments = past;
        this.updateEmptyMessageVisibility();
    }
    /********************/
    private void updateEmptyMessageVisibility()
    {this.aEmptyMessageTextView.setVisibility(this.getItemCount()==0?View.VISIBLE:View.INVISIBLE);}
    /********************/
    @Override public int getItemCount() {return this.getCurrentList().size();}
    /********************/
     @NonNull @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_appointment,parent,false));
    }
    /********************/
    @Override public void onBindViewHolder(ViewHolder holder, int position)
    {
        Appointment currentAppointment = this.getCurrentList().get(position);
        holder.aAppointmentTitle.setText(currentAppointment.getTitle());
        holder.aAppointmentDate.setText(currentAppointment.getDate());
        holder.aAppointmentTime.setText(currentAppointment.getTime());
        holder.aMoreVert.setOnClickListener(view-> showOptionDialog(view,currentAppointment));
        holder.aMoreVert.setOnClickListener(view-> showOptionDialog(view,currentAppointment));
        holder.aAppointmentButton.setOnClickListener(view->openEditFragment(view,currentAppointment));
    }
    /********************/
    private void openEditFragment(View view, Appointment appointment)
    {
        FragmentTransaction transaction;
        transaction = ((AppCompatActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SetAppointmentFragment(appointment));
        transaction.addToBackStack(null);
        transaction.commit();
    }
    /********************/
    @SuppressLint("NotifyDataSetChanged") public void setSelectedList(int id)
    {
        this.aSelectedListId = id;
        this.notifyDataSetChanged();
        this.updateEmptyMessageVisibility();
    }
    /********************/
    private List<Appointment> getCurrentList()
    {
        if(this.aSelectedListId == R.id.Upcoming)
            return this.aUpcomingAppointments;
        return this.aPastAppointments;
    }
    /********************/
    public void showOptionDialog(View view, Appointment appointment)
    {
        String[] options = view.getContext().getResources().getStringArray(R.array.options);
        AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
        builder.setItems(options, (dialog,which) ->onOptionClicked(view,appointment,which));
        builder.setTitle(appointment.getTitle());
        builder.show();
    }
    /********************/
    public void onOptionClicked(View view,Appointment appointment, int which)
    {
        switch (which)
        {
            case 0:this.openPhone(view, appointment.getPhone());break;
            case 1:this.openMap(view, appointment.getAddress());break;
            case 2:this.shareAppointment(view, appointment);break;
            case 3:this.showRemoveConfirmationDialog(view, appointment);break;
        }
    }
    /********************/
    private void openPhone(View view, String number)
    {
        if (number != null && PhoneNumberUtils.isGlobalPhoneNumber(number.replaceAll("\\s", "")))
            view.getContext().startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)));
        else
            Toast.makeText(view.getContext(),R.string.invalid_number, Toast.LENGTH_SHORT).show();
    }
    /********************/
    private void openMap(View view, String address)
    {
        if (address != null && !address.isEmpty())
        {
            Uri geoLocation = Uri.parse("geo:0,0?q=" + Uri.encode(address));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoLocation);
            mapIntent.setPackage("com.google.android.apps.maps");//Définit appli par défault
            view.getContext().startActivity(mapIntent);
        }
        else
            Toast.makeText(view.getContext(),R.string.invalid_address, Toast.LENGTH_SHORT).show();
    }
    /********************/
    private void shareAppointment(View view, Appointment appointment)
    {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, appointment.toString(view.getContext()));
        sendIntent.setType("text/plain");
        view.getContext().startActivity(sendIntent);
    }
    /********************/
    private void showRemoveConfirmationDialog(View view, Appointment appointment)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.confirmation);
        builder.setMessage(R.string.confirmation_message);
        builder.setNegativeButton(android.R.string.no, null);
        builder.setPositiveButton(android.R.string.yes,(dialog,which)
                -> this.removeAppointment(view, appointment));
        builder.show();
    }
    /********************/
    private void removeAppointment(View view,Appointment appointment)
    {
        //Supprime de la liste
        this.notifyItemRemoved(this.getCurrentList().indexOf(appointment));
        this.getCurrentList().remove(appointment);
        this.updateEmptyMessageVisibility();
        //Supprime de la base de donnée
        AppointmentDataBase db = new AppointmentDataBase(view.getContext());
        db.deleteAppointment(appointment.getId());
        db.close();

    }
    /********************/
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView aAppointmentTitle, aAppointmentDate, aAppointmentTime;
        private final RelativeLayout aAppointmentButton;
        private final ImageView aMoreVert;
        /********************/
        public ViewHolder(View itemView)
        {
            super(itemView);
            this.aAppointmentTitle = itemView.findViewById(R.id.appointment_title);
            this.aAppointmentDate = itemView.findViewById(R.id.appointment_date);
            this.aAppointmentTime = itemView.findViewById(R.id.appointment_time);
            this.aMoreVert = itemView.findViewById(R.id.more_vert);
            this.aAppointmentButton = itemView.findViewById(R.id.appointment_button);
        }
    }
}
