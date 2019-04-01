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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.intuit.craftdemoapps.api.intuitamigo.fixtures.FeedFixture;
import com.intuit.craftdemoapps.api.intuitamigo.fixtures.PostFixture;
import com.intuit.craftdemoapps.api.intuitamigo.model.Feed;
import com.intuit.craftdemoapps.api.intuitamigo.model.Link;
import com.intuit.craftdemoapps.api.intuitamigo.model.Post;
import com.intuit.craftdemoapps.api.intuitamigo.model.Timeline;
import com.intuit.craftdemoapps.api.intuitamigo.service.FeedService;
import com.intuit.craftdemoapps.api.intuitamigo.util.HateoasUtil;
import com.intuit.craftdemoapps.api.intuitamigo.util.UserSessionUtil;
import com.intuit.craftdemoapps.api.intuitamigo.webservice.FeedServiceRESTController;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HateoasUtil.class, UserSessionUtil.class})
public class FeedServiceRESTControllerImplTest {
	
	@Mock
	FeedService feedService;
	
	FeedServiceRESTController controller;
	
	@Before
	public void setUp() {
		controller = new FeedServiceRESTControllerImpl(feedService);
		PowerMockito.mockStatic(HateoasUtil.class);
		PowerMockito.mockStatic(UserSessionUtil.class);
	}
	
	@Test
	public void testDeletePost() {
		
		Response res = controller.deletePost("123");
		
		assertNotNull(res);
		assertEquals(res.getStatus(), 202);
		assertNull(res.getEntity());
		
		verify(feedService, times(1)).deletePost("123");
	}
	
	@Test(expected = BadRequestException.class)
	public void testDeletePost_Empty_Post_Id_Throws_Bad_Request_Exception() {		
		controller.deletePost(" ");
	}

	@Test
	public void testGetPost() {
		
		Post post = PostFixture.getPost();
		when(feedService.getPost("1")).thenReturn(post);
		when(HateoasUtil.getSelfLink()).thenReturn(new Link("self"));
		
		Response res = controller.getPost("1");
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertEquals(post, res.getEntity());
		assertNotNull(((Post)(res.getEntity())).getLinks());
		assertTrue(((Post)(res.getEntity())).getLinks().containsKey("self"));
		
		verify(feedService, times(1)).getPost("1");
		PowerMockito.verifyStatic();
		HateoasUtil.getSelfLink();
	}
	
	@Test(expected = NotFoundException.class)
	public void testGetPost_Null_Post_Throws_Not_Found_Exception() {
		
		when(feedService.getPost("1")).thenReturn(null);
		
		controller.getPost("1");
		
		verify(feedService, times(1)).getPost("1");
	}
	
	@Test(expected = BadRequestException.class)
	public void testGetPost_Empty_Post_Id_Throws_Bad_Request_Exception() {	
		controller.getPost(null);
	}

	@Test
	public void testGetFeed() {
		Feed feed = FeedFixture.getFeed();
		
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getTimelineLink("user1")).thenReturn(new Link("timeline"));
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));
		when(feedService.getFeed(0, 100)).thenReturn(feed);
		
		Response res = controller.getFeed(0, 100);
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertNotNull(res.getEntity());
		assertTrue(((Feed)res.getEntity()).getLinks().containsKey("timeline"));
		assertTrue(((Feed)res.getEntity()).getLinks().containsKey("profile"));
		assertEquals(feed.getPosts(), ((Feed)res.getEntity()).getPosts());
		
		verify(feedService, times(1)).getFeed(0, 100);
	}
	
	
	@Test
	public void testGetFeed_Pagination_Query_Params_Not_Provided_Selects_Default_Values() {
		Feed feed = FeedFixture.getFeed();
		
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getTimelineLink("user1")).thenReturn(new Link("timeline"));
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));
		when(feedService.getFeed(0, 100)).thenReturn(feed);
		
		Response res = controller.getFeed(null, null);
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertNotNull(res.getEntity());
		assertTrue(((Feed)res.getEntity()).getLinks().containsKey("timeline"));
		assertTrue(((Feed)res.getEntity()).getLinks().containsKey("profile"));
		assertEquals(feed.getPosts(), ((Feed)res.getEntity()).getPosts());
		
		verify(feedService, times(1)).getFeed(0, 100);
	}
	
	@Test(expected = InternalServerErrorException.class)
	public void testGetFeed_Null_Feed_Throws_Internal_Server_Error_Exception() {
		
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getTimelineLink("user1")).thenReturn(new Link("timeline"));
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));
		when(feedService.getFeed(0, 100)).thenReturn(null);
		
		controller.getFeed(0, 100);
		
		verify(feedService, times(1)).getFeed(0, 100);
	}

	@Test
	public void testCreatePost() {
		Post post = PostFixture.getPost();
		
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));	
		when(feedService.createPost(post)).thenReturn(post);
		when(HateoasUtil.getPostLink(post.getId())).thenReturn(new Link("self"));
		
		Response res = controller.createPost(post);
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertNotNull(res.getEntity());
		assertEquals(post, (Post)res.getEntity());
		assertTrue(((Post)res.getEntity()).getLinks().containsKey("self"));
		assertTrue(((Post)res.getEntity()).getLinks().containsKey("profile"));
		
		verify(feedService, times(1)).createPost(post);
	}
	
	@Test(expected = InternalServerErrorException.class)
	public void testCreatePost_Null_Post_Response_Throws_Internal_Server_Error_Exception() {
		Post post = PostFixture.getPost();
		
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));	
		when(feedService.createPost(post)).thenReturn(null);
		when(HateoasUtil.getPostLink(post.getId())).thenReturn(new Link("self"));
		
		controller.createPost(post);
		
		verify(feedService, times(1)).createPost(post);
	}

	@Test(expected = InternalServerErrorException.class)
	public void testUpdatePost_Null_Post_Response_Throws_Internal_Server_Error_Exception() {
		Post post = PostFixture.getPost();
		
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));
		when(HateoasUtil.getPostLink(post.getId())).thenReturn(new Link("self"));	
		when(feedService.updatePost(post.getId(), post)).thenReturn(null);
		
		controller.updatePost(post.getId(), post);
		
		verify(feedService, times(1)).updatePost(post.getId(), post);
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
		when(feedService.updatePost(post.getId(), post)).thenReturn(post);
		
		Response res = controller.updatePost(post.getId(), post);
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertNotNull(res.getEntity());
		assertEquals(post, (Post)res.getEntity());
		assertTrue(((Post)res.getEntity()).getLinks().containsKey("self"));
		assertTrue(((Post)res.getEntity()).getLinks().containsKey("profile"));
		
		verify(feedService, times(1)).updatePost(post.getId(), post);
	}

	@Test
	public void testGetTimeline_Pagination_Query_Params_Not_Provided_Selects_Default_Values() {
		Timeline timeline = FeedFixture.getTimline();
		
		when(HateoasUtil.getSelfLink()).thenReturn(new Link("self"));
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));
		when(feedService.getTimeline("user1", 0, 100)).thenReturn(timeline);
		
		Response res = controller.getTimeline("user1", null, null);
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertNotNull(res.getEntity());
		assertTrue(((Timeline)res.getEntity()).getLinks().containsKey("self"));
		assertTrue(((Timeline)res.getEntity()).getLinks().containsKey("profile"));
		assertEquals(timeline.getPosts(), ((Timeline)res.getEntity()).getPosts());
		
		verify(feedService, times(1)).getTimeline("user1", 0, 100);
	}
	
	@Test
	public void testGetTimeline() {
		Timeline timeline = FeedFixture.getTimline();
		
		when(HateoasUtil.getSelfLink()).thenReturn(new Link("self"));
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));
		when(feedService.getTimeline("user1", 0, 100)).thenReturn(timeline);
		
		Response res = controller.getTimeline("user1", 0, 100);
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertNotNull(res.getEntity());
		assertTrue(((Timeline)res.getEntity()).getLinks().containsKey("self"));
		assertTrue(((Timeline)res.getEntity()).getLinks().containsKey("profile"));
		assertEquals(timeline.getPosts(), ((Timeline)res.getEntity()).getPosts());
		
		verify(feedService, times(1)).getTimeline("user1", 0, 100);
	}
	
	@Test(expected = InternalServerErrorException.class)
	public void testGetTimeline_Null_Timeline_Throws_Internal_Server_Error_Exception() {
		
		when(HateoasUtil.getSelfLink()).thenReturn(new Link("self"));
		when(HateoasUtil.getProfileLink("user1")).thenReturn(new Link("profile"));
		when(feedService.getTimeline("user1", 0, 100)).thenReturn(null);
		
		controller.getTimeline("user1", 0, 100);
		
		verify(feedService, times(1)).getTimeline("user1", 0, 100);
	}

}
