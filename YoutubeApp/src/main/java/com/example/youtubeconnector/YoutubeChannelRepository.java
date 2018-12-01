package com.example.youtubeconnector;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface YoutubeChannelRepository extends MongoRepository<YoutubeChannel, String> {
}
