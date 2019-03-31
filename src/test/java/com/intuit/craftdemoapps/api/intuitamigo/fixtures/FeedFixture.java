package com.intuit.craftdemoapps.api.intuitamigo.fixtures;

import java.util.Date;

import com.intuit.craftdemoapps.api.intuitamigo.model.Feed;
import com.intuit.craftdemoapps.api.intuitamigo.model.Timeline;

public final class FeedFixture {

	private FeedFixture() {
		
	}
	
	public static Feed getFeed() {
		Feed feed = new Feed();
		feed.setIndex(0);
		feed.setLimit(100);
		feed.setPosts(PostFixture.getPosts());
		feed.setRefreshedAt(new Date());
		feed.setUsername("user1");
		return feed;
	}
	
	public static Timeline getTimline() {
		Timeline timeline = new Timeline();
		timeline.setIndex(0);
		timeline.setLimit(100);
		timeline.setPosts(PostFixture.getPosts());
		timeline.setRefreshedAt(new Date());
		timeline.setUsername("user1");
		return timeline;
	}
}
