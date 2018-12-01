package com.example.youtubeconnector;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface YoutubeVideoRepository extends MongoRepository<YoutubeVideo, String> {

}
