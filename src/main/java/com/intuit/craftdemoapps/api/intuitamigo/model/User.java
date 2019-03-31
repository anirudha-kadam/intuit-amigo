package com.intuit.craftdemoapps.api.intuitamigo.model;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class User {

	@ApiModelProperty(example = "1234")
	private String id;
	
	@ApiModelProperty(example = "ani_kadam")
	private String username;
	
	@ApiModelProperty(example = "secret")
	private String password;
	
	@ApiModelProperty(example = "Anirudha Kadam")
	private String name;
	
	@ApiModelProperty(example = "2018-08-11")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date memberSince;
	
	@ApiModelProperty(example = "2345")
	private Integer followers;
	
	@ApiModelProperty(example = "123")
	private Integer following;
	
	@ApiModelProperty
	private Map<String, Link> links;
}
