package org.koreait.chatbot.services;

import org.junit.jupiter.api.Test;
import org.koreait.chatbot.entities.ChatData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatServiceTest {
    @Autowired
    private ChatService service;

    @Test
    void test() {
        ChatData data = service.process("밥은 먹고 다니니?");
        System.out.println(data);
    }
}
