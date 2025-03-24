package com.kd.liftable.repositories;

import com.kd.liftable.models.Record;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LifterDataRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByName(String name);

    @Query("SELECT l FROM LifterData l WHERE MATCH(l.name) AGAINST (:searchTerm IN NATURAL LANGUAGE MODE)")
    List<Record> searchByName(@Param("searchTerm") String searchTerm);

    List<Record> findAllByFederation(String federation);

    @Query("SELECT l FROM LifterData l WHERE l.federation = :federation ORDER BY l.goodlift DESC")
    List<Record> findTop10ByFederationOrderByGoodliftDesc(String regionName, PageRequest pageable);
}