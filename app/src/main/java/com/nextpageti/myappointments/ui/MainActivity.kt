package com.nextpageti.myappointments.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.nextpageti.myappointments.PreferenceHelper
import kotlinx.android.synthetic.main.activity_main.*
import com.nextpageti.myappointments.PreferenceHelper.get
import com.nextpageti.myappointments.PreferenceHelper.set
import com.nextpageti.myappointments.R

class MainActivity : AppCompatActivity() {

    private val snackBar by lazy {
        Snackbar.make(mainLayout, R.string.press_back_again, Snackbar.LENGTH_SHORT)
    }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // Shared prefereneces
            /*
            val preferences = getSharedPreferences("general", Context.MODE_PRIVATE)
            val session = preferences.getBoolean("session",false)
            */
            val preferences = PreferenceHelper.defaultPrefs(this)

            if(preferences["session",false])
                goToMainActivity()

            // Btn Ingresar
            btnLogin.setOnClickListener {
                // Validar
                createSessionPreferences()
                goToMainActivity()
            }

            // TextView Registerw
            tvGoToRegister.setOnClickListener{
                Toast.makeText(this,getString(R.string.label_message),Toast.LENGTH_SHORT).show()

                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

        private fun createSessionPreferences(){
            /* PreferenceManager.getDefaultSharedPreferences(Context.MODE_PRIVATE)
             val preferences = getSharedPreferences("general", Context.MODE_PRIVATE)
             val editor = preferences.edit()
             editor.putBoolean("session",true)
             editor.apply()
             */
            val preferences = PreferenceHelper.defaultPrefs(this)
            preferences["session"]=true
        }

        private fun goToMainActivity (){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        override fun onBackPressed() {

            if(snackBar.isShown)
                super.onBackPressed()

            else
                snackBar.show()
        }
    }


