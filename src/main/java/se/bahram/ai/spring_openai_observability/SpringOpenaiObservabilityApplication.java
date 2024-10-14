package se.bahram.ai.spring_openai_observability;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
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
				.advisors(new SimpleLoggerAdvisor())
				.user("""
      					Tell me a joke. Be concise. Don't say anything except a joke
						""")
				.call()
				.content();

		return Map.of("joke", reply);
	}

	@GetMapping("/actor-films")
	ActorFilms actorFilms() {
		ActorFilms actorFilms = chatClient.prompt()
				.advisors(new SimpleLoggerAdvisor())
				.user("Generate the filmography for a random actor.")
				.call()
				.entity(ActorFilms.class);
		return actorFilms;
	}

	@GetMapping("/actors-films")
	List<ActorFilms> actorsFilms() {
		List<ActorFilms> actorFilms = chatClient.prompt()
				.advisors(new SimpleLoggerAdvisor())
				.user("Generate the filmography of 5 movies for Tom Hanks and Bill Murray.")
				.call()
				.entity(new ParameterizedTypeReference<List<ActorFilms>>() {});
		return actorFilms;
	}

}

record ActorFilms(String actor, List<String> movies){}
