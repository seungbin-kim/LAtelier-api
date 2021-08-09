package com.latelier.api.global.config.resttemplate;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    factory.setConnectTimeout(5000);
    factory.setReadTimeout(5000);

    CloseableHttpClient client = HttpClientBuilder.create()
        .setMaxConnTotal(50)
        .setMaxConnPerRoute(20)
        .build();
    factory.setHttpClient(client);

    RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(factory));
    restTemplate.setInterceptors(Collections.singletonList(new RestTemplateClientHttpRequestInterceptor()));
    return restTemplate;
  }

}
