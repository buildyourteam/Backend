package com.eskiiimo.web;


import com.eskiiimo.web.configs.FileUploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan("com.eskiiimo.repository")
@EnableJpaRepositories("com.eskiiimo.repository")
@EnableConfigurationProperties(FileUploadProperties.class)
@EnableAspectJAutoProxy
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}

