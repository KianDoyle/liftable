package com.kd.liftable.repositories;

import com.kd.liftable.models.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    // Find top 20 lifters by goodlift score (corrected query)
    @Query(value = "SELECT * FROM lifter_data " +
            "WHERE goodlift IS NOT NULL AND event = 'SBD' AND equipment = 'Raw' ORDER BY goodlift DESC LIMIT 20",
            nativeQuery = true)
    List<Record> findTopLiftersByGoodlift();

    // Search lifters with filters (with pagination)
    @Query(value = "SELECT * FROM lifter_data WHERE " +
            "(:name IS NOT NULL OR LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:weightClass IS NOT NULL OR weightclasskg = :weightClass) AND " +
            "(:gender IS NOT NULL OR sex = :gender) AND " +
            "(:ageClass IS NOT NULL OR ageclass = :ageClass) AND " +
            "(:federation IS NOT NULL OR federation = :federation) AND " +
            "goodlift IS NOT NULL ORDER BY goodlift DESC",
            countQuery = "SELECT COUNT(*) FROM lifter_data WHERE " +
                    "(:name IS NOT NULL OR LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
                    "(:weightClass IS NOT NULL OR weightclasskg = :weightClass) AND " +
                    "(:gender IS NOT NULL OR sex = :gender) AND " +
                    "(:ageClass IS NOT NULL OR ageclass = :ageClass) AND " +
                    "(:federation IS NOT NULL OR federation = :federation)",
            nativeQuery = true)
    Page<Record> searchLifters1(
            @Param("name") String name,
            @Param("weightClass") String weightClass,
            @Param("gender") String gender,
            @Param("ageClass") String ageClass,
            @Param("federation") String federation,
            Pageable pageable);

    @Query(value = "SELECT * "+
            "FROM ( "+
            "SELECT *, "+
            "ROW_NUMBER() OVER (PARTITION BY name ORDER BY goodlift DESC) AS rn "+
            "FROM lifter_data WHERE event = 'SBD' AND equipment = 'Raw' AND goodlift IS NOT NULL"+
            ") ranked "+
            "WHERE rn = 1 "+
            "ORDER BY goodlift DESC ",
            countQuery = "SELECT COUNT(*) "+
                    "FROM ( "+
                    "SELECT *, "+
                    "ROW_NUMBER() OVER (PARTITION BY name ORDER BY goodlift DESC) AS rn "+
                    "FROM lifter_data WHERE event = 'SBD' AND equipment = 'Raw'"+
                    ") ranked "+
                    "WHERE rn = 1 ",
            nativeQuery = true)
    Page<Record> searchLifters(
            @Param("name") String name,
            @Param("weightClass") String weightClass,
            @Param("gender") String gender,
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
}