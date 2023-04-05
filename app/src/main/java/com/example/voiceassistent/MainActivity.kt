package com.example.voiceassistent

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var sendButton : Button
    lateinit var chatWindow: TextView
    lateinit var questionText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendButton = findViewById(R.id.sendButton)
        chatWindow = findViewById(R.id.chatWindow)
        questionText = findViewById(R.id.questionField)

        sendButton.setOnClickListener {
            onSend()
        }
    }

    private fun onSend(){
        val text = questionText.text.toString()
        chatWindow.append(text + "\n")
        val answer = "Вопрос понял. Думаю…"
        chatWindow.append(answer + "\n")
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