package com.example.testcomposaapplication

import android.util.Log
import java.util.*

class CalendarState(
    val year: Int,
    val month: Int,
    val selectedDay: Int?
) {
    companion object {
        val default: CalendarState
            get() {
                val calendar = Calendar.getInstance()
                val monthNum = calendar.get(Calendar.MONTH)
                return CalendarState(
                    year = calendar.get(Calendar.YEAR),
                    month = monthNum,
                    selectedDay = null
                )
            }
    }

    val weeksCount: Int by lazy {
        getCalendar().getActualMaximum(Calendar.WEEK_OF_MONTH)
    }

    val selectedDate: Date?
        get() {
            if (selectedDay == null) return null
            return getCalendar().let { cal ->
                cal.set(Calendar.DAY_OF_MONTH, selectedDay)
                cal.set(Calendar.HOUR_OF_DAY, 12)
                cal.set(Calendar.MINUTE, 0)
                cal.time
            }
        }

    init {
        Log.d(
            "CalendarState",
            "Created new calendar state of CalendarState(year=$year, month=$month, selectedDay=$selectedDay)"
        )
        if (month < 0 || month > getCalendar().getActualMaximum(Calendar.MONTH)) {
            throw IllegalArgumentException("Provided month must be bigger than 0 and less than number of months in given year")
        }
        if (selectedDay != null &&
            (selectedDay <= 0 || selectedDay > getCalendar().getActualMaximum(Calendar.DAY_OF_MONTH))
        ) {
            throw IllegalArgumentException("Selected day must be bigger than 0 and less than number of days in given month")
        }
    }

    fun getDayNumber(weekOfMonth: Int, dayOfWeek: Int): Int {
        return getCalendar().let { cal ->
            cal.set(Calendar.WEEK_OF_MONTH, weekOfMonth)
            cal.set(Calendar.DAY_OF_WEEK, dayOfWeek)
            cal.get(Calendar.DAY_OF_MONTH)
        }
    }

    fun isInMonth(weekOfMonth: Int, dayOfWeek: Int): Boolean {
        return getCalendar().let { cal ->
            cal.set(Calendar.WEEK_OF_MONTH, weekOfMonth)
            cal.set(Calendar.DAY_OF_WEEK, dayOfWeek)
            cal.get(Calendar.MONTH) == month
        }
    }

    fun selectDay(day: Int?): CalendarState {
        return CalendarState(
            year = year,
            month = month,
            selectedDay = day
        )
    }

    fun previousMonth() = incrementMonth(-1)

    fun nextMonth() = incrementMonth(1)

    private fun incrementMonth(months: Int): CalendarState {
        return getCalendar().let { cal ->
            cal.add(Calendar.MONTH, months)
            val monthNum = cal.get(Calendar.MONTH)
            CalendarState(
                year = cal.get(Calendar.YEAR),
                month = monthNum,
                selectedDay = null
            )
        }
    }

    private fun getCalendar(): Calendar = Calendar.getInstance().apply {
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
    }
}