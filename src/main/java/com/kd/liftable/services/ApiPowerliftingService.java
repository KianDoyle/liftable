package com.kd.liftable.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kd.liftable.models.Record;
import com.kd.liftable.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

    public ArrayList<Record> searchLifters(String name) {
        return recordRepository.searchLifterNames(name);
    }

    public ArrayList<String> findDistinctNames() {
        return recordRepository.findDistinctNames();
    }

    public Page<Record> fetchAllLifters(int page, int size, String sortBy, Sort.Direction direction,
                                        String event, String equipment, String weightClass,
                                        String ageClass, String federation) {
        // Create pageable object with sort options
        Pageable pageable = PageRequest.of(page, size,
                direction == null ? Sort.Direction.DESC : direction,
                sortBy != null ? sortBy : "goodlift");

        // Search with filters
        return recordRepository.searchLifters(event, equipment, weightClass, ageClass, federation, pageable);
    }

    public Page<Record> fetchLifterRecords(int page, int size, String sortBy, Sort.Direction direction,
                                           String name, String event, String equipment, String weightClass,
                                           String ageClass, String federation) {

        Pageable pageable = PageRequest.of(page, size,
                direction == null ? Sort.Direction.DESC : direction,
                sortBy != null ? sortBy : "date");

        return recordRepository.searchLifterRecords(
                name, event, equipment, weightClass, ageClass, federation, pageable);
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

    // Methods for filter options
    public Map<String, List<String>> getFilterOptions() {
        Map<String, List<String>> options = new HashMap<>();
        options.put("weightClasses", recordRepository.findAllWeightClasses());
        options.put("federations", recordRepository.findAllFederations());
        options.put("ageClasses", recordRepository.findAllAgeClasses());
        return options;
    }

    public void cacheLifters(ArrayList<File> files, ArrayList<String> events, ArrayList<String> equipment) throws IOException {

//        Path pathSBDRaw = fileSBDRaw.toPath();
//        Path pathSBDSinglePly = fileSBDSinglePly.toPath();
//        Path pathBRaw = fileBRaw.toPath();
//        Path pathBSinglePly = fileBSinglePly.toPath();
//
//        Files.deleteIfExists(pathSBDRaw);
//        Files.deleteIfExists(pathSBDSinglePly);
//        Files.deleteIfExists(pathBRaw);
//        Files.deleteIfExists(pathBSinglePly);
//
//        List<Record> recordsSBDRaw = apiPowerliftingService.fetchAllSBDRawLiftersCache();
//        List<Record> recordsSBDSinglePly = apiPowerliftingService.fetchAllSBDSinglePlyLiftersCache();
//        List<Record> recordsBRaw = apiPowerliftingService.fetchAllBRawLiftersCache();
//        List<Record> recordsBSinglePly = apiPowerliftingService.fetchAllBSinglePlyLiftersCache();
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.findAndRegisterModules();
//
//        mapper.writeValue(fileSBDRaw, recordsSBDRaw);
//        mapper.writeValue(fileSBDSinglePly, recordsSBDSinglePly);
//        mapper.writeValue(fileBRaw, recordsBRaw);
//        mapper.writeValue(fileBSinglePly, recordsBSinglePly);

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        for (int i = 0; i < files.size(); i++) {
            Path path = files.get(i).toPath();
            Files.deleteIfExists(path);
            List<Record> cacheableLifters = recordRepository.findCachableLifters(events.get(i%2), equipment.get(i%2));
            mapper.writeValue(files.get(i), cacheableLifters);
        }
    }

}
