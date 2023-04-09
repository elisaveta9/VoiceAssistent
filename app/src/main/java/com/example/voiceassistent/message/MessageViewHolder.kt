package com.example.voiceassistent.message

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voiceassistent.R

open class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var messageText: TextView? = itemView.findViewById(R.id.messageTextView)
    private var messageDate: TextView? = itemView.findViewById(R.id.messageDateView)
    fun bind(message: Message) {
        messageText!!.text = message.text
        val fmt = SimpleDateFormat("dd-MM-yyyy, HH:mm:ss")
        messageDate!!.text = fmt.format(message.date)
    }
}