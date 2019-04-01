package com.intuit.craftdemoapps.api.intuitamigo.service;

import com.intuit.craftdemoapps.api.intuitamigo.model.Feed;
import com.intuit.craftdemoapps.api.intuitamigo.model.Timeline;

public interface FeedService {

	Feed getFeed(Integer index, Integer limit);
	
	Timeline getTimeline(String username, Integer index, Integer limit);
}
