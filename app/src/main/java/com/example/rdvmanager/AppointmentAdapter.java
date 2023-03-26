package com.example.rdvmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rdvmanager.fragments.MyAppointmentsFragment;
import com.example.rdvmanager.fragments.SetAppointmentFragment;

import java.util.List;

/********************/
public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>
{
    private List<Appointment> aAppointments;
    private final MyAppointmentsFragment aMAF;
    /********************/
    public AppointmentAdapter(MyAppointmentsFragment fragment, List<Appointment> appointments)
    {
        this.aAppointments = appointments;
        this.aMAF = fragment;
    }
    /********************/
    @SuppressLint("NotifyDataSetChanged")
    public void setAppointments(List<Appointment> appointments)
    {
        this.aAppointments = appointments;
        this.notifyDataSetChanged();
    }
    /********************/
    @Override public int getItemCount() {return aAppointments.size();}
    /********************/
    @NonNull @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_appointment,parent,false));
    }
    /********************/
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Appointment currentAppointment = this.aAppointments.get(position);
        holder.aAppointmentTitle.setText(currentAppointment.getTitle());
        holder.aAppointmentDate.setText(currentAppointment.getDate());
        holder.aAppointmentTime.setText(currentAppointment.getTime());
        holder.aMoreVert.setOnClickListener(view-> showOptionDialog(view,position));
        holder.aAppointmentButton.setOnClickListener(view->
            this.aMAF.loadFragment(new SetAppointmentFragment(this.aMAF, aAppointments.get(position))));
    }
    /********************/
    private void showOptionDialog(View view, int position)
    {
        CharSequence[] options = new CharSequence[]{"Appeler", "Ouvrir dans Maps", "Supprimer"};
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(),R.style.MyDialogTheme);
        builder.setTitle(this.aAppointments.get(position).getTitle());
        builder.setItems(options, (dialog,which)->onOptionClicked(view,position,which));
        builder.show();
    }
    /********************/
    private void onOptionClicked(View view, int position, int which)
    {
        switch (which)
        {
            case 0:break; // Appeler break;
            case 1:break; // Ouvrir dans Maps break;
            case 2:this.showRemoveConfirmationDialog(view, position);break; // Supprimer
        }
    }
    /********************/
    private void showRemoveConfirmationDialog(View view, int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(),R.style.MyDialogTheme);
        builder.setTitle("Confirmation");
        builder.setMessage("Êtes-vous sûr de vouloir supprimer cet élément ?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setNegativeButton(android.R.string.no, null);
        builder.setPositiveButton(android.R.string.yes,(dialog,which)->removeAppointment(view,position));
        builder.show();
    }
    /********************/
    private void removeAppointment(View view, int position)
    {
        DatabaseManager db = new DatabaseManager(view.getContext());
        db.deleteAppointment(aAppointments.get(position).getId());
        db.close();
        aAppointments.remove(position);
        notifyItemRemoved(position);
    }
    /********************/
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView aAppointmentTitle, aAppointmentDate, aAppointmentTime;
        private final RelativeLayout aAppointmentButton;
        private final ImageView aMoreVert;
        /********************/
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.aAppointmentTitle= itemView.findViewById(R.id.appointment_title);
            this.aAppointmentDate= itemView.findViewById(R.id.appointment_date);
            this.aAppointmentTime = itemView.findViewById(R.id.appointment_time);
            this.aMoreVert = itemView.findViewById(R.id.more_vert);
            this.aAppointmentButton = itemView.findViewById(R.id.appointment_button);
        }
    }
}
