package com.CahERafa.ProjectAI;

public record ChatGptResponseChoice (
        int index,
        Message message,
        Object logprobs,
        Object finish_reason
        ){
}
