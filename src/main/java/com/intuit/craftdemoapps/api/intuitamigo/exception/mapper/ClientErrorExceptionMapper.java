package com.intuit.craftdemoapps.api.intuitamigo.exception.mapper;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.intuit.craftdemoapps.api.intuitamigo.model.Error;

@Component
@Provider
public class ClientErrorExceptionMapper implements ExceptionMapper<ClientErrorException> {

	@Override
	public Response toResponse(ClientErrorException exception) {

		Error error = new Error();
		error.setStatus(Status.fromStatusCode(exception.getResponse().getStatus()));
		error.setMessage(exception.getLocalizedMessage());
		return Response.status(exception.getResponse().getStatus()).entity(error).build();
	}

}
