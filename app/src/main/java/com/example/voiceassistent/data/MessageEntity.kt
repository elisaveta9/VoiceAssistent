package com.example.voiceassistent.data

import com.example.voiceassistent.adapter.Message

data class MessageEntity(
    var text: String,
    var date: Long,
    var isSend:Boolean
) {
    constructor(message: Message) : this(message.text, message.date.time, message.isSend)
}