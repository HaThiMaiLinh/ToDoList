package com.example.todolist.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale

const val DATE_FORMAT = "dd/MM/yyyy"
const val DATE_TIME_FORMAT = "HH:mm-dd/MM/yyyy"

object Format {
    @SuppressLint("NewApi")
    fun formatDateTimeToString(time: String, date: String): String {
        return try {
            val dateTimeString = "$time-$date"
            val inputFormatter = DateTimeFormatter.ofPattern("H:m-dd/MM/yyyy", Locale.getDefault())
            val localDateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
            DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.getDefault())
                .format(localDateTime)
        } catch (e: DateTimeParseException) {
            DateTimeFormatter
                .ofPattern(DATE_TIME_FORMAT, Locale.getDefault())
                .format(LocalDateTime.of(1970, 1, 1, 0, 0))
        }
    }

    // từ 2 String -> HH:mm-dd/MM/yyyy
    @SuppressLint("NewApi")
    fun formatDateTimeToDate(time: String, date: String): LocalDateTime {
        val inputFormatter = DateTimeFormatter.ofPattern("H:m-dd/MM/yyyy", Locale.getDefault())
        return try {
            val dateTimeString = "$time-$date"
            LocalDateTime.parse(dateTimeString, inputFormatter)
        } catch (e: DateTimeParseException) {
            LocalDateTime.of(1970, 1, 1, 0, 0)
        }
    }

    //từ LocalDateTime -> thành 1 chuỗi dd/MM/yyyy
    @SuppressLint("NewApi")
    fun formatDate(localDateTime: LocalDateTime?): String {
        return DateTimeFormatter
            .ofPattern(DATE_FORMAT, Locale.getDefault())
            .format(localDateTime)
    }

    fun sdf(date: Date?): String {
        val d = SimpleDateFormat(DATE_FORMAT)
        return d.format(date)
    }

    fun formattedTime(hourOfDay: Int, minute: Int): String {
        return String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
    }
}
