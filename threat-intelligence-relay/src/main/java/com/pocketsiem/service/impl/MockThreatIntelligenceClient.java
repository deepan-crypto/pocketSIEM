package com.pocketsiem.service.impl;

import com.pocketsiem.dto.IpReputationResponse;
import com.pocketsiem.service.ThreatIntelligenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Random;

@Slf4j
@Service
public class MockThreatIntelligenceClient implements ThreatIntelligenceClient {
    
    private static final String[] CATEGORIES = {
        "Safe", "Suspicious", "Botnet", "Malware", "C2", "Phishing", "Proxy"
    };
    
    private final Random random = new Random();
    
    @Override
    public IpReputationResponse checkIpReputation(String ip) {
        log.info("Checking reputation for IP: {}", ip);
        
        int riskScore = random.nextInt(101);
        String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
        
        return IpReputationResponse.builder()
            .ip(ip)
            .riskScore(riskScore)
            .category(category)
            .status("success")
            .cachedAt(System.currentTimeMillis())
            .build();
    }
}
