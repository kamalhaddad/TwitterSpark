package com.twitterspark.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
