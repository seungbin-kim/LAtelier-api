package com.latelier.api.global.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.OAS_30)
        .useDefaultResponseMessages(false)
        .consumes(getConsumeContentTypes())
        .produces(getProduceContentTypes())
        .apiInfo(getApiInfo())
        .select()
        .apis(RequestHandlerSelectors
            .basePackage("com.latelier.api.domain"))
        .paths(PathSelectors.any())
        .build()
        .securitySchemes(Lists.newArrayList(apiKey()));
  }


  private Set<String> getConsumeContentTypes() {
    Set<String> consumes = new HashSet<>();
    consumes.add("application/json;charset=UTF-8");
    return consumes;
  }


  private Set<String> getProduceContentTypes() {
    Set<String> produces = new HashSet<>();
    produces.add("application/json;charset=UTF-8");
    return produces;
  }


  private ApiInfo getApiInfo() {
    return new ApiInfoBuilder()
        .title("API")
        .description("[Latelier] API")
        .contact(new Contact(
            "Latelier Swagger",
            "https://www.notion.so/396ce7dc3ff04b8faeac3a6e8df8f609",
            "seungbin.kim.dev@gmail.com"))
        .version("1.0")
        .build();
  }


  private ApiKey apiKey() {
    return new ApiKey("Authorization", "JWT", "header");
  }

}
