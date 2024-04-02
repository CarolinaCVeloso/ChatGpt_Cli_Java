package com.RafaCa.ProjectAI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class ProjectAiApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Bem vindo ao Chat GPT! \n Digite o que deseja buscar: \n");
		String searchString = scanner.nextLine();

		String input = """
				{
				    "model": "gpt-3.5-turbo",
				    "messages": [
				     {
				      "role": "user",
				      "content": "%s"
				      }
				    ],
				    "temperature": 0.7
				}
				""".formatted(searchString);

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.openai.com/v1/chat/completions"))
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + Dotenv.load().get("OPENAI_API_KEY"))
				.POST(HttpRequest.BodyPublishers.ofString(input))
				.build();

		HttpClient client = HttpClient.newHttpClient();
		var response = client.send(request, HttpResponse.BodyHandlers.ofString());

		ObjectMapper objectMapper = new ObjectMapper();
		if (response.statusCode() == 200) {
			ChatGptResponse chatGptResponse = objectMapper.readValue(response.body(), ChatGptResponse.class);
			if (chatGptResponse.choices() != null && chatGptResponse.choices().length > 0) {
				ChatGptResponseChoice lastChoice = chatGptResponse.choices()[chatGptResponse.choices().length - 1];
				if ("stop".equals(lastChoice.finish_reason())) {
					Message messageResponse = lastChoice.message();
					if (messageResponse != null) {
						String answer = messageResponse.content();
						System.out.println("A resposta é: " + answer);
					} else {
						System.out.println("Resposta não encontrada");
					}
				}
			}
		}
	}
}



