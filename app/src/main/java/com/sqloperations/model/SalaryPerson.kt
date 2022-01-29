package com.sqloperations.model

data class SalaryPerson(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val location: String,
    val salary: Double,
    val departmentName: String = "",
    val departmentId: Int = 0
)