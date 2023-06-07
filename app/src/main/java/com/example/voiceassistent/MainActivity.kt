package com.example.voiceassistent

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voiceassistent.adapter.Message
import com.example.voiceassistent.adapter.MessageListAdapter
import com.example.voiceassistent.ai.AI
import com.example.voiceassistent.data.DBHelper
import com.example.voiceassistent.data.MessageEntity
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var sendButton : Button
    private lateinit var messageListAdapter: MessageListAdapter
    private lateinit var questionText: EditText
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var chatMessageList: RecyclerView
    private lateinit var sPref: SharedPreferences
    private lateinit var dBHelper: DBHelper
    private lateinit var database: SQLiteDatabase
    private val APP_PREFERENCES = "MySettings"
    private val THEME = "THEME"
    private var isLight = true

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("LOG", "onCreate")

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dBHelper = DBHelper(this)
        database = dBHelper.writableDatabase
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

        database = dBHelper.writableDatabase
        database.delete(dBHelper.TABLE_NAME, null, null)
        for (i in 0 until messageListAdapter.itemCount){
            val entity = MessageEntity(messageListAdapter.messageList[i])

            val contentValues = ContentValues()
            contentValues.put(dBHelper.FIELD_MESSAGE, entity.text)
            contentValues.put(dBHelper.FIELD_SEND, entity.isSend)
            contentValues.put(dBHelper.FIELD_DATE, entity.date)

            database.insert(dBHelper.TABLE_NAME,null,contentValues)
        }
        database.close()

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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViewElements(){
        messageListAdapter = MessageListAdapter()

        val cursor: Cursor =
            database.query(dBHelper.TABLE_NAME, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val messageIndex = cursor.getColumnIndex(dBHelper.FIELD_MESSAGE)
            val dateIndex = cursor.getColumnIndex(dBHelper.FIELD_DATE)
            val sendIndex = cursor.getColumnIndex(dBHelper.FIELD_SEND)
            do {
                val entity = MessageEntity(
                    cursor.getString(messageIndex),
                    cursor.getLong(dateIndex), cursor.getInt(sendIndex)==1
                )
                val message = Message(entity)
                messageListAdapter.messageList.add(message)
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()

        sendButton = findViewById(R.id.sendButton)
        chatMessageList = findViewById(R.id.chatMessageList)
        chatMessageList.layoutManager = LinearLayoutManager(this)
        chatMessageList.adapter = messageListAdapter
        chatMessageList.scrollToPosition(messageListAdapter.itemCount - 1)
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

        AI(applicationContext).getAnswer(text) { s ->
            answer += "$s "
            messageListAdapter.messageList[lastId] = Message(answer, false)
            messageListAdapter.notifyDataSetChanged()
            chatMessageList.scrollToPosition(messageListAdapter.itemCount - 1)
            textToSpeech.speak(s, TextToSpeech.QUEUE_ADD,null, null )
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