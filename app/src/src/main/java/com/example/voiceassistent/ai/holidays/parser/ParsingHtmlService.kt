@file:Suppress("DEPRECATION")

package com.example.voiceassistent.ai.holidays.parser

import android.annotation.SuppressLint
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class ParsingHtmlService() {
    private val url = "https://mirkosmosa.ru/holiday/"
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy")

    fun getHoliday(date: Date?, wrongDate: String, defaultAnswer: String, correctAnswer: String): String {
        val year = date!!.year + 1900

        if (year < 1903 || year > 2030)
            return wrongDate
                .replace("{Date}", dateFormat.format(date))
                .replace("{Start}", "1903")
                .replace("{End}", "2030")
        val localUrl = url + "$year"
        val document: Document = Jsoup.connect(localUrl)
            .userAgent("Google")
            .timeout(5000)
            .get()
        val body: Element = document.body()
        val hols: Elements = body.select("div.month_cel")

        val localDate = LocalDate.of(year, date.month + 1, date.date)
        val day = localDate.dayOfYear - 1

        var hol = correctAnswer.replace("{Date}", dateFormat.format(date))
        val prtmp: Elements =
            hols[day].select("div.month_cel > ul > li")
        if (prtmp.size == 0)
            return defaultAnswer.replace("{Date}", dateFormat.format(date))
        for (i in 0 until prtmp.size) {
            hol += prtmp[i].text()
            if (i + 1 < prtmp.size) hol += ", "
        }
        return hol
    }
}