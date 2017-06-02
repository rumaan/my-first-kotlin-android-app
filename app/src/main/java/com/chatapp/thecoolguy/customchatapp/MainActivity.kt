package com.chatapp.thecoolguy.customchatapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    var recycler: RecyclerView? = null
    var sendButton: Button? = null
    var textField: EditText? = null
    var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Chat, ChatViewHolder>? = null
    val TAG = "MainActivity"
    val PERMISSION_REQUEST_CODE = 99

    var mAuth: FirebaseAuth? = null

    var lastPos = -1

    fun setFadeAnimation(view: View, position: Int) {
        if (position > lastPos) {
            val anim: AlphaAnimation = AlphaAnimation(0f, 1f)
            anim.interpolator = FastOutSlowInInterpolator()
            anim.duration = 700
            view.startAnimation(anim)
            lastPos = position
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // check for permission results
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permissions granted
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show()
                FirebaseDatabase.getInstance().setPersistenceEnabled(true)
            } else {
                // permissions denied
                Toast.makeText(this, "Permissions Denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        // link the Views
        recycler = findViewById(R.id.chat_recycler) as RecyclerView?
        sendButton = findViewById(R.id.btn_send_message) as Button?
        textField = findViewById(R.id.text_field) as EditText?

        val layoutManager = LinearLayoutManager(this)
        layoutManager.isSmoothScrollbarEnabled = true

        recycler?.layoutManager = layoutManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE
                    ),
                    PERMISSION_REQUEST_CODE)

        } else {
            Log.d(TAG, "Persistence : true")
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }

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

    override fun onStart() {
        super.onStart()
        val mUser: FirebaseUser? = mAuth?.currentUser
        updateUI(mUser)
    }

    fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_LONG)
                    .show()

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
