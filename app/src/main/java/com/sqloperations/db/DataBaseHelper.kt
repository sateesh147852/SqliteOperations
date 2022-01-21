package com.sqloperations.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.sqloperations.model.SalaryPerson
import com.sqloperations.utils.Constants

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, Constants.DATA_BASE_EXAMPLE, null, Constants.DATA_BASE_VERSION) {

    private val TAG = "DatabaseHelper"

    override fun onCreate(database: SQLiteDatabase?) {
        val CREATE_EXAMPLE_TABLE = "CREATE TABLE " + Constants.SALARY_TABLE_NAME + " (" +
                Constants.PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constants.PERSON_NAME + " TEXT," +
                Constants.PERSON_AGE + " INTEGER NOT NULL," +
                Constants.GENDER + " TEXT," +
                Constants.LOCATION + " TEXT UNIQUE," +
                Constants.PERSON_SALARY + " REAL)"

        database?.execSQL(CREATE_EXAMPLE_TABLE)
    }

    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    companion object {

        private var databaseHelper: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            synchronized(this) {
                if (databaseHelper == null)
                    databaseHelper = DatabaseHelper(context)
            }
            return databaseHelper!!
        }
    }

    fun insertPeronData(salaryPerson: SalaryPerson) {
        val database = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Constants.PERSON_NAME, salaryPerson.name)
        contentValues.put(Constants.PERSON_AGE, salaryPerson.age)
        contentValues.put(Constants.GENDER, salaryPerson.gender)
        contentValues.put(Constants.LOCATION, salaryPerson.location)
        contentValues.put(Constants.PERSON_SALARY, salaryPerson.salary)
        database.insert(Constants.SALARY_TABLE_NAME, null, contentValues)
    }

    fun getPersonData(): List<SalaryPerson> {
        val database = writableDatabase
        val values = ArrayList<SalaryPerson>()
        val getQuery =
            "SELECT * FROM ${Constants.SALARY_TABLE_NAME}"
        var cursor: Cursor? = null
        try {
            cursor = database.rawQuery(getQuery, null)
        } catch (exception: SQLiteException) {
            Log.i(TAG, "getPersonData: ")
        }

        if (cursor != null && cursor.moveToFirst()) {
            var id: Int
            var name: String
            var age: Int
            var gender: String
            var location: String
            var salary: Double
            do {
                id = cursor.getInt(0)
                name = cursor.getString(1)
                age = cursor.getInt(2)
                gender = cursor.getString(3)
                location = cursor.getString(4)
                salary = cursor.getDouble(5)
                values.add(SalaryPerson(id, name, age, gender, location, salary))
            } while (cursor.moveToNext())
        }
        return values
    }

    fun getPersonAgeAndSalary(): List<SalaryPerson> {
        val dataBase = writableDatabase
        val values = ArrayList<SalaryPerson>()
        val salaryQuery =
            "SELECT " + Constants.PERSON_AGE + "," + Constants.PERSON_SALARY + " " + "FROM " + Constants.SALARY_TABLE_NAME
        var cursor: Cursor? = null

        cursor = dataBase.rawQuery(salaryQuery, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {

                val salaryPerson =
                    SalaryPerson(
                        age = cursor.getInt(0),
                        salary = cursor.getDouble(1),
                        id = 0,
                        name = "",
                        gender = "",
                        location = ""
                    )
                values.add(salaryPerson)

            }
        }
        return values
    }

    fun getTotalSalary(): Double {
        val dataBase = writableDatabase
        val totalSalaryQuery =
            "SELECT SUM " + "(" + Constants.PERSON_SALARY + ")" + " FROM " + Constants.SALARY_TABLE_NAME
        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(totalSalaryQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getDouble(0)
        }
        return 0.0
    }

    fun getMaximumSalary(): Double {
        val dataBase = writableDatabase
        val totalSalaryQuery =
            "SELECT MAX " + "(" + Constants.PERSON_SALARY + ")" + " FROM " + Constants.SALARY_TABLE_NAME
        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(totalSalaryQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getDouble(0)
        }
        return 0.0
    }

    fun getMinimumSalary(): Double {
        val dataBase = writableDatabase
        val totalSalaryQuery =
            "SELECT MIN " + "(" + Constants.PERSON_SALARY + ")" + " FROM " + Constants.SALARY_TABLE_NAME
        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(totalSalaryQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getDouble(0)
        }
        return 0.0
    }

    fun getAverageSalary(): Double {
        val dataBase = writableDatabase
        val totalSalaryQuery =
            "SELECT AVG " + "(" + Constants.PERSON_SALARY + ")" + " FROM " + Constants.SALARY_TABLE_NAME
        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(totalSalaryQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getDouble(0)
        }
        return 0.0
    }

    fun countSalary(): Double {
        val dataBase = writableDatabase
        val totalSalaryQuery =
            "SELECT COUNT " + "( DISTINCT " + Constants.PERSON_SALARY + ")" + " as total FROM " + Constants.SALARY_TABLE_NAME
        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(totalSalaryQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getDouble(0)
        }
        return 0.0
    }

    fun getRowCount(): Int {
        val dataBase = writableDatabase
        val totalSalaryQuery =
            "SELECT COUNT " + "(*)" + " as total FROM " + Constants.SALARY_TABLE_NAME
        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(totalSalaryQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0)
        }
        return 0
    }

    fun getColumnIndex(): Int {
        val dataBase = writableDatabase
        val sqlQuery =
            "SELECT " + Constants.PERSON_NAME + "," + Constants.PERSON_SALARY + " FROM ${Constants.SALARY_TABLE_NAME}"
        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(sqlQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            //return cursor.getColumnIndex(Constants.PERSON_NAME)  return 0
            return cursor.getColumnIndex(Constants.PERSON_SALARY) // return 1
        }
        return -1;
    }

    fun runWhereAndClause(): ArrayList<SalaryPerson> {

        val dataBase = writableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =
            "SELECT * FROM " + Constants.SALARY_TABLE_NAME + " WHERE ${Constants.GENDER} = 'M' and ${Constants.PERSON_SALARY} = 65000"
        //"SELECT * FROM " + Constants.SALARY_TABLE_NAME + " WHERE ${Constants.GENDER} = 'M'"
        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(sqlQuery, null)
        if (cursor != null) {
            var id: Int
            var name: String
            var age: Int
            var gender: String
            var location: String
            var salary: Double
            while (cursor.moveToNext()) {
                id = cursor.getInt(0)
                name = cursor.getString(1)
                age = cursor.getInt(2)
                gender = cursor.getString(3)
                location = cursor.getString(4)
                salary = cursor.getDouble(5)
                data.add(SalaryPerson(id, name, age, gender, location, salary))
            }
        }
        return data
    }

    fun runWhereOrClause(): ArrayList<SalaryPerson> {
        val dataBase = writableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =
            "SELECT * FROM ${Constants.SALARY_TABLE_NAME} WHERE ${Constants.LOCATION} = 'Pune' OR ${Constants.LOCATION} = 'Hyderabad'"
        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(sqlQuery, null)
        if (cursor != null) {
            var id: Int
            var name: String
            var age: Int
            var gender: String
            var location: String
            var salary: Double
            while (cursor.moveToNext()) {
                id = cursor.getInt(0)
                name = cursor.getString(1)
                age = cursor.getInt(2)
                gender = cursor.getString(3)
                location = cursor.getString(4)
                salary = cursor.getDouble(5)
                data.add(SalaryPerson(id, name, age, gender, location, salary))
            }
        }
        return data
    }

    fun betweenClause(): ArrayList<SalaryPerson> {

        val dataBase = writableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =
            "SELECT * FROM " + Constants.SALARY_TABLE_NAME + " WHERE ${Constants.PERSON_SALARY} between 70000 and 200000"
        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(sqlQuery, null)
        if (cursor != null) {
            var id: Int
            var name: String
            var age: Int
            var gender: String
            var location: String
            var salary: Double
            while (cursor.moveToNext()) {
                id = cursor.getInt(0)
                name = cursor.getString(1)
                age = cursor.getInt(2)
                gender = cursor.getString(3)
                location = cursor.getString(4)
                salary = cursor.getDouble(5)
                data.add(SalaryPerson(id, name, age, gender, location, salary))
            }
        }
        return data
    }

    fun whereGreaterAndLessQuery(): ArrayList<SalaryPerson> {

        val dataBase = writableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =
            "SELECT * FROM " + Constants.SALARY_TABLE_NAME + " WHERE ${Constants.PERSON_SALARY} > 70000 and ${Constants.PERSON_SALARY} < 250001"
        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(sqlQuery, null)
        if (cursor != null) {
            var id: Int
            var name: String
            var age: Int
            var gender: String
            var location: String
            var salary: Double
            while (cursor.moveToNext()) {
                id = cursor.getInt(0)
                name = cursor.getString(1)
                age = cursor.getInt(2)
                gender = cursor.getString(3)
                location = cursor.getString(4)
                salary = cursor.getDouble(5)
                data.add(SalaryPerson(id, name, age, gender, location, salary))
            }
        }
        return data
    }


    fun performTwentyPercentHike(): ArrayList<SalaryPerson> {

        val dataBase = writableDatabase
        val data = ArrayList<SalaryPerson>()
        var cursor: Cursor? = null
        val percentageQuery =
            "SELECT salary + (salary * 0.2) as newSal FROM " + Constants.SALARY_TABLE_NAME
        // "SELECT salary + (1000) as newSal FROM " + Constants.SALARY_TABLE_NAME
        cursor = dataBase.rawQuery(percentageQuery, null)
        if (cursor != null) {
            var salary: Double
            while (cursor.moveToNext()) {
                salary = cursor.getDouble(0)
                data.add(SalaryPerson(0, "", 0, "", "", salary))
            }
        }
        return data
    }

    //Not working
    fun performTwentyPercentHike2(): ArrayList<SalaryPerson> {

        val dataBase = writableDatabase
        val data = ArrayList<SalaryPerson>()
        var cursor: Cursor? = null
        val percentageQuery =

            "SELECT id,name,age,gender,location" +
                    "case id" +
                    "when 1 then salary + salary * 0.2 " +
                    "when 2 then salary + salary * 0.2 " +
                    "when 3 then salary + salary * 0.2 "
        "when 4 then salary + salary * 0.2 " +
                "when 5 then salary + salary * 0.2 " +
                "else salary + salary * 0.5 " +
                "end as newSal FROM " + Constants.SALARY_TABLE_NAME


        cursor = dataBase.rawQuery(percentageQuery, null)
        if (cursor != null) {
            var id: Int
            var name: String
            var age: Int
            var gender: String
            var location: String
            var salary: Double
            while (cursor.moveToNext()) {
                id = cursor.getInt(0)
                name = cursor.getString(1)
                age = cursor.getInt(2)
                gender = cursor.getString(3)
                location = cursor.getString(4)
                salary = cursor.getDouble(5)
                data.add(SalaryPerson(id, name, age, gender, location, salary))
            }
        }
        return data

    }

    // Group by
    fun getGenderStrength(): List<SalaryPerson> {
        val database = writableDatabase
        val query =
            "SELECT gender, COUNT(*) as total FROM " + Constants.SALARY_TABLE_NAME + " GROUP BY " + Constants.GENDER

        /*val query = "SELECT gender, COUNT(*) as total FROM " + Constants.SALARY_TABLE_NAME + " GROUP BY " + Constants.GENDER +
                " ORDER BY gender DESC"*/

        val data = ArrayList<SalaryPerson>()

        var cursor: Cursor? = null
        cursor = database.rawQuery(query, null)
        if (cursor != null) {
            var id: Int
            var gender: String
            while (cursor.moveToNext()) {
                gender = cursor.getString(0)
                id = cursor.getInt(1)
                data.add(SalaryPerson(id, "", 0, gender, "", 0.0))
            }
        }
        return data
    }

    // Group by and having
    fun getGenderStrengthOfMoreThanTwo(): List<SalaryPerson> {
        val database = writableDatabase
        val query =
            "SELECT gender, COUNT(*) as total FROM " + Constants.SALARY_TABLE_NAME + " GROUP BY " + Constants.GENDER +
                    " HAVING COUNT(*) > 1 " + "ORDER BY gender DESC"

        val data = ArrayList<SalaryPerson>()

        var cursor: Cursor? = null
        cursor = database.rawQuery(query, null)
        if (cursor != null) {
            var id: Int
            var location: String
            while (cursor.moveToNext()) {
                location = cursor.getString(0)
                id = cursor.getInt(1)
                data.add(SalaryPerson(id, "", 0, "", location, 0.0))
            }
        }
        return data
    }

    fun runInQuery(): ArrayList<SalaryPerson> {

        val dataBase = writableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =

            "SELECT * FROM ${Constants.SALARY_TABLE_NAME} WHERE ${Constants.PERSON_SALARY} IN (65000,250000,100000)"

        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(sqlQuery, null)
        if (cursor != null) {
            var id: Int
            var name: String
            var age: Int
            var gender: String
            var location: String
            var salary: Double
            while (cursor.moveToNext()) {
                id = cursor.getInt(0)
                name = cursor.getString(1)
                age = cursor.getInt(2)
                gender = cursor.getString(3)
                location = cursor.getString(4)
                salary = cursor.getDouble(5)
                data.add(SalaryPerson(id, name, age, gender, location, salary))
            }
        }
        return data

    }


    fun runSubQuery(): ArrayList<SalaryPerson> {

        val dataBase = writableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =

            "SELECT * FROM ${Constants.SALARY_TABLE_NAME} WHERE ${Constants.PERSON_SALARY} = (SELECT MIN (${Constants.PERSON_SALARY}) FROM ${Constants.SALARY_TABLE_NAME})"

        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(sqlQuery, null)
        if (cursor != null) {
            var id: Int
            var name: String
            var age: Int
            var gender: String
            var location: String
            var salary: Double
            while (cursor.moveToNext()) {
                id = cursor.getInt(0)
                name = cursor.getString(1)
                age = cursor.getInt(2)
                gender = cursor.getString(3)
                location = cursor.getString(4)
                salary = cursor.getDouble(5)
                data.add(SalaryPerson(id, name, age, gender, location, salary))
            }
        }
        return data

    }


    fun runSubQueryWithWhereClause() : List<SalaryPerson> {

        val dataBase = writableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =

            "SELECT * FROM ${Constants.SALARY_TABLE_NAME} WHERE ${Constants.PERSON_SALARY} = (SELECT MAX(${Constants.PERSON_SALARY}) FROM ${Constants.SALARY_TABLE_NAME} WHERE ${Constants.GENDER} = 'M')"

        var cursor: Cursor? = null
        cursor = dataBase.rawQuery(sqlQuery, null)
        if (cursor != null) {
            var id: Int
            var name: String
            var age: Int
            var gender: String
            var location: String
            var salary: Double
            while (cursor.moveToNext()) {
                id = cursor.getInt(0)
                name = cursor.getString(1)
                age = cursor.getInt(2)
                gender = cursor.getString(3)
                location = cursor.getString(4)
                salary = cursor.getDouble(5)
                data.add(SalaryPerson(id, name, age, gender, location, salary))
            }
        }
        return data

    }

    fun getPersonAgeAndSalaryWithReference(): List<SalaryPerson> {
        val dataBase = writableDatabase
        val values = ArrayList<SalaryPerson>()
        val salaryQuery =

            "SELECT P.age, P.name, P.gender FROM ${Constants.SALARY_TABLE_NAME} P"

        var cursor: Cursor? = null

        cursor = dataBase.rawQuery(salaryQuery, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {

                val salaryPerson =
                    SalaryPerson(
                        age = cursor.getInt(0),
                        salary = 0.0,
                        id = 0,
                        name = cursor.getString(1),
                        gender = cursor.getString(2),
                        location = ""
                    )
                values.add(salaryPerson)

            }
        }
        return values
    }




}