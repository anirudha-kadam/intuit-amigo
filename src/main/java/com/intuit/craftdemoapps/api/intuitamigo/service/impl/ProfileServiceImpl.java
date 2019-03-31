package com.intuit.craftdemoapps.api.intuitamigo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.intuit.craftdemoapps.api.intuitamigo.dao.IntuitAmigoRepository;
import com.intuit.craftdemoapps.api.intuitamigo.model.User;
import com.intuit.craftdemoapps.api.intuitamigo.service.ProfileService;
import com.intuit.craftdemoapps.api.intuitamigo.util.UserSessionUtil;

@Component
public class ProfileServiceImpl implements ProfileService, UserDetailsService {

	@Autowired
	private IntuitAmigoRepository repository;
	
	@Override
	public User createProfile(User user) {
		
		return Optional.ofNullable(repository.createUser(user))
				.map(usr -> {
					usr.setPassword(null); 
					usr.setFollowers(repository.getNumOfFollowers(usr.getUsername()));
					usr.setFollowers(repository.getNumOfFollowing(usr.getUsername()));
					return usr;
				})
				.orElse(null);
	}

	@Override
	public boolean isValidUserName(String username) {
		
		return !repository.isUserPresent(username);
	}

	@Override
	public User getProfile(String username) {
		
		return Optional.ofNullable(repository.getUser(username))
				.map(usr -> {
					usr.setPassword(null); 
					usr.setFollowers(repository.getNumOfFollowers(username));
					usr.setFollowers(repository.getNumOfFollowing(username));
					return usr;
				})
				.orElse(null);
	}

	@Override
	public void follow(String username) {
		repository.follow(UserSessionUtil.getUsername(), username);
	}

	@Override
	public void unfollow(String username) {
		repository.unfollow(UserSessionUtil.getUsername(), username);
	}

	@Override
	public List<User> getFollowers(String username) {
		return repository.getFollowers(username);
	}

	@Override
	public List<User> getFollowing(String username) {
		return repository.getFollowing(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = repository.getUser(username);

	    UserBuilder builder = null;
	    if (user != null) {
	      builder = org.springframework.security.core.userdetails.User.withUsername(username);
	      builder.password(user.getPassword());
	      builder.roles("USER");
	    } else {
	      throw new UsernameNotFoundException("User not found.");
	    }

	    return builder.build();
	}

}
