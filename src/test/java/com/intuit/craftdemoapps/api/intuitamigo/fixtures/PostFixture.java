package com.intuit.craftdemoapps.api.intuitamigo.fixtures;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.intuit.craftdemoapps.api.intuitamigo.model.Post;

public final class PostFixture {

	private PostFixture() {
		
	}
	
	public static Post getPost() {
		Post post = new Post();
		post.setAuthor("user1");
		post.setId("1");
		post.setPostedAt(new Date());
		post.setText("Awesome");
		return post;
	}
	
	public static List<Post> getPosts() {
		List<Post> posts = new ArrayList<>();
		posts.add(getPost());
		posts.add(getPost());
		return posts;
	}
}
