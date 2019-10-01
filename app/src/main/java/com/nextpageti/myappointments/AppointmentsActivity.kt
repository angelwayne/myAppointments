package com.nextpageti.myappointments

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.nextpageti.myappointments.Model.Appointment
import kotlinx.android.synthetic.main.activity_appointments.*

class AppointmentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        val appointments = ArrayList<Appointment>()
        appointments.add(
            Appointment(1,"Medico Wayne","12/12/2019","3:00 PM")
        )
        appointments.add(
            Appointment(1,"Medico Bruce","10/12/2019","3:30 PM")
        )
        appointments.add(
            Appointment(1,"Medico House","12/10/2019","4:00 PM")
        )

        recyclerViewAppointments.layoutManager=LinearLayoutManager(this) // GridLayoutManager
        recyclerViewAppointments.adapter = AppointmentAdapter(appointments)
    }
}
