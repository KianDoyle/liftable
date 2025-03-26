package com.kd.liftable.repositories;

import com.kd.liftable.models.RegionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegionalRecordRepository extends JpaRepository<RegionRecord, Long> {
    @Query(value = "SELECT * FROM top_lifters_per_federation WHERE Federation = :federation ORDER BY Goodlift DESC", nativeQuery = true)
    List<RegionRecord> findTop10UniqueByFederationOrderedByGoodliftDescRawSbd(String federation);
}