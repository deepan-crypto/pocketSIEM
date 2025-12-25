package com.pocketsiem.repository;

import com.pocketsiem.model.ThreatReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThreatReportRepository extends JpaRepository<ThreatReport, Long> {
    List<ThreatReport> findByTargetIp(String targetIp);
}
