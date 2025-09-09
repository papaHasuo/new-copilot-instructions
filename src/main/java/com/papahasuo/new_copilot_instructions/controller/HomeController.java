package com.papahasuo.new_copilot_instructions.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home(Model model) {
        log.info("ホームページへのアクセス");
        model.addAttribute("title", "ユーザー管理システム");
        model.addAttribute("message", "シンプルなユーザー管理アプリケーションです");
        return "index";
    }
}
