package com.intuit.craftdemoapps.api.intuitamigo.service;

import com.intuit.craftdemoapps.api.intuitamigo.model.Post;

public interface PostService {

	Post createPost(Post post);

	Post updatePost(String postId, Post post);

	void deletePost(String postId);

	Post getPost(String postId);
}
