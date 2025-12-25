package com.pocketsiem.service.impl;

import com.pocketsiem.dto.IpReputationResponse;
import com.pocketsiem.service.ThreatIntelligenceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
public class MockThreatIntelligenceClient implements ThreatIntelligenceClient {

    private static final Logger log = LoggerFactory.getLogger(MockThreatIntelligenceClient.class);
    private static final String[] CATEGORIES = {
        "Safe", "Suspicious", "Botnet", "Malware", "C2", "Phishing", "Proxy", "Scanner", "Spam"
    };

    private static final Map<String, Integer> KNOWN_MALICIOUS_IPS = Map.of(
        "185.220.101.1", 95,
        "45.33.32.156", 85,
        "192.168.1.1", 0,
        "10.0.0.1", 0,
        "8.8.8.8", 5
    );

    private final Random random = new Random();

    @Override
    public IpReputationResponse checkIpReputation(String ip) {
        log.info("[MockClient] Checking reputation for IP: {}", ip);

        if (KNOWN_MALICIOUS_IPS.containsKey(ip)) {
            int riskScore = KNOWN_MALICIOUS_IPS.get(ip);
            String category = riskScore > 50 ? "Malware" : "Safe";
            return buildResponse(ip, riskScore, category);
        }

        if (isPrivateIp(ip)) {
            return buildResponse(ip, 0, "Safe");
        }

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
        if (riskScore < 60) return CATEGORIES[random.nextInt(3) + 2];
        if (riskScore < 80) return CATEGORIES[random.nextInt(3) + 5];
        return "Malware";
    }

    private boolean isPrivateIp(String ip) {
        return ip.startsWith("10.") ||
               ip.startsWith("192.168.") ||
               ip.startsWith("172.16.") ||
               ip.startsWith("127.");
    }
}
