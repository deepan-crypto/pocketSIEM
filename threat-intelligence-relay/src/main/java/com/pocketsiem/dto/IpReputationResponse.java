package com.pocketsiem.dto;

public class IpReputationResponse {
    private String ip;
    private Integer riskScore;
    private String category;
    private String status;
    private Long cachedAt;
    private String provider;

    public IpReputationResponse() {}

    public IpReputationResponse(String ip, Integer riskScore, String category, String status, Long cachedAt, String provider) {
        this.ip = ip;
        this.riskScore = riskScore;
        this.category = category;
        this.status = status;
        this.cachedAt = cachedAt;
        this.provider = provider;
    }

    // Getters and Setters
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCachedAt() { return cachedAt; }
    public void setCachedAt(Long cachedAt) { this.cachedAt = cachedAt; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String ip;
        private Integer riskScore;
        private String category;
        private String status;
        private Long cachedAt;
        private String provider;

        public Builder ip(String ip) { this.ip = ip; return this; }
        public Builder riskScore(Integer riskScore) { this.riskScore = riskScore; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder cachedAt(Long cachedAt) { this.cachedAt = cachedAt; return this; }
        public Builder provider(String provider) { this.provider = provider; return this; }

        public IpReputationResponse build() {
            return new IpReputationResponse(ip, riskScore, category, status, cachedAt, provider);
        }
    }
}
