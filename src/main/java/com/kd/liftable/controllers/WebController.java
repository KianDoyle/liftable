package com.kd.liftable.controllers;

import com.kd.liftable.models.Lifter;
import com.kd.liftable.models.PowerliftingRecord;
import com.kd.liftable.services.OpenPowerliftingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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
    public String getHome(Model model) throws Exception {
        model.addAttribute("records", null);
        model.addAttribute("regionalRankings", true);
        model.addAttribute("leaderboards", openPowerliftingService.fetchAllRegionalRankings());
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "query", required = false) String query, Model model) throws Exception {
        model.addAttribute("lifterList", openPowerliftingService.fetchLiftersDisambiguationList(query));
        return "fragments/search :: search"; // Renders search.html
    }

    @GetMapping("/search/lifter/{name}")
    public String getLifterData(@PathVariable String name, Model model) throws Exception {
        model.addAttribute("lifter", openPowerliftingService.createLifterCard(name));
        model.addAttribute("records", openPowerliftingService.getLifterRecords(name));
        return "fragments/lifter-details :: lifter-details";
    }

    @GetMapping("/showdownSearch")
    public String showdownSearch(@RequestParam(name = "query", required = false) String query, Model model) throws Exception {
        model.addAttribute("lifterList", openPowerliftingService.fetchLiftersDisambiguationList(query));
        return "fragments/search :: search";
    }

    @GetMapping("/showdownSearch/lifter/{name}")
    public String getShowdownLifter(@PathVariable String name, Model model) throws Exception {
        model.addAttribute("lifter", openPowerliftingService.createLifterCard(name));
        return "fragments/lifter-card :: lifter-card";
    }

}
