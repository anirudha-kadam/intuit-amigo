package com.intuit.craftdemoapps.api.intuitamigo.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class Feed {

	@ApiModelProperty(example = "10")
	private Integer index;
	
	@ApiModelProperty(example = "20")
	private Integer limit;
	
	@ApiModelProperty(example = "ani_kadam")
	private String username;
		
	@ApiModelProperty
	private List<Post> posts;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	@ApiModelProperty
	private Date refreshedAt;
	
	@ApiModelProperty
	private Map<String, Link> links;
}
