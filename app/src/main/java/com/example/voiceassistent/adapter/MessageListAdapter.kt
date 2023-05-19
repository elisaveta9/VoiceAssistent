package com.example.voiceassistent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voiceassistent.R

class MessageListAdapter : RecyclerView.Adapter<MessageViewHolder>() {
    var messageList: ArrayList <Message> = ArrayList()
    private val ASSISTANT_TYPE = 0
    private val USER_TYPE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view: View
        if (viewType == USER_TYPE) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_message,parent,false)
        }
        else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.assistant_message,parent,false);
        }
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        MessageViewHolder(holder.itemView).bind(messageList[position])
    }

    override fun getItemViewType(index: Int): Int {
        var message = messageList[index]
        return if (message.isSend) {
            USER_TYPE
        } else ASSISTANT_TYPE
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}