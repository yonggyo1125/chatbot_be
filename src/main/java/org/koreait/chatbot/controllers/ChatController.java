package org.koreait.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.chatbot.entities.ChatData;
import org.koreait.chatbot.services.ChatService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService service;

    /**
     * /chat?message=문장...
     * @param message
     * @return
     */
    @GetMapping
    public ChatData chat(@RequestParam("message") String message) {
        return service.process(message);
    }
}
