package org.tbank.hw5.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.tbank.hw5.client.model.TestResponse;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest(httpPort = 8081)
@SpringBootTest
class ApiClientTest {
    @Autowired
    private TestRequestApi apiClient;

    @Test
    public void testFetchFromApi() throws InterruptedException {
        WireMock.stubFor(WireMock.get(urlEqualTo("/api/myresponse"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("[{\"data\":\"data1\"},{\"data\":\"data2\"}]")
                        .withHeader("Content-Type", "application/json")));

        int numRequests = 10;
        CountDownLatch latch = new CountDownLatch(numRequests);
        ExecutorService executor = Executors.newFixedThreadPool(numRequests);
        List<List<TestResponse>> results = new CopyOnWriteArrayList<>();

        for (int i = 0; i < numRequests; i++) {
            executor.submit(() -> {
                List<TestResponse> response = apiClient.fetch();
                results.add(response);
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(numRequests, results.size());
        for (List<TestResponse> response : results) {
            assertEquals(2, response.size());
            assertEquals("data1", response.get(0).getData());
            assertEquals("data2", response.get(1).getData());
        }
    }

    @Test
    public void testFetchFromApiWithDelay() throws InterruptedException {
        WireMock.stubFor(WireMock.get(urlEqualTo("/api/myresponse"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("[{\"data\":\"data1\"},{\"data\":\"data2\"}]")
                        .withHeader("Content-Type", "application/json")
                        .withFixedDelay(1000)));

        int numRequests = 3;
        CountDownLatch latch = new CountDownLatch(numRequests);
        ExecutorService executor = Executors.newFixedThreadPool(numRequests);
        List<List<TestResponse>> results = new CopyOnWriteArrayList<>();
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numRequests; i++) {
            executor.submit(() -> {
                List<TestResponse> response = apiClient.fetch();
                results.add(response);
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertEquals(numRequests, results.size());
        for (List<TestResponse> response : results) {
            assertEquals(2, response.size());
            assertEquals("data1", response.get(0).getData());
            assertEquals("data2", response.get(1).getData());
        }

        assertTrue(duration >= 2000);
    }
}