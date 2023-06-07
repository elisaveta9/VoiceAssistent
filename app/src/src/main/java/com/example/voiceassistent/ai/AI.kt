@file:Suppress("DEPRECATION")

package com.example.voiceassistent.ai

import android.content.Context
import com.example.voiceassistent.R
import com.example.voiceassistent.ai.geocoder.GeocodeLocale
import com.example.voiceassistent.ai.holidays.parser.ParsingHtmlService
import com.example.voiceassistent.ai.weather.ForecastToString
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.function.Consumer

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
        context.getString(R.string.month_january).toRegex(RegexOption.IGNORE_CASE) to 1,
        context.getString(R.string.month_february).toRegex(RegexOption.IGNORE_CASE) to 2,
        context.getString(R.string.month_march).toRegex(RegexOption.IGNORE_CASE) to 3,
        context.getString(R.string.month_april).toRegex(RegexOption.IGNORE_CASE) to 4,
        context.getString(R.string.month_may).toRegex(RegexOption.IGNORE_CASE) to 5,
        context.getString(R.string.month_june).toRegex(RegexOption.IGNORE_CASE) to 6,
        context.getString(R.string.month_july).toRegex(RegexOption.IGNORE_CASE) to 7,
        context.getString(R.string.month_august).toRegex(RegexOption.IGNORE_CASE) to 8,
        context.getString(R.string.month_september).toRegex(RegexOption.IGNORE_CASE) to 9,
        context.getString(R.string.month_october).toRegex(RegexOption.IGNORE_CASE) to 10,
        context.getString(R.string.month_november).toRegex(RegexOption.IGNORE_CASE) to 11,
        context.getString(R.string.month_december).toRegex(RegexOption.IGNORE_CASE) to 12
    )
    private val queAndAns = mapOf(
        context.getString(R.string.hello_pattern).toRegex(RegexOption.IGNORE_CASE) to
                context.getString(R.string.hello_answer),

        context.getString(R.string.how_are_you_pattern).toRegex(RegexOption.IGNORE_CASE) to
                context.getString(R.string.how_are_you_answer),

        context.getString(R.string.what_are_you_doing_pattern).toRegex(RegexOption.IGNORE_CASE) to
                context.getString(R.string.what_are_you_doing_answer),

        context.getString(R.string.date_today_pattern).toRegex(RegexOption.IGNORE_CASE) to
                context.getString(R.string.date_today_answer).replace("{CurrentDate}",
                LocalDate.now().format(DateTimeFormatter
                    .ofPattern("dd MMM yyyy")).toString()),

        context.getString(R.string.what_time_is_it_pattern).toRegex(RegexOption.IGNORE_CASE) to
                context.getString(R.string.what_time_is_it_answer).replace(
                    "{CurrentTime}",
                    LocalTime.now().format(DateTimeFormatter.ofPattern("kk:mm:ss")).
                    toString()),

        context.getString(R.string.day_of_the_week_today_pattern).toRegex(RegexOption.IGNORE_CASE) to
                context.getString(R.string.day_of_the_week_today_answer).
                replace("{CurrentDayOfWeek}", daysOfWeek[LocalDate.now().dayOfWeek]!!)
    )
    private val dateRegex = context.getString(
        R.string.date_pattern
    ).toRegex(RegexOption.IGNORE_CASE)
    private val diffBetweenDatesQuestion = context.getString(
        R.string.diff_between_dates_pattern
    ).toRegex(RegexOption.IGNORE_CASE)
    private val weatherQuestion = context.getString(R.string.current_weather_question_pattern).toRegex(RegexOption.IGNORE_CASE)
    private val weatherQuestionFirstPath = context.getString(R.string.current_weather_question_fist_part).toRegex(RegexOption.IGNORE_CASE)
    private val holidayByDateQuestion = context.getString(R.string.holiday_by_date_pattern).toRegex(RegexOption.IGNORE_CASE)
    private val yesterday = context.getString(R.string.yesterday).toRegex(RegexOption.IGNORE_CASE)
    private val today = context.getString(R.string.today).toRegex(RegexOption.IGNORE_CASE)
    private val tomorrow = context.getString(R.string.tomorrow).toRegex(RegexOption.IGNORE_CASE)
    private val incorrectDateMessage = context.getString(R.string.message_date_error)
    private val incorrectMonthMessage = context.getString(R.string.message_month_error)
    private val pastDateMessage = context.getString(R.string.message_past_date)
    private val dateIsTodayMessage = context.getString(R.string.message_date_is_today)
    private val dateIsTomorrowMessage = context.getString(R.string.message_date_is_tomorrow)
    private val diffBetweenDatesMessage = context.getString(R.string.message_difference_between_dates)
    private val defaultAnswer = context.getString(R.string.default_answer)
    private val defaultWeatherAnswer = context.getString(R.string.default_weather_answer)
    private val detailedWeatherAnswer = context.getString(R.string.detailed_weather_answer)
    private val wrongDateOfHolidayAnswer = context.getString(R.string.wrong_date_of_holiday_answer)
    private val defaultHolidayAnswer = context.getString(R.string.default_holiday_answer)
    private val correctHolidayAnswer = context.getString(R.string.correct_holidays_answer)
    private val currentLanguage = context.getString(R.string.app_language)

    fun getAnswer(question:String, callback: Consumer<String?>){
        var answer = ""
        for ((key, value) in queAndAns.entries){
            if (key.containsMatchIn(question)){
                callback.accept(value)
                answer += "$value "
            }
        }

        var matchDiffBetweenDatesQuestion = diffBetweenDatesQuestion.find(question)
        while (matchDiffBetweenDatesQuestion != null) {
            val result = getDiffBetweenDates(matchDiffBetweenDatesQuestion.value)
            callback.accept(result)
            answer += "$result "
            matchDiffBetweenDatesQuestion = matchDiffBetweenDatesQuestion.next()
        }

        var matchHolidayQuestion = holidayByDateQuestion.find(question)
        while (matchHolidayQuestion != null) {
            val dateStr = dateRegex.find(matchHolidayQuestion.value)
            val date: Date = if (dateStr == null) {
                var localDate = LocalDate.now()
                if (yesterday.matches(matchHolidayQuestion.value)) localDate = localDate.minusDays(1)
                else if (tomorrow.matches((matchHolidayQuestion.value))) localDate = localDate.plusDays(1)
                Date(localDate.year - 1900, localDate.monthValue - 1, localDate.dayOfMonth)
            } else
                getDateFromStr(dateStr.value)
            val result = ParsingHtmlService().getHoliday(date
                , wrongDateOfHolidayAnswer, defaultHolidayAnswer, correctHolidayAnswer)
            callback.accept(result)
            answer += "$result "
            matchHolidayQuestion = matchHolidayQuestion.next()
        }

        var matchWeatherQuestion = weatherQuestion.find(question)
        if (matchWeatherQuestion == null)
            if (answer == "")
                callback.accept(defaultAnswer)

        while (matchWeatherQuestion != null){
            val strFirstPath = weatherQuestionFirstPath.find(matchWeatherQuestion.value)
            var cityName = matchWeatherQuestion.value
                .drop(strFirstPath!!.value.length)
                .replace("?", "")
            GeocodeLocale().getGeocode(cityName) { geoObject ->
                val lon = geoObject?.point?.pos?.split(" ")?.get(0)?.toDoubleOrNull()
                val lat = geoObject?.point?.pos?.split(" ")?.get(1)?.toDoubleOrNull()
                if (lon != null) cityName = geoObject.name!!
                ForecastToString().getForecast(lat, lon, cityName, currentLanguage,
                    detailedWeatherAnswer, defaultWeatherAnswer) { weatherString ->
                    callback.accept(weatherString)
                }
            }
            matchWeatherQuestion = matchWeatherQuestion.next()
        }
    }

    private fun getDiffBetweenDates(question: String):String{
        try {
            val startDate = Date()
            val date = dateRegex.find(question)?.value
            val endDate = getDateFromStr(date.toString())

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
        val date = strDate.split("[-./ ]".toRegex()).toTypedArray()

        val day = date[0].toInt()
        var month = 0

        if (date[1].length == 2)
            month = date[1].toInt()
        else for ((key, value) in months.entries){
            if (key.matches(date[1]))
                month = value
        }
        if (month < 1 || month > 12)
            throw Exception(incorrectMonthMessage.replace
                ("{Date}", strDate))

        var year = Date().year

        if (date.size > 2){
            year = date[2].toInt()
            if (year < 100) year += 100
            else year -= 1900
        }

        try{ return Date(year, month - 1, day) }
        catch (e: Exception) { throw Exception(incorrectDateMessage
            .replace("{Date}", strDate)) }
    }
}