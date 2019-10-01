package com.nextpageti.myappointments

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Shared prefereneces

        val preferences = getSharedPreferences("general", Context.MODE_PRIVATE)
        val session = preferences.getBoolean("Active_Session",false)

        if(session)
            goToMainActivity()

        // Btn Ingresar
        btnLogin.setOnClickListener {
            // Validar
            createSessionPreferences()
            goToMainActivity()
        }

        // TextView Register
        tvGoToRegister.setOnClickListener{
            Toast.makeText(this,getString(R.string.label_message),Toast.LENGTH_SHORT).show()

            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createSessionPreferences(){
       // PreferenceManager.getDefaultSharedPreferences(Context.MODE_PRIVATE)
        val preferences = getSharedPreferences("general", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("session",true)
        editor.apply()
    }

    private fun goToMainActivity (){
        val intent = Intent(this,MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}
