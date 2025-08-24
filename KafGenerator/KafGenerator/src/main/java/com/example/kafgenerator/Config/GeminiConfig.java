package com.example.kafgenerator.Config;

import com.google.genai.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GeminiConfig {

    public Client geminiClient( ){
        return new Client();
    }

}
