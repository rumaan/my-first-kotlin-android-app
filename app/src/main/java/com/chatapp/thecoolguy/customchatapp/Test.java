package com.chatapp.thecoolguy.customchatapp;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Test {
    Test() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseRecyclerAdapter f = new FirebaseRecyclerAdapter<Chat, ChatViewHolder>
                (Chat.class, android.R.layout.two_line_list_item, ChatViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(ChatViewHolder chatViewHolder, Chat chat, int i) {

            }
        };
    }
}
