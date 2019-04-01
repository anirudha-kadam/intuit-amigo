package com.intuit.craftdemoapps.api.intuitamigo.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.intuit.craftdemoapps.api.intuitamigo.model.Post;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("/post")
@Path("/v1/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PostServiceRESTController {

	@POST
	@ApiResponses(@ApiResponse(code = 200, response = Post.class, message = ""))
	Response createPost(Post post);

	@PUT
	@Path("/{postId}")
	@ApiResponses(@ApiResponse(code = 200, response = Post.class, message = ""))
	Response updatePost(
			@ApiParam(value = "Post Identifier", required = true, example = "1234") @PathParam("postId") String postId,
			Post post);

	@DELETE
	@Path("/{postId}")
	@ApiResponses(@ApiResponse(code = 201, message = ""))
	Response deletePost(
			@ApiParam(value = "Post Identifier", required = true, example = "1234") @PathParam("postId") String postId);

	@GET
	@Path("/{postId}")
	@ApiResponses(@ApiResponse(code = 200, response = Post.class, message = ""))
	Response getPost(
			@ApiParam(value = "Post Identifier", required = true, example = "1234") @PathParam("postId") String postId);
}
