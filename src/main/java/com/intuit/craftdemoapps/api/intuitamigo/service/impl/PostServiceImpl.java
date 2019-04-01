package com.intuit.craftdemoapps.api.intuitamigo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.intuit.craftdemoapps.api.intuitamigo.dao.IntuitAmigoRepository;
import com.intuit.craftdemoapps.api.intuitamigo.model.Post;
import com.intuit.craftdemoapps.api.intuitamigo.service.PostService;
import com.intuit.craftdemoapps.api.intuitamigo.util.UserSessionUtil;

@Component
public class PostServiceImpl implements PostService {

	private IntuitAmigoRepository repository;

	@Autowired
	public PostServiceImpl(IntuitAmigoRepository repository) {
		this.repository = repository;
	}

	@Override
	public Post createPost(Post post) {
		return repository.createPost(UserSessionUtil.getUsername(), post);
	}

	@Override
	public Post updatePost(String postId, Post post) {
		return repository.updatePost(postId, post);
	}

	@Override
	public void deletePost(String postId) {
		repository.deletePost(UserSessionUtil.getUsername(), postId);
	}

	@Override
	public Post getPost(String postId) {
		return repository.getPost(postId);
	}

}
