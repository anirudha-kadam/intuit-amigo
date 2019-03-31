package com.intuit.craftdemoapps.api.intuitamigo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = SwaggerProperties.SWAGGER_PREFIX)
public class SwaggerProperties {

	public static final String SWAGGER_PREFIX = "swagger";
	
	@Value("${cxf.path:/}")
	private String basePath;
	private boolean enabled;
	private String description;
	private String title;
	private String version;
	private String prettyPrint;
	private String contact;
	private List<String> schemes;
	
	
	public void init() {
		if(this.prettyPrint == null) {
			this.prettyPrint = "false";
		}		
	}
}
