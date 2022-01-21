package com.sqloperations

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sqloperations.db.DatabaseHelper
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
        /*databaseHelper.insertPeronData(SalaryPerson(1, "Sateesh", 28, "M", "Bengaluru", 65000.0))
        databaseHelper.insertPeronData(SalaryPerson(1, "Mallu", 30, "M", "Pune", 100000.0))
        databaseHelper.insertPeronData(SalaryPerson(1, "Dareppa", 32, "F", "Hyderabad", 250000.0))
        databaseHelper.insertPeronData(SalaryPerson(1, "santu", 30, "F", "Delhi", 200000.0))
        databaseHelper.insertPeronData(SalaryPerson(1, "Raju", 31, "M", "Noida", 65000.0))*/

        /*
        Not working
        val values = databaseHelper.whereGreaterAndLessQuery()*/

        /*val values = databaseHelper.getPersonAgeAndSalary()
        val values = databaseHelper.getPersonData()
        val values = databaseHelper.runWhereAndClause()
        val values = databaseHelper.betweenClause()
        val values = databaseHelper.runWhereOrClause()
        val values = databaseHelper.getPersonAgeAndSalaryWithReference()


        val values = databaseHelper.whereGreaterAndLessQuery()
        val values = databaseHelper.performTwentyPercentHike()

        val values = databaseHelper.getGenderStrength()
        val values = databaseHelper.getGenderStrengthOfMoreThanTwo()


        val values = databaseHelper.runInQuery()
        val values = databaseHelper.runSubQuery()
        val values = databaseHelper.runSubQueryWithWhereClause()

        */


        /*Log.i(TAG, databaseHelper.getMaximumSalary().toString())
        Log.i(TAG, databaseHelper.getMinimumSalary().toString())
        Log.i(TAG, databaseHelper.getTotalSalary().toString())
        Log.i(TAG, databaseHelper.getAverageSalary().toString())
        Log.i(TAG, databaseHelper.countSalary().toString())
        Log.i(TAG, databaseHelper.getRowCount().toString())*/

        //Log.i(TAG, databaseHelper.getColumnIndex().toString())

        val values = databaseHelper.getPersonAgeAndSalaryWithReference()
        for (person in values)
            Log.i(TAG, person.toString())


    }
}