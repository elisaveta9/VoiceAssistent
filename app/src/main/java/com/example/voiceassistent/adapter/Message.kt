package com.example.voiceassistent.adapter

import java.util.*

data class Message(val text: String, val date: Date, val isSend: Boolean){
    constructor(text: String, isSend: Boolean) : this(text, Date(), isSend)
}
