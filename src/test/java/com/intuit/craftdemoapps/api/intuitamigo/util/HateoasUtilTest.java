package com.intuit.craftdemoapps.api.intuitamigo.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HateoasUtilTest {

	private MockHttpServletRequest request;

	@Before
	public void setUp() throws Exception {
		this.request = new MockHttpServletRequest();
		this.request.setScheme("https");
		this.request.setServerName("localhost");
		this.request.setServerPort(8080);
		this.request.setRequestURI("/intuit-amigo");
		this.request.setContextPath("/intuit-amigo");

		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	public void testGetProfileURI() {
		String profileUri = HateoasUtil.getProfileURI("user1").toString();
		assertEquals("https://localhost:8080/intuit-amigo/api/v1/profiles/user1", profileUri);
	}

	@Test
	public void testGetProfileLink() {
		String profileLink = HateoasUtil.getProfileLink("user1").getHref();
		assertEquals("https://localhost:8080/intuit-amigo/api/v1/profiles/user1", profileLink);

	}

	@Test
	public void testGetFollowLink() {
		String followLink = HateoasUtil.getFollowLink("user1").getHref();
		assertEquals("https://localhost:8080/intuit-amigo/api/v1/profiles/user1/follow", followLink);

	}

	@Test
	public void testGetUnfollowLink() {
		String unfollowLink = HateoasUtil.getUnfollowLink("user1").getHref();
		assertEquals("https://localhost:8080/intuit-amigo/api/v1/profiles/user1/unfollow", unfollowLink);

	}

	@Test
	public void testGetFollowersLink() {
		String followersLink = HateoasUtil.getFollowersLink("user1").getHref();
		assertEquals("https://localhost:8080/intuit-amigo/api/v1/profiles/user1/followers", followersLink);

	}

	@Test
	public void testGetFollowingLink() {
		String followingLink = HateoasUtil.getFollowingLink("user1").getHref();
		assertEquals("https://localhost:8080/intuit-amigo/api/v1/profiles/user1/following", followingLink);

	}

	@Test
	public void testGetPostLink() {
		String postLink = HateoasUtil.getPostLink("1234").getHref();
		assertEquals("https://localhost:8080/intuit-amigo/api/v1/posts/1234", postLink);

	}

	@Test
	public void testGetTimelineLink() {
		String timelineLink = HateoasUtil.getTimelineLink("user1").getHref();
		assertEquals("https://localhost:8080/intuit-amigo/api/v1/feed/user1", timelineLink);

	}

	@Test
	public void testGetSelfLink() {
		String selfLink = HateoasUtil.getSelfLink().getHref();
		assertEquals("https://localhost:8080/intuit-amigo", selfLink);
	}

}
