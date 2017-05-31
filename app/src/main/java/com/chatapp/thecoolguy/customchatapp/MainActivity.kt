package com.chatapp.thecoolguy.customchatapp

import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.EditText
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    var recycler: RecyclerView? = null
    var sendButton: Button? = null
    var textField: EditText? = null
    var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Chat, ChatViewHolder>? = null
    val TAG = "MainActivity"

    var lastPos = -1
    fun setFadeAnimation(view: View, position: Int) {
        if (position > lastPos) {
            val scaleAnim: ScaleAnimation = ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            scaleAnim.duration = 700
            scaleAnim.interpolator = FastOutSlowInInterpolator()
            view.startAnimation(scaleAnim)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // link the Views
        recycler = findViewById(R.id.chat_recycler) as RecyclerView?
        sendButton = findViewById(R.id.btn_send_message) as Button?
        textField = findViewById(R.id.text_field) as EditText?

        val layoutManager = LinearLayoutManager(this)
        layoutManager.isSmoothScrollbarEnabled = true

        recycler?.layoutManager = layoutManager

        val databaseReference = FirebaseDatabase.getInstance().reference

        firebaseRecyclerAdapter =
                object : FirebaseRecyclerAdapter<Chat, ChatViewHolder>(Chat::class.java,
                        R.layout.chat_msg_list_item, ChatViewHolder::class.java, databaseReference) {
                    override fun populateViewHolder(chatViewHolder: ChatViewHolder, chat: Chat, i: Int) {
                        chatViewHolder.setMessage(chat.message!!)
                        chatViewHolder.setName(chat.title!!)

                        setFadeAnimation(chatViewHolder.getRootView(), i)
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
            databaseReference.push().setValue(Chat("Sender", x?.toString()))
            textField!!.text.clear()
            recycler?.smoothScrollToPosition(recycler!!.adapter.itemCount - 1)
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
        // empty constructor
    }

    constructor(title: String?, message: String?) {
        this.message = message
        this.title = title
    }
}
