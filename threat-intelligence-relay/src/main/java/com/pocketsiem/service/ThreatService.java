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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreatService {
    
    private final ThreatIntelligenceClient threatIntelligenceClient;
    private final ThreatReportRepository threatReportRepository;
    
    // Simple in-memory cache with TTL tracking
    private final Map<String, CachedReputation> reputationCache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL_MS = 3600000; // 1 hour
    
    /**
     * Check IP reputation with caching to avoid external API spam.
     * Uses both Spring @Cacheable and manual HashMap for demonstration.
     */
    @Cacheable(value = "ipReputation", key = "#ip")
    public IpReputationResponse getIpReputation(String ip) {
        log.info("Checking reputation for IP: {}", ip);
        
        // Check manual cache first
        CachedReputation cached = reputationCache.get(ip);
        if (cached != null && !cached.isExpired()) {
            log.debug("Cache hit for IP: {}", ip);
            return cached.response;
        }
        
        // Fetch from external source
        log.info("Cache miss - fetching from external source for IP: {}", ip);
        IpReputationResponse response = threatIntelligenceClient.checkIpReputation(ip);
        
        // Store in manual cache
        reputationCache.put(ip, new CachedReputation(response));
        
        return response;
    }
    
    /**
     * Store a crowdsourced threat report.
     */
    public ThreatReportResponse recordThreatReport(ThreatReportRequest request) {
        log.info("Recording threat report for IP: {} from app: {}", 
                 request.getTargetIp(), request.getAppName());
        
        // Convert Unix timestamp to LocalDateTime
        LocalDateTime reportedAt = Instant.ofEpochMilli(request.getTimestamp())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        
        // Get reputation for the reported IP
        IpReputationResponse reputation = getIpReputation(request.getTargetIp());
        
        // Create and save entity
        ThreatReport report = ThreatReport.builder()
            .appName(request.getAppName())
            .targetIp(request.getTargetIp())
            .reportedAt(reportedAt)
            .category(reputation != null ? reputation.getCategory() : "Unknown")
            .riskScore(reputation != null ? reputation.getRiskScore() : null)
            .build();
        
        ThreatReport savedReport = threatReportRepository.save(report);
        
        log.info("Threat report saved with ID: {}", savedReport.getId());
        
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
    
    /**
     * Clear the reputation cache (for testing/admin purposes).
     */
    public void clearCache() {
        reputationCache.clear();
        log.info("Reputation cache cleared");
    }
    
    /**
     * Get cache statistics.
     */
    public Map<String, Object> getCacheStats() {
        long validEntries = reputationCache.values().stream()
            .filter(c -> !c.isExpired())
            .count();
        
        return Map.of(
            "totalEntries", reputationCache.size(),
            "validEntries", validEntries,
            "expiredEntries", reputationCache.size() - validEntries
        );
    }
    
    // Inner class for cache entries with TTL
    private static class CachedReputation {
        final IpReputationResponse response;
        final long timestamp;
        
        CachedReputation(IpReputationResponse response) {
            this.response = response;
            this.timestamp = System.currentTimeMillis();
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_TTL_MS;
        }
    }
}
