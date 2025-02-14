package com.nextpageti.myappointments.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.nextpageti.myappointments.Model.Doctor
import com.nextpageti.myappointments.Model.Schedule
import com.nextpageti.myappointments.Model.Specialty
import com.nextpageti.myappointments.R
import com.nextpageti.myappointments.io.ApiService
import kotlinx.android.synthetic.main.activity_create_appointment.*
import kotlinx.android.synthetic.main.card_view_step_one.*
import kotlinx.android.synthetic.main.card_view_step_tree.*
import kotlinx.android.synthetic.main.card_view_step_two.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class CreateAppointmentActivity : AppCompatActivity() {

    private val apiService : ApiService by lazy {
        ApiService.create()
    }
    private val selectedCalendar = Calendar.getInstance()
    private var selectedTypeRadiobutton : RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_appointment)

        btnNext.setOnClickListener{
            if(etDescription.text.toString().length < 5){
                etDescription.error = getString(R.string.validate_appointment_description)
            } else {
                // continue to step 2
                cvStep1.visibility= View.GONE
                cvStep2.visibility=View.VISIBLE
                }
            }
        btnNext2.setOnClickListener{
            when {
                edtScheduleDate.text.toString().isEmpty() -> edtScheduleDate.error = getString(R.string.validate_appointment_date)

                selectedTypeRadiobutton == null
                -> Snackbar
                    .make(createAppointmentLienarLayout,
                        R.string.validate_appointment_time, Snackbar.LENGTH_SHORT).show()
                else -> {
                    // continue to step 3
                    showAppointmentDataToconfirm()
                    cvStep2.visibility= View.GONE
                    cvStep3.visibility=View.VISIBLE
                }
            }
        }


        btnConfirmAppointment.setOnClickListener{

            Toast.makeText(this,"Cita  Registrada Correctamente",Toast.LENGTH_SHORT).show()
            finish()
        }

        loadSpecialties()
        listenSpecialtyChanges()
        listenDoctorAndDateChanges()
    }

    private fun loadSpecialties ()
    {
        val call = apiService.getSpecialties()
        call.enqueue(object : retrofit2.Callback<ArrayList<Specialty>> {
            override fun onFailure(call: Call<ArrayList<Specialty>>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity,"Ocurrio un problema al cargar las especialidades",Toast.LENGTH_SHORT).show()
                Toast.makeText(this@CreateAppointmentActivity,"Esto paso "+t,Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(call: Call<ArrayList<Specialty>>, response: Response<ArrayList<Specialty>>) {
                if(response.isSuccessful)
                {
                    val specialties = response.body()
                    spinnerSpecialty.adapter = ArrayAdapter<Specialty>(this@CreateAppointmentActivity,android.R.layout.simple_list_item_1,specialties)
                }
            }
        })
    }

    private fun listenSpecialtyChanges() {
        spinnerSpecialty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val specialty  = adapter?.getItemAtPosition(position) as Specialty
                loadDocors(specialty.id)
            }

        }
    }

    private fun loadDocors(specialtyId : Int){
        var call = apiService.getDoctors(specialtyId)
        call.enqueue(object : Callback<ArrayList<Doctor>>{
            override fun onFailure(call: Call<ArrayList<Doctor>>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity,R.string.error_loading_doctors,Toast.LENGTH_SHORT).show()
                Toast.makeText(this@CreateAppointmentActivity,"Esto paso "+t,Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<ArrayList<Doctor>>, response: Response<ArrayList<Doctor>>) {
                if(response.isSuccessful)
                {
                    val doctors = response.body()
                    sipinnerDoctor.adapter = ArrayAdapter<Doctor>(this@CreateAppointmentActivity,android.R.layout.simple_list_item_1,doctors)
                }
            }

        })
    }

    private fun listenDoctorAndDateChanges () {
        //doctor changes
        sipinnerDoctor.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               val doctor = parent?.getItemAtPosition(position) as Doctor
                loadHours(doctor.id, edtScheduleDate.text.toString())
            }

        }

        // schedule date changes
        edtScheduleDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val doctor = sipinnerDoctor.selectedItem as Doctor
                loadHours(doctor.id,edtScheduleDate.text.toString())
            }

        })
    }

    private fun loadHours (doctorId : Int, scheduleDate: String) {

        if (scheduleDate.isEmpty())
        {
            return
        }
        val call = apiService.getHours(doctorId,scheduleDate)
        call.enqueue(object : Callback<Schedule> {
            override fun onFailure(call: Call<Schedule>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity,"No se han podido cargar las horas",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                if(response.isSuccessful){
                    val schedule = response.body()
                    //Toast.makeText(this@CreateAppointmentActivity,"morning: ${schedule?.morning?.size}, afternoon: ${schedule?.afternoon?.size}",Toast.LENGTH_SHORT).show()
                    tvSelectDoctorAndDate.visibility = View.GONE
                    schedule?.let{
                        val intervals = it.morning + it.afternoon
                        val hours = ArrayList<String>()
                        intervals.forEach{interval ->
                            hours.add(interval.start)
                        }
                        displayIntervalRadios(hours)
                    }

                }
            }

        })
        //Toast.makeText(this,"Id del medico $doctorId y fecha $scheduleDate",Toast.LENGTH_SHORT).show()
    }

    private fun showAppointmentDataToconfirm () {
        tvConfirmDesc.text= etDescription.text.toString()
        tvConfirmSpecialty.text = spinnerSpecialty.selectedItem.toString()

        val selectedRadioBtnId = radioGrouptype.checkedRadioButtonId // access to id selected of radio button
        val selectedType = radioGrouptype.findViewById<RadioButton>(selectedRadioBtnId) // find view element

        tvConfirmType.text = selectedType.text.toString()

        tvConfirmDcotorName.text = sipinnerDoctor.selectedItem.toString()
        tvConfirmDate.text = edtScheduleDate.text.toString()
        tvConfirmTime.text =  selectedTypeRadiobutton?.text.toString()
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
                    (m+1).twoDigits(),
                d.twoDigits()
                )
            )
            edtScheduleDate.error = null

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

     private fun displayIntervalRadios(hours : ArrayList<String>) {

         selectedTypeRadiobutton= null
         radioGroupLeft.removeAllViews()
         radioGroupRight.removeAllViews()

         if(hours.isEmpty()){
            tvNotAvilableHours.visibility = View.VISIBLE
             return
         }

         tvNotAvilableHours.visibility = View.GONE
         //val hours = arrayOf("3:00 PM","3:30PM","4:00PM","4:30PM")
         var goToLeft = true

         hours.forEach {

             val radioButton = RadioButton(this)
             radioButton.id= View.generateViewId()
             radioButton.text= it

             radioButton.setOnClickListener{view ->
                 selectedTypeRadiobutton?.isChecked = false // desmarcamos un radio button

                 selectedTypeRadiobutton = view as RadioButton? // Obtenemos la referencia del nuevo elemento seleccionado
                 selectedTypeRadiobutton?.isChecked = true // mantenemos el check en el nueco RB check

             }

             if (goToLeft)
             radioGroupLeft.addView(radioButton)
             else
                 radioGroupRight.addView(radioButton)
             goToLeft = !goToLeft
         }




    }

    fun Int.twoDigits() = if (this >=10) this.toString() else "0$this"

    override fun onBackPressed() {

        when {
            cvStep3.visibility == View.VISIBLE -> {
                cvStep3.visibility = View.GONE
                cvStep2.visibility = View.VISIBLE
            }
            cvStep2.visibility == View.VISIBLE -> {
                cvStep2.visibility = View.GONE
                cvStep1.visibility = View.VISIBLE
            }
            cvStep1.visibility == View.VISIBLE -> {

                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.dialog_create_appointment_exit_title))
                builder.setMessage(getString(R.string.dialog_create_appointment_exit_message))
                builder.setPositiveButton(getString(R.string.dialog_create_appointment_exit_positive_btn)) { _, _ ->
                    finish()
                }
                builder.setNegativeButton(getString(R.string.dialog_create_appointment_exit_negative_btn)) { dialog, _ ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }
        }
    }
}
