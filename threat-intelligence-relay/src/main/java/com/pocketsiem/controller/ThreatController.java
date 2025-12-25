package com.pocketsiem.controller;

import com.pocketsiem.dto.IpReputationResponse;
import com.pocketsiem.dto.ThreatReportRequest;
import com.pocketsiem.dto.ThreatReportResponse;
import com.pocketsiem.service.ThreatService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ThreatController {

    private static final Logger log = LoggerFactory.getLogger(ThreatController.class);
    private final ThreatService threatService;

    public ThreatController(ThreatService threatService) {
        this.threatService = threatService;
    }

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

    private boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        String ipv4Pattern = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipv4Pattern);
    }
}
