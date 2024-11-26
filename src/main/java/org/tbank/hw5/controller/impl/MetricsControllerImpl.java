package org.tbank.hw5.controller.impl;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsControllerImpl {

    private final MeterRegistry meterRegistry;

    public MetricsControllerImpl(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/count")
    public String incrementCounter() {
        meterRegistry.counter("custom_metric", "type", "requests").increment();
        return "Request received!";
    }
}
