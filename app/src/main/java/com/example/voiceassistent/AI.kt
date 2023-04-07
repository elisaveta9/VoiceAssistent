package com.example.voiceassistent

import android.content.Context
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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

    fun getAnswer(question:String):String{
        question.lowercase()
        var answer = ""
        for ((key, value) in queAndAns.entries){
            if (question.matches(key))
                answer += "$value "
        }
        if (answer == "")
            answer = "Вопрос понял. Думаю..."
        return answer
    }
}