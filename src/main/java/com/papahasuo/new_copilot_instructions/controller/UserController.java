package com.papahasuo.new_copilot_instructions.controller;

import com.papahasuo.new_copilot_instructions.model.User;
import com.papahasuo.new_copilot_instructions.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    // ユーザー一覧表示
    @GetMapping
    public String listUsers(Model model) {
        log.info("ユーザー一覧表示を開始します");
        try {
            model.addAttribute("users", userService.getAllUsers());
            log.info("ユーザー一覧の取得が完了しました");
            return "users/list";
        } catch (Exception e) {
            log.error("ユーザー一覧の取得中にエラーが発生しました", e);
            model.addAttribute("error", "ユーザー一覧の取得に失敗しました");
            return "error";
        }
    }
    
    // 新規ユーザー登録フォーム表示
    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        log.info("新規ユーザー登録フォームを表示します");
        model.addAttribute("user", new User());
        return "users/form";
    }
    
    // ユーザー登録処理
    @PostMapping
    public String saveUser(@ModelAttribute User user, Model model) {
        log.info("新規ユーザー登録を開始します: {}", user.getName());
        try {
            userService.saveUser(user);
            log.info("ユーザーの登録が完了しました: {}", user.getName());
            model.addAttribute("message", "ユーザーが正常に登録されました！");
            return "redirect:/users";
        } catch (Exception e) {
            log.error("ユーザー登録中にエラーが発生しました: {}", user.getName(), e);
            model.addAttribute("error", "エラーが発生しました: " + e.getMessage());
            model.addAttribute("user", user);
            return "users/form";
        }
    }
    
    // ユーザー詳細表示
    @GetMapping("/{id}")
    public String showUser(@PathVariable Long id, Model model) {
        log.info("ユーザー詳細表示を開始します: ID={}", id);
        try {
            User user = userService.findById(id);
            if (user != null) {
                model.addAttribute("user", user);
                log.info("ユーザー詳細の取得が完了しました: {}", user.getName());
                return "users/detail";
            } else {
                log.warn("指定されたIDのユーザーが見つかりません: ID={}", id);
                model.addAttribute("error", "指定されたユーザーが見つかりません");
                return "redirect:/users";
            }
        } catch (Exception e) {
            log.error("ユーザー詳細の取得中にエラーが発生しました: ID={}", id, e);
            model.addAttribute("error", "ユーザー情報の取得に失敗しました");
            return "redirect:/users";
        }
    }
}
