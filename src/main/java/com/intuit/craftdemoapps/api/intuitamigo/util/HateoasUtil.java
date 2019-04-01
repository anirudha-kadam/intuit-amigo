package com.intuit.craftdemoapps.api.intuitamigo.util;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.intuit.craftdemoapps.api.intuitamigo.model.Link;

public final class HateoasUtil {

	private HateoasUtil() {}

	private static final String BASE_PATH_PROFILES = "/api/v1/profiles";

	private static final String BASE_PATH_FEED = "/api/v1/feed";

	public static URI getProfileURI(String username) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(BASE_PATH_PROFILES + "/" + username).build()
				.toUri();
	}

	public static Link getProfileLink(String username) {

		String link = ServletUriComponentsBuilder.fromCurrentContextPath().path(BASE_PATH_PROFILES + "/" + username)
				.build().toUri().toString();
		return new Link(link);
	}

	public static Link getFollowLink(String username) {
		String link = ServletUriComponentsBuilder.fromCurrentContextPath().path(BASE_PATH_PROFILES).path("/")
				.path(username).path("/follow").build().toUri().toString();
		return new Link(link);
	}

	public static Link getUnfollowLink(String username) {
		String link = ServletUriComponentsBuilder.fromCurrentContextPath().path(BASE_PATH_PROFILES).path("/")
				.path(username).path("/unfollow").build().toUri().toString();
		return new Link(link);
	}

	public static Link getFollowersLink(String username) {
		String link = ServletUriComponentsBuilder.fromCurrentContextPath().path(BASE_PATH_PROFILES).path("/")
				.path(username).path("/followers").build().toUri().toString();
		return new Link(link);
	}

	public static Link getFollowingLink(String username) {
		String link = ServletUriComponentsBuilder.fromCurrentContextPath().path(BASE_PATH_PROFILES).path("/")
				.path(username).path("/following").build().toUri().toString();
		return new Link(link);
	}

	public static Link getPostLink(String postId) {
		String link = ServletUriComponentsBuilder.fromCurrentContextPath().path(BASE_PATH_FEED).path("/").path(postId)
				.path("/view").build().toUri().toString();
		return new Link(link);
	}

	public static Link getTimelineLink(String username) {
		String link = ServletUriComponentsBuilder.fromCurrentContextPath().path(BASE_PATH_FEED).path("/").path(username)
				.build().toUri().toString();
		return new Link(link);
	}

	public static Link getSelfLink() {
		String link = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
		return new Link(link);
	}
}
