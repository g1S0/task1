package org.tbank.hw5.controller.impl;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/stack-overflow")
    public String triggerStackOverflow() {
        recursiveMethod();
        return "This will never be reached";
    }

    private void recursiveMethod() {
        recursiveMethod();
    }

    @GetMapping("/out-of-memory")
    public String triggerOutOfMemory() {
        List<int[]> list = new ArrayList<>();
        while (true) {
            int[] array = new int[1000000];
            list.add(array);
        }
    }
}
