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
    private Integer riskScore;
    private String category;
    private String status;
    private Long cachedAt;
}
