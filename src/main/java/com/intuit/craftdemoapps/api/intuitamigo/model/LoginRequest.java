package com.intuit.craftdemoapps.api.intuitamigo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class LoginRequest {

	@ApiModelProperty
	private String username;
	
	@ApiModelProperty
	private String password;
}
