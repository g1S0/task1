package org.tbank.hw5.initializer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


public class WireMockStubManager {

    private static void createStub(String url, String bodyFile) {
        stubFor(get(urlEqualTo(url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile(bodyFile)
                        .withHeader("Content-Type", "application/json")));
    }

    public static void setDefaultCategoryStub() {
        createStub("/place-categories/", "valid_category.json");
    }

    public static void setDefaultLocationStub() {
        createStub("/locations/", "valid_location.json");
    }

    public static void setInvalidLocationStub() {
        createStub("/locations/", "invalid_location.json");
    }

    public static void setInvalidCategoryStub() {
        createStub("/place-categories/", "invalid_category.json");
    }
}
