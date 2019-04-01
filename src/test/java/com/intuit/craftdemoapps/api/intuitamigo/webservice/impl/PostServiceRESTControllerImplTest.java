package com.intuit.craftdemoapps.api.intuitamigo.webservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.intuit.craftdemoapps.api.intuitamigo.fixtures.PostFixture;
import com.intuit.craftdemoapps.api.intuitamigo.model.Link;
import com.intuit.craftdemoapps.api.intuitamigo.model.Post;
import com.intuit.craftdemoapps.api.intuitamigo.service.PostService;
import com.intuit.craftdemoapps.api.intuitamigo.util.HateoasUtil;
import com.intuit.craftdemoapps.api.intuitamigo.util.UserSessionUtil;
import com.intuit.craftdemoapps.api.intuitamigo.webservice.PostServiceRESTController;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HateoasUtil.class, UserSessionUtil.class})
public class PostServiceRESTControllerImplTest {
	
	@Mock
	PostService postService;
	
	PostServiceRESTController controller;

	@Before
	public void setUp() throws Exception {
		controller = new PostServiceRESTControllerImpl(postService);
		PowerMockito.mockStatic(HateoasUtil.class);
		PowerMockito.mockStatic(UserSessionUtil.class);
	}

	@Test
	public void testDeletePost() {
		
		Response res = controller.deletePost("123");
		
		assertNotNull(res);
		assertEquals(res.getStatus(), 202);
		assertNull(res.getEntity());
		
		verify(postService, times(1)).deletePost("123");
	}
	
	@Test(expected = BadRequestException.class)
	public void testDeletePost_Empty_Post_Id_Throws_Bad_Request_Exception() {		
		controller.deletePost(" ");
	}

	@Test
	public void testGetPost() {
		
		Post post = PostFixture.getPost();
		when(postService.getPost("1")).thenReturn(post);
		when(HateoasUtil.getSelfLink()).thenReturn(new Link("self"));
		
		Response res = controller.getPost("1");
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertEquals(post, res.getEntity());
		assertNotNull(((Post)(res.getEntity())).getLinks());
		assertTrue(((Post)(res.getEntity())).getLinks().containsKey("self"));
		
		verify(postService, times(1)).getPost("1");
		PowerMockito.verifyStatic();
		HateoasUtil.getSelfLink();
	}
	
	@Test(expected = NotFoundException.class)
	public void testGetPost_Null_Post_Throws_Not_Found_Exception() {
		
		when(postService.getPost("1")).thenReturn(null);
		
		controller.getPost("1");
		
		verify(postService, times(1)).getPost("1");
	}
	
	@Test(expected = BadRequestException.class)
	public void testGetPost_Empty_Post_Id_Throws_Bad_Request_Exception() {	
		controller.getPost(null);
	}
	
	@Test
	public void testCreatePost() {
		Post post = PostFixture.getPost();
		
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));	
		when(postService.createPost(post)).thenReturn(post);
		when(HateoasUtil.getPostLink(post.getId())).thenReturn(new Link("self"));
		
		Response res = controller.createPost(post);
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertNotNull(res.getEntity());
		assertEquals(post, (Post)res.getEntity());
		assertTrue(((Post)res.getEntity()).getLinks().containsKey("self"));
		assertTrue(((Post)res.getEntity()).getLinks().containsKey("profile"));
		
		verify(postService, times(1)).createPost(post);
	}
	
	@Test(expected = InternalServerErrorException.class)
	public void testCreatePost_Null_Post_Response_Throws_Internal_Server_Error_Exception() {
		Post post = PostFixture.getPost();
		
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));	
		when(postService.createPost(post)).thenReturn(null);
		when(HateoasUtil.getPostLink(post.getId())).thenReturn(new Link("self"));
		
		controller.createPost(post);
		
		verify(postService, times(1)).createPost(post);
	}

	@Test(expected = InternalServerErrorException.class)
	public void testUpdatePost_Null_Post_Response_Throws_Internal_Server_Error_Exception() {
		Post post = PostFixture.getPost();
		
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));
		when(HateoasUtil.getPostLink(post.getId())).thenReturn(new Link("self"));	
		when(postService.updatePost(post.getId(), post)).thenReturn(null);
		
		controller.updatePost(post.getId(), post);
		
		verify(postService, times(1)).updatePost(post.getId(), post);
	}
	
	@Test(expected = BadRequestException.class)
	public void testUpdatePost_Empty_PostId_Throws_Bad_Request_Exception() {
		Post post = PostFixture.getPost();
		controller.updatePost(null, post);
	}
	
	@Test
	public void testUpdatePost() {
		Post post = PostFixture.getPost();
		
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));
		when(HateoasUtil.getPostLink(post.getId())).thenReturn(new Link("self"));	
		when(postService.updatePost(post.getId(), post)).thenReturn(post);
		
		Response res = controller.updatePost(post.getId(), post);
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertNotNull(res.getEntity());
		assertEquals(post, (Post)res.getEntity());
		assertTrue(((Post)res.getEntity()).getLinks().containsKey("self"));
		assertTrue(((Post)res.getEntity()).getLinks().containsKey("profile"));
		
		verify(postService, times(1)).updatePost(post.getId(), post);
	}

}
