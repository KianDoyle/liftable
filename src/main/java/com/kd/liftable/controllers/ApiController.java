package com.kd.liftable.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kd.liftable.models.Record;
import com.kd.liftable.services.ApiPowerliftingService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Configure as needed for production
public class ApiController {

    private final ApiPowerliftingService apiPowerliftingService;

    @Autowired
    public ApiController(ApiPowerliftingService apiPowerliftingService) {
        this.apiPowerliftingService = apiPowerliftingService;
    }

    @GetMapping("search/{name}")
    public ResponseEntity<ArrayList<Record>> searchLifters(@PathVariable String name) {
        return ResponseEntity.ok(apiPowerliftingService.searchLifters(name));
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getDistinctNames() {
        return ResponseEntity.ok(apiPowerliftingService.findDistinctNames());
    }

    @GetMapping("/lifters")
    public ResponseEntity<Page<Record>> getAllLifters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String event,
            @RequestParam(required = false) String equipment,
            @RequestParam(required = false) String weightclass,
            @RequestParam(required = false) String ageclass,
            @RequestParam(required = false) String federation) {

        Sort.Direction sortDirection = direction != null && direction.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        return ResponseEntity.ok(apiPowerliftingService.fetchAllLifters(
                page, size, sortBy, sortDirection,
                event, equipment, weightclass, ageclass, federation));
    }

    @PostConstruct
    public void cacheAllLifters() throws IOException {
        ArrayList<File> files = new ArrayList<>();

        files.add(new File("src/main/resources/static/cached/GoodliftOrdered-SBD-Raw-LifterRecords.json"));
        files.add(new File("src/main/resources/static/cached/GoodliftOrdered-SBD-Single-ply-LifterRecords.json"));
        files.add(new File("src/main/resources/static/cached/GoodliftOrdered-B-Raw-LifterRecords.json"));
        files.add(new File("src/main/resources/static/cached/GoodliftOrdered-B-Single-ply-LifterRecords.json"));

        ArrayList<String> events = new ArrayList<>();
        events.add("SBD");
        events.add("B");

        ArrayList<String> equipment = new ArrayList<>();
        equipment.add("Raw");
        equipment.add("Single-ply");

        apiPowerliftingService.cacheLifters(files, events, equipment);
    }

    @GetMapping("/lifter/{name}")
    public ResponseEntity<Page<Record>> getLifterData(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String event,
            @RequestParam(required = false) String equipment,
            @RequestParam(required = false) String weightclass,
            @RequestParam(required = false) String ageclass,
            @RequestParam(required = false) String federation) {

        Sort.Direction sortDirection = direction != null && direction.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        System.out.println("Name: " + name);

        return ResponseEntity.ok(apiPowerliftingService.fetchLifterRecords(
                page, size, sortBy, sortDirection,
                name, event, equipment, weightclass, ageclass, federation));
    }

    @GetMapping("/lifter/{name}/stats")
    public ResponseEntity<Map<String, Object>> getLifterStats(
            @PathVariable String name,
            @RequestParam(defaultValue = "Raw") String equipment) {
        return ResponseEntity.ok(apiPowerliftingService.fetchLifterStats(name, equipment));
    }

    @GetMapping("/filters")
    public ResponseEntity<Map<String, List<String>>> getFilterOptions() {
        return ResponseEntity.ok(apiPowerliftingService.getFilterOptions());
    }
}