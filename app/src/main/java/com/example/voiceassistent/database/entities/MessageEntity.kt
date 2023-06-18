package com.example.voiceassistent.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.voiceassistent.adapter.Message

@Entity(
    tableName = "message"
)
data class MessageEntity(
    var text: String,
    var date: Long,
    var isSend:Boolean
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0

    constructor(message: Message) : this(message.text, message.date.time, message.isSend)
}