package com.pocketsiem.service;

import com.pocketsiem.dto.IpReputationResponse;

/**
 * Interface for external threat intelligence API clients.
 * Implementations can integrate with AbuseIPDB, VirusTotal, etc.
 */
public interface ThreatIntelligenceClient {
    
    /**
     * Check IP reputation against external threat intelligence database.
     * @param ip The IP address to check
     * @return IpReputationResponse containing risk score and category
     */
    IpReputationResponse checkIpReputation(String ip);
    
    /**
     * Get the name of the threat intelligence provider.
     * @return Provider name
     */
    default String getProviderName() {
        return "Unknown";
    }
}
