package com.nextpageti.myappointments.io

import android.telecom.Call
import com.nextpageti.myappointments.Model.Doctor
import com.nextpageti.myappointments.Model.Specialty
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService
{
    @GET("specialties")
   abstract fun getSpecialties(): retrofit2.Call<ArrayList<Specialty>>

    @GET("specialties/{specialty}/doctors")
    abstract fun getDoctors(@Path("specialty") spacialtyId: Int): retrofit2.Call<ArrayList<Doctor>>

    companion object Factory {
        private const val BASE_URL = "http://www.nextpageti.com/my-appoiments/public/api/"
        fun create () : ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}