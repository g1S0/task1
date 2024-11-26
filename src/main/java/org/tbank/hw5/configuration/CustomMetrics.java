package org.tbank.hw5.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    @Autowired
    public CustomMetrics(MeterRegistry meterRegistry) {
        meterRegistry.counter("custom_metric", "type", "requests");
    }
}
