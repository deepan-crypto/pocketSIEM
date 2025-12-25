package com.pocketsiem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ThreatReportRequest {

    @NotBlank(message = "App name cannot be blank")
    @Size(min = 1, max = 255)
    private String appName;

    @NotBlank(message = "Target IP cannot be blank")
    @Pattern(regexp = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$", message = "Invalid IP address format")
    private String targetIp;

    @NotNull(message = "Timestamp cannot be null")
    private Long timestamp;

    public ThreatReportRequest() {}

    public ThreatReportRequest(String appName, String targetIp, Long timestamp) {
        this.appName = appName;
        this.targetIp = targetIp;
        this.timestamp = timestamp;
    }

    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getTargetIp() { return targetIp; }
    public void setTargetIp(String targetIp) { this.targetIp = targetIp; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
