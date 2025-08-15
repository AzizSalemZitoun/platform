package com.example.kafgenerator.Services;

import com.google.genai.Client;

import com.google.genai.types.GenerateContentResponse;
import lombok.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service

    public class GenerateTextFromTextInput {
        public static String askGemini(String prompt) {
            Client client = new Client();

            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-2.5-flash",
                            prompt,
                            null);

return response.text();        }


    }



