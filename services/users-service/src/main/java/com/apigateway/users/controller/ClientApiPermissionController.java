package com.apigateway.users.controller;

import com.apigateway.users.dto.request.ClientApiPermissionRequestDTO;
import com.apigateway.users.dto.response.ClientApiPermissionResponseDTO;
import com.apigateway.users.logic.ClientApiPermissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permissions")
public class ClientApiPermissionController {

    private final ClientApiPermissionService service;

    public ClientApiPermissionController(ClientApiPermissionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientApiPermissionResponseDTO grant(@Valid @RequestBody ClientApiPermissionRequestDTO dto) {
        return service.grant(dto);
    }

    @GetMapping("/client/{clientId}")
    public List<ClientApiPermissionResponseDTO> findByClient(@PathVariable Long clientId) {
        return service.findByClientId(clientId);
    }

    @GetMapping("/check")
    public Map<String, Boolean> check(@RequestParam Long clientId, @RequestParam String apiCode) {
        return Map.of("allowed", service.hasPermission(clientId, apiCode));
    }

    @DeleteMapping("/client/{clientId}/api/{apiCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revoke(@PathVariable Long clientId, @PathVariable String apiCode) {
        service.revoke(clientId, apiCode);
    }
}