package com.pocketsiem.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "threat_reports")
public class ThreatReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String appName;

    @Column(nullable = false, length = 50)
    private String targetIp;

    @Column(nullable = false)
    private LocalDateTime reportedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(length = 100)
    private String category;

    @Column
    private Integer riskScore;

    public ThreatReport() {}

    public ThreatReport(Long id, String appName, String targetIp, LocalDateTime reportedAt, 
                        LocalDateTime createdAt, String category, Integer riskScore) {
        this.id = id;
        this.appName = appName;
        this.targetIp = targetIp;
        this.reportedAt = reportedAt;
        this.createdAt = createdAt;
        this.category = category;
        this.riskScore = riskScore;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getTargetIp() { return targetIp; }
    public void setTargetIp(String targetIp) { this.targetIp = targetIp; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String appName;
        private String targetIp;
        private LocalDateTime reportedAt;
        private LocalDateTime createdAt;
        private String category;
        private Integer riskScore;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder appName(String appName) { this.appName = appName; return this; }
        public Builder targetIp(String targetIp) { this.targetIp = targetIp; return this; }
        public Builder reportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder riskScore(Integer riskScore) { this.riskScore = riskScore; return this; }

        public ThreatReport build() {
            return new ThreatReport(id, appName, targetIp, reportedAt, createdAt, category, riskScore);
        }
    }
}
