package com.intuit.craftdemoapps.api.intuitamigo.webservice.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.intuit.craftdemoapps.api.intuitamigo.model.Link;
import com.intuit.craftdemoapps.api.intuitamigo.service.FeedService;
import com.intuit.craftdemoapps.api.intuitamigo.util.HateoasUtil;
import com.intuit.craftdemoapps.api.intuitamigo.util.UserSessionUtil;
import com.intuit.craftdemoapps.api.intuitamigo.webservice.FeedServiceRESTController;

@Component
public class FeedServiceRESTControllerImpl implements FeedServiceRESTController {

	private FeedService feedService;
	
	@Autowired
	public FeedServiceRESTControllerImpl(FeedService feedService) {
		this.feedService = feedService;
	}
	
	@Override
	public Response getFeed(Integer index, Integer limit) {
		
		index = index != null ? index : 0;
		limit = limit != null ? limit : 100;
		
		Map<String, Link> links = new HashMap<>(2);
		links.put("timeline", HateoasUtil.getTimelineLink(UserSessionUtil.getUsername()));
		links.put("profile", HateoasUtil.getProfileLink(UserSessionUtil.getUsername()));

		return Optional.ofNullable(feedService.getFeed(index, limit))
				.map(f -> {
					f.setLinks(links);
					return f;
				})
				.map(Response::ok)
				.map(ResponseBuilder::build)
				.orElseThrow(() -> new InternalServerErrorException("Feed returned is null. Something went wrong"));
	}

	
	
	@Override
	public Response getTimeline(String username, Integer index, Integer limit) {
		
		index = index != null ? index : 0;
		limit = limit != null ? limit : 100;
		
		Map<String, Link> links = new HashMap<>(2);
		links.put("self", HateoasUtil.getSelfLink());
		links.put("profile", HateoasUtil.getProfileLink(UserSessionUtil.getUsername()));
		
		return Optional.ofNullable(feedService.getTimeline(username, index, limit))
				.map(t -> {
					t.setLinks(links);
					return t;
				})
				.map(Response::ok)
				.map(ResponseBuilder::build)
				.orElseThrow(() -> new InternalServerErrorException("Timeline returned is null. Something went wrong"));
	}

}
