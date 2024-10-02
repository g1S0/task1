package org.tbank.hw5.initializer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.utility.MountableFile;
import org.wiremock.integrations.testcontainers.WireMockContainer;

public class WireMockManager {
    static WireMockContainer createContainer() {
        return new WireMockContainer("wiremock/wiremock:3.6.0")
                .withCopyFileToContainer(
                        MountableFile.forHostPath("src/test/resources/wiremock/json"),
                        "/home/wiremock/./__files");
    }

    static void setPropertiesPath(DynamicPropertyRegistry registry, WireMockContainer wireMock) {
        registry.add("kudago.api.base-url", () -> String.format("http://%s:%d",
                wireMock.getHost(), wireMock.getFirstMappedPort()));

    }
}
