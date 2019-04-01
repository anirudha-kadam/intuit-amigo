package com.intuit.craftdemoapps.api.intuitamigo.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.intuit.craftdemoapps.api.intuitamigo.fixtures.FeedFixture;
import com.intuit.craftdemoapps.api.intuitamigo.model.Feed;
import com.intuit.craftdemoapps.api.intuitamigo.model.Timeline;
import com.intuit.craftdemoapps.api.intuitamigo.service.FeedService;
import com.intuit.craftdemoapps.api.intuitamigo.util.UserSessionUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UserSessionUtil.class})
public class FeedServiceImplTest {

	@Mock
	IntuitAmigoRepository repository;
	
	FeedService feedService;
	
	@Before
	public void setUp() throws Exception {
		feedService = new FeedServiceImpl(repository);
		PowerMockito.mockStatic(UserSessionUtil.class);
	}

	@Test
	public void testGetFeed() {
		Feed feed = FeedFixture.getFeed();
		when(UserSessionUtil.getUsername()).thenReturn("user1");
		when(repository.getFeed("user1", 0, 100)).thenReturn(feed);

		Feed feedRead = feedService.getFeed(0, 100);

		assertNotNull(feed);
		assertEquals(feed, feedRead);
		
		verify(repository, times(1)).getFeed("user1", 0, 100);
	}

	@Test
	public void testGetTimeline() {
		Timeline timeline = FeedFixture.getTimline();
		when(repository.getTimeline("user1", 0, 100)).thenReturn(timeline);

		Timeline timelineRead = feedService.getTimeline("user1", 0, 100);

		assertNotNull(timeline);
		assertEquals(timeline, timelineRead);
		
		verify(repository, times(1)).getTimeline("user1", 0, 100);
	}

}
