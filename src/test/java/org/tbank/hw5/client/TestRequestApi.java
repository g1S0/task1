package org.tbank.hw5.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.tbank.hw5.client.model.TestResponse;

import java.util.List;

@Component
public class TestRequestApi extends ApiClient<TestResponse> {
    private final String baseUrl = "http://localhost:8081";

    private String getApiUrl() {
        return baseUrl + "/api/myresponse";
    }

    public TestRequestApi(RestClient restClient) {
        super(restClient);
    }

    public List<TestResponse> fetch() {
        return fetchFromApi(getApiUrl(), TestResponse[].class);
    }
}