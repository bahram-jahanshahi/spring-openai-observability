package se.bahram.ai.spring_openai_observability;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@SpringBootApplication
public class SpringOpenaiObservabilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringOpenaiObservabilityApplication.class, args);
	}

	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		return builder.build();
	}
}

@Controller
@ResponseBody
class JokeController {

	private final ChatClient chatClient;

	public JokeController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("/joke")
	Map<String, String> joke() {
		var reply = chatClient
				.prompt()
				.user("""
      					Tell me a joke. Be concise. Don't say anything except a joke
						""")
				.call()
				.content();

		return Map.of("joke", reply);
	}



}
