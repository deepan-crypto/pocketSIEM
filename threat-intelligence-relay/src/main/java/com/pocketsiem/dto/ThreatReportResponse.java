package com.pocketsiem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThreatReportResponse {
    private Long id;
    private String appName;
    private String targetIp;
    private String message;
    private Long createdAt;
}
