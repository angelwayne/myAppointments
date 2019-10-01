package com.nextpageti.myappointments

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_appointment.*
import java.util.*

class CreateAppointmentActivity : AppCompatActivity() {

    private val selectedCalendar = Calendar.getInstance()
    private var selectedRadiobutton : RadioButton? = null

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

        val specialtyOptions = arrayOf("Sepecialty A","Sepecialty B","Sepecialty C")
        spinnerSpecialty.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,specialtyOptions)

        val doctorOptions = arrayOf("Dcotor A","Docotor B","Doctor C")
        sipinnerDoctor.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,doctorOptions)
    }

    fun onClickScheduleDate(v : View){

        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener {datePicker, y,m,d ->
            //Toast.makeText(this,"$y-$m-$d",Toast.LENGTH_SHORT).show()
            selectedCalendar.set(y,m,d)
            edtScheduleDate.setText(
                resources.getString(
                R.string.date_fromat,
                y,
                m.twoDigits(),
                d.twoDigits()
                )
            )
            displayRadioButtons()
        }

        // new dialog
        val datePickerDialog =DatePickerDialog(this,listener,year,month,dayOfMonth)
        val datePicker = datePickerDialog.datePicker

        // set limits of scheduleDate
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH,1)
            datePicker.minDate =  selectedCalendar.timeInMillis// + 1 now
            calendar.add(Calendar.DAY_OF_MONTH,30)// + 30 now
            datePicker.maxDate = calendar.timeInMillis

        //show Dialog
            datePickerDialog.show()
    }

     private fun displayRadioButtons() {

//         radioGroup.clearCheck()
//         radioGroup.removeAllViews()
//         radioGroup.checkedRadioButtonId
         selectedRadiobutton= null
         radioGroupLeft.removeAllViews()
         radioGroupRight.removeAllViews()



         val hours = arrayOf("3:00 PM","3:30PM","4:00PM","4:30PM")
         var goToLeft = true

         hours.forEach {

             val radioButton = RadioButton(this)
             radioButton.id= View.generateViewId()
             radioButton.text= it

             radioButton.setOnClickListener{view ->
                 selectedRadiobutton?.isChecked = false // desmarcamos un radio button

                 selectedRadiobutton = view as RadioButton? // Obtenemos la referencia del nuevo elemento seleccionado
                 selectedRadiobutton?.isChecked = true // mantenemos el check

             }

             if (goToLeft)
             radioGroupLeft.addView(radioButton)
             else
                 radioGroupRight.addView(radioButton)
             goToLeft = !goToLeft
         }




    }

    fun Int.twoDigits() = if (this >=10) this.toString() else "0$this"
}
