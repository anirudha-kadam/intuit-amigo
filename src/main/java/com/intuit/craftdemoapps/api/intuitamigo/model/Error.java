package com.intuit.craftdemoapps.api.intuitamigo.model;

import javax.ws.rs.core.Response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class Error {

	private Response.Status status;
	private String message;
}
