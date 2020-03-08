package com.nextpageti.myappointments.Model

/*

    "id": 3,
        "name": "Angel Gonzalez Zepeda",
        "email": "agonzalez@nextpageti.com",
        "cedula": "8634302",
        "address": "New Asgard",
        "phone": "7221182870",
        "role": "doctor"
 */

data class  User(
    val id: Int,
    val name: String,
    val email: String,
    val dni: String,
    val address: String,
    val phone: String,
    val role: String){
    override fun toString(): String {
        return name
    }
}