package com.pocketsiem.service.impl;

import com.pocketsiem.dto.IpReputationResponse;
import com.pocketsiem.service.ThreatIntelligenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class MockThreatIntelligenceClient implements ThreatIntelligenceClient {
    
    private static final String[] CATEGORIES = {
        "Safe", "Suspicious", "Botnet", "Malware", "C2", "Phishing", "Proxy", "Scanner", "Spam"
    };
    
    // Known malicious IPs for demo purposes
    private static final Map<String, Integer> KNOWN_MALICIOUS_IPS = Map.of(
        "185.220.101.1", 95,   // Known Tor exit node
        "45.33.32.156", 85,    // scanme.nmap.org
        "192.168.1.1", 0,      // Private IP - safe
        "10.0.0.1", 0,         // Private IP - safe
        "8.8.8.8", 5           // Google DNS - safe
    );
    
    private final Random random = new Random();
    
    @Override
    public IpReputationResponse checkIpReputation(String ip) {
        log.info("[MockClient] Checking reputation for IP: {}", ip);
        
        // Check if it's a known IP
        if (KNOWN_MALICIOUS_IPS.containsKey(ip)) {
            int riskScore = KNOWN_MALICIOUS_IPS.get(ip);
            String category = riskScore > 50 ? "Malware" : "Safe";
            return buildResponse(ip, riskScore, category);
        }
        
        // Check for private IP ranges
        if (isPrivateIp(ip)) {
            return buildResponse(ip, 0, "Safe");
        }
        
        // Generate random risk score for unknown IPs
        int riskScore = random.nextInt(101);
        String category = getCategoryForRiskScore(riskScore);
        
        return buildResponse(ip, riskScore, category);
    }
    
    @Override
    public String getProviderName() {
        return "MockThreatIntelligence";
    }
    
    private IpReputationResponse buildResponse(String ip, int riskScore, String category) {
        return IpReputationResponse.builder()
            .ip(ip)
            .riskScore(riskScore)
            .category(category)
            .status("success")
            .cachedAt(System.currentTimeMillis())
            .build();
    }
    
    private String getCategoryForRiskScore(int riskScore) {
        if (riskScore < 20) return "Safe";
        if (riskScore < 40) return "Suspicious";
        if (riskScore < 60) return CATEGORIES[random.nextInt(3) + 2]; // Botnet, Malware, C2
        if (riskScore < 80) return CATEGORIES[random.nextInt(3) + 5]; // Phishing, Proxy, Scanner
        return "Malware";
    }
    
    private boolean isPrivateIp(String ip) {
        return ip.startsWith("10.") || 
               ip.startsWith("192.168.") || 
               ip.startsWith("172.16.") ||
               ip.startsWith("127.");
    }
}
