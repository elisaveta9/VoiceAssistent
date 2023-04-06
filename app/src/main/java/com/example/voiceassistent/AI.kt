package com.example.voiceassistent

class AI {
    val queAndAns = mapOf(
        Regex(".*((привет(ик)?)|(здравствуй)).*") to "Привет. ",
        Regex(".*как (дел(а|(ишки)))\\?.*") to "Не плохо. ",
        Regex(".*((а )?чем занимаешься\\?).*") to "Отвечаю на вопросы. "
    )

    public fun getAnswer(question:String):String{
        question.lowercase()
        var answer = ""
        for ((key, value) in queAndAns.entries){
            if (question.matches(key))
                answer += value
        }
        if (answer == "")
            answer = "Вопрос понял. Думаю..."
        return answer
    }
}