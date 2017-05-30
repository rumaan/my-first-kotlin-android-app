package com.chatapp.thecoolguy.customchatapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    var recycler: RecyclerView? = null
    var sendButton: Button? = null
    var textField: EditText? = null
    var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Chat, ChatViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // link the Views
        recycler = findViewById(R.id.chat_recycler) as RecyclerView?
        sendButton = findViewById(R.id.btn_send_message) as Button?
        textField = findViewById(R.id.text_field) as EditText?

        recycler!!.setHasFixedSize(false)
        recycler!!.layoutManager = LinearLayoutManager(this)

        val databaseReference = FirebaseDatabase.getInstance().reference

        firebaseRecyclerAdapter =
                object : FirebaseRecyclerAdapter<Chat, ChatViewHolder>(Chat::class.java,
                        android.R.layout.two_line_list_item, ChatViewHolder::class.java, databaseReference) {
                    override fun populateViewHolder(chatViewHolder: ChatViewHolder, chat: Chat, i: Int) {
                        chatViewHolder.setMessage(chat.message!!)
                        chatViewHolder.setName(chat.title!!)
                    }
                }

        recycler?.adapter = firebaseRecyclerAdapter

        sendButton?.setOnClickListener {
            val x: Editable? = textField?.text
            if (x.isNullOrBlank()) {
                textField?.error = "Empty Message"
                textField?.requestFocus()
                return@setOnClickListener
            }
            databaseReference.push().setValue(Chat("Title", x?.toString()))
            textField!!.text.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        firebaseRecyclerAdapter?.cleanup()
    }
}

class Chat {
    var message: String? = null
    var title: String? = null

    constructor() {
        // empty contructor
    }

    constructor(title: String?, message: String?) {
        this.message = message
        this.title = title
    }
}
