package com.kd.liftable.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.kd.liftable.services.ApiPowerliftingService;
import com.kd.liftable.services.OpenPowerliftingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiController {
    private final ApiPowerliftingService apiPowerliftingService;

    public ApiController(ApiPowerliftingService apiPowerliftingService) {
        this.apiPowerliftingService = apiPowerliftingService;
    }

    @GetMapping("/csv/{name}")
    public String getLifterDataCSV(@PathVariable String name) throws Exception {
        return apiPowerliftingService.fetchLifterDataRaw(name);
    }

    @GetMapping("/json/lifter/{name}")
    public JsonNode getLifterDataJSON(@PathVariable String name) throws Exception {
        return apiPowerliftingService.getLifterJson(name);
    }

    @GetMapping("/json/region/{region}")
    public JsonNode getRegionalRankingsJson(@PathVariable String region) throws Exception {
        return apiPowerliftingService.getRegionalRankingsJSON(region);
    }
//    @GetMapping("/json/region/names/{region}")
//    public JsonNode getRegionalRankingsJsonNameOnly(@PathVariable String region) throws Exception {
//        return apiPowerliftingService.getRegionalRankingsJSONNameOnly(region);
//    }
}
