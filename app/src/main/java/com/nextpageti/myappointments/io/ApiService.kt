package com.nextpageti.myappointments.io

import android.telecom.Call
import com.nextpageti.myappointments.Model.Doctor
import com.nextpageti.myappointments.Model.Specialty
import com.nextpageti.myappointments.io.response.loginResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.nextpageti.myappointments.Model.Schedule as Schedule1
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.POST


interface ApiService
{
    @GET("specialties")
    fun getSpecialties(): retrofit2.Call<ArrayList<Specialty>>

   @GET("specialties/{specialty}/doctors")
    fun getDoctors(@Path("specialty") spacialtyId: Int):
           retrofit2.Call<ArrayList<Doctor>>

   @GET ("schedule/hours")
   fun getHours(@Query("doctor_Id") doctorId: Int, @Query ("date") date : String):
           retrofit2.Call<Schedule1>

    @POST("login")
    fun postLogin(@Query("email") email: String, @Query ("password") password : String):
            retrofit2.Call<loginResponse>


    companion object Factory {
        private const val BASE_URL = "http://www.nextpageti.com/my-appoiments/public/api/"
        fun create () : ApiService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level= HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}