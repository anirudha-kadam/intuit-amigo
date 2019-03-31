package com.intuit.craftdemoapps.api.intuitamigo.webservice.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.intuit.craftdemoapps.api.intuitamigo.model.Link;
import com.intuit.craftdemoapps.api.intuitamigo.model.LoginRequest;
import com.intuit.craftdemoapps.api.intuitamigo.model.User;
import com.intuit.craftdemoapps.api.intuitamigo.service.ProfileService;
import com.intuit.craftdemoapps.api.intuitamigo.util.HateoasUtil;
import com.intuit.craftdemoapps.api.intuitamigo.webservice.ProfileServiceRESTController;

@Component
public class ProfileServiceRESTControllerImpl implements ProfileServiceRESTController {

	private ProfileService profileService;
	
	private AuthenticationManager authenticationManager;

	@Autowired
	public ProfileServiceRESTControllerImpl(ProfileService profileService,
			AuthenticationManager authenticationManager) {
		this.profileService = profileService;
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Response getProfile(String username) {
		
		if(StringUtils.isBlank(username)) {
			throw new BadRequestException("username must not be empty");
		}
		
		Map<String, Link> links = new HashMap<>(6);
		links.put("self", HateoasUtil.getSelfLink());
		links.put("followers", HateoasUtil.getFollowersLink(username));
		links.put("following", HateoasUtil.getFollowingLink(username));
		links.put("follow",HateoasUtil.getFollowLink(username));
		links.put("unfollow", HateoasUtil.getUnfollowLink(username));
		links.put("timeline", HateoasUtil.getTimelineLink(username));
		
		return Optional.ofNullable(profileService.getProfile(username))
				.map(p -> {
					p.setLinks(links);
					return p;
				})
				.map(Response::ok)
				.map(ResponseBuilder::build)
				.orElseThrow(() -> new NotFoundException("User not found"));
	}

	@Override
	public Response signup(User user) {
		
		if (StringUtils.isBlank(user.getName()) || StringUtils.isBlank(user.getUsername())
				|| StringUtils.isBlank(user.getPassword())) {
			throw new BadRequestException("username, password, name are required fields");
		}

		if (!profileService.isValidUserName(user.getUsername())) {
			throw new BadRequestException("User already exists with username " + user.getUsername());
		}

		return Optional.ofNullable(profileService.createProfile(user))
				.map(usr -> HateoasUtil.getProfileURI(user.getUsername()))
				.map(Response::created)
				.map(ResponseBuilder::build)
				.orElseThrow(() -> new InternalServerErrorException("Could not create profile. Something went wrong"));
	}
	
	@Override
	public Response follow(String username) {
		if(StringUtils.isBlank(username)) {
			throw new BadRequestException("username must not be empty");
		}
		
		profileService.follow(username);
		return Response.ok().build();
	}

	@Override
	public Response unfollow(String username) {
		if(StringUtils.isBlank(username)) {
			throw new BadRequestException("username must not be empty");
		}
		
		profileService.unfollow(username);
		return Response.ok().build();
	}

	@Override
	public Response getFollowers(String username) {
		
		if(StringUtils.isBlank(username)) {
			throw new BadRequestException("username must not be empty");
		}
		
		return Optional.ofNullable(profileService.getFollowers(username))
		.map(Response::ok)
		.map(ResponseBuilder::build)
		.orElse(null);
	}

	@Override
	public Response getFollowing(String username) {
		return Optional.ofNullable(profileService.getFollowing(username))
				.map(Response::ok)
				.map(ResponseBuilder::build)
				.orElse(null);
	}

	@Override
	public Response login(LoginRequest loginRequest) {
		if (StringUtils.isBlank(loginRequest.getUsername())
				|| StringUtils.isBlank(loginRequest.getPassword())) {
			throw new BadRequestException("username, password are required fields");
		}
		
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		Authentication authentication = this.authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return Response.ok(authentication.getPrincipal()).build();
	}

	@Override
	public Response logout() {
		return Response.ok("Please Log In").build();
	}

}
