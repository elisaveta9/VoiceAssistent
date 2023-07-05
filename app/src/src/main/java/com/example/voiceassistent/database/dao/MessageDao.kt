package com.example.voiceassistent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.voiceassistent.database.entity.MessageEntity

@Dao
interface MessageDao {
    @Insert
    fun insertNewMessage(messageEntity: MessageEntity): Int

    @Query("SELECT * FROM messages")
    fun getAllMessages(): LiveData<List<MessageEntity>>

    @Delete
    fun deleteMessage(messageEntity: MessageEntity): Int

    @Query("DELETE FROM messages")
    fun deleteAllMessages(): Int
}