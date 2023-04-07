package com.example.voiceassistent

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var sendButton : Button
    lateinit var chatWindow: TextView
    lateinit var questionText: EditText
    lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Locale.setDefault(Locale("ru"))

        sendButton = findViewById(R.id.sendButton)
        chatWindow = findViewById(R.id.chatWindow)
        questionText = findViewById(R.id.questionField)
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if (it != TextToSpeech.ERROR) {    textToSpeech.language = Locale.getDefault()}
        })

        sendButton.setOnClickListener {
            onSend()
        }
    }

    private fun onSend() {
        val text = questionText.text.toString()
        chatWindow.append(text + "\n")
        val answer = AI(context = applicationContext).getAnswer(text)
        chatWindow.append(answer + "\n")
        textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH,null, null )
        questionText.text.clear()
        dismissKeyboard()
    }

    private fun dismissKeyboard() {
        val view: View? = this.currentFocus // элемент, который имеет текущий фокус ввода
        if (view != null) {
            // определить менеджер, отвечающий  за ввод
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            // менеджер скрывает экранную клавиатуру
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}