package com.example.restservice.greeting;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.websocket.server.PathParam;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {

    private final Logger LOG = LoggerFactory.getLogger(GreetingController.class);
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final GreetingService service;

    public GreetingController(GreetingService greetingService) {
        this.service = greetingService;
    }

    @GetMapping("/greeting")
    public ResponseEntity<List<Greeting>> getGreeting() {
        List<Greeting> greetings = service.getGreetings(0, 10);
        return ResponseEntity.ok(greetings);
    }

    @GetMapping("/greeting/{greetingId}")
    public Greeting greeting(@PathVariable(value = "greetingId") Long greetingId) {

        LOG.debug("Incoming Greeting Id {}", greetingId);

        Greeting greeting = this.service.getGreetingById(greetingId);
        return greeting;
    }

    @PostMapping("/greeting")
    public ResponseEntity<Void> createGreeting(@RequestParam(value = "greeting") String greeting) {
        LOG.debug("Incoming Greeting {}", greeting);

        long greetingId = this.service.saveGreeting(greeting);

        return ResponseEntity.created(URI.create(String.format("/greeting/%d", greetingId))).build();
    }
}
