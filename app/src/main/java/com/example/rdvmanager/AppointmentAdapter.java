package com.example.rdvmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

/********************/
public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>
{
    private List<Appointment> aAppointments;
    private final MyAppointmentsFragment aMAF;
    private final TextView aEmptyMessageTextView;
    /********************/
    public AppointmentAdapter(MyAppointmentsFragment fragment, TextView textView)
    {
        this.aEmptyMessageTextView = textView;
        this.aAppointments = new ArrayList<>();
        this.aMAF = fragment;
        this.showEmptyMessage();
    }
    /********************/
    @SuppressLint("NotifyDataSetChanged")
    public void setAppointments(List<Appointment> appointments)
    {
        this.aAppointments = appointments;
        this.notifyDataSetChanged();
        this.showEmptyMessage();
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
        holder.aMoreVert.setOnClickListener(view-> showOptionDialog(view,currentAppointment));
        holder.aAppointmentButton.setOnClickListener(view->
            aMAF.loadFragment(new SetAppointmentFragment(currentAppointment)));
    }
    /********************/
    private void showEmptyMessage()
    {
        if(this.getItemCount()==0)
            this.aEmptyMessageTextView.setVisibility(View.VISIBLE);
        else
            this.aEmptyMessageTextView.setVisibility(View.INVISIBLE);
    }
    /********************/
    private void showOptionDialog(View view, Appointment appointment)
    {
        Context context = view.getContext();
        CharSequence[] options = new CharSequence[]
                {context.getString(R.string.call),context.getString(R.string.open_in_map),
                context.getString(R.string.share), context.getString(R.string.remove)};
        AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext(),R.style.MyDialogTheme);
        builder.setTitle(appointment.getTitle());
        builder.setItems(options, (dialog,which)->onOptionClicked(view,appointment,which));
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_black);
        alertDialog.show();
    }
    /********************/
    private void onOptionClicked(View view,Appointment appointment, int which)
    {
        switch (which)
        {
            case 0:this.aMAF.openPhone(view, appointment.getPhone());break;
            case 1:this.aMAF.openMap(view, appointment.getAddress());break; // Ouvrir dans Maps break;
            case 2:this.aMAF.shareAppointment(view, appointment);break;
            case 3:this.showRemoveConfirmationDialog(view, appointment);break;
        }
    }
    /********************/
    private void showRemoveConfirmationDialog(View view, Appointment appointment)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(),R.style.MyDialogTheme);
        builder.setTitle(R.string.confirmation);
        builder.setMessage(R.string.confirmation_message);
        builder.setNegativeButton(android.R.string.no, null);
        builder.setPositiveButton(android.R.string.yes,(dialog,which)->removeAppointment(view,appointment));
        builder.show();
    }
    /********************/
    private void removeAppointment(View view, Appointment appointment)
    {
        AppointmentDataBase db = new AppointmentDataBase(view.getContext());
        db.deleteAppointment(appointment.getId());
        db.close();
        notifyItemRemoved(this.aAppointments.indexOf(appointment));
        this.aAppointments.remove(appointment);
        this.showEmptyMessage();
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
