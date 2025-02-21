package com.kd.liftable.controllers;

import com.kd.liftable.services.OpenPowerliftingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lifter")
public class ApiController {
    private final OpenPowerliftingService openPowerliftingService;

    public ApiController(OpenPowerliftingService openPowerliftingService) {
        this.openPowerliftingService = openPowerliftingService;
    }

    @GetMapping("/{name}")
    public String getLifterData(@PathVariable String name) throws Exception {
        return openPowerliftingService.fetchLifterDataRaw(name);
    }
}
