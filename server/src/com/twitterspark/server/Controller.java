package com.twitterspark.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    private final TweetRepository tweetRepository;

    Controller(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }
    @GetMapping("/")
    public Map<String, String> homepage() {
        return Collections.singletonMap("Hello", new String("World"));
    }

    @GetMapping("/tweets")
    List<Tweet> all() {
        return tweetRepository.findAll();
    }

    @PostMapping("/tweet")
    Tweet newEmployee(@RequestBody Tweet newTweet) {
        return tweetRepository.save(newTweet);
    }
}
