package com.intuit.craftdemoapps.api.intuitamigo.webservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.authentication.AuthenticationManager;

import com.intuit.craftdemoapps.api.intuitamigo.fixtures.UserFixture;
import com.intuit.craftdemoapps.api.intuitamigo.model.Link;
import com.intuit.craftdemoapps.api.intuitamigo.model.User;
import com.intuit.craftdemoapps.api.intuitamigo.service.ProfileService;
import com.intuit.craftdemoapps.api.intuitamigo.util.HateoasUtil;
import com.intuit.craftdemoapps.api.intuitamigo.webservice.ProfileServiceRESTController;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HateoasUtil.class})
public class ProfileServiceRESTControllerImplTest {

	@Mock
	ProfileService profileService;

	@Mock
	AuthenticationManager authenticationManager;

	ProfileServiceRESTController controller;

	@Before
	public void setUp() {
		controller = new ProfileServiceRESTControllerImpl(profileService, authenticationManager);
		PowerMockito.mockStatic(HateoasUtil.class);
	}

	@Test
	public void testGetProfile() {
		User user = UserFixture.getUser();
		
		when(HateoasUtil.getSelfLink()).thenReturn(new Link("self"));
		when(HateoasUtil.getFollowersLink(user.getUsername())).thenReturn(new Link("followers"));
		when(HateoasUtil.getFollowingLink(user.getUsername())).thenReturn(new Link("following"));
		when(HateoasUtil.getFollowLink(user.getUsername())).thenReturn(new Link("follow"));
		when(HateoasUtil.getUnfollowLink(user.getUsername())).thenReturn(new Link("unfollow"));
		when(HateoasUtil.getTimelineLink(user.getUsername())).thenReturn(new Link("timeline"));
		when(profileService.getProfile(user.getUsername())).thenReturn(user);
		
		Response res = controller.getProfile(user.getUsername());
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertNotNull(res.getEntity());
		assertEquals(user, (User)res.getEntity());
		assertEquals(6, ((User)res.getEntity()).getLinks().size());
		
		verify(profileService, times(1)).getProfile(user.getUsername());
	}
	
	@Test(expected = NotFoundException.class)
	public void testGetProfile_Throws_Not_Found_Exception_When_User_Returned_Is_Null() {
		User user = UserFixture.getUser();
		
		when(HateoasUtil.getSelfLink()).thenReturn(new Link("self"));
		when(HateoasUtil.getFollowersLink(user.getUsername())).thenReturn(new Link("followers"));
		when(HateoasUtil.getFollowingLink(user.getUsername())).thenReturn(new Link("following"));
		when(HateoasUtil.getFollowLink(user.getUsername())).thenReturn(new Link("follow"));
		when(HateoasUtil.getUnfollowLink(user.getUsername())).thenReturn(new Link("unfollow"));
		when(HateoasUtil.getTimelineLink(user.getUsername())).thenReturn(new Link("timeline"));
		when(profileService.getProfile(user.getUsername())).thenReturn(null);
		
		controller.getProfile(user.getUsername());
		
		verify(profileService, times(1)).getProfile(user.getUsername());
	}

	@Test(expected = BadRequestException.class)
	public void testGetProfile_Throws_Bad_Request_Exception_When_Username_Is_Null() {	
		controller.getProfile(null);
	}

	@Test
	public void testFollow() {	
		
		when(profileService.isUserExists("user1")).thenReturn(true);
		doAnswer((i) -> {
			return null;
		}).when(profileService).follow("user1");
		Response res = controller.follow("user1");		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		
		verify(profileService, times(1)).isUserExists("user1");
		verify(profileService, times(1)).follow("user1");
	}
	
	@Test(expected = BadRequestException.class)
	public void testFollow_Throws_Bad_Request_Exception_When_Username_Is_Empty() {	
		controller.follow("");		
	}
	
	@Test(expected = NotFoundException.class)
	public void testFollow_Throws_Not_Found_Exception_When_User_Does_Not_Exist() {	
		when(profileService.isUserExists("user1")).thenReturn(false);
		controller.follow("user1");
		verify(profileService, times(1)).isUserExists("user1");
	}

	@Test
	public void testUnfollow() {
		when(profileService.isUserExists("user1")).thenReturn(true);
		doAnswer((i) -> {
			return null;
		}).when(profileService).unfollow("user1");
		Response res = controller.unfollow("user1");		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		
		verify(profileService, times(1)).isUserExists("user1");
		verify(profileService, times(1)).unfollow("user1");
	}
	
	@Test(expected = BadRequestException.class)
	public void testUnfollow_Throws_Bad_Request_Exception_When_Username_Is_Empty() {
		controller.unfollow("");
	}
	
	@Test(expected = NotFoundException.class)
	public void testUnFollow_Throws_Not_Found_Exception_When_User_Does_Not_Exist() {	
		when(profileService.isUserExists("user1")).thenReturn(false);
		controller.unfollow("user1");
		verify(profileService, times(1)).isUserExists("user1");
	}

	@Test
	public void testGetFollowers() {
		List<User> followers = UserFixture.getUsers();
		when(profileService.isUserExists("user1")).thenReturn(true);
		when(profileService.getFollowers("user1")).thenReturn(followers);
		
		Response res = controller.getFollowers("user1");
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertEquals(followers, (List<User>)res.getEntity());
		
		verify(profileService, times(1)).isUserExists("user1");
		verify(profileService, times(1)).getFollowers("user1");
	}
	
	@Test
	public void testGetFollowers_Returns_Empty_List_For_No_Followers() {
		when(profileService.isUserExists("user1")).thenReturn(true);
		when(profileService.getFollowers("user1")).thenReturn(null);
		
		Response res = controller.getFollowers("user1");
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertTrue(((List<User>) res.getEntity()).isEmpty());
		
		verify(profileService, times(1)).isUserExists("user1");
		verify(profileService, times(1)).getFollowers("user1");
	}
	
	@Test(expected = BadRequestException.class)
	public void testGetFollowers_Throws_Bad_Request_Exception_When_Username_Is_Empty() {	
		controller.getFollowers("  ");
	}
	
	@Test(expected = NotFoundException.class)
	public void testGetFollowers_Throws_Not_Found_Exception_When_User_Does_Not_Exist() {
		when(profileService.isUserExists("user1")).thenReturn(false);
		controller.getFollowers("user1");
		verify(profileService, times(1)).isUserExists("user1");
	}

	@Test
	public void testGetFollowing() {
		List<User> following = UserFixture.getUsers();
		when(profileService.isUserExists("user1")).thenReturn(true);
		when(profileService.getFollowing("user1")).thenReturn(following);
		
		Response res = controller.getFollowing("user1");
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertEquals(following, (List<User>)res.getEntity());
		
		verify(profileService, times(1)).isUserExists("user1");
		verify(profileService, times(1)).getFollowing("user1");
	}
	
	@Test
	public void testGetFollowing_Returns_Empty_List_For_No_Following() {
		when(profileService.isUserExists("user1")).thenReturn(true);
		when(profileService.getFollowing("user1")).thenReturn(null);
		
		Response res = controller.getFollowing("user1");
		
		assertNotNull(res);
		assertEquals(200, res.getStatus());
		assertTrue(((List<User>) res.getEntity()).isEmpty());
		
		verify(profileService, times(1)).isUserExists("user1");
		verify(profileService, times(1)).getFollowing("user1");
	}
	
	@Test(expected = BadRequestException.class)
	public void testGetFollowing_Throws_Bad_Request_Exception_When_Username_Is_Empty() {	
		controller.getFollowing("  ");
	}
	
	@Test(expected = NotFoundException.class)
	public void testGetFollowing_Throws_Not_Found_Exception_When_User_Does_Not_Exist() {
		when(profileService.isUserExists("user1")).thenReturn(false);
		controller.getFollowing("user1");
		verify(profileService, times(1)).isUserExists("user1");
	}

//	@Test
//	public void testLogin() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testLogout() {
//		fail("Not yet implemented");
//	}
//	
//	@Test
//	public void testSignup() {
//		fail("Not yet implemented");
//	}

}
