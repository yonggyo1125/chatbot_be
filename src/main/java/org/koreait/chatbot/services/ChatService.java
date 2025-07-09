package org.koreait.chatbot.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.koreait.chatbot.entities.ChatData;
import org.koreait.global.configs.PythonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.stream.Collectors;

@Lazy
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(PythonProperties.class)
public class ChatService {

    private final PythonProperties pythonProperties;
    private final WebApplicationContext ctx;
    private final ObjectMapper om;

    public ChatData process(String message) {

        try {

            boolean isProduction = Arrays.stream(ctx.getEnvironment().getActiveProfiles()).anyMatch(s -> s.equals("prod") || s.equals("mac"));

            String activationCommand = null, pythonPath = null;
            if (isProduction) { // 리눅스 환경, 서비스 환경
                activationCommand = String.format("%s/activate", pythonProperties.getBase());
                pythonPath = pythonProperties.getBase() + "/python";
            } else { // 윈도우즈 환경
                activationCommand = String.format("%s/activate.bat", pythonProperties.getBase());
                pythonPath = pythonProperties.getBase() + "/python.exe";
            }

            ProcessBuilder builder = isProduction ? new ProcessBuilder("/bin/sh", activationCommand) : new ProcessBuilder(activationCommand); // 가상환경 활성화
            Process process = builder.start();
            if (process.waitFor() == 0) { // 정상 수행된 경우
                builder = new ProcessBuilder(pythonPath, pythonProperties.getModel() + "/generate.py", message);
                process = builder.start();
                int statusCode = process.waitFor();
                if (statusCode == 0) {
                    String json = process.inputReader().lines().collect(Collectors.joining());

                    return om.readValue(json, ChatData.class);
                } else {
                    System.out.println("statusCode:" + statusCode);
                    process.errorReader().lines().forEach(System.out::println);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
