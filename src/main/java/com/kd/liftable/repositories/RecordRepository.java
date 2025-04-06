package com.kd.liftable.repositories;

import com.kd.liftable.models.Record;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    // Find records by name ordered by date (with pagination)
    @Query(value = "SELECT * FROM lifter_data WHERE name = :name ORDER BY date DESC",
            countQuery = "SELECT COUNT(*) FROM lifter_data WHERE name = :name",
            nativeQuery = true)
    Page<Record> findAllByNameOrderedByDateDesc(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT * FROM lifter_data WHERE name = :name ORDER BY date ASC",
            countQuery = "SELECT COUNT(*) FROM lifter_data WHERE name = :name",
            nativeQuery = true)
    Page<Record> findAllByNameOrderedByDateAsc(@Param("name") String name, Pageable pageable);

    // Find best lifts for a lifter with specific equipment
    @Query(value = "SELECT MAX(best3squatkg) as best_squat, MAX(best3benchkg) as best_bench, " +
            "MAX(best3deadliftkg) as best_deadlift, MAX(totalkg) as best_total, " +
            "MAX(goodlift) as best_goodlift " +
            "FROM lifter_data WHERE name = :name AND equipment = :equip",
            nativeQuery = true)
    List<Object[]> findLargestStats(@Param("name") String name, @Param("equip") String equip);

    // Search lifter with filters
    @Query(value = "SELECT * FROM lifter_data WHERE name = :name",
            nativeQuery = true)
    Page<Record> searchLifterRecords(
            @Param("name") String name,
            @Param("event") String event,
            @Param("equipment") String equipment,
            @Param("weightClass") String weightClass,
            @Param("ageClass") String ageClass,
            @Param("federation") String federation,
            Pageable pageable);

    @Query(value = "SELECT * FROM "+
                "(SELECT *, "+
                "ROW_NUMBER() OVER (PARTITION BY name ORDER BY goodlift DESC) AS rn "+
                "FROM lifter_data WHERE event = :event AND equipment = :equipment " +
                "AND goodlift IS NOT NULL) " +
            "ranked WHERE rn = 1",
            countQuery = "SELECT COUNT(*) "+
                    "FROM ( "+
                    "SELECT *, "+
                    "ROW_NUMBER() OVER (PARTITION BY name ORDER BY goodlift DESC) AS rn "+
                    "FROM lifter_data WHERE event = :event AND equipment = :equipment"+
                    ") ranked "+
                    "WHERE rn = 1 ",
            nativeQuery = true)
    Page<Record> searchLifters(
            @Param("event") String event,
            @Param("equipment") String equipment,
            @Param("weightClass") String weightClass,
            @Param("ageClass") String ageClass,
            @Param("federation") String federation,
            Pageable pageable);

    // Get unique values for filter dropdowns
    @Query(value = "SELECT DISTINCT weightclasskg FROM lifter_data WHERE weightclasskg IS NOT NULL ORDER BY weightclasskg",
            nativeQuery = true)
    List<String> findAllWeightClasses();

    @Query(value = "SELECT DISTINCT federation FROM lifter_data WHERE federation IS NOT NULL ORDER BY federation",
            nativeQuery = true)
    List<String> findAllFederations();

    @Query(value = "SELECT DISTINCT ageclass FROM lifter_data WHERE ageclass IS NOT NULL ORDER BY ageclass",
            nativeQuery = true)
    List<String> findAllAgeClasses();

    @Query(value = "SELECT DISTINCT ON (name) FROM lifter_data WHERE to_tsvector(name) @@ to_tsquery(:name)", nativeQuery = true)
    ArrayList<Record> searchLifterNames(String name);

    @Query(value = "SELECT DISTINCT ON (name) name FROM lifter_data ORDER BY name", nativeQuery = true)
    ArrayList<String> findDistinctNames();

    @Query(value = "SELECT * FROM "+
            "(SELECT *, "+
            "ROW_NUMBER() OVER (PARTITION BY name ORDER BY goodlift DESC) AS rn "+
            "FROM lifter_data WHERE event = :event AND equipment = :equipment " +
            "AND goodlift IS NOT NULL) " +
            "ranked WHERE rn = 1", nativeQuery = true)
    List<Record> findCachableLifters(@Param("event") String event, @Param("equipment") String equipment);

    @Query(value = "SELECT * FROM lifter_data WHERE event = 'SBD' AND equipment = 'Raw' AND goodlift IS NOT NULL ORDER BY goodlift DESC", nativeQuery = true)
    List<Record> findAllSBDRawOrderedByGoodlift();

    @Query(value = "SELECT * FROM lifter_data WHERE event = 'SBD' AND equipment = 'Single-ply' AND goodlift IS NOT NULL ORDER BY goodlift DESC", nativeQuery = true)
    List<Record> findAllSBDSinglePlyOrderedByGoodlift();

    @Query(value = "SELECT * FROM lifter_data WHERE event = 'B' AND equipment = 'Raw' AND goodlift IS NOT NULL ORDER BY goodlift DESC", nativeQuery = true)
    List<Record> findAllBRawOrderedByGoodlift();

    @Query(value = "SELECT * FROM lifter_data WHERE event = 'B' AND equipment = 'Single-ply' AND goodlift IS NOT NULL ORDER BY goodlift DESC", nativeQuery = true)
    List<Record> findAllBSinglePlyOrderedByGoodlift();
}
