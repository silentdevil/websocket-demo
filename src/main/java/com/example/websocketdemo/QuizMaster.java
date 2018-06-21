package com.example.websocketdemo;

import com.example.websocketdemo.controller.ChatController;
import com.example.websocketdemo.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QuizMaster {

        @Autowired
        private SimpMessageSendingOperations messagingTemplate;

    private Map<String, String> questions = new HashMap<>();
    public String currentQuestion = "";
    private  List<String> questionList;

    public QuizMaster() {
        questions.put("What is the Capital of the Philippines? ", "Manila");
        questions.put("What is the Capital of the Indonesia? ", "Jakarta");
        questions.put("What is the Capital of the Zimbabwe? ", "Harare");
        questions.put("What is the Capital of the Canada? ", "Ottawa");

        questionList = new ArrayList<>(questions.keySet());

    }

    public  void showQuestion(boolean isNew, String userName) {
        System.out.println("Hey");
        if (isNew) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender("QuizMaster");
            chatMessage.setType(ChatMessage.MessageType.CHAT);
            currentQuestion = currentQuestion.equals("") ?
                    questionList.get(new Random().nextInt(questionList.size())) : currentQuestion;
            chatMessage.setContent(currentQuestion);
            System.out.println("userName = " + userName);
            if(!userName.equals(""))
                messagingTemplate.convertAndSendToUser(userName,"/topic/public", chatMessage);
            else
                messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

    public  synchronized void checkAnswer(String question, ChatMessage chatMessage) {
        boolean isNew = false;
        if(questions.get(question).equalsIgnoreCase(chatMessage.getContent())) {
           chatMessage.setContent(chatMessage.getSender() + " is Correct");
           isNew = true;
           currentQuestion = "";
        } else {
            chatMessage.setContent(chatMessage.getSender() + " is Wrong!");
        }
        chatMessage.setSender("QuizMaster");
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
        showQuestion(isNew,"");
    }


}
