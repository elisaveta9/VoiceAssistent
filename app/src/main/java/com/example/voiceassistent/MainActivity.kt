package com.example.voiceassistent

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voiceassistent.message.Message
import com.example.voiceassistent.message.MessageListAdapter
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var sendButton : Button
    lateinit var messageListAdapter: MessageListAdapter
    lateinit var questionText: EditText
    lateinit var textToSpeech: TextToSpeech
    lateinit var chatMessageList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Locale.setDefault(Locale("ru"))

        messageListAdapter = MessageListAdapter()

        sendButton = findViewById(R.id.sendButton)
        chatMessageList = findViewById(R.id.chatMessageList)
        chatMessageList.layoutManager = LinearLayoutManager(this)
        chatMessageList.adapter = messageListAdapter
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
        messageListAdapter.messageList.add(Message(text, isSend = true))
        val answer = AI(context = applicationContext).getAnswer(text)
        messageListAdapter.messageList.add(Message(answer, isSend = false))
        messageListAdapter.notifyDataSetChanged()
        chatMessageList.scrollToPosition(messageListAdapter.messageList.size - 1)
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