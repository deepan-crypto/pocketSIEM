package com.pocketsiem.dto;

public class ThreatReportResponse {
    private Long id;
    private String appName;
    private String targetIp;
    private String message;
    private Long createdAt;

    public ThreatReportResponse() {}

    public ThreatReportResponse(Long id, String appName, String targetIp, String message, Long createdAt) {
        this.id = id;
        this.appName = appName;
        this.targetIp = targetIp;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getTargetIp() { return targetIp; }
    public void setTargetIp(String targetIp) { this.targetIp = targetIp; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String appName;
        private String targetIp;
        private String message;
        private Long createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder appName(String appName) { this.appName = appName; return this; }
        public Builder targetIp(String targetIp) { this.targetIp = targetIp; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder createdAt(Long createdAt) { this.createdAt = createdAt; return this; }

        public ThreatReportResponse build() {
            return new ThreatReportResponse(id, appName, targetIp, message, createdAt);
        }
    }
}
