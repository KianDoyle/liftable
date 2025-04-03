package com.kd.liftable.services;

import com.kd.liftable.models.Record;
import com.kd.liftable.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiPowerliftingService {

    private final RecordRepository recordRepository;

    @Autowired
    public ApiPowerliftingService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public Page<Record> fetchAllLifters(int page, int size, String sortBy, Sort.Direction direction,
                                        String name, String weightClass, String gender,
                                        String ageClass, String federation) {
        // Create pageable object with sort options
        Pageable pageable = PageRequest.of(page, size,
                direction == null ? Sort.Direction.DESC : direction,
                sortBy != null ? sortBy : "goodlift");

        // Search with filters
        return recordRepository.searchLifters(name, weightClass, gender, ageClass, federation, pageable);
    }

    public Page<Record> fetchLifter(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "date");
        return recordRepository.findAllByNameOrderedByDateDesc(name, pageable);
    }

    public Map<String, Object> fetchLifterStats(String name, String equipment) {
        List<Object[]> stats = recordRepository.findLargestStats(name, equipment);

        Map<String, Object> result = new HashMap<>();
        if (!stats.isEmpty() && stats.get(0) != null) {
            Object[] row = stats.get(0);
            result.put("bestSquat", row[0]);
            result.put("bestBench", row[1]);
            result.put("bestDeadlift", row[2]);
            result.put("bestTotal", row[3]);
            result.put("bestGoodlift", row[4]);
        }

        return result;
    }

    public List<Record> fetchTopLifters() {
        return recordRepository.findTopLiftersByGoodlift();
    }

    // Methods for filter options
    public Map<String, List<String>> getFilterOptions() {
        Map<String, List<String>> options = new HashMap<>();
        options.put("weightClasses", recordRepository.findAllWeightClasses());
        options.put("federations", recordRepository.findAllFederations());
        options.put("ageClasses", recordRepository.findAllAgeClasses());
        return options;
    }
}
