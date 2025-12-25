package com.pocketsiem.repository;

import com.pocketsiem.model.ThreatReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ThreatReportRepository extends JpaRepository<ThreatReport, Long> {
    
    List<ThreatReport> findByTargetIp(String targetIp);
    
    List<ThreatReport> findByAppName(String appName);
    
    List<ThreatReport> findByReportedAtAfter(LocalDateTime after);
    
    @Query("SELECT COUNT(t) FROM ThreatReport t WHERE t.targetIp = :ip")
    long countByTargetIp(String ip);
    
    @Query("SELECT t FROM ThreatReport t WHERE t.riskScore >= :minScore")
    List<ThreatReport> findHighRiskReports(Integer minScore);
}
