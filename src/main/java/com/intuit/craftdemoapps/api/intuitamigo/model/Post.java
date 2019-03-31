package com.intuit.craftdemoapps.api.intuitamigo.model;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class Post {

	@ApiModelProperty(example = "1234")
	private String id;
	
	@ApiModelProperty(example = "Hello all Awesome Assessors")
	private String text;
	
	@ApiModelProperty(example = "ani_kadam")
	private String author;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Date postedAt;
	
	@ApiModelProperty
	private Map<String, Link> links;
}
