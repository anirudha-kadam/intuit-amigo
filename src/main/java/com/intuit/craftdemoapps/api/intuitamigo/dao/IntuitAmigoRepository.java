package com.intuit.craftdemoapps.api.intuitamigo.dao;

import java.util.List;
import java.util.concurrent.Future;

import com.intuit.craftdemoapps.api.intuitamigo.model.Feed;
import com.intuit.craftdemoapps.api.intuitamigo.model.Post;
import com.intuit.craftdemoapps.api.intuitamigo.model.Timeline;
import com.intuit.craftdemoapps.api.intuitamigo.model.User;

public interface IntuitAmigoRepository {

	User createUser(User user);
	
	User getUser(String username);
	
	Future<User> getUserAsync(String username);
	
	Integer getNumOfFollowers(String username);
	
	Integer getNumOfFollowing(String username);

	boolean isUserPresent(String username);
	
	Post createPost(String username, Post post);
	
	Post updatePost(String postId, Post post);
	
	Post getPost(String postId);
	
	Future<Post> getPostAsync(String postId);
	
	void deletePost(String username, String postId);
	
	Timeline getTimeline(String username, Integer index, Integer limit);
	
	Feed getFeed(String username, Integer index, Integer limit);
	
	void follow(String follower, String followee);
	
	void unfollow(String follower, String followee);
	
	List<User> getFollowers(String username);
	
	List<User> getFollowing(String username);
}