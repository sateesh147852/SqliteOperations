package com.sqloperations.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.sqloperations.model.DepartmentDetails
import com.sqloperations.model.SalaryPerson
import com.sqloperations.utils.Constants

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, Constants.DATA_BASE_EXAMPLE, null, Constants.DATA_BASE_VERSION) {

    private val TAG = "DatabaseHelper"

    override fun onCreate(database: SQLiteDatabase?) {
        val CREATE_SALARY_TABLE = "CREATE TABLE " + Constants.SALARY_TABLE_NAME + " (" +
                Constants.PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constants.PERSON_NAME + " TEXT," +
                Constants.PERSON_AGE + " INTEGER NOT NULL," +
                Constants.GENDER + " TEXT," +
                Constants.LOCATION + " TEXT UNIQUE," +
                Constants.PERSON_SALARY + " REAL)"

        val CREATE_DETAILS_TABLE = "CREATE TABLE " + Constants.DEPARTMENT_TABLE_NAME + " (" +
                Constants.DEPARTMENT_ID + " INTEGER," +
                Constants.DEPARTMENT_NAME + " TEXT NOT NULL )"

        database?.execSQL(CREATE_SALARY_TABLE)
        database?.execSQL(CREATE_DETAILS_TABLE)
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

    fun insertDetailsData(details: DepartmentDetails) {
        val database = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Constants.DEPARTMENT_ID, details.departmentId)
        contentValues.put(Constants.DEPARTMENT_NAME, details.departmentName)
        database.insert(Constants.DEPARTMENT_TABLE_NAME, null, contentValues)
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
        val database = readableDatabase
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
        cursor?.close()
        return values
    }

    fun getPersonAgeAndSalary(): List<SalaryPerson> {
        val dataBase = readableDatabase
        val values = ArrayList<SalaryPerson>()
        val salaryQuery =
            "SELECT " + Constants.PERSON_AGE + "," + Constants.PERSON_SALARY + " FROM " + Constants.SALARY_TABLE_NAME

        val cursor = dataBase.rawQuery(salaryQuery, null)
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
        cursor.close()
        return values
    }

    fun getTotalSalary(): Double {
        val dataBase = readableDatabase
        val totalSalaryQuery =
            "SELECT SUM (" + Constants.PERSON_SALARY + ") FROM " + Constants.SALARY_TABLE_NAME
        val cursor = dataBase.rawQuery(totalSalaryQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getDouble(0)
        }
        cursor.close()
        return 0.0
    }

    fun getMaximumSalary(): Double {
        val dataBase = readableDatabase
        val totalSalaryQuery =
            "SELECT MAX (" + Constants.PERSON_SALARY + ") FROM " + Constants.SALARY_TABLE_NAME
        val cursor = dataBase.rawQuery(totalSalaryQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getDouble(0)
        }
        cursor.close()
        return 0.0
    }

    fun getMinimumSalary(): Double {
        val dataBase = readableDatabase
        val totalSalaryQuery =
            "SELECT MIN (" + Constants.PERSON_SALARY + ") FROM " + Constants.SALARY_TABLE_NAME
        val cursor = dataBase.rawQuery(totalSalaryQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getDouble(0)
        }
        cursor.close()
        return 0.0
    }

    fun getAverageSalary(): Double {
        val dataBase = readableDatabase
        val totalSalaryQuery =
            "SELECT AVG (" + Constants.PERSON_SALARY + ") FROM " + Constants.SALARY_TABLE_NAME
        val cursor = dataBase.rawQuery(totalSalaryQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getDouble(0)
        }
        cursor.close()
        return 0.0
    }

    fun countDistinctSalary(): Double {
        val dataBase = readableDatabase
        val totalSalaryQuery =
            "SELECT COUNT ( DISTINCT " + Constants.PERSON_SALARY + ") as total FROM " + Constants.SALARY_TABLE_NAME
        val cursor = dataBase.rawQuery(totalSalaryQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getDouble(0)
        }
        cursor.close()
        return 0.0
    }

    fun getRowCount(): Int {
        val dataBase = readableDatabase
        val totalSalaryQuery =
            "SELECT COUNT (*) as total FROM " + Constants.SALARY_TABLE_NAME
        val cursor = dataBase.rawQuery(totalSalaryQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0)
        }
        cursor.close()
        return 0
    }

    fun getColumnIndex(): Int {
        val dataBase = readableDatabase
        val sqlQuery =
            "SELECT " + Constants.PERSON_NAME + "," + Constants.PERSON_SALARY + " FROM ${Constants.SALARY_TABLE_NAME}"
        val cursor = dataBase.rawQuery(sqlQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            //return cursor.getColumnIndex(Constants.PERSON_NAME)  return 0
            //return cursor.getColumnIndex(Constants.PERSON_ID) // return -1
            return cursor.getColumnIndex(Constants.PERSON_SALARY) // return 1
        }
        cursor.close()
        return -1
    }

    fun runWhereAndClause(): ArrayList<SalaryPerson> {

        val dataBase = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =
            "SELECT * FROM " + Constants.SALARY_TABLE_NAME + " WHERE ${Constants.GENDER} = 'M' and ${Constants.PERSON_SALARY} = 65000"
        //"SELECT * FROM " + Constants.SALARY_TABLE_NAME + " WHERE ${Constants.GENDER} = 'M'"
        val cursor: Cursor = dataBase.rawQuery(sqlQuery, null)
        cursor.let {
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
        cursor.close()
        return data
    }

    fun runWhereOrClause(): ArrayList<SalaryPerson> {
        val dataBase = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =
            "SELECT * FROM ${Constants.SALARY_TABLE_NAME} WHERE ${Constants.LOCATION} = 'Pune' OR ${Constants.LOCATION} = 'Hyderabad'"
        val cursor = dataBase.rawQuery(sqlQuery, null)

        cursor.let {
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
        cursor.close()
        return data
    }

    fun betweenClause(): ArrayList<SalaryPerson> {

        val dataBase = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =
            "SELECT * FROM " + Constants.SALARY_TABLE_NAME + " WHERE ${Constants.PERSON_SALARY} between 70000 and 200000"
        val cursor = dataBase.rawQuery(sqlQuery, null)
        cursor.let {
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
        cursor.close()
        return data
    }

    //Not working
    fun whereGreaterAndLessQuery(): ArrayList<SalaryPerson> {

        val dataBase = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =
            "SELECT * FROM " + Constants.SALARY_TABLE_NAME + " WHERE ${Constants.PERSON_SALARY} > 70000 and ${Constants.PERSON_SALARY} < 250001"
        val cursor = dataBase.rawQuery(sqlQuery, null)
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
        cursor.close()
        return data
    }


    fun performTwentyPercentHike(): ArrayList<SalaryPerson> {

        val dataBase = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val percentageQuery =
            "SELECT salary + (salary * 0.2) as newSal FROM " + Constants.SALARY_TABLE_NAME
        // "SELECT salary + (1000) as newSal FROM " + Constants.SALARY_TABLE_NAME
        val cursor = dataBase.rawQuery(percentageQuery, null)
        if (cursor != null) {
            var salary: Double
            while (cursor.moveToNext()) {
                salary = cursor.getDouble(0)
                data.add(SalaryPerson(0, "", 0, "", "", salary))
            }
        }
        cursor.close()
        return data
    }

    //Not working
    fun performTwentyPercentHike2(): ArrayList<SalaryPerson> {

        val dataBase = readableDatabase
        val data = ArrayList<SalaryPerson>()
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


        val cursor = dataBase.rawQuery(percentageQuery, null)
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
        cursor.close()
        return data

    }

    // Group by
    fun getGenderStrength(): List<SalaryPerson> {
        val database = readableDatabase
        val query =
            "SELECT gender, COUNT(*) as total FROM " + Constants.SALARY_TABLE_NAME + " GROUP BY " + Constants.GENDER

        /*val query = "SELECT gender, COUNT(*) as total FROM " + Constants.SALARY_TABLE_NAME + " GROUP BY " + Constants.GENDER +
                " ORDER BY gender DESC"*/

        val data = ArrayList<SalaryPerson>()

        val cursor = database.rawQuery(query, null)
        if (cursor != null) {
            var id: Int
            var gender: String
            while (cursor.moveToNext()) {
                gender = cursor.getString(0)
                id = cursor.getInt(1)
                data.add(SalaryPerson(id, "", 0, gender, "", 0.0))
            }
        }
        cursor.close()
        return data
    }

    // Group by and having
    fun getGenderStrengthOfMoreThanTwo(): List<SalaryPerson> {
        val database = readableDatabase
        val query =
            "SELECT gender, COUNT(*) as total FROM " + Constants.SALARY_TABLE_NAME + " GROUP BY " + Constants.GENDER +
                    " HAVING COUNT(*) > 1 " + "ORDER BY gender DESC"

        val data = ArrayList<SalaryPerson>()

        val cursor = database.rawQuery(query, null)
        if (cursor != null) {
            var id: Int
            var location: String
            while (cursor.moveToNext()) {
                location = cursor.getString(0)
                id = cursor.getInt(1)
                data.add(SalaryPerson(id, "", 0, "", location, 0.0))
            }
        }
        cursor.close()
        return data
    }

    fun runInQuery(): ArrayList<SalaryPerson> {

        val dataBase = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =

            "SELECT * FROM ${Constants.SALARY_TABLE_NAME} WHERE ${Constants.PERSON_SALARY} (65000,250000,100000)"

        val cursor = dataBase.rawQuery(sqlQuery, null)
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
        cursor.close()
        return data

    }


    fun runSubQuery(): ArrayList<SalaryPerson> {

        val dataBase = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =

            "SELECT * FROM ${Constants.SALARY_TABLE_NAME} WHERE ${Constants.PERSON_SALARY} = (SELECT MIN (${Constants.PERSON_SALARY}) FROM ${Constants.SALARY_TABLE_NAME})"

        val cursor = dataBase.rawQuery(sqlQuery, null)
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
        cursor.close()
        return data

    }


    fun runSubQueryWithWhereClause(): List<SalaryPerson> {

        val dataBase = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =

            "SELECT * FROM ${Constants.SALARY_TABLE_NAME} WHERE ${Constants.PERSON_SALARY} = (SELECT MAX(${Constants.PERSON_SALARY}) FROM ${Constants.SALARY_TABLE_NAME} WHERE ${Constants.GENDER} = 'M')"

        val cursor = dataBase.rawQuery(sqlQuery, null)
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
        cursor.close()
        return data

    }

    fun getPersonAgeNameAndGenderWithReference(): List<SalaryPerson> {
        val dataBase = readableDatabase
        val values = ArrayList<SalaryPerson>()
        val salaryQuery =

            "SELECT P.age, P.name, P.gender FROM ${Constants.SALARY_TABLE_NAME} P"

        val cursor = dataBase.rawQuery(salaryQuery, null)
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
        cursor.close()
        return values
    }

    //find average salary of gender
    fun getAverageSalaryOfTheGender(): List<SalaryPerson> {
        val database = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val sqlQuery =
            "SELECT ${Constants.GENDER} , AVG(${Constants.PERSON_SALARY}) as average_salary FROM ${Constants.SALARY_TABLE_NAME} GROUP BY ${Constants.GENDER}"
        val cursor = database.rawQuery(sqlQuery, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val salaryPerson =
                    SalaryPerson(
                        age = 0,
                        salary = cursor.getDouble(1),
                        id = 0,
                        name = cursor.getString(0),
                        gender = "",
                        location = ""
                    )
                data.add(salaryPerson)
            }
        }
        cursor.close()
        return data
    }

    fun getDataFromTwoTable(): List<SalaryPerson> {
        val database = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val query =
            "SELECT P.name, D.departmentName, D.departmentId , P.id FROM ${Constants.SALARY_TABLE_NAME} P, ${Constants.DEPARTMENT_TABLE_NAME} D WHERE P.id = D.departmentId"
        val cursor: Cursor = database.rawQuery(query, null)
        cursor.let {
            while (cursor.moveToNext()) {

                val salaryPerson =
                    SalaryPerson(
                        age = 0,
                        salary = 0.0,
                        id = cursor.getInt(3),
                        name = cursor.getString(0),
                        gender = "",
                        location = "",
                        departmentName = cursor.getString(1),
                        departmentId = cursor.getInt(2)
                    )
                data.add(salaryPerson)
            }
        }
        cursor.close()
        return data
    }

    fun getDepartmentWiseStrength(): List<SalaryPerson> {
        val database = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val query =
            "SELECT D.departmentName, count(*) FROM ${Constants.SALARY_TABLE_NAME} P, ${Constants.DEPARTMENT_TABLE_NAME} D WHERE P.location = D.departmentName GROUP BY D.departmentName"
        val cursor = database.rawQuery(query, null)
        cursor.let {
            while (cursor.moveToNext()) {
                val salaryPerson =
                    SalaryPerson(
                        age = 0,
                        salary = 0.0,
                        id = 0,
                        name = "",
                        gender = "",
                        location = "",
                        departmentName = cursor.getString(0),
                        departmentId = cursor.getInt(1)  // count
                    )
                data.add(salaryPerson)
            }
        }
        cursor.close()
        return data
    }

    fun getDepartmentWiseStrengthHasMoreThanOne(): List<SalaryPerson> {
        val database = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val query =
            "SELECT D.departmentName, count(*) FROM ${Constants.SALARY_TABLE_NAME} P, ${Constants.DEPARTMENT_TABLE_NAME} D WHERE P.location = D.departmentName GROUP BY D.departmentName HAVING COUNT(*)>1"
        val cursor = database.rawQuery(query, null)
        cursor.let {
            while (cursor.moveToNext()) {
                val salaryPerson =
                    SalaryPerson(
                        age = 0,
                        salary = 0.0,
                        id = 0,
                        name = "",
                        gender = "",
                        location = "",
                        departmentName = cursor.getString(0),
                        departmentId = cursor.getInt(1)  // count
                    )
                data.add(salaryPerson)
            }
        }
        cursor.close()
        return data
    }

    //Like operator
    fun getDataByLikeOperator(): List<SalaryPerson> {
        val database = readableDatabase
        val data = ArrayList<SalaryPerson>()
        val query =
            "SELECT * FROM ${Constants.SALARY_TABLE_NAME} WHERE ${Constants.PERSON_NAME} like '%a_'"
        val cursor = database.rawQuery(query, null)
        cursor.let {
            while (cursor.moveToNext()) {
                val salaryPerson =
                    SalaryPerson(
                        id = cursor.getInt(0),
                        name = cursor.getString(1),
                        age = cursor.getInt(2),
                        gender = cursor.getString(3),
                        location = cursor.getString(3),
                        salary = cursor.getDouble(4),
                    )
                data.add(salaryPerson)
            }
        }
        cursor.close()
        return data
    }

    fun convertNamesToUpperCase(): List<String> {
        val database = readableDatabase
        val data = ArrayList<String>()
        //val query = "SELECT UPPER(${Constants.PERSON_NAME}) FROM ${Constants.SALARY_TABLE_NAME}"
        //val query = "SELECT LOWER(${Constants.PERSON_NAME}) FROM ${Constants.SALARY_TABLE_NAME}"
        //val query = "SELECT LENGTH(${Constants.PERSON_NAME}) FROM ${Constants.SALARY_TABLE_NAME}"

        //val query = "SELECT SUBSTR(${Constants.PERSON_NAME},1) FROM ${Constants.SALARY_TABLE_NAME}" //Complete name
        //val query = "SELECT SUBSTR(${Constants.PERSON_NAME},0) FROM ${Constants.SALARY_TABLE_NAME}" //Complete name
        //val query = "SELECT SUBSTR(${Constants.PERSON_NAME},1,5) FROM ${Constants.SALARY_TABLE_NAME}" //5 chars from starting

        //val query = "SELECT SUBSTR(${Constants.PERSON_NAME},-1) FROM ${Constants.SALARY_TABLE_NAME}" //last character
        //val query = "SELECT SUBSTR(${Constants.PERSON_NAME},-5) FROM ${Constants.SALARY_TABLE_NAME}" //last 5 character "kumar"
        //val query = "SELECT SUBSTR(${Constants.PERSON_NAME},-5,3) FROM ${Constants.SALARY_TABLE_NAME}" //from last 5th position to right side character "kum"
        //val query = "SELECT SUBSTR(${Constants.PERSON_NAME},-5,-3) FROM ${Constants.SALARY_TABLE_NAME}" //(3 chars) from 6th position to the left side

        //val query = "SELECT UPPER(SUBSTR(${Constants.PERSON_NAME},1,5)) FROM ${Constants.SALARY_TABLE_NAME}"
        //val query = "SELECT LOWER(SUBSTR(${Constants.PERSON_NAME},1,5)) FROM ${Constants.SALARY_TABLE_NAME}"
        val query =
            "SELECT LENGTH(SUBSTR(${Constants.PERSON_NAME},1,20)) FROM ${Constants.SALARY_TABLE_NAME}"

        val cursor: Cursor = database.rawQuery(query, null)
        cursor.let {
            while (cursor.moveToNext()) {
                data.add(cursor.getString(0))
            }
        }
        cursor.close()
        return data
    }

    fun getLimitedData(): List<Int> {
        val database = readableDatabase
        val data = ArrayList<Int>()
        val query = "SELECT * FROM ${Constants.SALARY_TABLE_NAME} LIMIT 3"
        val cursor = database.rawQuery(query, null)
        cursor.let {
            while (cursor.moveToNext()) {
                data.add(cursor.getInt(0))
            }
        }
        cursor.close()
        return data
    }


}