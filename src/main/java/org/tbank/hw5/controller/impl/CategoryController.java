package org.tbank.hw5.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CategoryController {

    @GetMapping("/api/greeting")
    public ResponseEntity<Map<String, String>> getGreeting() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello, World!");
        return ResponseEntity.ok(response);
    }
}
