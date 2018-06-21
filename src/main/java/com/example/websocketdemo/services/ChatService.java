package com.example.websocketdemo.services;


import com.example.websocketdemo.model.ChatMessage;

public interface ChatService {
    void send(String string, ChatMessage chatMessage);
}
