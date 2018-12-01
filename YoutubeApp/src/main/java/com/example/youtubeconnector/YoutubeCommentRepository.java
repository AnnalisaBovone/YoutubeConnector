package com.example.youtubeconnector;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface YoutubeCommentRepository extends MongoRepository<YoutubeComment, String>{	
	//youtubeCommentRepository.findByQuery(".*"+input+".*");
	@Query("{'text': {$regex: ?0 }}")
	YoutubeComment findByQuery(String word);
}
