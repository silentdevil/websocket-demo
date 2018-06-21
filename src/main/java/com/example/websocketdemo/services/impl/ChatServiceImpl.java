package com.example.websocketdemo.services.impl;

import com.example.websocketdemo.model.ChatMessage;
import com.example.websocketdemo.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private List<ChatMessage> chatMessageList = new ArrayList<>();

    @Override
    public void send(String string, ChatMessage chatMessage) {
        kafkaTemplate.send("chat", string);
        chatMessageList.add(chatMessage);

        chatMessageList.forEach(x -> System.out.println(x.getContent()));
    }

    public void listen(ChatMessage chatMessage) {
        System.out.println(chatMessage.getContent());
    }
}
