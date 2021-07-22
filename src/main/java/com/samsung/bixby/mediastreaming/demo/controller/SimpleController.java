package com.samsung.bixby.mediastreaming.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class SimpleController {
    @GetMapping("home")
    public String home(Model model){
        model.addAttribute("data", "Welcome!");
        return "index";
    }
}
