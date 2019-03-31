package com.intuit.craftdemoapps.api.intuitamigo.config;

import java.io.IOException;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Configuration
public class RedissonClientConfigurer {

	@Bean
	public RedissonClient redissonClient() throws IOException {
		Config config = Config.fromJSON(ResourceUtils.getFile("classpath:singleNodeConfig.json"));
		RedissonClient client = Redisson.create(config);
		return client;
	}
}
