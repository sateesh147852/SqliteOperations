package com.sqloperations

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sqloperations.db.DatabaseHelper
import com.sqloperations.model.DepartmentDetails
import com.sqloperations.model.SalaryPerson

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private val TAG = "MyMainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeDatabase()
    }

    private fun initializeDatabase() {
        databaseHelper = DatabaseHelper.getInstance(this)

        //insertData()

        val values = databaseHelper.getLimitedData()
        for (person in values)
            Log.i(TAG, person.toString())

        //Log.i(TAG, databaseHelper.getColumnIndex().toString())



    }

    private fun insertData() {
        databaseHelper.insertDetailsData(DepartmentDetails(1,"Bengaluru"))
        databaseHelper.insertDetailsData(DepartmentDetails(1,"Pune"))
        databaseHelper.insertDetailsData(DepartmentDetails(2,"Hyderabad"))
        databaseHelper.insertDetailsData(DepartmentDetails(3,"Delhi"))
        databaseHelper.insertDetailsData(DepartmentDetails(3,"Delhi"))
        databaseHelper.insertDetailsData(DepartmentDetails(4,"surat"))

        databaseHelper.insertPeronData(SalaryPerson(1, "SateeshKumar", 28, "M", "Bengaluru", 65000.0))
        databaseHelper.insertPeronData(SalaryPerson(1, "Mallu", 30, "M", "Pune", 100000.0))
        databaseHelper.insertPeronData(SalaryPerson(1, "Dareppa", 32, "F", "Hyderabad", 250000.0))
        databaseHelper.insertPeronData(SalaryPerson(1, "santosh", 30, "F", "Delhi", 200000.0))
        databaseHelper.insertPeronData(SalaryPerson(1, "RajKumar", 31, "M", "Noida", 65000.0))
    }
}