package com.nextpageti.myappointments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nextpageti.myappointments.Model.Appointment
import kotlinx.android.synthetic.main.item_appointment.*
import kotlinx.android.synthetic.main.item_appointment.view.*

class AppointmentAdapter(private val appointments:ArrayList<Appointment>)
    : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(appointment: Appointment) = with(itemView){
        tvAppointmenId.text = context.getString(R.string.item_Apppointment_id,appointment.id)
        tvscheduleDate.text = context.getString(R.string.item_Apppointment_date,appointment.scheduleDate)
        tvScheduleTime.text = context.getString(R.string.item_Apppointment_time,appointment.scheduleTime)
        tvDoctorName.text = appointment.doctorName
    }

    }
    // inflate XML item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_appointment,parent,false)
        )
    }

    // Binds data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]

        holder.bind(appointment)

    }

    // Return of elements (count)
    override fun getItemCount() = appointments.size
}