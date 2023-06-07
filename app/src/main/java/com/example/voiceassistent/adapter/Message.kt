package com.example.voiceassistent.adapter

import com.example.voiceassistent.data.MessageEntity
import java.util.*

data class Message(val text: String, val date: Date, val isSend: Boolean){
    constructor(text: String, isSend: Boolean) : this(text, Date(), isSend)

    constructor(messageEntity: MessageEntity) :
            this(messageEntity.text, Date(messageEntity.date), messageEntity.isSend)
}
