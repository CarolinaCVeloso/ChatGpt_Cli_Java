package com.CarolinaVeloso.ProjectAI;

public record ChatGptResponse(
        String id,
        String object,
        int created,
        String model,
        ChatGptResponseChoice[] choices,
        ChatGptResponseUsage usage,
        String system_fingerprint) {
}
