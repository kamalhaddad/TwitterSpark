package com.twitterspark.server;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Tweet {
    private @Id
    @GeneratedValue
    Long id;
    private String text;


    public Tweet() {

    }

    public Tweet(Long id, String text) {
        this.id = id;
        this.text = text;
    }
}
