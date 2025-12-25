package com.pocketsiem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IpReputationResponse {
    private String ip;
    private Integer riskScore;      // 0-100 risk score
    private String category;         // e.g., "Botnet", "Safe", "Malware"
    private String status;           // "success" or "error"
    private Long cachedAt;           // Timestamp when cached
    private String provider;         // Threat intelligence provider name
}
