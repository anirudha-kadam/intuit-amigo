package com.intuit.craftdemoapps.api.intuitamigo.fixtures;

import java.util.ArrayList;
import java.util.List;

import com.intuit.craftdemoapps.api.intuitamigo.model.User;

public final class UserFixture {
	
	private UserFixture() {
		
	}
	
	public static User getUser() {
		User user = new User();
		user.setName("Anirudha");
		user.setUsername("user1");
		user.setPassword("secret");
		return user;
	}
	
	
	public static List<User> getUsers() {
		User user1 = getUser();
		User user2 = getUser();
		List<User> users = new ArrayList<User>();
		users.add(user1);
		users.add(user2);
		return users;
	}
}
