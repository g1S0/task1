package org.tbank.hw8.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
public class CurrencyRestClientConfiguration {
    @Value("${currency-api.cbr-api}")
    private String apiUrl;

    @Bean
    public HttpMessageConverter<Object> createXmlHttpMessageConverter() {
        return new MappingJackson2XmlHttpMessageConverter();
    }

    @Bean
    public RestClient bankRestClient(HttpMessageConverter<Object> createXmlHttpMessageConverter) {
        return RestClient
                .builder()
                .baseUrl(apiUrl)
                .messageConverters(httpMessageConverters ->
                        httpMessageConverters.add(createXmlHttpMessageConverter))
                .build();
    }
}