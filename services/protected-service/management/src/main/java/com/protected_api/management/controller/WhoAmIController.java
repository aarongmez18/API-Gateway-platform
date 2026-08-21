package com.protected_api.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/protected")
public class WhoAmIController {

    @GetMapping("/whoami")
    public Map<String, String> whoAmI(@RequestHeader("X-Client-ID") String clientId, @RequestHeader("X-Client-Name") String clientName) {
        return Map.of("clientId", clientId, "clientName", clientName);
    }
}