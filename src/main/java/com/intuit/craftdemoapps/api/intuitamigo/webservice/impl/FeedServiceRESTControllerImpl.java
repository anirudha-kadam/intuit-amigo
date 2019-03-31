package com.intuit.craftdemoapps.api.intuitamigo.webservice.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.intuit.craftdemoapps.api.intuitamigo.model.Link;
import com.intuit.craftdemoapps.api.intuitamigo.model.Post;
import com.intuit.craftdemoapps.api.intuitamigo.service.FeedService;
import com.intuit.craftdemoapps.api.intuitamigo.util.HateoasUtil;
import com.intuit.craftdemoapps.api.intuitamigo.util.UserSessionUtil;
import com.intuit.craftdemoapps.api.intuitamigo.webservice.FeedServiceRESTController;

@Component
public class FeedServiceRESTControllerImpl implements FeedServiceRESTController {

	private FeedService feedService;
	
	@Autowired
	public FeedServiceRESTControllerImpl(FeedService feedService) {
		this.feedService = feedService;
	}
	
	@Override
	public Response deletePost(String postId) {
		if(StringUtils.isBlank(postId)) {
			throw new BadRequestException("postId must not be empty");
		}
		
		feedService.deletePost(postId);
		return Response.accepted().build();
	}

	@Override
	public Response getPost(String postId) {
		
		if(StringUtils.isBlank(postId)) {
			throw new BadRequestException("postId must not be empty");
		}
		
		Map<String, Link> links = new HashMap<>(1);
		links.put("self", HateoasUtil.getSelfLink());
		
		return Optional.ofNullable(feedService.getPost(postId))
				.map(p -> {
					p.setLinks(links);
					return p;
				})
				.map(Response::ok)
				.map(ResponseBuilder::build)
				.orElseThrow(() -> new NotFoundException("Post Not Found"));
	}

	@Override
	public Response getFeed(Integer index, Integer limit) {
		
		index = index != null ? index : 0;
		limit = limit != null ? limit : 100;
		
		Map<String, Link> links = new HashMap<>(2);
		links.put("timeline", HateoasUtil.getTimelineLink(UserSessionUtil.getUsername()));
		links.put("profile", HateoasUtil.getProfileLink(UserSessionUtil.getUsername()));

		return Optional.ofNullable(feedService.getFeed(index, limit))
				.map(f -> {
					f.setLinks(links);
					return f;
				})
				.map(Response::ok)
				.map(ResponseBuilder::build)
				.orElse(null);
	}

	@Override
	public Response createPost(Post post) {
		
		Map<String, Link> links = new HashMap<>(2);
		links.put("profile", HateoasUtil.getProfileLink(UserSessionUtil.getUsername()));

		return Optional.ofNullable(feedService.createPost(post))
				.map(p -> {
					links.put("self", HateoasUtil.getPostLink(p.getId()));
					p.setLinks(links);
					return p;
				})
				.map(Response::ok)
				.map(ResponseBuilder::build)
				.orElse(null);
	}

	@Override
	public Response updatePost(String postId, Post post) {
		
		if(StringUtils.isBlank(postId)) {
			throw new BadRequestException("postId must not be empty");
		}
		
		return Optional.ofNullable(feedService.updatePost(postId, post))
				.map(Response::ok)
				.map(ResponseBuilder::build)
				.orElse(null);
	}
	
	@Override
	public Response getTimeline(String username, Integer index, Integer limit) {
		
		index = index != null ? index : 0;
		limit = limit != null ? limit : 100;
		
		Map<String, Link> links = new HashMap<>(2);
		links.put("self", HateoasUtil.getSelfLink());
		links.put("profile", HateoasUtil.getProfileLink(UserSessionUtil.getUsername()));
		
		return Optional.ofNullable(feedService.getTimeline(username, index, limit))
				.map(t -> {
					t.setLinks(links);
					return t;
				})
				.map(Response::ok)
				.map(ResponseBuilder::build)
				.orElse(null);
	}

}
