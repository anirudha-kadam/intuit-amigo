package com.intuit.craftdemoapps.api.intuitamigo.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
public class CxfSwaggerConfigurer {

	@Autowired
	private SwaggerProperties swagger;
	
	@Bean
	public Swagger2Feature swagger2Feature() {
		
		Swagger2Feature result = new Swagger2Feature();
		
		if(StringUtils.isNotBlank(swagger.getTitle())) {
			result.setTitle(swagger.getTitle());
		}
		
		if(StringUtils.isNotBlank(swagger.getDescription())) {
			result.setDescription(swagger.getDescription());
		}
		
		if(StringUtils.isNotBlank(swagger.getBasePath())) {
			result.setBasePath(swagger.getBasePath());
		}
		
		if(StringUtils.isNotBlank(swagger.getVersion())) {
			result.setVersion(swagger.getVersion());
		}
		
		if(StringUtils.isNotBlank(swagger.getPrettyPrint())) {
			result.setPrettyPrint(Boolean.valueOf(swagger.getPrettyPrint()));
		}
		
		if(StringUtils.isNotBlank(swagger.getContact())) {
			result.setContact(swagger.getContact());
		}
		
		if(!CollectionUtils.isEmpty(swagger.getSchemes())) {
			result.setSchemes(swagger.getSchemes().toArray(new String[swagger.getSchemes().size()]));
		} else {
			result.setSchemes(new String[] {"http", "https"});
		}
		
		
		return result;
	}
}
