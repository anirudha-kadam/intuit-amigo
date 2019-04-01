package com.intuit.craftdemoapps.api.intuitamigo.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.intuit.craftdemoapps.api.intuitamigo.model.Feed;
import com.intuit.craftdemoapps.api.intuitamigo.model.Timeline;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("/feed")
@Path("/v1/feed")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface FeedServiceRESTController {


	@GET
	@ApiResponses(@ApiResponse(code = 200, response = Feed.class, message = ""))
	Response getFeed(@ApiParam(value = "Page Index", required = true, example = "0") @QueryParam("index") Integer index,
			@ApiParam(value = "Page Size", required = true, example = "100") @QueryParam("limit") Integer limit);
	
	@GET
	@Path("/{username}")
	@ApiResponses(@ApiResponse(code = 200, response = Timeline.class, message = ""))
	Response getTimeline(
			@ApiParam(value = "User's Identifier", required = true, example = "ani_kadam") @PathParam("username") String username,
			@ApiParam(value = "Page Index", required = true, example = "0") @QueryParam("index") Integer index,
			@ApiParam(value = "Page Size", required = true, example = "100") @QueryParam("limit") Integer limit);

}
