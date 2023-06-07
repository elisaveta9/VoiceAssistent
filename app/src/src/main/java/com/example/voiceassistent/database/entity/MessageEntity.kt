package com.example.voiceassistent.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity(tableName = "messages")
data class MessageEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "message_id")
    val id: Int,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "status")
    val isSend: Boolean

) : Serializable