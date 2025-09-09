package com.papahasuo.new_copilot_instructions.service;

import com.papahasuo.new_copilot_instructions.exception.UserNotFoundException;
import com.papahasuo.new_copilot_instructions.exception.UserValidationException;
import com.papahasuo.new_copilot_instructions.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserService テスト")
class UserServiceTest {
    
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        userService = new UserService();
    }
    
    @Test
    @DisplayName("初期データが正しく設定されていること")
    void 初期データが正しく設定されていること() {
        List<User> users = userService.getAllUsers();
        
        assertEquals(2, users.size());
        assertEquals("田中太郎", users.get(0).getName());
        assertEquals("佐藤花子", users.get(1).getName());
    }
    
    @Test
    @DisplayName("正常なユーザー保存")
    void 正常なユーザー保存() {
        User newUser = new User("山田次郎", "yamada@example.com", 28);
        
        User savedUser = userService.saveUser(newUser);
        
        assertNotNull(savedUser.getId());
        assertEquals("山田次郎", savedUser.getName());
        assertEquals("yamada@example.com", savedUser.getEmail());
        assertEquals(28, savedUser.getAge());
        
        List<User> allUsers = userService.getAllUsers();
        assertEquals(3, allUsers.size());
    }
    
    @Test
    @DisplayName("重複メールアドレスでユーザー保存時に例外が発生すること")
    void 重複メールアドレスでユーザー保存時に例外が発生すること() {
        User duplicateUser = new User("テストユーザー", "tanaka@example.com", 30);
        
        UserValidationException exception = assertThrows(
            UserValidationException.class,
            () -> userService.saveUser(duplicateUser)
        );
        
        assertTrue(exception.getMessage().contains("既に登録されています"));
    }
    
    @Test
    @DisplayName("無効なメールアドレスでユーザー保存時に例外が発生すること")
    void 無効なメールアドレスでユーザー保存時に例外が発生すること() {
        User invalidEmailUser = new User("テストユーザー", "invalid-email", 25);
        
        UserValidationException exception = assertThrows(
            UserValidationException.class,
            () -> userService.saveUser(invalidEmailUser)
        );
        
        assertTrue(exception.getMessage().contains("有効なメールアドレス"));
    }
    
    @Test
    @DisplayName("名前が空でユーザー保存時に例外が発生すること")
    void 名前が空でユーザー保存時に例外が発生すること() {
        User emptyNameUser = new User("", "test@example.com", 25);
        
        UserValidationException exception = assertThrows(
            UserValidationException.class,
            () -> userService.saveUser(emptyNameUser)
        );
        
        assertTrue(exception.getMessage().contains("名前は必須"));
    }
    
    @Test
    @DisplayName("年齢が範囲外でユーザー保存時に例外が発生すること")
    void 年齢が範囲外でユーザー保存時に例外が発生すること() {
        User invalidAgeUser = new User("テストユーザー", "test@example.com", -1);
        
        UserValidationException exception = assertThrows(
            UserValidationException.class,
            () -> userService.saveUser(invalidAgeUser)
        );
        
        assertTrue(exception.getMessage().contains("年齢は0歳から150歳"));
    }
    
    @Test
    @DisplayName("IDでユーザーを正常に取得できること")
    void IDでユーザーを正常に取得できること() {
        User user = userService.findById(1L);
        
        assertNotNull(user);
        assertEquals("田中太郎", user.getName());
        assertEquals("tanaka@example.com", user.getEmail());
    }
    
    @Test
    @DisplayName("存在しないIDでnullが返されること")
    void 存在しないIDでnullが返されること() {
        User user = userService.findById(999L);
        
        assertNull(user);
    }
    
    @Test
    @DisplayName("ユーザーを正常に更新できること")
    void ユーザーを正常に更新できること() {
        User updatedUser = new User("田中太郎（更新済み）", "tanaka-updated@example.com", 26);
        
        User result = userService.updateUser(1L, updatedUser);
        
        assertEquals("田中太郎（更新済み）", result.getName());
        assertEquals("tanaka-updated@example.com", result.getEmail());
        assertEquals(26, result.getAge());
    }
    
    @Test
    @DisplayName("存在しないIDでユーザー更新時に例外が発生すること")
    void 存在しないIDでユーザー更新時に例外が発生すること() {
        User updatedUser = new User("テストユーザー", "test@example.com", 25);
        
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> userService.updateUser(999L, updatedUser)
        );
        
        assertTrue(exception.getMessage().contains("ユーザーが見つかりません"));
    }
    
    @Test
    @DisplayName("ユーザーを正常に削除できること")
    void ユーザーを正常に削除できること() {
        boolean deleted = userService.deleteUser(1L);
        
        assertTrue(deleted);
        
        List<User> allUsers = userService.getAllUsers();
        assertEquals(1, allUsers.size());
        
        User deletedUser = userService.findById(1L);
        assertNull(deletedUser);
    }
    
    @Test
    @DisplayName("存在しないIDでユーザー削除時にfalseが返されること")
    void 存在しないIDでユーザー削除時にfalseが返されること() {
        boolean deleted = userService.deleteUser(999L);
        
        assertFalse(deleted);
    }
}
