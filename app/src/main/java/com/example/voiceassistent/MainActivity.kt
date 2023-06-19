package com.example.voiceassistent

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voiceassistent.adapter.Message
import com.example.voiceassistent.adapter.MessageListAdapter
import com.example.voiceassistent.ai.AI
import com.example.voiceassistent.database.context.MessageDatabase
import com.example.voiceassistent.database.dao.MessageDao
import com.example.voiceassistent.database.entities.MessageEntity
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var sendButton : Button
    private lateinit var messageListAdapter: MessageListAdapter
    private lateinit var questionText: EditText
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var chatMessageList: RecyclerView
    private lateinit var sPref: SharedPreferences
    private lateinit var messageDao: MessageDao
    private val APP_PREFERENCES = "MySettings"
    private val THEME = "THEME"
    private var isLight = true

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("LOG", "onCreate")

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageDao = MessageDatabase(this).getMessageDao()
        messageListAdapter = MessageListAdapter()

        initViewElements()

        sPref = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE)
        isLight = sPref.getBoolean(THEME, true)
        if (!isLight)
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES

        textToSpeech = TextToSpeech(applicationContext) {
            if (it != TextToSpeech.ERROR)
                textToSpeech.language = Locale.getDefault()
        }

        sendButton.setOnClickListener {
            onSend()
        }
    }

    override fun onStop() {
        val editor: SharedPreferences.Editor = sPref.edit()
        editor.putBoolean(THEME, isLight)
        editor.apply()

        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("NotifyDataSetChanged")
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
        else if (item.itemId == R.id.delete_all_messages){
            messageListAdapter.messageList = arrayListOf()
            messageListAdapter.notifyDataSetChanged()
            lifecycleScope.launch {
                val messages = messageDao.getAllMessage()
                messages.forEach{ messageDao.deleteMessage(it) }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViewElements(){
        sendButton = findViewById(R.id.sendButton)
        chatMessageList = findViewById(R.id.chatMessageList)
        chatMessageList.layoutManager = LinearLayoutManager(this)
        chatMessageList.adapter = messageListAdapter

        lifecycleScope.launch {
            val messageList = messageDao.getAllMessage()
            messageList.forEach {
                messageListAdapter.messageList.add(Message(it))
                messageListAdapter.notifyDataSetChanged()
            }
            chatMessageList.scrollToPosition(messageListAdapter.itemCount - 1)
        }

        questionText = findViewById(R.id.questionField)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onSend() {
        val text = questionText.text.toString()
        messageListAdapter.messageList.add(Message(text, true))
        questionText.text.clear()
        dismissKeyboard()

        messageListAdapter.messageList.add(Message("", false))
        val lastId = messageListAdapter.messageList.lastIndex
        var answer = ""
        var lastIdData = lastId

        lifecycleScope.launch {
            messageDao.addMessage(
                MessageEntity(messageListAdapter.messageList[lastId - 1]))
            messageDao.addMessage(
                MessageEntity(messageListAdapter.messageList[lastId]))
            lastIdData = messageDao.getAllMessage().last().id
        }

        AI(applicationContext).getAnswer(text) { s ->
            textToSpeech.speak(s, TextToSpeech.QUEUE_ADD,null, null )
            answer += "$s "
            messageListAdapter.messageList[lastId] = Message(answer, false)
            lifecycleScope.launch {
                messageDao.updateMessage(MessageEntity(lastIdData,
                    answer,
                    messageListAdapter.messageList[lastId].date.time,
                    false)) }
            messageListAdapter.notifyDataSetChanged()
            chatMessageList.scrollToPosition(messageListAdapter.itemCount - 1)
        }
    }

    private fun dismissKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}