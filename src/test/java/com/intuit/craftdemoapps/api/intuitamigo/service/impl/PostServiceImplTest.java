package com.intuit.craftdemoapps.api.intuitamigo.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.intuit.craftdemoapps.api.intuitamigo.dao.IntuitAmigoRepository;
import com.intuit.craftdemoapps.api.intuitamigo.fixtures.PostFixture;
import com.intuit.craftdemoapps.api.intuitamigo.model.Post;
import com.intuit.craftdemoapps.api.intuitamigo.service.PostService;
import com.intuit.craftdemoapps.api.intuitamigo.util.UserSessionUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UserSessionUtil.class})
public class PostServiceImplTest {

	@Mock
	IntuitAmigoRepository repository;

	PostService postService;

	@Before
	public void setUp() throws Exception {
		postService = new PostServiceImpl(repository);
		PowerMockito.mockStatic(UserSessionUtil.class);
	}

	@Test
	public void testCreatePost() {
		Post post = PostFixture.getPost();
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(repository.createPost("user1", post)).thenReturn(post);

		Post createdPost = postService.createPost(post);

		assertNotNull(createdPost);
		assertEquals(createdPost, post);

		verify(repository, times(1)).createPost("user1", post);
	}

	@Test
	public void testUpdatePost() {
		Post post = PostFixture.getPost();

		when(repository.updatePost("1234", post)).thenReturn(post);

		Post updatedPost = postService.updatePost("1234", post);

		assertNotNull(updatedPost);
		assertEquals(updatedPost, post);

		verify(repository, times(1)).updatePost("1234", post);
	}

	@Test
	public void testDeletePost() {
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		doAnswer((i) -> {
			return null;
		}).when(repository).deletePost("user1", "1234");
		postService.deletePost("1234");
		verify(repository, times(1)).deletePost("user1", "1234");
	}

	@Test
	public void testGetPost() {
		Post post = PostFixture.getPost();

		when(repository.getPost("1234")).thenReturn(post);

		Post updatedPost = postService.getPost("1234");

		assertNotNull(updatedPost);
		assertEquals(updatedPost, post);

		verify(repository, times(1)).getPost("1234");
	}

}
