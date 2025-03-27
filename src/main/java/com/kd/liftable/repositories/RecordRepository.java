package com.kd.liftable.repositories;

import com.kd.liftable.models.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query(value = "SELECT * FROM lifter_data WHERE Name = :name ORDER BY Date DESC", nativeQuery = true)
    List<Record> findAllByNameOrderedByDateDesc(@Param("name") String name);

    @Query(value = "SELECT * FROM lifter_data WHERE Name = :name ORDER BY Date ASC", nativeQuery = true)
    List<Record> findAllByNameOrderedByDateAsc(@Param("name") String name);

    @Query(value = "SELECT MAX(Best3SquatKg), MAX(Best3BenchKg), MAX(Best3DeadliftKg), MAX(TotalKg), MAX(Goodlift) FROM (" +
                "SELECT Name, Best3SquatKg, Best3BenchKg, Best3DeadliftKg, TotalKg, Goodlift FROM lifter_data WHERE Name = :name AND Equipment = :equip)" +
                "AS grouped" , nativeQuery = true)
    List<Float[]> findLargestStats(@Param("name") String name, @Param("equip") String equip);

    @Query(value = "SELECT DISTINCT Date, BodyweightKg, Best3SquatKg, Best3BenchKg, Best3DeadliftKg, TotalKg, Goodlift FROM lifter_data WHERE Name = :name ORDER BY Date ASC", nativeQuery = true)
    List<Object[]> findStatsByNameDistinctDateOrderByDateAsc(@Param("name") String name);

    @Query(value = "SELECT DISTINCT Date, BodyweightKg,Goodlift FROM lifter_data WHERE Name = :name AND Equipment = :equip AND Event = :event ORDER BY Date ASC", nativeQuery = true)
    List<Object[]> findBWGLByNameDistinctDateOrderByDateAsc(@Param("name") String name, @Param("equip") String equip, @Param("event") String event);

    @Query(value = "SELECT DISTINCT Date, Best3SquatKg, Best3BenchKg, Best3DeadliftKg FROM lifter_data WHERE Name = :name AND Equipment = :equip ORDER BY Date ASC", nativeQuery = true)
    List<Object[]> findSBDByNameDistinctDateOrderByDateAsc(@Param("name") String name, @Param("equip") String equip);

    @Query(value = "SELECT DISTINCT Date, TotalKg FROM lifter_data WHERE Name = :name AND Equipment = :equip AND Event = :event ORDER BY Date ASC", nativeQuery = true)
    List<Object[]> findTotalByNameDistinctDateOrderByDateAsc(@Param("name") String name, @Param("equip") String equip, @Param("event") String event);

}