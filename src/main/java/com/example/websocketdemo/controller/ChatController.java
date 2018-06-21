package com.example.websocketdemo.controller;

import com.example.websocketdemo.QuizMaster;
import com.example.websocketdemo.model.ChatMessage;
import com.example.websocketdemo.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;


import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
@Controller
public class ChatController {

        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    @Autowired
    private ChatService chatService;

    @Autowired
    private QuizMaster quizMaster;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        executor.schedule(() -> quizMaster.checkAnswer(quizMaster.currentQuestion, chatMessage), 100, TimeUnit.MILLISECONDS);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor,
                               Map<String, String> map) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        executor.schedule(() -> {
            try {
                quizMaster.showQuestion(true,headerAccessor.getUser().getName());

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        },100, TimeUnit.MILLISECONDS);
        return chatMessage;
    }


}
