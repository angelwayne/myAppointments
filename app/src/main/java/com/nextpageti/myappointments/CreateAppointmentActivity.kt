package com.nextpageti.myappointments

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_appointment.*

class CreateAppointmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_appointment)

        btnNext.setOnClickListener{
            cvStep1.visibility= View.GONE
            cvStep2.visibility=View.VISIBLE
        }

        btnConfirmAppointment.setOnClickListener{

            Toast.makeText(this,"Cita  Registrada Correctamente",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
