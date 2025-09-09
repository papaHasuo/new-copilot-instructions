package com.papahasuo.new_copilot_instructions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "ユーザー管理システム");
        model.addAttribute("message", "シンプルなユーザー管理アプリケーションです");
        return "index";
    }
}
