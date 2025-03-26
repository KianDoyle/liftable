package com.kd.liftable.controllers;

import com.kd.liftable.models.*;
import com.kd.liftable.models.Record;
import com.kd.liftable.services.OpenPowerliftingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
        return "fragments/details :: details";
    }

    @GetMapping("/showdownSearch")
    public String showdownSearch(@RequestParam(name = "query", required = false) String query, Model model) {
        model.addAttribute("names", openPowerliftingService.fetchPossibleLifters(query, "showdown"));
        return "fragments/showdown-search :: showdown-search";
    }

    @GetMapping("/showdown/{name}")
    public String getShowdownLifter(@PathVariable String name, Model model) throws Exception {
        model.addAttribute("lifter", openPowerliftingService.fetchLifterShowdown(name).getLifterCard());
        return "fragments/lifter-card :: lifter-card";
    }

}
