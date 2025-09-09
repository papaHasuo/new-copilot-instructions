package com.papahasuo.new_copilot_instructions.controller;

import com.papahasuo.new_copilot_instructions.model.User;
import com.papahasuo.new_copilot_instructions.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ユーザー管理のREST APIコントローラー
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {
    
    private final UserService userService;
    
    /**
     * 全ユーザー取得
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("API: 全ユーザー取得リクエスト");
        List<User> users = userService.getAllUsers();
        log.info("API: ユーザー{}件を返却", users.size());
        return ResponseEntity.ok(users);
    }
    
    /**
     * 指定IDのユーザー取得
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("API: ユーザー取得リクエスト - ID: {}", id);
        User user = userService.findById(id);
        if (user != null) {
            log.info("API: ユーザーを返却 - {}", user.getName());
            return ResponseEntity.ok(user);
        } else {
            log.warn("API: ユーザーが見つからない - ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * ユーザー作成
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("API: ユーザー作成リクエスト - {}", user.getName());
        try {
            User createdUser = userService.saveUser(user);
            log.info("API: ユーザー作成完了 - {} (ID: {})", createdUser.getName(), createdUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            log.error("API: ユーザー作成失敗 - {}", user.getName(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * ユーザー更新
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("API: ユーザー更新リクエスト - ID: {}, Name: {}", id, user.getName());
        try {
            User updatedUser = userService.updateUser(id, user);
            log.info("API: ユーザー更新完了 - {}", updatedUser.getName());
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("API: ユーザー更新失敗 - ID: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * ユーザー削除
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("API: ユーザー削除リクエスト - ID: {}", id);
        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                log.info("API: ユーザー削除完了 - ID: {}", id);
                return ResponseEntity.noContent().build();
            } else {
                log.warn("API: 削除対象ユーザーが見つからない - ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("API: ユーザー削除失敗 - ID: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }
}
