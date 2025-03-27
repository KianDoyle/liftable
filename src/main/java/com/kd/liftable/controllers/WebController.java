package com.kd.liftable.controllers;

import com.kd.liftable.models.*;
import com.kd.liftable.models.Record;
import com.kd.liftable.services.OpenPowerliftingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("web")
@SessionAttributes({"leaderboards", "names", "lifter", "records", "filters"})
public class WebController {

    private final OpenPowerliftingService openPowerliftingService;

    public WebController(OpenPowerliftingService openPowerliftingService) {
        this.openPowerliftingService = openPowerliftingService;
    }

    // Initialize session attributes using @ModelAttribute
    @ModelAttribute("leaderboards")
    public LinkedHashMap<String, ArrayList<RegionRecord>> initializeLeaderboards() {
        return null; // Default empty list or logic to initialize if needed
    }

    @ModelAttribute("names")
    public ArrayList<Name> initializeNames() {
        return null; // Default empty list
    }

    @ModelAttribute("lifter")
    public LifterCard initializeLifter() {
        return null; // Default empty object
    }

    @ModelAttribute("records")
    public ArrayList<Record> initializeRecords() {
        return null; // Default empty list
    }

    @ModelAttribute("filters")
    public List<String> initializeFilters() {
        return null; // Default empty list
    }

    @GetMapping("")
    public String resetSession(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/web/home";
    }

    @GetMapping("/home")
    public String getHome(@ModelAttribute ("leaderboards") LinkedHashMap<String, ArrayList<RegionRecord>> leaderboards,
                          @ModelAttribute("names") ArrayList<Name> names,
                          @ModelAttribute("lifter") LifterCard lifter,
                          @ModelAttribute("records") ArrayList<Record> records,
                          @ModelAttribute("filters") List<String> filters,
                          Model model) {
        leaderboards = openPowerliftingService.fetchTop10LiftersInEachRegion();
        names = null;
        lifter = null;
        records = null;
        filters = null;
        model.addAttribute("leaderboards", leaderboards);
        model.addAttribute("names", names);
        model.addAttribute("lifter", lifter);
        model.addAttribute("records", records);
        model.addAttribute("filters", filters);
        return "index";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("filters") List<String> filters,
                         @ModelAttribute("records") ArrayList<Record> records,
                         @ModelAttribute("lifter") LifterCard lifter,
                         @ModelAttribute("names") ArrayList<Name> names,
                         @RequestParam(value = "query") String query,
                         Model model) {
        filters = null;
        records = null;
        lifter = null;
        names = openPowerliftingService.fetchPossibleLifters(query, "search");
        model.addAttribute("filters", filters);
        model.addAttribute("records", records);
        model.addAttribute("lifter", lifter);
        model.addAttribute("names", names);
        return "index";
    }

    @GetMapping("/lifter/{name}")
    public String getLifterData(@ModelAttribute("lifter") LifterCard lifter,
                                @ModelAttribute("records") ArrayList<Record> records,
                                @ModelAttribute("filters") List<String> filters,
                                @PathVariable String name,
                                Model model) {
        LifterData lifterData = openPowerliftingService.fetchLifter(name);
        lifter = lifterData.getLifterCard();
        records = lifterData.getRecords();
        filters = openPowerliftingService.getFilters();
        model.addAttribute("lifter", lifter);
        model.addAttribute("records", records);
        model.addAttribute("filters", filters);
        return "index";
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
