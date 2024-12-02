package com.hami.bluechat;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private BluetoothSocket socket;
    private List<String> messages;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private EditText messageEditText;
    private ImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerViewChat);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        messages = new ArrayList<>();

        // Get the Bluetooth socket
        String deviceAddress = getIntent().getStringExtra("device_address");
        // Create BluetoothSocket here based on deviceAddress

        // Set up RecyclerView
        chatAdapter = new ChatAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(v -> sendMessage());

        // Start reading incoming messages
        startReadingMessages();
    }

    private void sendMessage() {
        String message = messageEditText.getText().toString();
        if (!message.isEmpty()) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(message.getBytes());
                outputStream.flush();
                messages.add("You: " + message);
                chatAdapter.notifyDataSetChanged();
                messageEditText.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startReadingMessages() {
        // Read incoming messages from the BluetoothSocket and update RecyclerView
        new Thread(() -> {
            try {
                InputStream inputStream = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int bytes;
                while ((bytes = inputStream.read(buffer)) != -1) {
                    String message = new String(buffer, 0, bytes);
                    runOnUiThread(() -> {
                        messages.add("Other: " + message);
                        chatAdapter.notifyDataSetChanged();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
