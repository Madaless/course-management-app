package com.course.courseapp.service;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

    private final OpenAiChatModel chatClient;

    public AiServiceImpl(OpenAiChatModel chatClientBuilder) {
        this.chatClient = chatClientBuilder;
    }

    public String generateDescription(String title) {
        String prompt = "Write a short, professional course description for a course titled: \"" + title + "\"";
        return chatClient.call(prompt);    }
}