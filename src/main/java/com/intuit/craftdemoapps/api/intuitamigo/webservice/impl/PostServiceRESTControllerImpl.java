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
import org.springframework.stereotype.Component;

import com.intuit.craftdemoapps.api.intuitamigo.model.Link;
import com.intuit.craftdemoapps.api.intuitamigo.model.Post;
import com.intuit.craftdemoapps.api.intuitamigo.service.PostService;
import com.intuit.craftdemoapps.api.intuitamigo.util.HateoasUtil;
import com.intuit.craftdemoapps.api.intuitamigo.util.UserSessionUtil;
import com.intuit.craftdemoapps.api.intuitamigo.webservice.PostServiceRESTController;

@Component
public class PostServiceRESTControllerImpl implements PostServiceRESTController {

	private PostService postService;
	
	@Autowired
	public PostServiceRESTControllerImpl(PostService postService) {
		this.postService = postService;
	}
	
	@Override
	public Response createPost(Post post) {
		
		Map<String, Link> links = new HashMap<>(2);
		links.put("profile", HateoasUtil.getProfileLink(UserSessionUtil.getUsername()));

		return Optional.ofNullable(postService.createPost(post))
				.map(p -> {
					links.put("self", HateoasUtil.getPostLink(p.getId()));
					p.setLinks(links);
					return p;
				})
				.map(Response::ok)
				.map(ResponseBuilder::build)
				.orElseThrow(() -> new InternalServerErrorException("Could not create Post. Something went wrong"));
	}
	
	@Override
	public Response getPost(String postId) {
		
		if(StringUtils.isBlank(postId)) {
			throw new BadRequestException("postId must not be empty");
		}
		
		Map<String, Link> links = new HashMap<>(1);
		links.put("self", HateoasUtil.getSelfLink());
		
		return Optional.ofNullable(postService.getPost(postId))
				.map(p -> {
					p.setLinks(links);
					return p;
				})
				.map(Response::ok)
				.map(ResponseBuilder::build)
				.orElseThrow(() -> new NotFoundException("Post Not Found"));
	}

	@Override
	public Response updatePost(String postId, Post post) {
		
		if(StringUtils.isBlank(postId)) {
			throw new BadRequestException("postId must not be empty");
		}
		
		Map<String, Link> links = new HashMap<>(2);
		links.put("profile", HateoasUtil.getProfileLink(UserSessionUtil.getUsername()));
		links.put("self", HateoasUtil.getPostLink(postId));
		
		return Optional.ofNullable(postService.updatePost(postId, post))
				.map(p -> {
					p.setLinks(links);
					return p;
				})
				.map(Response::ok)
				.map(ResponseBuilder::build)
				.orElseThrow(() -> new InternalServerErrorException("Could not update Post. Something went wrong"));
	}
	
	@Override
	public Response deletePost(String postId) {
		if(StringUtils.isBlank(postId)) {
			throw new BadRequestException("postId must not be empty");
		}
		
		postService.deletePost(postId);
		return Response.accepted().build();
	}

}
