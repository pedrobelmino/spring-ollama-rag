package br.com.pedrobelmino.langchain4j.ollama;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final CustomerSupportAgent agent;

    public ChatController(CustomerSupportAgent agent) {
        this.agent = agent;
    }

    @GetMapping("/chat")
    public RadarResponse chat(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        return agent.chat(message);
    }
}
