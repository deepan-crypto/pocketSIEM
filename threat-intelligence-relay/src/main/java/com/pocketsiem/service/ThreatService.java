package com.pocketsiem.service;

import com.pocketsiem.dto.IpReputationResponse;
import com.pocketsiem.dto.ThreatReportRequest;
import com.pocketsiem.dto.ThreatReportResponse;
import com.pocketsiem.model.ThreatReport;
import com.pocketsiem.repository.ThreatReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreatService {
    
    private final ThreatIntelligenceClient threatIntelligenceClient;
    private final ThreatReportRepository threatReportRepository;
    
    @Cacheable(value = "ipReputation", key = "#ip")
    public IpReputationResponse getIpReputation(String ip) {
        log.info("Fetching reputation for IP: {} from external source", ip);
        return threatIntelligenceClient.checkIpReputation(ip);
    }
    
    public ThreatReportResponse recordThreatReport(ThreatReportRequest request) {
        log.info("Recording threat report for IP: {} from app: {}", 
                 request.getTargetIp(), request.getAppName());
        
        LocalDateTime reportedAt = Instant.ofEpochMilli(request.getTimestamp())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        
        ThreatReport report = ThreatReport.builder()
            .appName(request.getAppName())
            .targetIp(request.getTargetIp())
            .reportedAt(reportedAt)
            .build();
        
        ThreatReport savedReport = threatReportRepository.save(report);
        
        return ThreatReportResponse.builder()
            .id(savedReport.getId())
            .appName(savedReport.getAppName())
            .targetIp(savedReport.getTargetIp())
            .message("Threat report recorded successfully")
            .createdAt(savedReport.getCreatedAt()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli())
            .build();
    }
}
