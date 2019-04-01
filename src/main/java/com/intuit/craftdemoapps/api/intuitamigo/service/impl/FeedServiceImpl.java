package com.intuit.craftdemoapps.api.intuitamigo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.intuit.craftdemoapps.api.intuitamigo.dao.IntuitAmigoRepository;
import com.intuit.craftdemoapps.api.intuitamigo.model.Feed;
import com.intuit.craftdemoapps.api.intuitamigo.model.Timeline;
import com.intuit.craftdemoapps.api.intuitamigo.service.FeedService;
import com.intuit.craftdemoapps.api.intuitamigo.util.UserSessionUtil;

@Component
public class FeedServiceImpl implements FeedService {

	private IntuitAmigoRepository repository;

	@Autowired
	public FeedServiceImpl(IntuitAmigoRepository repository) {
		this.repository = repository;
	}
	

	@Override
	public Feed getFeed(Integer index, Integer limit) {
		return repository.getFeed(UserSessionUtil.getUsername(), index, limit);
	}

	@Override
	public Timeline getTimeline(String username, Integer index, Integer limit) {
		return repository.getTimeline(username, index, limit);
	}

}
