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
import com.nextpageti.myappointments.io.ApiService
import com.nextpageti.myappointments.io.response.loginResponse
import com.nextpageti.myappointments.util.toast
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    private val apiService : ApiService by lazy{
        ApiService.create()
    }


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

            if(preferences["jwt",""].contains("."))
                goToMainActivity()

            // Btn Ingresar
            btnLogin.setOnClickListener {
                // Validate
                performLogin()

            }

            // TextView Registerw
            tvGoToRegister.setOnClickListener{
                Toast.makeText(this,getString(R.string.label_message),Toast.LENGTH_SHORT).show()

                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

        private fun performLogin(){
            val call = apiService.postLogin(edtEmail.text.toString(), edtPassword.text.toString())
            call.enqueue(object: retrofit2.Callback<loginResponse>{
                override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                    toast(t.localizedMessage)
                }

                override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                    if(response.isSuccessful){
                        val loginResponse = response.body()
                        if (loginResponse == null){
                            // Toast
                            toast("Se obtuvo una respuesta inesperada del servidor 1.1")
                            return
                        }
                        if(loginResponse.success){
                            createSessionPreferences(loginResponse.jwt)
                            goToMainActivity()
                        }else {
                            toast("Las credenciales son incorrectas")
                        }
                    }else { // error 404, etc
                        toast("Error interno del servidor")
                    }
                }

            })
        }

        private fun createSessionPreferences(jwt : String){
            /* PreferenceManager.getDefaultSharedPreferences(Context.MODE_PRIVATE)
             val preferences = getSharedPreferences("general", Context.MODE_PRIVATE)
             val editor = preferences.edit()
             editor.putBoolean("session",true)
             editor.apply()
             */
            val preferences = PreferenceHelper.defaultPrefs(this)
            preferences["jwt"]=jwt
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


