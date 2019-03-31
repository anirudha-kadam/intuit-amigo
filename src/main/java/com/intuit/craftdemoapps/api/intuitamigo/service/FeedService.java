package com.intuit.craftdemoapps.api.intuitamigo.service;

import com.intuit.craftdemoapps.api.intuitamigo.model.Feed;
import com.intuit.craftdemoapps.api.intuitamigo.model.Post;
import com.intuit.craftdemoapps.api.intuitamigo.model.Timeline;

public interface FeedService {

	Post createPost(Post post);

	Post updatePost(String postId, Post post);

	void deletePost(String postId);

	Post getPost(String postId);

	Feed getFeed(Integer index, Integer limit);
	
	Timeline getTimeline(String username, Integer index, Integer limit);
}
