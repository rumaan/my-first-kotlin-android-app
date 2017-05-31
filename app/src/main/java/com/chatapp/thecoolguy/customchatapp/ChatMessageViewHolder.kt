package com.chatapp.thecoolguy.customchatapp

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView
    private val msgTextView: TextView
    private val rootView: View

    init {
        nameTextView = itemView.findViewById(R.id.sender) as TextView
        msgTextView = itemView.findViewById(R.id.msg) as TextView
        rootView = itemView.findViewById(R.id.root_view)
    }

    fun getRootView(): View {
        return rootView
    }

    fun setName(name: String) {
        nameTextView.text = name
    }

    fun setMessage(message: String) {
        msgTextView.text = message
    }
}
