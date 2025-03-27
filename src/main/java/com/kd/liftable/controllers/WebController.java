package com.kd.liftable.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kd.liftable.models.*;
import com.kd.liftable.models.Record;
import com.kd.liftable.services.OpenPowerliftingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("web")
public class WebController {

    private final OpenPowerliftingService openPowerliftingService;

    public WebController(OpenPowerliftingService openPowerliftingService) {
        this.openPowerliftingService = openPowerliftingService;
    }

    @GetMapping("/home")
    public String getHome(Model model) {
        model.addAttribute("leaderboards", openPowerliftingService.fetchTop10LiftersInEachRegion());
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "query") String query, Model model) {
        ArrayList<Name> names = openPowerliftingService.fetchPossibleLifters(query, "search");
        model.addAttribute("lifterSearch", !names.isEmpty());
        model.addAttribute("names", names);
        return "fragments/search :: search";
    }

    @GetMapping("/lifter/{name}")
    public String getLifterData(@PathVariable String name, Model model) {
        LifterData lifterData = openPowerliftingService.fetchLifter(name);
        model.addAttribute("lifter", lifterData.getLifterCard());
        model.addAttribute("records", lifterData.getRecords());
        model.addAttribute("filters", openPowerliftingService.getFilters());
        return "fragments/details :: details";
    }

    @GetMapping("/showdownSearch")
    public String showdownSearch(@RequestParam(name = "query", required = false) String query, Model model) {
        model.addAttribute("names", openPowerliftingService.fetchPossibleLifters(query, "showdown"));
        return "fragments/showdown-search :: showdown-search";
    }

    @GetMapping("/showdown/{name}")
    public String getShowdownLifter(@PathVariable String name, Model model) {
        model.addAttribute("lifter", openPowerliftingService.fetchLifterShowdown(name).getLifterCard());
        return "fragments/lifter-card :: lifter-card";
    }

    @GetMapping("/chart-builder")
    public String getChart() {
        return "fragments/chart-builder :: chart-builder";
    }

    @PostMapping("/charts")
    public ResponseEntity<Map<String, Object>> getChartData(@RequestBody Map<String, List<String>> filters) {
        Map<String, Object> chartData = openPowerliftingService.getChartData(filters);
        return ResponseEntity.ok(chartData);
    }
}
