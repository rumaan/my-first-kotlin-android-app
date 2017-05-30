package com.chatapp.thecoolguy.customchatapp

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView
    private val msgTextView: TextView

    init {
        nameTextView = itemView.findViewById(android.R.id.text1) as TextView
        msgTextView = itemView.findViewById(android.R.id.text2) as TextView
    }


    fun setName(name: String) {
        nameTextView.text = name
    }

    fun setMessage(message: String) {
        msgTextView.text = message
    }
}
