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

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ThreatController {
    
    private final ThreatService threatService;
    
    @GetMapping("/reputation")
    public ResponseEntity<IpReputationResponse> checkIpReputation(
            @RequestParam(name = "ip") String ip) {
        
        log.info("Received reputation check request for IP: {}", ip);
        
        if (!isValidIp(ip)) {
            return ResponseEntity.badRequest().build();
        }
        
        IpReputationResponse response = threatService.getIpReputation(ip);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/report")
    public ResponseEntity<ThreatReportResponse> reportThreat(
            @Valid @RequestBody ThreatReportRequest request) {
        
        log.info("Received threat report for IP: {} from app: {}", 
                 request.getTargetIp(), request.getAppName());
        
        ThreatReportResponse response = threatService.recordThreatReport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("PocketSIEM backend is running");
    }
    
    private boolean isValidIp(String ip) {
        String ipv4Pattern = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$";
        return ip != null && ip.matches(ipv4Pattern);
    }
}
