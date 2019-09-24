package com.nextpageti.myappointments

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener {
          val intent = Intent(this,MenuActivity::class.java)
            startActivity(intent)
        }

        tvGoToRegister.setOnClickListener{
            Toast.makeText(this,getString(R.string.label_message),Toast.LENGTH_SHORT).show()

            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
