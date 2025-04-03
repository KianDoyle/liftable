package com.kd.liftable.controllers;

import com.kd.liftable.models.Record;
import com.kd.liftable.services.ApiPowerliftingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/lifters")
    public ResponseEntity<Page<Record>> getAllLifters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String weightClass,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String ageClass,
            @RequestParam(required = false) String federation) {

        Sort.Direction sortDirection = direction != null && direction.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        return ResponseEntity.ok(apiPowerliftingService.fetchAllLifters(
                page, size, sortBy, sortDirection,
                name, weightClass, gender, ageClass, federation));
    }

    @GetMapping("/lifter/{name}")
    public ResponseEntity<Page<Record>> getLifterData(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(apiPowerliftingService.fetchLifter(name, page, size));
    }

    @GetMapping("/lifter/{name}/stats")
    public ResponseEntity<Map<String, Object>> getLifterStats(
            @PathVariable String name,
            @RequestParam(defaultValue = "Raw") String equipment) {
        return ResponseEntity.ok(apiPowerliftingService.fetchLifterStats(name, equipment));
    }

    @GetMapping("/lifters/top")
    public ResponseEntity<List<Record>> getTopLifters() {
        return ResponseEntity.ok(apiPowerliftingService.fetchTopLifters());
    }

    @GetMapping("/filters")
    public ResponseEntity<Map<String, List<String>>> getFilterOptions() {
        return ResponseEntity.ok(apiPowerliftingService.getFilterOptions());
    }
}