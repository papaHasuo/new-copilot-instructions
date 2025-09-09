package com.papahasuo.new_copilot_instructions.controller;

import com.papahasuo.new_copilot_instructions.model.User;
import com.papahasuo.new_copilot_instructions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // ユーザー一覧表示
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }
    
    // 新規ユーザー登録フォーム表示
    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }
    
    // ユーザー登録処理
    @PostMapping
    public String saveUser(@ModelAttribute User user, Model model) {
        try {
            userService.saveUser(user);
            model.addAttribute("message", "ユーザーが正常に登録されました！");
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("error", "エラーが発生しました: " + e.getMessage());
            return "users/form";
        }
    }
    
    // ユーザー詳細表示
    @GetMapping("/{id}")
    public String showUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "users/detail";
        } else {
            return "redirect:/users";
        }
    }
}
