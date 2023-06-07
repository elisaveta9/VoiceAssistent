package com.example.voiceassistent.database.repository

import androidx.lifecycle.LiveData
import com.example.voiceassistent.database.dao.MessageDao
import com.example.voiceassistent.database.entity.MessageEntity

class MessageRepository(private val messagesDao: MessageDao) {

    fun getMessages(): LiveData<List<MessageEntity>> {
        return messagesDao.getAllMessages()
    }

    fun insertMessage(messageEntity: MessageEntity): Int {
        return messagesDao.insertNewMessage(messageEntity)
    }

    fun deleteMessage(messageEntity: MessageEntity):Int{
        return messagesDao.deleteMessage(messageEntity)
    }

    fun deleteMessages():Int{
        return messagesDao.deleteAllMessages()
    }
}