package com.example.voiceassistent

import android.content.Context
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class AI(context: Context) {
    private val daysOfWeek = mapOf(
        DayOfWeek.MONDAY to context.getString(R.string.day_of_week_monday),
        DayOfWeek.TUESDAY to context.getString(R.string.day_of_week_tuesday),
        DayOfWeek.WEDNESDAY to context.getString(R.string.day_of_week_wednesday),
        DayOfWeek.THURSDAY to context.getString(R.string.day_of_week_thursday),
        DayOfWeek.FRIDAY to context.getString(R.string.day_of_week_friday),
        DayOfWeek.SATURDAY to context.getString(R.string.day_of_week_saturday),
        DayOfWeek.SUNDAY to context.getString(R.string.day_of_week_sunday)
    )
    private val months = mapOf(
        context.getString(R.string.month_january).toRegex() to 1,
        context.getString(R.string.month_february).toRegex() to 2,
        context.getString(R.string.month_march).toRegex() to 3,
        context.getString(R.string.month_april).toRegex() to 4,
        context.getString(R.string.month_may).toRegex() to 5,
        context.getString(R.string.month_june).toRegex() to 6,
        context.getString(R.string.month_july).toRegex() to 7,
        context.getString(R.string.month_august).toRegex() to 8,
        context.getString(R.string.month_september).toRegex() to 9,
        context.getString(R.string.month_october).toRegex() to 10,
        context.getString(R.string.month_november).toRegex() to 11,
        context.getString(R.string.month_december).toRegex() to 12
    )
    private val queAndAns = mapOf(
        context.getString(R.string.hello_pattern).toRegex() to
                context.getString(R.string.hello_answer),

        context.getString(R.string.how_are_you_pattern).toRegex() to
                context.getString(R.string.how_are_you_answer),

        context.getString(R.string.what_are_you_doing_pattern).toRegex() to
                context.getString(R.string.what_are_you_doing_answer),

        context.getString(R.string.date_today_pattern).toRegex() to
                context.getString(R.string.date_today_answer).replace("{CurrentDate}",
                LocalDate.now().format(DateTimeFormatter
                    .ofPattern("dd MMM yyyy")).toString()),

        context.getString(R.string.what_time_is_it_pattern).toRegex() to
                context.getString(R.string.what_time_is_it_answer).replace(
                    "{CurrentTime}",
                    LocalTime.now().format(DateTimeFormatter.ofPattern("kk:mm:ss")).
                    toString()),

        context.getString(R.string.day_of_the_week_today_pattern).toRegex() to
                context.getString(R.string.day_of_the_week_today_answer).
                replace("{CurrentDayOfWeek}", daysOfWeek.get(LocalDate.now().dayOfWeek)!!)
    )
    private val dateRegex = context.getString(
        R.string.date_pattern
    ).toRegex()
    private val diffBetweenDatesReg = context.getString(
        R.string.diff_between_dates_pattern
    ).toRegex()
    private val incorrectDateMessage = context.getString(R.string.message_date_error)
    private val incorrectMonthMessage = context.getString(R.string.message_month_error)
    private val pastDateMessage = context.getString(R.string.message_past_date)
    private val dateIsTodayMessage = context.getString(R.string.message_date_is_today)
    private val dateIsTomorrowMessage = context.getString(R.string.message_date_is_tomorrow)
    private val diffBetweenDatesMessage = context.getString(R.string.message_difference_between_dates)
    private val defaultAnswer = context.getString(R.string.default_answer)

    fun getAnswer(question:String):String{
        var text = question.lowercase()
        var answer = ""
        for ((key, value) in queAndAns.entries){
            if (key.containsMatchIn(text))
                answer += "$value "
        }
        var matchResult = diffBetweenDatesReg.find(text)
        while (matchResult != null){
            answer += getDiffBetweenDates(matchResult.value) + " "
            matchResult = matchResult.next()
        }

        if (answer == "")
            answer = defaultAnswer
        return answer
    }

    private fun getDiffBetweenDates(question: String):String{
        try {
            var startDate = Date()
            var date = dateRegex.find(question)?.value
            var endDate = getDateFromStr(date.toString())

            val daysBetween =
                ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant()).toInt()
            if (daysBetween < 0)
                return pastDateMessage.replace("{Date}", date.toString())
            else if (daysBetween == 0) {
                return if (startDate == endDate)
                    dateIsTodayMessage.replace("{Date}", date.toString())
                else
                    dateIsTomorrowMessage.replace("{Date}", date.toString())
            }
            return diffBetweenDatesMessage
                .replace("{Date}", date.toString())
                .replace("{DaysBetween}", "$daysBetween")
        }
        catch (e: Exception) {
            return "${e.message}"
        }
    }

    private fun getDateFromStr(strDate: String):Date{
        var date = strDate.split("[-\\./ ]".toRegex()).toTypedArray()

        var day = date?.get(0)!!.toInt()
        var month = 0

        if (date?.get(1)?.length == 2)
            month = date?.get(1).toInt()
        else for ((key, value) in months.entries){
            if (key.matches(date?.get(1).toString()))
                month = value
        }
        if (month < 1 || month > 12)
            throw Exception(incorrectMonthMessage.replace
                ("{Date}", strDate))

        var year = Date().year

        if (date?.size!! > 2){
            year = date?.get(2).toInt()
            if (year < 100) year += 100
            else year -= 1900
        }

        try{ return Date(year, month - 1, day) }
        catch (e: Exception) { throw Exception(incorrectDateMessage
            .replace("{Date}", strDate)) }
    }
}