package com.pocketsiem.service;

import com.pocketsiem.dto.IpReputationResponse;

public interface ThreatIntelligenceClient {
    IpReputationResponse checkIpReputation(String ip);
}
