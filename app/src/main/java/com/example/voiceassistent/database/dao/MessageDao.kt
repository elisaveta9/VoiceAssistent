package com.example.voiceassistent.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.voiceassistent.database.entities.MessageEntity

@Dao
interface MessageDao {

    @Insert
    suspend fun addMessage(messageEntity: MessageEntity)

    @Query("SELECT * FROM message ORDER BY id")
    suspend fun getAllMessage(): List<MessageEntity>

    @Update
    suspend fun updateMessage(messageEntity: MessageEntity)

    @Delete
    suspend fun deleteMessage(messageEntity: MessageEntity)

    @Query("DELETE FROM message")
    suspend fun deleteAllMessage()
}