package com.nextpageti.myappointments.io.response

import com.nextpageti.myappointments.Model.User

data class loginResponse( val success: Boolean, val User: User, val jwt: String)