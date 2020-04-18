package com.example.sudohelp.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sudohelp.ApplicantsAdapter;
import com.example.sudohelp.ApplicationsObject;
import com.example.sudohelp.R;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private String currentUserId, applicantId;

    private EditText message;
    private Button sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        message =(EditText) findViewById(R.id.message);
        sendButton=(Button) findViewById(R.id.send);
        applicantId=getIntent().getExtras().getString("applicantId");
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        mChatLayoutManager = new LinearLayoutManager(Chat.this);
        recyclerView.setLayoutManager(mChatLayoutManager);

        mChatAdapter = new ChatsAdapter(getDataSetChat(), Chat.this);
        recyclerView.setAdapter(mChatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }

    private void sendMessage() {
    }

    private ArrayList<ChatObject> resultsChats = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultsChats;
    }
}
