package com.intuit.craftdemoapps.api.intuitamigo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class Link {

	@ApiModelProperty(example = "https://mydomain.com/myservice/api/users/1234")
	private String href;
	
	public Link(String link) {
		this.href = link;
	}
}
