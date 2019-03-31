package com.intuit.craftdemoapps.api.intuitamigo.webservice;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.intuit.craftdemoapps.api.intuitamigo.model.LoginRequest;
import com.intuit.craftdemoapps.api.intuitamigo.model.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("/profile")
@Path("/v1/profiles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ProfileServiceRESTController {

	@GET
	@Path("/{username}")
	@ApiResponses(@ApiResponse(code = 200, response = User.class, message = ""))
	Response getProfile(
			@ApiParam(value = "User's Identifier", required = true, example = "ani_kadam") @PathParam("username") String username);

	@PUT
	@Path("/{username}/follow")
	@ApiResponses(@ApiResponse(code = 200, message = ""))
	Response follow(
			@ApiParam(value = "User's Identifier", required = true, example = "ani_kadam") @PathParam("username") String username);

	@PUT
	@Path("/{username}/unfollow")
	@ApiResponses(@ApiResponse(code = 200, message = ""))
	Response unfollow(
			@ApiParam(value = "User's Identifier", required = true, example = "ani_kadam") @PathParam("username") String username);

	@GET
	@Path("/{username}/followers")
	@ApiResponses(@ApiResponse(code = 200, response = List.class, message = ""))
	Response getFollowers(
			@ApiParam(value = "User's Identifier", required = true, example = "ani_kadam") @PathParam("username") String username);

	@GET
	@Path("/{username}/following")
	@ApiResponses(@ApiResponse(code = 200, response = List.class, message = ""))
	Response getFollowing(
			@ApiParam(value = "User's Identifier", required = true, example = "ani_kadam") @PathParam("username") String username);

	@POST
	@Path("/signup")
	@ApiResponses(@ApiResponse(code = 200, response = User.class, message = ""))
	Response signup(User user);

	@POST
	@Path("/login")
	Response login(LoginRequest logingRequest);
	
	@POST
	@Path("/logout")
	Response logout();

}
