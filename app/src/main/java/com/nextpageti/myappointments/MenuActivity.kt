package com.nextpageti.myappointments

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_create_appointment.*
import kotlinx.android.synthetic.main.activity_menu.*
import com.nextpageti.myappointments.PreferenceHelper.get
import com.nextpageti.myappointments.PreferenceHelper.set

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnCreateAppointment.setOnClickListener{
            val intent = Intent (this,CreateAppointmentActivity::class.java)
            startActivity(intent)
        }

        btnMyAppointments.setOnClickListener{
            val intent = Intent (this,AppointmentsActivity::class.java)
            startActivity(intent)
        }

        // Btn Close session
        btnLogOut.setOnClickListener {
            clearSessionPreferences()
            val intent = Intent (this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun clearSessionPreferences(){
        /*
        val preferences = getSharedPreferences("general", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("session",false)
        editor.apply()
        */
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["session"] = false
    }

}
