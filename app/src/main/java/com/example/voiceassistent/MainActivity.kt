package com.example.voiceassistent

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voiceassistent.message.Message
import com.example.voiceassistent.message.MessageListAdapter
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var sendButton : Button
    private lateinit var messageListAdapter: MessageListAdapter
    private lateinit var questionText: EditText
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var chatMessageList: RecyclerView
    var sPref: SharedPreferences? = null
    private val APP_PREFERENCES = "mysettings"
    private var isLight = true
    private val THEME = "THEME"

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!isLight) AppCompatDelegate.MODE_NIGHT_YES

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("LOG", "onCreate")
        Locale.setDefault(Locale("ru"))

        messageListAdapter = MessageListAdapter()

        sendButton = findViewById(R.id.sendButton)
        chatMessageList = findViewById(R.id.chatMessageList)
        chatMessageList.layoutManager = LinearLayoutManager(this)
        chatMessageList.adapter = messageListAdapter
        chatMessageList.scrollToPosition(messageListAdapter.messageList.size - 1)
        questionText = findViewById(R.id.questionField)
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if (it != TextToSpeech.ERROR) {    textToSpeech.language = Locale.getDefault()}
        })

        sendButton.setOnClickListener {
            onSend()
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        messageListAdapter.messageList =
            savedInstanceState.getSerializable("messageListAdapter") as ArrayList<Message>
        messageListAdapter.notifyDataSetChanged()
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("messageListAdapter", messageListAdapter.messageList)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.theme_swap) {
            if (delegate.localNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
                isLight = true
            }
            else {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                isLight = false
            }
        }
        return super.onOptionsItemSelected(item)
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