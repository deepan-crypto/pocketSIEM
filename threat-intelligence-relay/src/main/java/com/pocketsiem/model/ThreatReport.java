package com.pocketsiem.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "threat_reports", indexes = {
    @Index(name = "idx_target_ip", columnList = "targetIp"),
    @Index(name = "idx_reported_at", columnList = "reportedAt")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
