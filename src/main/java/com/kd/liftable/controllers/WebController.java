package com.kd.liftable.controllers;

import com.kd.liftable.models.Lifter;
import com.kd.liftable.services.OpenPowerliftingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("web")
public class WebController {

    private final OpenPowerliftingService openPowerliftingService;

    public WebController(OpenPowerliftingService openPowerliftingService) {
        this.openPowerliftingService = openPowerliftingService;
    }

    @GetMapping("/home")
    public String getHome(Model model) throws Exception {
        model.addAttribute("lifterList", null);
        model.addAttribute("lifter", null);
        model.addAttribute("records", null);
        model.addAttribute("showdown", true);
        model.addAttribute("regionalRankings", true);
        model.addAttribute("leaderboards", openPowerliftingService.fetchAllRegionalRankings());
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "query", required = false) String query, Model model) throws Exception {
        model.addAttribute("lifterList", openPowerliftingService.fetchLiftersDisambiguationList(query));
        model.addAttribute("showdown", false);
        return "index"; // Renders search.html
    }

    @GetMapping("/lifter/{name}")
    public String getLifterData(@PathVariable String name, Model model) throws Exception {
        model.addAttribute("lifter", openPowerliftingService.fetchLifterData(name));
        model.addAttribute("records", openPowerliftingService.getLifterRecords(name));
        model.addAttribute("showdown", false);
        return "index";
    }

    @GetMapping("/showdownSearch")
    public String showdownSearch(@RequestParam(name = "query", required = false) String query, Model model) throws Exception {
        model.addAttribute("lifterList", openPowerliftingService.fetchLiftersDisambiguationList(query));
        return "fragments/search :: search";
    }

    @GetMapping("/showdownSearch/lifter/{name}")
    public String getShowdownLifter(@PathVariable String name, Model model) throws Exception {
        model.addAttribute("lifter", openPowerliftingService.fetchLifterData(name));
        return "fragments/lifter-info :: lifter-info";
    }

}
