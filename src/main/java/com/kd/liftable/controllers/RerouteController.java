package com.kd.liftable.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RerouteController {

    @GetMapping
    public String getHome() {
        return "redirect:/web/home";
    }
}
