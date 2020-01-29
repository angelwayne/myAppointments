package com.nextpageti.myappointments.Model

data class  HourInterval(val start: String, val end: String){
    override fun toString(): String {
        return "$start - $end"
    }
}
