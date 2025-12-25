package com.pocketsiem.controller;

import com.pocketsiem.dto.IpReputationResponse;
import com.pocketsiem.dto.ThreatReportRequest;
import com.pocketsiem.dto.ThreatReportResponse;
import com.pocketsiem.service.ThreatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ThreatController {
    
    private final ThreatService threatService;
    
    /**
     * GET /api/v1/reputation?ip={ip_address}
     * Check IP reputation against threat intelligence databases.
     * Returns JSON with risk score (0-100) and category.
     */
    @GetMapping("/reputation")
    public ResponseEntity<IpReputationResponse> checkIpReputation(
            @RequestParam(name = "ip") String ip) {
        
        log.info("Received reputation check request for IP: {}", ip);
        
        if (!isValidIp(ip)) {
            log.warn("Invalid IP format: {}", ip);
            return ResponseEntity.badRequest().body(
                IpReputationResponse.builder()
                    .ip(ip)
                    .status("error")
                    .category("Invalid IP format")
                    .riskScore(-1)
                    .build()
            );
        }
        
        IpReputationResponse response = threatService.getIpReputation(ip);
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /api/v1/report
     * Submit crowdsourced threat report.
     * Accepts JSON payload with App Name, Target IP, Timestamp.
     */
    @PostMapping("/report")
    public ResponseEntity<ThreatReportResponse> reportThreat(
            @Valid @RequestBody ThreatReportRequest request) {
        
        log.info("Received threat report for IP: {} from app: {}", 
                 request.getTargetIp(), request.getAppName());
        
        ThreatReportResponse response = threatService.recordThreatReport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * GET /api/v1/health
     * Health check endpoint (public).
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "PocketSIEM Threat Intelligence Relay",
            "timestamp", System.currentTimeMillis()
        ));
    }
    
    /**
     * GET /api/v1/cache/stats
     * Get cache statistics (admin endpoint).
     */
    @GetMapping("/cache/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        return ResponseEntity.ok(threatService.getCacheStats());
    }
    
    /**
     * DELETE /api/v1/cache
     * Clear the reputation cache (admin endpoint).
     */
    @DeleteMapping("/cache")
    public ResponseEntity<Map<String, String>> clearCache() {
        threatService.clearCache();
        return ResponseEntity.ok(Map.of("message", "Cache cleared successfully"));
    }
    
    private boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        String ipv4Pattern = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipv4Pattern);
    }
}
