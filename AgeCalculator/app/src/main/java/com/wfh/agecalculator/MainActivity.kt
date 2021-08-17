package com.wfh.agecalculator

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat

import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnDatePicker.setOnClickListener{
            val c: Calendar = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val dom = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog: DatePickerDialog = DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val sDate = "$dayOfMonth/${month+1}/$year"
                selectedDate.text = sDate;
                val sdf = SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH)
                val theDate = sdf.parse(sDate)
                val sDateInMinutes = theDate!!.time/60000
                val cDate = sdf.parse(sdf.format(System.currentTimeMillis()))
                val cDateInMinutes = cDate!!.time/60000
                val diffInMinutes = cDateInMinutes - sDateInMinutes
                ageOutputInMinutes.text = diffInMinutes.toString()
            },year,month,dom)
            datePickerDialog.datePicker.maxDate = Date().time - 86400000
            datePickerDialog.show()

        }
    }

}