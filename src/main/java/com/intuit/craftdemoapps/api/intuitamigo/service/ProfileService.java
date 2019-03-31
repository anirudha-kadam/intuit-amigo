package com.intuit.craftdemoapps.api.intuitamigo.service;

import java.util.List;

import com.intuit.craftdemoapps.api.intuitamigo.model.User;

public interface ProfileService {

	boolean isValidUserName(String username);

	User createProfile(User user);

	User getProfile(String username);

	void follow(String username);

	void unfollow(String username);

	List<User> getFollowers(String username);

	List<User> getFollowing(String username);

}
